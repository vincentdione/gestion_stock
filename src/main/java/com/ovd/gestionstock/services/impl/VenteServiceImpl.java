package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.dto.MvtStkDto;
import com.ovd.gestionstock.dto.VenteDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.*;
import com.ovd.gestionstock.repositories.ArticleRepository;
import com.ovd.gestionstock.repositories.LigneVenteRepository;
import com.ovd.gestionstock.repositories.VenteRepository;
import com.ovd.gestionstock.services.MvtStkService;
import com.ovd.gestionstock.services.VenteService;
import com.ovd.gestionstock.validators.UtilisateurValidator;
import com.ovd.gestionstock.validators.VenteValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VenteServiceImpl implements VenteService {

    private final VenteRepository venteRepository;
    private final ArticleRepository articleRepository;
    private final LigneVenteRepository ligneVenteRepository;

    private final MvtStkService mvtStkService;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<VenteDto> getAllVentes() {
        return venteRepository.findAll().stream().map(VenteDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteVentes(Long id) {
        if (id == null) {
            log.error("Vente ID is NULL");
            return;
        }
        List<LigneVente> ligneVentes = ligneVenteRepository.findAllByVenteId(id);
        if (!ligneVentes.isEmpty()) {
            throw new InvalidEntityException("Impossible de supprimer une vente ...",
                    ErrorCodes.VENTE_ALREADY_IN_USE);
        }
        venteRepository.deleteById(id);
    }

    @Override
    public VenteDto getVentesById(Long id) {

        if (id == null){
            log.error("ID est null");
            return null;
        }

        Optional<Ventes> ventes = venteRepository.findById(id);


        return venteRepository.findById(id)
                .map(VenteDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Aucun vente n'a ete trouve dans la BDD", ErrorCodes.VENTE_NOT_FOUND));
    }

    @Override
    public List<LigneVenteDto> findAllLigneVentesByVenteId(Long id) {
        return ligneVenteRepository.findAllByVenteId(id).stream().map(LigneVenteDto::fromEntity).collect(Collectors.toList());
    }



    @Override
    public VenteDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Vente CODE is NULL");
            return null;
        }
        return venteRepository.findVentesByCode(code)
                .map(VenteDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune vente client n'a ete trouve avec le CODE " + code, ErrorCodes.VENTE_NOT_VALID
                ));
    }

    @Override
    public VenteDto createVentes(VenteDto dto) {

        List<String> errors = VenteValidator.validate(dto);
        log.error("errors === " + errors);
        log.error("dto === " + dto);
        if (!errors.isEmpty()) {
            log.error("Ventes n'est pas valide");
            throw new InvalidEntityException("L'objet vente n'est pas valide", ErrorCodes.VENTE_NOT_VALID, errors);
        }

        List<String> articleErrors = new ArrayList<>();

        dto.getLigneVentes().forEach(ligneVenteDto -> {
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
            if (article.isEmpty()) {
                articleErrors.add("Aucun article avec l'ID " + ligneVenteDto.getArticle().getId() + " n'a ete trouve dans la BDD");
            }
        });

        if (!articleErrors.isEmpty()) {
            log.error("One or more articles were not found in the DB, {}", errors);
            throw new InvalidEntityException("Un ou plusieurs articles n'ont pas ete trouve dans la BDD", ErrorCodes.VENTE_NOT_VALID, errors);
        }

        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('SEQ_COMMANDE_VENTE')", Long.class);
        String code = "CMD-VEN" + String.format("%07d", nextVal);

        dto.setCode(code);

        Ventes savedVentes = venteRepository.save(VenteDto.toEntity(dto));

        log.error("savedVentes  " +savedVentes);

        if (dto.getLigneVentes() != null) {
            dto.getLigneVentes().forEach(ligneVenteDto -> {
                LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
                ligneVente.setVente(savedVentes);
                ligneVenteRepository.save(ligneVente);
                updateMvtStk(ligneVente);
            });
        }
        return VenteDto.fromEntity(savedVentes);
    }

    @Override
    public BigDecimal getMontantTotalVentes(List<Ventes> ventes) {
        BigDecimal venteTotal = BigDecimal.ZERO;

        for (Ventes vente : ventes) {
            List<LigneVente> ligneVentes = ligneVenteRepository.findAllByVenteId(vente.getId());
            if (ligneVentes != null) {
                for (LigneVente ligneVente : ligneVentes) {
                    if (ligneVente.getPrixUnitaire() != null && ligneVente.getQuantite() != null) {
                        BigDecimal prixUnitaire = new BigDecimal(String.valueOf(ligneVente.getPrixUnitaire()));
                        BigDecimal quantite = new BigDecimal(String.valueOf(ligneVente.getQuantite()));
                        BigDecimal montantLigne = prixUnitaire.multiply(quantite);
                        venteTotal = venteTotal.add(montantLigne);

                    }
                }
            }
        }
        log.info(String.valueOf(venteTotal));
        return venteTotal;

    }



    private void updateMvtStk(LigneVente lig) {
        MvtStkDto mvtStkDto = MvtStkDto.builder()
                .article(ArticleDto.fromEntity(lig.getArticle()))
                .dateMvt(Instant.now())
                .typeMvtStk(TypeMvtStk.SORTIE)
                .sourceMvt(SourceMvt.VENTE)
                .quantite(lig.getQuantite())
                .idEntreprise(lig.getIdEntreprise())
                .unite(lig.getUnite())
                .build();
        mvtStkService.sortieMvtStk(mvtStkDto);
    }

}

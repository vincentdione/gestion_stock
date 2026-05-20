package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.criteria.VenteSearchCriteria;
import com.ovd.gestionstock.dto.*;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.*;
import com.ovd.gestionstock.repositories.ArticleRepository;
import com.ovd.gestionstock.repositories.LigneVenteRepository;
import com.ovd.gestionstock.repositories.VenteRepository;
import com.ovd.gestionstock.services.MvtStkService;
import com.ovd.gestionstock.services.TenantSecurityService;
import com.ovd.gestionstock.services.VenteService;
import com.ovd.gestionstock.specifications.VenteSpecification;
import com.ovd.gestionstock.validators.VenteValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VenteServiceImpl implements VenteService {

    private final VenteRepository venteRepository;
    private final ArticleRepository articleRepository;
    private final LigneVenteRepository ligneVenteRepository;
    private final MvtStkService mvtStkService;
    private final JdbcTemplate jdbcTemplate;
    private final TenantContext tenantContext;
    private final TenantSecurityService tenantSecurityService;

    @Override
    public List<VenteDto> getAllVentes() {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        return venteRepository.findAll().stream()
                .filter(v -> Objects.equals(v.getIdEntreprise(), currentTenant))
                .map(VenteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public VenteDto getVentesById(Long id) {
        if (id == null) {
            log.error("ID est null");
            return null;
        }

        Ventes ventes = venteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune vente n'a été trouvée dans la BDD",
                        ErrorCodes.VENTE_NOT_FOUND));

        try {
            // 1. Vérifier l'accès à la vente elle-même
            tenantSecurityService.validateAccessToResource(ventes.getIdEntreprise());
        } catch (Exception e) {
            throw new RuntimeException("Accès refusé à la ressource", e);
        }

        // 2. Filtrer les lignes de vente par entreprise
        List<LigneVente> lignesFiltrees = ventes.getLigneVentes().stream()
                .filter(lv -> Objects.equals(lv.getIdEntreprise(), tenantContext.getCurrentTenant()))
                .collect(Collectors.toList());

        // Remplacer les lignes par celles filtrées
        ventes.setLigneVentes(lignesFiltrees);

        return VenteDto.fromEntity(ventes);
    }
    @Override
    public VenteDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Vente CODE is NULL");
            return null;
        }

        return venteRepository.findVentesByCode(code)
                .filter(v -> Objects.equals(v.getIdEntreprise(), tenantContext.getCurrentTenant()))
                .map(VenteDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune vente trouvée avec le code " + code,
                        ErrorCodes.VENTE_NOT_VALID));
    }

    @Override
    public VenteDto createVentes(VenteDto dto) {
        List<String> errors = VenteValidator.validate(dto);
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("L'objet vente n'est pas valide", ErrorCodes.VENTE_NOT_VALID, errors);
        }

        List<String> articleErrors = new ArrayList<>();
        dto.getLigneVentes().forEach(ligneVenteDto -> {
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
            if (article.isEmpty()) {
                articleErrors.add("Aucun article avec l'ID " + ligneVenteDto.getArticle().getId() + " n'a été trouvé");
            } else {
                try {
                    tenantSecurityService.validateAccessToResource(article.get().getIdEntreprise());
                } catch (Exception e) {
                    throw new RuntimeException("Accès refusé à l'article ID " + article.get().getId(), e);
                }
            }
        });

        if (!articleErrors.isEmpty()) {
            throw new InvalidEntityException("Articles invalides", ErrorCodes.VENTE_NOT_VALID, articleErrors);
        }

        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('SEQ_COMMANDE_VENTE')", Long.class);
        String code = "CMD-VEN" + String.format("%07d", nextVal);

        dto.setCode(code);
        dto.setIdEntreprise(tenantContext.getCurrentTenant());

        Ventes savedVentes = venteRepository.save(VenteDto.toEntity(dto));

        if (dto.getLigneVentes() != null) {
            dto.getLigneVentes().forEach(ligneVenteDto -> {
                LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
                ligneVente.setVente(savedVentes);
                ligneVente.setIdEntreprise(savedVentes.getIdEntreprise());
                ligneVenteRepository.save(ligneVente);
                updateMvtStk(ligneVente);
            });
        }

        return VenteDto.fromEntity(savedVentes);
    }

    @Override
    public void deleteVentes(Long id) {
        if (id == null) {
            log.error("Vente ID is NULL");
            return;
        }

        Ventes vente = venteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vente non trouvée", ErrorCodes.VENTE_NOT_FOUND));

        try {
            tenantSecurityService.validateAccessToResource(vente.getIdEntreprise());
        } catch (Exception e) {
            throw new RuntimeException("Accès refusé à la vente", e);
        }

        List<LigneVente> ligneVentes = ligneVenteRepository.findAllByVenteId(id);
        if (!ligneVentes.isEmpty()) {
            throw new InvalidEntityException("Impossible de supprimer une vente liée", ErrorCodes.VENTE_ALREADY_IN_USE);
        }

        venteRepository.deleteById(id);
    }

    @Override
    public List<LigneVenteDto> findAllLigneVentesByVenteId(Long id) {
        return ligneVenteRepository.findAllByVenteId(id).stream()
                .filter(lv -> Objects.equals(lv.getIdEntreprise(), tenantContext.getCurrentTenant()))
                .map(LigneVenteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getMontantTotalVentes(List<Ventes> ventes) {
        BigDecimal venteTotal = BigDecimal.ZERO;

        for (Ventes vente : ventes) {
            try {
                tenantSecurityService.validateAccessToResource(vente.getIdEntreprise());
            } catch (Exception e) {
                continue; // ignorer les ventes non autorisées
            }

            List<LigneVente> ligneVentes = ligneVenteRepository.findAllByVenteId(vente.getId());
            for (LigneVente ligneVente : ligneVentes) {
                if (ligneVente.getPrixUnitaire() != null && ligneVente.getQuantite() != null) {
                    venteTotal = venteTotal.add(ligneVente.getPrixUnitaire().multiply(ligneVente.getQuantite()));
                }
            }
        }

        return venteTotal;
    }

    @Override
    public VenteDto updateVente(Long id, VenteDto venteDto) {
        if (id == null) {
            log.error("Vente ID est null");
            throw new InvalidEntityException("ID de vente est requis", ErrorCodes.VENTE_NOT_VALID);
        }

        // Vérifier que la vente existe
        Ventes existingVente = venteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune vente n'a été trouvée avec l'ID " + id,
                        ErrorCodes.VENTE_NOT_FOUND));

        // Vérifier les droits d'accès à la vente existante
        try {
            tenantSecurityService.validateAccessToResource(existingVente.getIdEntreprise());
        } catch (Exception e) {
            throw new RuntimeException("Accès refusé à la vente", e);
        }

        // Valider les données de la vente à mettre à jour
        List<String> errors = VenteValidator.validate(venteDto);
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("Les données de la vente ne sont pas valides",
                    ErrorCodes.VENTE_NOT_VALID, errors);
        }

        // S'assurer que l'entreprise reste la même
        Long currentTenant = tenantContext.getCurrentTenant();
        if (!Objects.equals(existingVente.getIdEntreprise(), currentTenant)) {
            throw new RuntimeException("Tentative de modification d'une vente d'une autre entreprise");
        }

        // Vérifier si on essaie de modifier l'entreprise (non autorisé)
        if (venteDto.getIdEntreprise() != null &&
                !Objects.equals(venteDto.getIdEntreprise(), existingVente.getIdEntreprise())) {
            throw new InvalidEntityException("Impossible de modifier l'entreprise de la vente",
                    ErrorCodes.VENTE_NOT_VALID);
        }

        // Mettre à jour les informations de base de la vente
        updateVenteInfo(existingVente, venteDto);

        // Si des lignes de vente sont fournies, les gérer
        if (venteDto.getLigneVentes() != null && !venteDto.getLigneVentes().isEmpty()) {
            updateLignesVente(existingVente, venteDto);
        }

        // Sauvegarder la vente mise à jour
        Ventes updatedVente = venteRepository.save(existingVente);

        log.info("Vente mise à jour avec succès: {}", updatedVente.getCode());

        return VenteDto.fromEntity(updatedVente);
    }

    @Override
    public VenteDto updateVenteClient(Long id, VenteDto venteDto) {
        log.info("Mise à jour des informations client pour la vente ID: {}", id);

        if (id == null) {
            log.error("Vente ID est null");
            throw new InvalidEntityException("ID de vente est requis", ErrorCodes.VENTE_NOT_VALID);
        }
        // Vérifier que la vente existe
        Ventes existingVente = venteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune vente n'a été trouvée avec l'ID " + id,
                        ErrorCodes.VENTE_NOT_FOUND));
        try {
            tenantSecurityService.validateAccessToResource(existingVente.getIdEntreprise());
        } catch (Exception e) {
            throw new RuntimeException("Accès refusé à la vente", e);
        }
        VenteDto clientOnlyDto = createClientOnlyDto(venteDto);
        updateVenteInfo(existingVente, clientOnlyDto);
        Ventes updatedVente = venteRepository.save(existingVente);
        return VenteDto.fromEntity(updatedVente);
    }

    public List<VenteDto> get10LatestVentes() {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        // Récupérer toutes les ventes du tenant et trier par date décroissante
        List<Ventes> allVentes = venteRepository.findAll()
                .stream()
                .filter(v -> Objects.equals(v.getIdEntreprise(), currentTenant))
                .sorted((v1, v2) -> {
                    // Trier par date de vente décroissante (les plus récentes en premier)
                    if (v1.getDateVente() == null && v2.getDateVente() == null) {
                        return 0;
                    }
                    if (v1.getDateVente() == null) {
                        return 1; // Les ventes sans date à la fin
                    }
                    if (v2.getDateVente() == null) {
                        return -1; // Les ventes sans date à la fin
                    }
                    return v2.getDateVente().compareTo(v1.getDateVente());
                })
                .limit(10) // Limiter à 10 résultats
                .collect(Collectors.toList());

        // Convertir en DTOs
        List<VenteDto> result = allVentes.stream()
                .map(VenteDto::fromEntity)
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public Page<VenteDto> searchVentes(VenteSearchCriteria criteria) {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        log.info("Recherche de ventes avec critères: {} pour tenant: {}", criteria, currentTenant);

        // Créer la spécification
        Specification<Ventes> spec = VenteSpecification.withCriteria(criteria, currentTenant);

        // Exécuter la recherche paginée
        Page<Ventes> ventesPage = venteRepository.findAll(spec, criteria.toPageable());

        log.info("Trouvé {} ventes sur {} pages", ventesPage.getTotalElements(), ventesPage.getTotalPages());

        // Convertir en DTO
        return ventesPage.map(VenteDto::fromEntity);
    }

    @Override
    public Page<VenteDto> searchVentes(String nomClient, String prenomClient, String codeVente, Pageable pageable) {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        log.info("Recherche simple - nomClient: {}, prenomClient: {}, codeVente: {}",
                nomClient, prenomClient, codeVente);

        // Construire la spécification dynamiquement
        Specification<Ventes> spec = Specification.where(VenteSpecification.belongsToEntreprise(currentTenant));

        if (StringUtils.hasText(nomClient)) {
            spec = spec.and(VenteSpecification.hasNomClient(nomClient));
        }

        if (StringUtils.hasText(prenomClient)) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("prenomClient")), "%" + prenomClient.toLowerCase() + "%")
            );
        }

        if (StringUtils.hasText(codeVente)) {
            spec = spec.and(VenteSpecification.hasCodeVente(codeVente));
        }

        // Ajouter le tri par date décroissante si non spécifié
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "dateVente"));
        }

        Page<Ventes> ventesPage = venteRepository.findAll(spec, pageable);
        return ventesPage.map(VenteDto::fromEntity);
    }

    @Override
    public List<VenteDto> searchVentesByNomClient(String nomClient) {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        log.info("Recherche par nom client: {}", nomClient);

        // Utiliser les spécifications pour une recherche simple
        Specification<Ventes> spec = Specification
                .where(VenteSpecification.belongsToEntreprise(currentTenant))
                .and(VenteSpecification.hasNomClient(nomClient))
                .and((root, query, cb) -> {
                    query.orderBy(cb.desc(root.get("dateVente")));
                    return cb.conjunction();
                });

        List<Ventes> ventes = venteRepository.findAll(spec);
        log.info("Trouvé {} ventes pour le client: {}", ventes.size(), nomClient);

        return ventes.stream()
                .map(VenteDto::fromEntity)
                .collect(Collectors.toList());
    }




    /**
     * Crée un DTO avec uniquement les informations client
     */
    private VenteDto createClientOnlyDto(VenteDto originalDto) {
        return VenteDto.builder()
                .nomClient(originalDto.getNomClient())
                .prenomClient(originalDto.getPrenomClient())
                .numero(originalDto.getNumero())
                .adresse(originalDto.getAdresse())
                .commentaire(originalDto.getCommentaire())
                .build();
    }

    /**
     * Met à jour les informations de base de la vente
     */
    private void updateVenteInfo(Ventes existingVente, VenteDto venteDto) {
        // Mettre à jour les informations client
        existingVente.setNomClient(venteDto.getNomClient());
        existingVente.setPrenomClient(venteDto.getPrenomClient());
        existingVente.setNumero(venteDto.getNumero());
        existingVente.setAdresse(venteDto.getAdresse());
        existingVente.setCommentaire(venteDto.getCommentaire());

        // Mettre à jour le mode de paiement si fourni
        if (venteDto.getModePayement() != null) {
            existingVente.setModePayement(ModePayementDto.toEntity(venteDto.getModePayement()));        }

        // Mettre à jour la date de vente si fournie
        if (venteDto.getDateVente() != null) {
            existingVente.setDateVente(venteDto.getDateVente());
        }

        // Ne pas modifier le code et l'entreprise
        existingVente.setIdEntreprise(tenantContext.getCurrentTenant());
    }

    /**
     * Met à jour les lignes de vente
     * Note: Cette implémentation ajoute de nouvelles lignes sans supprimer les anciennes
     * Pour une mise à jour complète (suppression/modification), l'implémentation serait plus complexe
     */
    private void updateLignesVente(Ventes existingVente, VenteDto venteDto) {
        // Pour chaque ligne de vente dans le DTO
        for (LigneVenteDto ligneVenteDto : venteDto.getLigneVentes()) {
            // Vérifier l'article
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
            if (article.isEmpty()) {
                throw new InvalidEntityException("Article non trouvé avec l'ID " + ligneVenteDto.getArticle().getId(),
                        ErrorCodes.ARTICLE_NOT_FOUND);
            }

            try {
                tenantSecurityService.validateAccessToResource(article.get().getIdEntreprise());
            } catch (Exception e) {
                throw new RuntimeException("Accès refusé à l'article ID " + article.get().getId(), e);
            }

            // Créer une nouvelle ligne de vente
            LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
            ligneVente.setVente(existingVente);
            ligneVente.setArticle(article.get());
            ligneVente.setIdEntreprise(existingVente.getIdEntreprise());

            // Sauvegarder la ligne de vente
            ligneVenteRepository.save(ligneVente);

            // Mettre à jour le stock (sortie de stock)
            updateMvtStk(ligneVente);
        }

        // Recalculer le montant total après ajout de nouvelles lignes
        updateMontantTotal(existingVente);
    }

    /**
     * Recalcule le montant total de la vente
     */
    private void updateMontantTotal(Ventes vente) {
        List<LigneVente> lignesVente = ligneVenteRepository.findAllByVenteId(vente.getId());

        BigDecimal montantTotal = BigDecimal.ZERO;
        for (LigneVente ligne : lignesVente) {
            if (ligne.getPrixUnitaire() != null && ligne.getQuantite() != null) {
                montantTotal = montantTotal.add(
                        ligne.getPrixUnitaire().multiply(ligne.getQuantite())
                );
            }
        }

        // Mettre à jour le montant total (si votre entité a ce champ)
        // existingVente.setMontantTotal(montantTotal);
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

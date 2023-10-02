package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.*;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.*;
import com.ovd.gestionstock.repositories.ArticleRepository;
import com.ovd.gestionstock.repositories.CommandeFournisseurRepository;
import com.ovd.gestionstock.repositories.FournisseurRepository;
import com.ovd.gestionstock.repositories.LigneCommandeFournisseurRepository;
import com.ovd.gestionstock.services.CommandeFournisseurService;
import com.ovd.gestionstock.services.MvtStkService;
import com.ovd.gestionstock.validators.ArticleValidator;
import com.ovd.gestionstock.validators.CommandeFournisseurValidator;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {

    private final CommandeFournisseurRepository commandeFournisseurRepository;
    private final LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
    private final FournisseurRepository fournisseurRepository;
    private final ArticleRepository articleRepository;
    private final MvtStkService mvtStkService;
    private final JdbcTemplate jdbcTemplate;

    // private final AtomicLong codeCounter = new AtomicLong(0);


//    public CommandeFournisseurServiceImpl(CommandeFournisseurRepository commandeFournisseurRepository,
//                                          FournisseurRepository fournisseurRepository, ArticleRepository articleRepository,
//                                          LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository, MvtStkService mvtStkService) {
//        this.commandeFournisseurRepository = commandeFournisseurRepository;
//        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
//        this.fournisseurRepository = fournisseurRepository;
//        this.articleRepository = articleRepository;
//        this.mvtStkService = mvtStkService;
//    }

    @Override
    public CommandeFournisseurDto saveComFournisseur(CommandeFournisseurDto dto) {

        List<String> errors = CommandeFournisseurValidator.validate(dto);

        System.out.println(dto);


        if (!errors.isEmpty()) {
            log.error("Commande fournisseur n'est pas valide");
            throw new InvalidEntityException("La commande fournisseur n'est pas valide", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID, errors);
        }

        if (dto.getId() != null && dto.isCommandeLivree()) {
            throw new InvalidEntityException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(dto.getFournisseurDto().getId());
        if (fournisseur.isEmpty()) {
            log.warn("Fournisseur with ID {} was not found in the DB", dto.getFournisseurDto().getId());
            throw new EntityNotFoundException("Aucun fournisseur avec l'ID" + dto.getFournisseurDto().getId() + " n'a ete trouve dans la BDD",
                    ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }

        List<String> articleErrors = new ArrayList<>();

        if (dto.getLigneCommandeFournisseurDtos() != null) {
            dto.getLigneCommandeFournisseurDtos().forEach(ligCmdFrs -> {
                if (ligCmdFrs.getArticle() != null) {
                    Optional<Article> article = articleRepository.findById(ligCmdFrs.getArticle().getId());
                    if (article.isEmpty()) {
                        articleErrors.add("L'article avec l'ID " + ligCmdFrs.getArticle().getId() + " n'existe pas");
                    }
                } else {
                    articleErrors.add("Impossible d'enregister une commande avec un aticle NULL");
                }
            });
        }

        if (!articleErrors.isEmpty()) {
            log.warn("");
            throw new InvalidEntityException("Article n'existe pas dans la BDD", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }
        dto.setDateCommande(Instant.now());

        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('SEQ_COMMANDE_FOURNISSEUR')", Long.class);
        String code = "CMD-FN" + String.format("%07d", nextVal);

        dto.setCode(code);

        CommandeFournisseur savedCmdFrs = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(dto));
//        String uniqueId = generateUniqueCode();
//        dto.setCode(uniqueId);

        if (dto.getLigneCommandeFournisseurDtos() != null) {
            dto.getLigneCommandeFournisseurDtos().forEach(ligCmdFrs -> {
                LigneCommandeFournisseur ligneCommandeFournisseur = LigneCommandeFournisseurDto.toEntity(ligCmdFrs);
                ligneCommandeFournisseur.setCommandeFournisseur(savedCmdFrs);
                ligneCommandeFournisseur.setIdEntreprise(savedCmdFrs.getIdEntreprise());
                LigneCommandeFournisseur saveLigne = ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);

                effectuerEntree(saveLigne);
            });
        }

        return CommandeFournisseurDto.fromEntity(savedCmdFrs);
    }

//    private String generateUniqueCode() {
//        long code = codeCounter.incrementAndGet();
//        String uniqueId = "CMD-CL" + String.format("%05d", code); // Formatage pour obtenir un code à 5 chiffres
//        return uniqueId;
//    }


    @Override
    public CommandeFournisseurDto findById(Long id) {
        if (id == null) {
            log.error("Commande fournisseur ID is NULL");
            return null;
        }
        return commandeFournisseurRepository.findById(id)
                .map(CommandeFournisseurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande fournisseur n'a ete trouve avec l'ID " + id, ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
    }

    @Override
    public CommandeFournisseurDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Commande fournisseur CODE is NULL");
            return null;
        }
        return commandeFournisseurRepository.findCommandeFournisseurByCode(code)
                .map(CommandeFournisseurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande fournisseur n'a ete trouve avec le CODE " + code, ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
    }

    @Override
    public List<CommandeFournisseurDto> findAll() {
        return commandeFournisseurRepository.findAll().stream()
                .map(CommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Long idCommande) {
        return ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande).stream()
                .map(LigneCommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            log.error("Commande fournisseur ID is NULL");
            return;
        }

        List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(id);
        if (!ligneCommandeFournisseurs.isEmpty()) {
            throw new InvalidEntityException("Impossible de supprimer une commande fournisseur deja utilisee",
                    ErrorCodes.COMMANDE_FOURNISSEUR_ALREADY_IN_USE);
        }
        commandeFournisseurRepository.deleteById(id);
    }

    @Override
    public void deleteCommande(Long id) {
        if (id == null) {
            log.error("Commande fournisseur ID is NULL");
            return;
        }
        Optional<CommandeFournisseur> commandeFournisseur = commandeFournisseurRepository.findById(id);
        if (!commandeFournisseur.isPresent()){
            System.out.println("Commande n'existe pas !!!");
            return;
        }

        if(commandeFournisseur.get().getEtatCommande().equals(CommandeEtat.EN_PREPARATION)){
            commandeFournisseurRepository.deleteById(id);
        }

    }

    @Override
    public BigDecimal getMontantTotalComFournisseur(List<CommandeFournisseur> commandes) {
        BigDecimal venteTotal = BigDecimal.ZERO;

        for (CommandeFournisseur commande : commandes) {
            List<LigneCommandeFournisseur> ligneCommandes = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(commande.getId());
            if (ligneCommandes != null) {
                for (LigneCommandeFournisseur ligne : ligneCommandes) {
                    if (ligne.getPrixUnitaire() != null && ligne.getQuantite() != null) {
                        BigDecimal prixUnitaire = new BigDecimal(String.valueOf(ligne.getPrixUnitaire()));
                        BigDecimal quantite = new BigDecimal(String.valueOf(ligne.getQuantite()));
                        BigDecimal montantLigne = prixUnitaire.multiply(quantite);
                        venteTotal = venteTotal.add(montantLigne);

                    }
                }
            }
        }
        log.info(String.valueOf(venteTotal));
        return venteTotal;
    }

    @Override
    public List<CommandeFournisseurDto> getCommandesByFournisseur(String nom, String email, String codeCommande) {
        if (nom != null && email != null && codeCommande != null) {
            return commandeFournisseurRepository.findByFournisseurNomAndFournisseurEmailAndCode(nom, email, codeCommande)
                    .stream()
                    .map(CommandeFournisseurDto::fromEntity)
                    .collect(Collectors.toList());
        } else if (nom != null && email != null) {
            return commandeFournisseurRepository.findByFournisseurNomAndFournisseurEmail(nom, email)
                    .stream()
                    .map(CommandeFournisseurDto::fromEntity)
                    .collect(Collectors.toList());
        } else if (codeCommande != null) {
            return commandeFournisseurRepository.findByCode(codeCommande)
                    .stream()
                    .map(CommandeFournisseurDto::fromEntity)
                    .collect(Collectors.toList());
        } else {
            // Gérer le cas où aucun critère n'est spécifié
            return commandeFournisseurRepository.findAll()
                    .stream()
                    .map(CommandeFournisseurDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public CommandeFournisseurDto updateEtatCommande(Long idCommande, CommandeEtat etatCommande) {
        checkIdCommande(idCommande);
        if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
            log.error("L'etat de la commande fournisseur is NULL");
            throw new InvalidEntityException("Impossible de modifier l'etat de la commande avec un etat null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        commandeFournisseur.setEtatCommande(etatCommande);

        CommandeFournisseur savedCommande = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseur));
        if (commandeFournisseur.isCommandeLivree()) {
            updateMvtStk(idCommande);
        }
        return CommandeFournisseurDto.fromEntity(savedCommande);
    }

    @Override
    public CommandeFournisseurDto updateQuantiteCommande(Long idCommande, Long idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidEntityException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = findLigneCommandeFournisseur(idLigneCommande);

        LigneCommandeFournisseur ligneCommandeFounisseur = ligneCommandeFournisseurOptional.get();
        ligneCommandeFounisseur.setQuantite(quantite);
        ligneCommandeFournisseurRepository.save(ligneCommandeFounisseur);

        return commandeFournisseur;
    }

    @Override
    public CommandeFournisseurDto updateFournisseur(Long idCommande, Long idFournisseur) {
        checkIdCommande(idCommande);
        if (idFournisseur == null) {
            log.error("L'ID du fournisseur is NULL");
            throw new InvalidEntityException("Impossible de modifier l'etat de la commande avec un ID fournisseur null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        Optional<Fournisseur> fournisseurOptional = fournisseurRepository.findById(idFournisseur);
        if (fournisseurOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucun fournisseur n'a ete trouve avec l'ID " + idFournisseur, ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }
        commandeFournisseur.setFournisseurDto(FournisseurDto.fromEntity(fournisseurOptional.get()));

        return CommandeFournisseurDto.fromEntity(
                commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseur))
        );
    }

    @Override
    public CommandeFournisseurDto updateArticle(Long idCommande, Long idLigneCommande, Long idArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(idArticle, "nouvel");

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);

        Optional<LigneCommandeFournisseur> ligneCommandeFournisseur = findLigneCommandeFournisseur(idLigneCommande);

        Optional<Article> articleOptional = articleRepository.findById(idArticle);
        if (articleOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucune article n'a ete trouve avec l'ID " + idArticle, ErrorCodes.ARTICLE_NOT_FOUND);
        }

        List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("Article invalid", ErrorCodes.ARTICLE_NOT_FOUND, errors);
        }

        LigneCommandeFournisseur ligneCommandeFournisseurToSaved = ligneCommandeFournisseur.get();
        ligneCommandeFournisseurToSaved.setArticle(articleOptional.get());
        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseurToSaved);

        return commandeFournisseur;
    }

    @Override
    public CommandeFournisseurDto deleteArticle(Long idCommande, Long idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        // Just to check the LigneCommandeFournisseur and inform the fournisseur in case it is absent
        findLigneCommandeFournisseur(idLigneCommande);
        ligneCommandeFournisseurRepository.deleteById(idLigneCommande);

        return commandeFournisseur;
    }

    private CommandeFournisseurDto checkEtatCommande(Long idCommande) {
        CommandeFournisseurDto commandeFournisseur = findById(idCommande);
        if (commandeFournisseur.isCommandeLivree()) {
            throw new InvalidEntityException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        return commandeFournisseur;
    }

    private Optional<LigneCommandeFournisseur> findLigneCommandeFournisseur(Long idLigneCommande) {
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = ligneCommandeFournisseurRepository.findById(idLigneCommande);
        if (ligneCommandeFournisseurOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucune ligne commande fournisseur n'a ete trouve avec l'ID " + idLigneCommande, ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID);
        }
        return ligneCommandeFournisseurOptional;
    }

    private void checkIdCommande(Long idCommande) {
        if (idCommande == null) {
            log.error("Commande fournisseur ID is NULL");
            throw new InvalidEntityException("Impossible de modifier l'etat de la commande avec un ID null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private void checkIdLigneCommande(Long idLigneCommande) {
        if (idLigneCommande == null) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidEntityException("Impossible de modifier l'etat de la commande avec une ligne de commande null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private void checkIdArticle(Long idArticle, String msg) {
        if (idArticle == null) {
            log.error("L'ID de " + msg + " is NULL");
            throw new InvalidEntityException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private void updateMvtStk(Long idCommande) {
        List<LigneCommandeFournisseur> ligneCommandeFournisseur = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande);
        ligneCommandeFournisseur.forEach(lig -> {
            effectuerEntree(lig);
        });
    }

    private void effectuerEntree(LigneCommandeFournisseur lig) {
        MvtStkDto mvtStkDto = MvtStkDto.builder()
                .article(ArticleDto.fromEntity(lig.getArticle()))
                .dateMvt(Instant.now())
                .typeMvtStk(TypeMvtStk.ENTREE)
                .sourceMvt(SourceMvt.COMMANDE_FOURNISSEUR)
                .quantite(lig.getQuantite())
                .idEntreprise(lig.getIdEntreprise())
                .unite(lig.getUnite())
                .build();

        mvtStkService.entreeMvtStk(mvtStkDto);
    }
}

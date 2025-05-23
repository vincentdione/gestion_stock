package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.*;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.*;
import com.ovd.gestionstock.repositories.*;
import com.ovd.gestionstock.services.CommandeClientService;
import com.ovd.gestionstock.services.MvtStkService;
import com.ovd.gestionstock.services.TenantSecurityService;
import com.ovd.gestionstock.validators.ArticleValidator;
import com.ovd.gestionstock.validators.CommandeClientValidator;
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
public class CommandeClientServiceImpl implements CommandeClientService {

    private final CommandeClientRepository commandeClientRepository;
    private final LivraisonRepository livraisonRepository;

    private final ClientRepository clientRepository;

    private final ArticleRepository articleRepository;

    private final LigneCommandeClientRepository ligneClientRepository;

    private final MvtStkService mvtStkService;
    private final JdbcTemplate jdbcTemplate;
    private final TenantSecurityService tenantSecurityService;
    private final TenantContext tenantContext;


    @Override
    public List<CommandeClientDto> getAllCommandeClient() {

        return commandeClientRepository.findAll().stream().map(CommandeClientDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteCommandeClient(Long id) {
        if (id == null){
            log.error("Commande Client ID is NULL");
            return;
        }
        List<LigneCommandeClient> ligneCommandeClients = ligneClientRepository.findAllByCommandeClientId(id);
        if (!ligneCommandeClients.isEmpty()) {
            throw new InvalidEntityException("Impossible de supprimer une commande client deja utilisee",
                    ErrorCodes.COMMANDE_FOURNISSEUR_ALREADY_IN_USE);
        }

        commandeClientRepository.deleteById(id);
    }

    @Override
    public CommandeClientDto getCommandeClientById(Long id) {
        if(id == null){
            log.error("ID COMMANDE IS NULL");
            return null;
        }
        Optional<CommandeClient> getCom = commandeClientRepository.findById(id);
        return CommandeClientDto.fromEntity(getCom.get());
    }

    @Override
    public CommandeClientDto getCommandeClientByCode(String code) {
        if(code == null){
            log.error("CODE COMMANDE IS NULL");
            return null;
        }
        Optional<CommandeClient> getCom = commandeClientRepository.findByCode(code);
        return CommandeClientDto.fromEntity(getCom.get());
    }

    @Override
    public List<LigneCommandeClientDto> findAllLignesCommandesClientByCommandeClientId(Long idCommande) {

        return ligneClientRepository.
                findAllByCommandeClientId(idCommande)
                .stream().map(LigneCommandeClientDto::fromEntity).collect(Collectors.toList());
    }





    public void createCommandeClient(CommandeClientDto request) {
        List<String> errors = CommandeClientValidator.validate(request);

        System.out.println("+++++++++++++++++++" + request);

        if (!errors.isEmpty()) {
            log.error("Vérifier les champs obligatoires !");
            throw new InvalidEntityException(
                    "La commande client n'est pas valide",
                    ErrorCodes.COMMANDE_CLIENT_NOT_FOUND,
                    errors
            );
        }

        if (request.getId() != null && request.isCommandeLivree()) {
            log.warn("Commande déjà LIVREE");
            throw new InvalidEntityException(
                    "Cette commande ne peut pas être modifiée, elle est déjà livrée",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE
            );
        }

        Optional<Client> client = clientRepository.findById(request.getClientDto().getId());
        if (!client.isPresent()) {
            log.warn("Ce client avec ID {} n'a pas été trouvé " + request.getClientDto().getId());
            throw new EntityNotFoundException(
                    "Aucun client avec l'ID " + request.getClientDto().getId() + " n'a été trouvé dans la base",
                    ErrorCodes.CLIENT_NOT_FOUND
            );
        }

        List<String> articleErrors = new ArrayList<>();
        if (request.getLigneCommandeClients() != null) {
            request.getLigneCommandeClients().forEach(ligCmdClt -> {
                if (ligCmdClt.getArticle() != null) {
                    Optional<Article> article = articleRepository.findById(ligCmdClt.getArticle().getId());
                    if (article.isEmpty()) {
                        articleErrors.add("L'article avec l'ID " + ligCmdClt.getArticle().getId() + " n'existe pas");
                    }
                } else {
                    articleErrors.add("Impossible d'enregistrer une commande avec un article NULL");
                }
            });
        }

        if (!articleErrors.isEmpty()) {
            log.warn("");
            throw new InvalidEntityException(
                    "Article n'existe pas dans la BDD",
                    ErrorCodes.ARTICLE_NOT_FOUND,
                    articleErrors
            );
        }

        // Gestion du tenant
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('SEQ_COMMANDE_CLIENT')", Long.class);
        String code = "CMD-CLI" + String.format("%07d", nextVal);
        String codeLivraison = "LIV" + String.format("%07d", nextVal);

        request.setCode(code);

        CommandeClient commande = CommandeClientDto.toEntity(request);
        commande.setIdEntreprise(currentTenant); // Affecter le tenant à la commande

        CommandeClient savedCom = commandeClientRepository.save(commande);

        if (request.getLigneCommandeClients() != null) {
            request.getLigneCommandeClients().forEach(ligneCmt -> {
                LigneCommandeClient ligneCommandeClient = LigneCommandeClientDto.toEntity(ligneCmt);
                ligneCommandeClient.setCommandeClient(savedCom);
                ligneCommandeClient.setIdEntreprise(currentTenant); // Affecter le tenant à chaque ligne
                LigneCommandeClient savedLigneCmd = ligneClientRepository.save(ligneCommandeClient);
                effectuerSortie(savedLigneCmd);
            });
        }

        LivraisonDto livraisonDto = new LivraisonDto();
        livraisonDto.setEtat(LivraisonEtat.EN_COURS);
        livraisonDto.setCode(codeLivraison);
        livraisonDto.setCommandeClient(CommandeClientDto.fromEntity(savedCom));
        livraisonDto.setIdEntreprise(currentTenant);
        livraisonRepository.save(LivraisonDto.toEntity(livraisonDto));
    }



    @Override
    public CommandeClientDto deleteArticle(Long idCommande, Long idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        CommandeClientDto commandeClientDto = checkEtatCommande(idCommande);
        findLigneCommandeClient(idLigneCommande);
        ligneClientRepository.deleteById(idLigneCommande);
        return commandeClientDto;
    }

    @Override
    public CommandeClientDto updateEtatCommande(Long idCommande, CommandeEtat commandeEtat) {
        checkIdCommande(idCommande);
        if(!StringUtils.hasLength(String.valueOf(commandeEtat))){
            log.error("Commande Client ID is null");
            throw new InvalidEntityException("Impossible de modifier l'état de la commande avec un ID",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClientDto commandeClient = checkEtatCommande(idCommande);
        commandeClient.setEtat(commandeEtat);

        CommandeClient savedCommandeClient = commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient));
        if (commandeClient.isCommandeLivree()){
            updateMvtStk(idCommande);
        }

        return CommandeClientDto.fromEntity(savedCommandeClient);
    }

    @Override
    public CommandeClientDto updateClient(Long idCommande, Long idClient) {
        checkIdCommande(idCommande);

        CommandeClientDto commandeClient = checkEtatCommande(idCommande);

        Optional<Client> clientOptional = clientRepository.findById(idClient);

        if(idClient == null || clientOptional.isEmpty()){
            log.error(" client ID is null");
            throw new InvalidEntityException("l'ID du client est null ", ErrorCodes.CLIENT_NOT_FOUND);
        }

        commandeClient.setClientDto(ClientDto.fromEntity(clientOptional.get()));

        return CommandeClientDto.fromEntity(commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient)));
    }

    @Override
    public CommandeClientDto updateArticle(Long idCommande, Long idLigneCommande, Long idNewArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(idNewArticle,"Nouvel");
        CommandeClientDto commandeClient = checkEtatCommande(idCommande);

        Optional<LigneCommandeClient> ligneCommandeClientOptional = findLigneCommandeClient(idLigneCommande);

        Optional<Article> articleArticleOptional = articleRepository.findById(idNewArticle);
        if (articleArticleOptional.isEmpty()){
            log.error(" client ID is null");
            throw new EntityNotFoundException("l'ID du client est null ", ErrorCodes.ARTICLE_NOT_FOUND);
        }

        List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleArticleOptional.get()));
        if(!errors.isEmpty()){
            log.error("Vérifier les champs obligatoires !");
            throw new InvalidEntityException("Article est invalide", ErrorCodes.ARTICLE_NOT_FOUND,errors);
        }
        LigneCommandeClient savedLigneCommande = ligneCommandeClientOptional.get();
        savedLigneCommande.setArticle(articleArticleOptional.get());

        ligneClientRepository.save(savedLigneCommande);

        return commandeClient;
    }

    @Override
    public CommandeClientDto updateQuantieCommande(Long idCommande, Long idLigneCommande, BigDecimal quantity) {

        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);



        if(quantity == null || quantity.compareTo(BigDecimal.ZERO) == 0){
            log.error("Ligne Commande client ID is null");
            throw new InvalidEntityException("Impossible de modifier l'état de la commande avec une quantité nulle ou Zero", ErrorCodes.COMMANDE_CLIENT_NOT_FOUND);
        }

        CommandeClientDto commandeClient = checkEtatCommande(idCommande);
        Optional<LigneCommandeClient> ligneCommandeClientOptional = findLigneCommandeClient(idLigneCommande);

        LigneCommandeClient ligneCommandeClient = ligneCommandeClientOptional.get();

        ligneCommandeClient.setQuantite(quantity);

        ligneClientRepository.save(ligneCommandeClient);


        return commandeClient;
    }

    @Override
    public BigDecimal getMontantTotalComClient(List<CommandeClient> commandes) {
        BigDecimal venteTotal = BigDecimal.ZERO;

        for (CommandeClient commande : commandes) {
            List<LigneCommandeClient> ligneCommandes = ligneClientRepository.findAllByCommandeClientId(commande.getId());
            if (ligneCommandes != null) {
                for (LigneCommandeClient ligne : ligneCommandes) {
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
    public List<CommandeClientDto> getCommandesByClient(String nom, String email, String codeCommande) {
        System.out.println("******************************************************************");
        System.out.println("codeCommande"+codeCommande);
        System.out.println("nom"+nom);
        System.out.println("email"+email);
        List<CommandeClient> commandeClients = new ArrayList<>();
        if (nom != null && email != null && codeCommande != null) {
            commandeClients = commandeClientRepository.findByClientNomAndClientEmailAndCode(nom, email, codeCommande);
        } else if (nom != null && email != null) {
            commandeClients = commandeClientRepository.findByClientNomAndClientEmail(nom, email);
        } else if (codeCommande != null && nom == null && email == null) {
            System.out.println(" codeCommande != null && nom == null && email == null ");
            Optional<CommandeClient> optionalCommandeClient  = commandeClientRepository.findByCode(codeCommande);
           // CommandeClient commandeClient = optionalCommandeClient.orElse(new CommandeClient());
            if (optionalCommandeClient.isPresent()) {
                commandeClients.add(optionalCommandeClient.get());
            }
            else {
                System.out.println(" pas de commandes avec ce code !!!");
            }
        }
        else if (nom != null && codeCommande == null && email == null) {
            System.out.println(" nom != null && codeCommande == null && email == null ");

            Optional<CommandeClient> optionalCommandeClient  = commandeClientRepository.findByClientNom(nom);
            //CommandeClient commandeClient = optionalCommandeClient.orElse(new CommandeClient());
            if (optionalCommandeClient.isPresent()) {
                commandeClients.add(optionalCommandeClient.get());
            }
            else {
                System.out.println(" pas de commandes avec ce nom !!!");
            }
        }
        else if (email != null && codeCommande == null && nom == null) {
            System.out.println(" email != null && codeCommande == null && nom == null");

            Optional<CommandeClient> optionalCommandeClient  = commandeClientRepository.findByClientEmail(email);
            //CommandeClient commandeClient = optionalCommandeClient.orElse(new CommandeClient());

            if (optionalCommandeClient.isPresent()) {
                commandeClients.add(optionalCommandeClient.get());
            }
            else {
                System.out.println(" pas de commandes avec cet email !!!");
            }
        }
        else {
            System.out.println(" ENTRE ICI !!!!!!!!!!!");
            // Gérer le cas où aucun critère n'est spécifié
            commandeClients = commandeClientRepository.findAll();

        }
        System.out.println("taille "+commandeClients.size());
        System.out.println("******************************************************************");

        return commandeClients
                .stream()
                .map(CommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    private Optional<LigneCommandeClient> findLigneCommandeClient(Long idLigneCommande) {
        Optional<LigneCommandeClient> ligneCommandeClientOptional = ligneClientRepository.findById(idLigneCommande);
        if (ligneCommandeClientOptional.isEmpty()){
            throw new InvalidEntityException("Aucune ligne de commande n'a été trouvé",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        return ligneCommandeClientOptional;
    }


    private void checkIdCommande(Long idCommande){
        if(idCommande == null){
            log.error("Commande client IDis null");
            throw new InvalidEntityException("la commande client n'est pas valide", ErrorCodes.COMMANDE_CLIENT_NOT_FOUND);
        }
    }

    private void checkIdLigneCommande(Long idLigneCommande){
        if(idLigneCommande == null){
            log.error("Ligne Commande client ID is null");
            throw new InvalidEntityException("la commande client n'est pas valide", ErrorCodes.COMMANDE_CLIENT_NOT_FOUND);
        }
    }

    private void checkIdArticle(Long idArticle,String msg){
        if(idArticle == null){
            log.error("ID de {msg} Article est null");
            throw new InvalidEntityException("Impossible de modifier l'état de la commande avec un "+msg + " ID article null", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private CommandeClientDto checkEtatCommande(Long idCommande){
        CommandeClientDto commandeClient = getCommandeClientById(idCommande);
        if (commandeClient.isCommandeLivree()){
            throw new InvalidEntityException("Impossible de modifier la commande lorsqu'elle est livrée",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        return commandeClient;
    }


    private void updateMvtStk(Long idCommande){
        List<LigneCommandeClient> ligneCommandeClients = ligneClientRepository.findAllByCommandeClientId(idCommande);
        ligneCommandeClients.forEach(lig -> {
            effectuerSortie(lig);
        });
    }

    private void effectuerSortie(LigneCommandeClient lig) {
        MvtStkDto mvtStkDto = MvtStkDto.builder()
                .article(ArticleDto.fromEntity(lig.getArticle()))
                .dateMvt(Instant.now())
                .typeMvtStk(TypeMvtStk.SORTIE)
                .sourceMvt(SourceMvt.COMMANDE_CLIENT)
                .quantite(lig.getQuantite())
                .idEntreprise(lig.getIdEntreprise())
                .unite(lig.getUnite())
                .build();
        mvtStkService.sortieMvtStk(mvtStkDto);
    }

}

package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.*;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.*;
import com.ovd.gestionstock.repositories.*;
import com.ovd.gestionstock.services.CommandeClientService;
import com.ovd.gestionstock.services.MvtStkService;
import com.ovd.gestionstock.validators.ArticleValidator;
import com.ovd.gestionstock.validators.CommandeClientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
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

    private final ClientRepository clientRepository;

    private final ArticleRepository articleRepository;

    private final LigneCommandeClientRepository ligneClientRepository;

    private final MvtStkService mvtStkService;

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





    @Override
    public void createCommandeClient(CommandeClientDto request) {
        List<String> errors = CommandeClientValidator.validate(request);

        if(!errors.isEmpty()){
            log.error("Vérifier les champs obligatoires !");
            throw new InvalidEntityException("la commande client n'est pas valide", ErrorCodes.COMMANDE_CLIENT_NOT_FOUND,errors);
        }

        System.out.println(request);

        if (request.getId() != null && request.isCommandeLivree()){
            log.warn("Commande déja LIVREE");
            throw new InvalidEntityException("Cette commande ne peut pas etre modifiée,elle est déja livrée",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }


        Optional<Client> client = clientRepository.findById(request.getClientDto().getId());
        if(!client.isPresent()){
            log.warn("Ce client avec ID {} n'a pas été trouvé "+request.getClientDto().getId());
            throw new EntityNotFoundException("Aucun client avec l'ID " + request.getClientDto().getId() +"n'a été trouvé dans la base",ErrorCodes.CLIENT_NOT_FOUND);
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
                    articleErrors.add("Impossible d'enregister une commande avec un aticle NULL");
                }
            });
        }

        if (!articleErrors.isEmpty()) {
            log.warn("");
            throw new InvalidEntityException("Article n'existe pas dans la BDD", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }

            CommandeClient savedCom = commandeClientRepository.save(CommandeClientDto.toEntity(request));

            if (request.getLigneCommandeClients() != null) {
                request.getLigneCommandeClients().forEach(ligneCmt ->{
                    LigneCommandeClient ligneCommandeClient = LigneCommandeClientDto.toEntity(ligneCmt);
                    ligneCommandeClient.setCommandeClient(savedCom);
                    LigneCommandeClient savedLigneCmd = ligneClientRepository.save(ligneCommandeClient);

                    effectuerSortie(savedLigneCmd);

                });
            }

            CommandeClientDto.fromEntity(savedCom);
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

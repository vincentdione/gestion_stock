package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.CommandeClientDto;
import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.models.CommandeClient;
import com.ovd.gestionstock.models.CommandeEtat;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeClientService {

     List<CommandeClientDto> getAllCommandeClient();
     void deleteCommandeClient(Long id);

     CommandeClientDto getCommandeClientById(Long id);
     CommandeClientDto getCommandeClientByCode(String code);

    List<LigneCommandeClientDto> findAllLignesCommandesClientByCommandeClientId(Long id);

    void createCommandeClient(CommandeClientDto request);

    CommandeClientDto deleteArticle(Long idCommande,Long idLigneCommande);

    CommandeClientDto updateEtatCommande(Long idCommande, CommandeEtat commandeEtat);
    CommandeClientDto updateClient(Long idCommande, Long idClient);
    CommandeClientDto updateArticle(Long idCommande, Long idLigneCommande,Long idNewArticle);
    CommandeClientDto updateQuantieCommande(Long idCommande, Long idLigneCommande, BigDecimal quantity);
}

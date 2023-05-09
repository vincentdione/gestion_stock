package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.CommandeClientController;
import com.ovd.gestionstock.dto.CommandeClientDto;
import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.models.CommandeEtat;
import com.ovd.gestionstock.services.CommandeClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommandeClientApi implements CommandeClientController {

    private  final CommandeClientService commandeClientService;

    @Override
    public ResponseEntity<CommandeClientDto> save(CommandeClientDto request) {
        return null;
    }

    @Override
    public ResponseEntity<CommandeClientDto> updateEtatCommande(Long idCommande, CommandeEtat etatCommande) {
        return ResponseEntity.ok(commandeClientService.updateEtatCommande(idCommande, etatCommande));
    }

    @Override
    public ResponseEntity<CommandeClientDto> updateClient(Long idCommande, Long idClient) {
        return ResponseEntity.ok(commandeClientService.updateClient(idCommande,idClient));
    }

    @Override
    public ResponseEntity<CommandeClientDto> updateQuantiteCommande(Long idCommande, Long idLigneCommande, BigDecimal quantite) {
        return ResponseEntity.ok(commandeClientService.updateQuantieCommande(idCommande,idLigneCommande,quantite));
    }

    @Override
    public ResponseEntity<CommandeClientDto> updateArticle(Long idCommande, Long idLigneCommande, Long idArticle) {
        return ResponseEntity.ok(commandeClientService.updateArticle(idCommande,idLigneCommande,idArticle));
    }

    @Override
    public ResponseEntity<List<CommandeClientDto>> getAllCommandeClients() {

        return null;
    }

    @Override
    public ResponseEntity<CommandeClientDto> getCommandeClientById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<LigneCommandeClientDto>> findAllLignesCommandesClientByCommandeClientId(Long idCommande) {
        return ResponseEntity.ok(commandeClientService.findAllLignesCommandesClientByCommandeClientId(idCommande));
    }

    @Override
    public ResponseEntity<Void> deleteCommandeClient(Long id) {

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<CommandeClientDto> deleteArticle(Long idCommande, Long idLigneCommande) {
        return ResponseEntity.ok(commandeClientService.deleteArticle(idCommande,idLigneCommande));
    }
}

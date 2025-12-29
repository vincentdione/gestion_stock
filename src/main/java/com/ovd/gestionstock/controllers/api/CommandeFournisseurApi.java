package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.CommandeFournisseurController;
import com.ovd.gestionstock.dto.*;
import com.ovd.gestionstock.models.CommandeEtat;
import com.ovd.gestionstock.services.CommandeFournisseurService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "commandeFournisseurs")
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class CommandeFournisseurApi implements CommandeFournisseurController {

    private  final CommandeFournisseurService commandeFournisseurService;
    @Override
    public ResponseEntity<CommandeFournisseurDto> saveCommandeFournisseurs(CommandeFournisseurDto request) {
        return ResponseEntity.ok(commandeFournisseurService.saveComFournisseur(request));
    }

    @Override
    public ResponseEntity<CommandeFournisseurDto> updateEtatCommande(Long idCommande, CommandeEtat etatCommande) {
        return ResponseEntity.ok(commandeFournisseurService.updateEtatCommande(idCommande,etatCommande));
    }

    @Override
    public ResponseEntity<CommandeFournisseurDto> updateFournisseur(Long idCommande, Long idFournisseur) {
        return ResponseEntity.ok(commandeFournisseurService.updateFournisseur(idCommande,idFournisseur));
    }

    @Override
    public ResponseEntity<CommandeFournisseurDto> updateQuantiteCommande(Long idCommande, Long idLigneCommande, BigDecimal quantite) {
        return ResponseEntity.ok(commandeFournisseurService.updateQuantiteCommande(idCommande,idLigneCommande,quantite));
    }

    @Override
    public ResponseEntity<CommandeFournisseurDto> updateArticle(Long idCommande, Long idLigneCommande, Long idArticle) {
        return ResponseEntity.ok(commandeFournisseurService.updateArticle(idCommande,idLigneCommande,idArticle));
    }

    @Override
    public ResponseEntity<List<CommandeFournisseurDto>> getAllCommandeFournisseurs() {
        return ResponseEntity.ok(commandeFournisseurService.findAll());
    }

    @Override
    public ResponseEntity<CommandeFournisseurDto> getCommandeFournisseurById(Long idCommande) {
        return ResponseEntity.ok(commandeFournisseurService.findById(idCommande));
    }

    @Override
    public ResponseEntity<List<LigneCommandeFournisseurDto>> findAllLignesCommandesFournisseurByCommandeFournisseurId(Long idCommande) {
        return ResponseEntity.ok(commandeFournisseurService.findAllLignesCommandesFournisseurByCommandeFournisseurId(idCommande));
    }

    @Override
    public ResponseEntity<Void> deleteCommandeFournisseur(Long idCommande) {
        commandeFournisseurService.delete(idCommande);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<CommandeFournisseurDto> deleteFournisseurArticle(Long idCommande, Long idLigneCommande) {
        return ResponseEntity.ok(commandeFournisseurService.deleteArticle(idCommande,idLigneCommande));
    }

    @Override
    public ResponseEntity<BigDecimal> getMontantTotalComFournisseur() {
        List<CommandeFournisseurDto> commandes = commandeFournisseurService.findAll();
        if(!commandes.isEmpty()){
            return ResponseEntity.ok(commandeFournisseurService.getMontantTotalComFournisseur(commandes.stream().map(CommandeFournisseurDto::toEntity).collect(Collectors.toList())));
        }
        else {
            System.out.println("pas de Commandes trouvées !!!");
            return null;
        }
    }

    @GetMapping(value="/search/comfournisseurs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommandeFournisseurDto>> getCommandesByFournisseur(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String codeCommande) {
        return ResponseEntity.ok(commandeFournisseurService.getCommandesByFournisseur(nom, email, codeCommande));
    }

}

package com.ovd.gestionstock.services;

import com.ovd.gestionstock.criteria.CommandeFournisseurSearchCriteria;
import com.ovd.gestionstock.dto.CommandeFournisseurDto;
import com.ovd.gestionstock.dto.LigneCommandeFournisseurDto;
import com.ovd.gestionstock.models.CommandeEtat;
import com.ovd.gestionstock.models.CommandeFournisseur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeFournisseurService {

    CommandeFournisseurDto saveComFournisseur(CommandeFournisseurDto dto);

    CommandeFournisseurDto updateEtatCommande(Long idCommande, CommandeEtat etatCommande);

    CommandeFournisseurDto updateQuantiteCommande(Long idCommande, Long idLigneCommande, BigDecimal quantite);

    CommandeFournisseurDto updateFournisseur(Long idCommande, Long idFournisseur);

    CommandeFournisseurDto updateArticle(Long idCommande, Long idLigneCommande, Long idArticle);

    // Delete article ==> delete LigneCommandeFournisseur
    CommandeFournisseurDto deleteArticle(Long idCommande, Long idLigneCommande);

    CommandeFournisseurDto findById(Long id);

    CommandeFournisseurDto findByCode(String code);

    List<CommandeFournisseurDto> findAll();

    List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Long idCommande);

    void delete(Long id);
    void deleteCommande(Long id);

    BigDecimal getMontantTotalComFournisseur(List<CommandeFournisseur> commandes);

    List<CommandeFournisseurDto> getCommandesByFournisseur(String nom, String email, String codeCommande);

    // Méthodes de recherche avancée
    List<CommandeFournisseurDto> searchCommandesFournisseur(CommandeFournisseurSearchCriteria criteria);
    Page<CommandeFournisseurDto> searchCommandesFournisseurPage(CommandeFournisseurSearchCriteria criteria, Pageable pageable);
    List<CommandeFournisseurDto> searchCommandesFournisseurByText(String searchText);

}

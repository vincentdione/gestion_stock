package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.LivraisonDto;

import java.util.List;

public interface LivraisonService {

     List<LivraisonDto> getAllLivraisons();
     void deleteLivraison(Long id);

     LivraisonDto getLivraisonById(Long id);

     LivraisonDto updateStatut(Long id,String etat);

     LivraisonDto affecterLivraison(Long livraisonId);

     public List<LivraisonDto> voirLivraisonsUtilisateurEnCours();
     List<LivraisonDto> getLivraisonsByStatut(String statut);
}

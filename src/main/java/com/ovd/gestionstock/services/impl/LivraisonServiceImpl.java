package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.LivraisonDto;
import com.ovd.gestionstock.dto.MvtStkDto;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.CommandeClient;
import com.ovd.gestionstock.models.Livraison;
import com.ovd.gestionstock.models.TypeMvtStk;
import com.ovd.gestionstock.repositories.CommandeClientRepository;
import com.ovd.gestionstock.repositories.LivraisonRepository;
import com.ovd.gestionstock.repositories.MvtStkRepository;
import com.ovd.gestionstock.services.ArticleService;
import com.ovd.gestionstock.services.LivraisonService;
import com.ovd.gestionstock.services.MvtStkService;
import com.ovd.gestionstock.validators.MvtStkValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LivraisonServiceImpl implements LivraisonService {

    private final LivraisonRepository livraisonRepository;

    private final CommandeClientRepository commandeClientRepository;


    @Override
    public List<LivraisonDto> getAllLivraisons() {
        return livraisonRepository.findAll().stream().map(LivraisonDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteLivraison(Long id) {
        if (id == null){
            log.error("Id Mode Payement est null");
            return;
        }
        try {
            livraisonRepository.deleteById(id);
            log.info("La livraison avec l'ID {} a été supprimé avec succès", id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Aucune livraison trouvée avec l'ID {}. Aucune suppression effectuée.", id);
        } catch (Exception e) {
            log.error("Une erreur s'est produite lors de la suppression de la livraison avec l'ID " + id, e);
        }
    }

    @Override
    public LivraisonDto getLivraisonById(Long id) {
        // Validation du paramètre
        Objects.requireNonNull(id, "L'ID de la livraison ne peut pas être nul");

        Optional<Livraison> findLivraison = livraisonRepository.findById(id);

        if (!findLivraison.isPresent()) {
            log.warn("Aucune livraion trouvée avec l'ID {}. Aucune conversion en DTO effectuée.", id);
            return null;
        }

        return LivraisonDto.fromEntity(findLivraison.get());
    }
}

package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.EntrepriseDto;
import com.ovd.gestionstock.dto.UtilisateurDto;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.exceptions.InvalidOperationException;
import com.ovd.gestionstock.models.Entreprise;
import com.ovd.gestionstock.models.FileInfo;
import com.ovd.gestionstock.models.Role;
import com.ovd.gestionstock.repositories.EntrepriseRepository;
import com.ovd.gestionstock.services.EntrepriseService;
import com.ovd.gestionstock.services.FilesStorageService;
import com.ovd.gestionstock.services.UtilisateurService;
import com.ovd.gestionstock.validators.EntrepriseValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntrepriseServiceImpl implements EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;

    private final UtilisateurService utilisateurService;
    private final FilesStorageService filesStorageService;


    @Override
    public List<EntrepriseDto> getAllEntreprise() {
        return entrepriseRepository.findAll()
                .stream()
                .map(EntrepriseDto::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteEntreprise(Long id) {
        if (id == null){
            log.error("ID est null");
            return;
        }
        entrepriseRepository.deleteById(id);
    }

    @Override
    public EntrepriseDto getEntrepriseById(Long id) {
        if (id == null){
            log.error("ID est null");
            return null;
        }
        Optional<Entreprise> entreprise = entrepriseRepository.findById(id);
        EntrepriseDto entrepriseDto = EntrepriseDto.fromEntity(entreprise.get());
        return entrepriseDto;
    }

    @Override
    public EntrepriseDto createEntreprise(EntrepriseDto entrepriseDto) {
        // Validation
        List<String> errors = EntrepriseValidator.validate(entrepriseDto);
        if (!errors.isEmpty()) {
            log.error("Entreprise invalide: {}", entrepriseDto);
            throw new InvalidEntityException(
                    "L'entreprise n'est pas valide",
                    ErrorCodes.ENTREPRISE_NOT_VALID,
                    errors
            );
        }

        log.info("Création d'entreprise SANS logo: {}", entrepriseDto.getNom());

        // Conversion et sauvegarde SANS logo
        Entreprise saved = entrepriseRepository.save(EntrepriseDto.toEntity(entrepriseDto));
        return EntrepriseDto.fromEntity(saved);
    }

//    @Override
//    public EntrepriseDto createEntreprise(EntrepriseDto request, MultipartFile logoFile) {
//        List<String> errors = EntrepriseValidator.validate(request);
//        if (!errors.isEmpty()) {
//            log.error("Entreprise invalide: {}", request);
//            throw new InvalidEntityException(
//                    "L'entreprise n'est pas valide",
//                    ErrorCodes.ENTREPRISE_NOT_VALID,
//                    errors
//            );
//        }
//
//        // Traitement du logo si fourni
//        if (logoFile != null && !logoFile.isEmpty()) {
//            try {
//                request.setLogo(logoFile.getBytes());
//            } catch (IOException e) {
//                log.error("Erreur de lecture du logo", e);
//                throw new InvalidOperationException(
//                        "Erreur lors de la lecture du fichier logo",
//                        ErrorCodes.FILE_UPLOAD_ERROR
//                );
//            }
//        }
//
//        // Conversion et sauvegarde
//        Entreprise entreprise = EntrepriseDto.toEntity(request);
//        Entreprise savedEntreprise = entrepriseRepository.save(entreprise);
//
//        // Ne pas retourner le logo (optionnel, selon le besoin)
//        EntrepriseDto responseDto = EntrepriseDto.fromEntity(savedEntreprise);
//        responseDto.setLogo(null); // retire le logo du retour si non nécessaire
//
//        return responseDto;
//    }


    private UtilisateurDto fromEntreprise(EntrepriseDto dto) {
        return UtilisateurDto.builder()
                .adresse(dto.getAdresseDto())
                .nom(dto.getNom())
                .prenom(dto.getCodeFiscal())
                .email(dto.getEmail())
                .password(generateRandomPassword())
                .entrepriseDto(dto)
                .dateDeNaissance(Instant.now())
                .build();
    }

    private String generateRandomPassword() {
        return "som3R@nd0mP@$$word";
    }


}

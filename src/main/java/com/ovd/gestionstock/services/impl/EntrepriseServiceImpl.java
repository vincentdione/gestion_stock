package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.EntrepriseDto;
import com.ovd.gestionstock.dto.UtilisateurDto;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Entreprise;
import com.ovd.gestionstock.models.Role;
import com.ovd.gestionstock.repositories.EntrepriseRepository;
import com.ovd.gestionstock.services.EntrepriseService;
import com.ovd.gestionstock.services.UtilisateurService;
import com.ovd.gestionstock.validators.EntrepriseValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public EntrepriseDto createEntreprise(EntrepriseDto request) {
        List<String> errors = EntrepriseValidator.validate(request);
        if (!errors.isEmpty()) {
            log.error("Entreprise is not valid {}", request);
            throw new InvalidEntityException("L'entreprise n'est pas valide", ErrorCodes.ENTREPRISE_NOT_VALID, errors);
        }
        EntrepriseDto savedEntreprise = EntrepriseDto.fromEntity(
                entrepriseRepository.save(EntrepriseDto.toEntity(request))
        );

        UtilisateurDto utilisateur = fromEntreprise(savedEntreprise);

        utilisateur.setRole(Role.ADMIN);

        UtilisateurDto savedUser = utilisateurService.createUtilisateur(utilisateur);

//        RolesDto rolesDto = RolesDto.builder()
//                .roleName("ADMIN")
//                .utilisateur(savedUser)
//                .build();


        return  savedEntreprise;
    }

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

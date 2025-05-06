package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.ModePayementDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.exceptions.InvalidOperationException;
import com.ovd.gestionstock.models.ModePayement;
import com.ovd.gestionstock.repositories.ModePayementRepository;
import com.ovd.gestionstock.services.ModePayementService;
import com.ovd.gestionstock.services.TenantSecurityService;
import com.ovd.gestionstock.validators.ModePayementValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModePayementServiceImpl implements ModePayementService {

    private final ModePayementRepository modePayementRepository;
    private final TenantContext tenantContext;
    private final TenantSecurityService tenantSecurityService;

    @Override
    public List<ModePayementDto> getAllModePayement() {
        log.debug("Récupération de tous les modes de paiement pour l'entreprise {}", tenantContext.getCurrentTenant());

        if (tenantContext.getCurrentTenant() == null) {
            throw new InvalidOperationException("Aucun tenant défini dans le contexte", ErrorCodes.TENANT_CONTEXT_REQUIRED);
        }

        List<ModePayement> modes = modePayementRepository.findAll();

        if (modes.isEmpty()) {
            log.warn("Aucun mode de paiement trouvé pour l'entreprise {}", tenantContext.getCurrentTenant());
        }

        return modes.stream()
                .filter(mode -> mode.getIdEntreprise().equals(tenantContext.getCurrentTenant()))
                .map(ModePayementDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteModePayement(Long id) {
        log.debug("Suppression du mode de paiement {} pour l'entreprise {}", id, tenantContext.getCurrentTenant());

        if (id == null) {
            log.error("ID de mode de paiement ne peut pas être null");
            throw new EntityNotFoundException("ID ne peut pas être null", ErrorCodes.MODE_PAYEMENT_NOT_FOUND);
        }

        ModePayement mode = modePayementRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Mode de paiement avec ID {} non trouvé", id);
                    return new EntityNotFoundException("Aucun mode de paiement avec l'id = " + id + " n'a été trouvé", ErrorCodes.MODE_PAYEMENT_NOT_FOUND);
                });

        try {
            tenantSecurityService.validateAccessToResource(mode.getIdEntreprise());
        } catch (Exception e) {
            log.error("Accès refusé : {}", e.getMessage());
            throw new InvalidOperationException("Accès refusé au mode de paiement", ErrorCodes.MODE_PAYEMENT_NOT_FOUND);
        }

        modePayementRepository.delete(mode);
        log.info("Mode de paiement {} supprimé avec succès", id);
    }

    @Override
    public ModePayementDto getModePayementById(Long id) {
        if (id == null) {
            log.error("ID du mode de paiement est null");
            return null;
        }

        ModePayement mode = modePayementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aucun mode de paiement avec l'id = " + id + " n'a été trouvé", ErrorCodes.MODE_PAYEMENT_NOT_FOUND));

        try {
            tenantSecurityService.validateAccessToResource(mode.getIdEntreprise());
        } catch (Exception e) {
            log.error("Accès refusé : {}", e.getMessage());
            throw new InvalidOperationException("Accès refusé au mode de paiement", ErrorCodes.MODE_PAYEMENT_NOT_FOUND);
        }

        return ModePayementDto.fromEntity(mode);
    }

    @Override
    public ModePayementDto createModePayement(ModePayementDto modeDto) {
        log.debug("Création d'un nouveau mode de paiement pour l'entreprise {}", tenantContext.getCurrentTenant());

        List<String> errors = ModePayementValidator.validate(modeDto);
        if (!errors.isEmpty()) {
            log.error("Données de mode de paiement non valides : {}", errors);
            throw new InvalidEntityException("Les données sont invalides : " + String.join(", ", errors), ErrorCodes.MODE_PAYEMENT_NOT_FOUND);
        }

        if (tenantContext.getCurrentTenant() == null) {
            throw new InvalidOperationException("Aucun tenant défini dans le contexte", ErrorCodes.TENANT_CONTEXT_REQUIRED);
        }

        ModePayement mode = ModePayementDto.toEntity(modeDto);
        mode.setIdEntreprise(tenantContext.getCurrentTenant());

        ModePayement savedMode = modePayementRepository.save(mode);
        log.info("Mode de paiement créé avec succès avec l'ID {}", savedMode.getId());

        return ModePayementDto.fromEntity(savedMode);
    }
}

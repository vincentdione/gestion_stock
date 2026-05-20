package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.VenteController;
import com.ovd.gestionstock.criteria.VenteSearchCriteria;
import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.dto.VenteDto;
import com.ovd.gestionstock.services.VenteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
@Tag(name = "ventes")
public class VenteApi implements VenteController {

    private final VenteService venteService;

    @Override
    public ResponseEntity<VenteDto> saveVente(VenteDto request) {
        return ResponseEntity.ok(venteService.createVentes(request));
    }

    @Override
    public ResponseEntity<List<VenteDto>> getAllVentes() {
        return ResponseEntity.ok(venteService.getAllVentes());
    }

    @Override
    public ResponseEntity<VenteDto> getVenteById(Long id) {
        return ResponseEntity.ok(venteService.getVentesById(id));
    }

    @Override
    public ResponseEntity<VenteDto> getVenteByCode(String code) {
        return ResponseEntity.ok(venteService.findByCode(code));
    }

    @Override
    public ResponseEntity<List<LigneVenteDto>> findAllLigneVenteByVenteId(Long idVente) {
             return ResponseEntity.ok(venteService.findAllLigneVentesByVenteId(idVente));
    }



    @Override
    public ResponseEntity deleteVente(Long id) {
        venteService.deleteVentes(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BigDecimal> getMontantTotalVentes() {
        List<VenteDto> ventes = venteService.getAllVentes();
        if(!ventes.isEmpty()){
            return ResponseEntity.ok(venteService.getMontantTotalVentes(ventes.stream().map(VenteDto::toEntity).collect(Collectors.toList())));
        }
        else {
            System.out.println("pas de ventes trouvées !!!");
            return null;
        }
    }

    public ResponseEntity<VenteDto> updateVente(Long id, VenteDto venteDto) {
        try {
            VenteDto updatedVente = venteService.updateVente(id, venteDto);
            return ResponseEntity.ok(updatedVente);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    public ResponseEntity<VenteDto> updateClientInfo(Long id, VenteDto venteDto) {
        try {
            VenteDto updatedVente = venteService.updateVenteClient(id, venteDto);
            return ResponseEntity.ok(updatedVente);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    public ResponseEntity<List<VenteDto>> getLatestVentes() {
        try {
            List<VenteDto> latestVentes = venteService.get10LatestVentes();
            return ResponseEntity.ok(latestVentes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Page<VenteDto>> searchVentes(@RequestBody VenteSearchCriteria criteria) {
        try {
            Page<VenteDto> result = venteService.searchVentes(criteria);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Page<VenteDto>> searchVentesByParams(
            String nomClient,
            String prenomClient,
            String codeVente,
            Integer page,
            Integer size) {

        try {
            VenteSearchCriteria criteria = VenteSearchCriteria.builder()
                    .nomClient(nomClient)
                    .prenomClient(prenomClient)
                    .codeVente(codeVente)
                    .page(page)
                    .size(size)
                    .build();

            Page<VenteDto> result = venteService.searchVentes(criteria);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

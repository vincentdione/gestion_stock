package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.criteria.RapportSearchCriteria;
import com.ovd.gestionstock.dto.RapportDto;
import com.ovd.gestionstock.models.PeriodeRapport;
import com.ovd.gestionstock.services.RapportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
@Tag(name = "rapports")
@Slf4j
public class RapportApi {

    private final RapportService rapportService;

    @PostMapping(value = "/rapports", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Générer un rapport personnalisé")
    public ResponseEntity<RapportDto> genererRapport(@RequestBody RapportSearchCriteria criteria) {
        log.info("Génération du rapport avec critères: {}", criteria);
        return ResponseEntity.ok(rapportService.genererRapport(criteria));
    }

    @GetMapping(value = "/rapports/hier", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rapport d'hier")
    public ResponseEntity<RapportDto> rapportHier() {
        log.info("Génération du rapport d'hier");
        return ResponseEntity.ok(rapportService.rapportHier());
    }

    @GetMapping(value = "/rapports/semaine", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rapport de la semaine")
    public ResponseEntity<RapportDto> rapportSemaine() {
        log.info("Génération du rapport de la semaine");
        return ResponseEntity.ok(rapportService.rapportSemaine());
    }

    @GetMapping(value = "/rapports/mois", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rapport du mois en cours")
    public ResponseEntity<RapportDto> rapportMoisEnCours() {
        log.info("Génération du rapport du mois en cours");
        return ResponseEntity.ok(rapportService.rapportMoisEnCours());
    }

    @GetMapping(value = "/rapports/annee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rapport de l'année en cours")
    public ResponseEntity<RapportDto> rapportAnneeEnCours() {
        log.info("Génération du rapport de l'année en cours");
        return ResponseEntity.ok(rapportService.rapportAnneeEnCours());
    }

    @GetMapping(value = "/rapports/top-articles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Top des articles les plus vendus")
    public ResponseEntity<RapportDto> topArticles(@RequestParam(required = false) String periode,
                                                  @RequestParam(required = false) Integer limit) {
        PeriodeRapport periodeEnum = periode != null ?
                PeriodeRapport.valueOf(periode.toUpperCase()) :
                PeriodeRapport.MOIS_EN_COURS;

        RapportSearchCriteria criteria = RapportSearchCriteria.builder()
                .periode(periodeEnum)
                .limitArticles(limit != null ? limit : 10)
                .build();

        log.info("Top articles avec critères: {}", criteria);
        return ResponseEntity.ok(rapportService.topArticles(criteria));
    }

    @GetMapping(value = "/rapports/chiffre-affaire", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Chiffre d'affaire par période")
    public ResponseEntity<RapportDto> chiffreAffaireParPeriode(@RequestParam(required = false) String periode) {
        PeriodeRapport periodeEnum = periode != null ?
                PeriodeRapport.valueOf(periode.toUpperCase()) :
                PeriodeRapport.MOIS_EN_COURS;

        RapportSearchCriteria criteria = RapportSearchCriteria.builder()
                .periode(periodeEnum)
                .avecDetails(true)
                .build();

        log.info("Chiffre d'affaire avec critères: {}", criteria);
        return ResponseEntity.ok(rapportService.chiffreAffaireParPeriode(criteria));
    }

    @GetMapping(value = "/rapports/ventes-jour", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Chiffre d'affaire du jour")
    public ResponseEntity<BigDecimal> chiffreAffaireJour() {
        LocalDate aujourdHui = LocalDate.now();
        RapportSearchCriteria criteria = RapportSearchCriteria.builder()
                .periode(PeriodeRapport.PERSONNALISEE)
                .dateDebut(java.sql.Date.valueOf(aujourdHui))
                .dateFin(java.sql.Date.valueOf(aujourdHui))
                .build();

        RapportDto rapport = rapportService.genererRapport(criteria);
        return ResponseEntity.ok(rapport.getChiffreAffaireTotal());
    }
}
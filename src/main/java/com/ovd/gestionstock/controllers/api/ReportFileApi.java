package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.dto.CommandeClientDto;
import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.models.CommandeEtat;
import com.ovd.gestionstock.services.CommandeClientService;
import com.ovd.gestionstock.services.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "reports")
@RequestMapping("/api/v1/admin")
public class ReportFileApi {

    private  final ReportService reportService;

    @GetMapping(value="/reports/clients/{format}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateReport(@PathVariable String format) throws JRException, FileNotFoundException {

        return  ResponseEntity.ok(reportService.exportClientCommande(format));

    }

}

package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.EntrepriseDto;
import com.ovd.gestionstock.utils.Constants;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EntrepriseController {


//    @PostMapping(value = "/entreprises", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //@Hidden
//    @PostMapping(value = "/entreprises",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    ResponseEntity<EntrepriseDto> saveEntreprise(
//        @RequestPart("entreprise")  @Valid @ModelAttribute EntrepriseDto request,
//        @RequestPart("logo") MultipartFile logo
//    );

    @PostMapping(value = "/entreprises", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EntrepriseDto> saveEntreprise(@RequestBody @Valid EntrepriseDto request);


    @GetMapping(value = "/entreprises/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EntrepriseDto>> getAllEntreprises();

    @GetMapping(value = "/entreprises/{idEntreprise}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntrepriseDto> getEntrepriseById(@PathVariable("idEntreprise") Long id);


    @DeleteMapping(value = "/entreprises/delete/{idEntreprise}")
    public ResponseEntity deleteEntreprise(@PathVariable("idEntreprise") Long id);


}

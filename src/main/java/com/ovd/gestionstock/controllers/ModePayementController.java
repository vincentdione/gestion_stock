package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.ModePayementDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface ModePayementController {
    @PostMapping(value = "/modes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<ModePayementDto> saveMode(@RequestBody ModePayementDto request);

    @GetMapping(value = "/modes/all", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<List<ModePayementDto>> getAllModes();

    @GetMapping(value = "/modes/{idMode}", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<ModePayementDto> getModeById(@PathVariable("idMode") Long idMode);


    @GetMapping(value = "/modes/{nom}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModePayementDto> getModeByNom(@PathVariable("nom") String nom);

    @DeleteMapping(value = "/modes/delete/{idMode}")

    public ResponseEntity deleteMode(@PathVariable("idMode") Long idMode);


}

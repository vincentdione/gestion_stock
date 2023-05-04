package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.UtilisateurDto;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(Constants.APP_ROOT)
public interface UtilisateurController {


    @PostMapping(value = "/utilisateurs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UtilisateurDto> save(@RequestBody UtilisateurDto request);

    @GetMapping(value = "/utilisateurs/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UtilisateurDto>> getAllUtilisateurs();

    @GetMapping(value = "/utilisateurs/{idUtilisateur}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UtilisateurDto> getUtilisateurById(@PathVariable("idUtilisateur") Long id);


    @DeleteMapping(value = "/utilisateurs/delete/{idUtilisateur}")
    public ResponseEntity deleteUtilisateur(@PathVariable("idUtilisateur") Long id);


}

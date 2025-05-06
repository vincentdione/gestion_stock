package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.UtilisateurDto;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UtilisateurEntrepriseController {

    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping(value = "/utilisateurs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UtilisateurDto> saveUser(@RequestBody UtilisateurDto request);

    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping(value = "/utilisateurs/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UtilisateurDto>> getAllUtilisateurs();

    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping(value = "/utilisateurs/{idUtilisateur}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UtilisateurDto> getUtilisateurById(@PathVariable("idUtilisateur") Long id);

    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping(value = "/utilisateurs/getUser/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UtilisateurDto> findByUsername(@PathVariable("username") String username);


    @PreAuthorize("hasAuthority('admin:delete')")
    @Hidden
    @DeleteMapping(value = "/utilisateurs/delete/{idUtilisateur}")
    public ResponseEntity deleteUtilisateur(@PathVariable("idUtilisateur") Long idUtilisateur);


}

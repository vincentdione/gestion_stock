package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.dto.UtilisateurDto;
import com.ovd.gestionstock.services.UtilisateurEntrepriseService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "utilisateurs-entreprises")
@RequestMapping("/api/v1/admin")
public class UtilisateurEntrepriseApi {

    private final UtilisateurEntrepriseService utilisateurService;

    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping(value = "/entreprise/utilisateurs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UtilisateurDto> saveUser(@RequestBody  UtilisateurDto request) {
        return ResponseEntity.ok(utilisateurService.createUtilisateur(request));
    }

    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping(value = "/entreprise/utilisateurs/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UtilisateurDto>> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateur());
    }

    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping(value = "/entreprise/utilisateurs/{idUtilisateur}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UtilisateurDto> getUtilisateurById(@PathVariable("idUtilisateur") Long idUtilisateur) {
        return ResponseEntity.ok(utilisateurService.getUtilisateurById(idUtilisateur));
    }

    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping(value = "/entreprise/utilisateurs/getUser/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UtilisateurDto> findByUsername(@PathVariable("username") String username) {
        log.info("Find By Username !!! ");
        log.info(username);
        log.info("Find By Username !!! ");
        return ResponseEntity.ok(utilisateurService.findByUsername(username));
    }

    @PreAuthorize("hasAuthority('admin:delete')")
    @Hidden
    @DeleteMapping(value = "/entreprise/utilisateurs/delete/{idUtilisateur}")
    public ResponseEntity deleteUtilisateur(@PathVariable("idUtilisateur") Long idUtilisateur) {
        utilisateurService.deleteUtilisateur(idUtilisateur);
        return ResponseEntity.ok().build();
    }
}

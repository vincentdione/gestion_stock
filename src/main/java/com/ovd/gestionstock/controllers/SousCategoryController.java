package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.SousCategoryDto;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface SousCategoryController {
    @PostMapping(value = Constants.APP_ROOT+"/souscategories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Enregistrer une catégorie (Ajouter / Modifier)", notes = "Cette Méthode permet d'enregistrer ou modifier une catégorie", response = CategoryDto.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "L'object catégorie créee / modifiée"),
//            @ApiResponse(code = 400, message = "L'object catégorie n'est pas valide")
//    })
    public ResponseEntity<SousCategoryDto> saveSousCategory(@RequestBody SousCategoryDto request);

    @GetMapping(value = Constants.APP_ROOT+"/souscategories/all", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Afficher la liste des catégories ", notes = "Cette Méthode permet de rechercher et renvoyer la liste de toutes les catégories", responseContainer = "List<CategoryDto>")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Liste des catégories / liste vide")
//    })
    public ResponseEntity<List<SousCategoryDto>> getAllSousCategorys();

    @GetMapping(value = Constants.APP_ROOT+"/souscategories/{idSousCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Rechercher une catégorie ", notes = "Cette Méthode permet de chercher une catégorie", response = CategoryDto.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "La catégorie a été retrouvée dans la BDD"),
//            @ApiResponse(code = 404, message = "Aucune catégorie n'existe dans la base avec l'ID fourni"),
//    })
    public ResponseEntity<SousCategoryDto> getSousCategoryById(@PathVariable("idSousCategory") Long idSousCategory);

//    @ApiOperation(value = "Rechercher une catégorie par nom ", notes = "Cette Méthode permet de chercher une catégorie par nom", response = CategoryDto.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "La catégorie a été retrouvée dans la BDD"),
//            @ApiResponse(code = 404, message = "Aucune catégorie n'existe dans la base avec le nom fourni"),
//    })
    @GetMapping(value = Constants.APP_ROOT+"/souscategories/{nom}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SousCategoryDto> getSousCategoryByNom(@PathVariable("nom") String nom);

    @DeleteMapping(value = Constants.APP_ROOT+"/souscategories/delete/{idSousCategory}")
//    @ApiOperation(value = "Supprimer une catégorie ", notes = "Cette Méthode permet de supprimer une catégorie par ID")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "La catégorie a été supprimée dans la BDD"),
//            @ApiResponse(code = 404, message = "Aucune catégorie n'existe dans la base avec l'ID fourni"),
//    })
    public ResponseEntity deleteSousCategory(@PathVariable("idSousCategory") Long idSousCategory);


}

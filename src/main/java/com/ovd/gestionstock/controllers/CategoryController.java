package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.CategoryDto;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@PreAuthorize("hasRole('ADMIN')")
public interface CategoryController {
    @PostMapping(value = Constants.APP_ROOT+"/categories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Enregistrer une catégorie (Ajouter / Modifier)", notes = "Cette Méthode permet d'enregistrer ou modifier une catégorie", response = CategoryDto.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "L'object catégorie créee / modifiée"),
//            @ApiResponse(code = 400, message = "L'object catégorie n'est pas valide")
//    })
    public ResponseEntity<CategoryDto> save(@RequestBody CategoryDto request);

    @GetMapping(value = Constants.APP_ROOT+"/categories/all", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Afficher la liste des catégories ", notes = "Cette Méthode permet de rechercher et renvoyer la liste de toutes les catégories", responseContainer = "List<CategoryDto>")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Liste des catégories / liste vide")
//    })
    public ResponseEntity<List<CategoryDto>> getAllCategorys();

    @GetMapping(value = Constants.APP_ROOT+"/categories/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Rechercher une catégorie ", notes = "Cette Méthode permet de chercher une catégorie", response = CategoryDto.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "La catégorie a été retrouvée dans la BDD"),
//            @ApiResponse(code = 404, message = "Aucune catégorie n'existe dans la base avec l'ID fourni"),
//    })
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("idCategory") Long id);

//    @ApiOperation(value = "Rechercher une catégorie par nom ", notes = "Cette Méthode permet de chercher une catégorie par nom", response = CategoryDto.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "La catégorie a été retrouvée dans la BDD"),
//            @ApiResponse(code = 404, message = "Aucune catégorie n'existe dans la base avec le nom fourni"),
//    })
    @GetMapping(value = Constants.APP_ROOT+"/categories/{nom}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> getCategoryByNom(@PathVariable("nom") String nom);

    @DeleteMapping(value = Constants.APP_ROOT+"/categories/delete/{idCategory}")
//    @ApiOperation(value = "Supprimer une catégorie ", notes = "Cette Méthode permet de supprimer une catégorie par ID")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "La catégorie a été supprimée dans la BDD"),
//            @ApiResponse(code = 404, message = "Aucune catégorie n'existe dans la base avec l'ID fourni"),
//    })
    public ResponseEntity deleteCategory(@PathVariable("idCategory") Long id);


}

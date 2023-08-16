package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.ConditionAVDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface ConditionAVController {
    @PostMapping(value = "/conditions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConditionAVDto> saveCondition(@RequestBody ConditionAVDto request);

    @GetMapping(value = "/conditions/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ConditionAVDto>> getAllConditions();

    @GetMapping(value = "/conditions/distinct/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ConditionAVDto>> getAllConditionWithDistincts();

    @GetMapping(value = "/conditions/{idCondition}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConditionAVDto> getConditionById(@PathVariable("idCondition") Long idCondition);
    @GetMapping(value = "/conditions/{nom}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConditionAVDto> getConditionByNom(@PathVariable("nom") String nom);

    @DeleteMapping(value = "/conditions/delete/{idCondition}")
    public ResponseEntity deleteCondition(@PathVariable("idCondition") Long idCondition);


}

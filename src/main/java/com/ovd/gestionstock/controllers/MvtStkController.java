package com.ovd.gestionstock.controllers;


import com.ovd.gestionstock.dto.MvtStkDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


public interface MvtStkController  {
  @GetMapping(value = "/mvtstk/stockreel/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<BigDecimal> stockReelArticle(@PathVariable("idArticle") Long idArticle);

  @GetMapping(value = "/mvtstk/all", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<List<MvtStkDto>> findAllMvtStock ();


  @GetMapping(value= "/mvtstk/filter/article/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<List<MvtStkDto>> mvtStkArticle(@PathVariable("idArticle") Long idArticle);

  @PostMapping(value = "/mvtstk/entree", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<MvtStkDto> entreeStock(@RequestBody MvtStkDto dto);

  @PostMapping(value = "/mvtstk/sortie", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<MvtStkDto> sortieStock(@RequestBody MvtStkDto dto);

  @PostMapping(value = "/mvtstk/correctionpos", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<MvtStkDto> correctionStockPos(@RequestBody MvtStkDto dto);

  @PostMapping(value = "/mvtstk/correctionneg", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<MvtStkDto> correctionStockNeg(@RequestBody MvtStkDto dto);
}

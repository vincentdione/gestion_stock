package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.models.CommandeFournisseur;
import com.ovd.gestionstock.services.CommandeFournisseurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {
    @Override
    public List<CommandeFournisseur> getAllCommandeFournisseur() {
        return null;
    }

    @Override
    public void deleteCommandeFournisseur(Long id) {

    }

    @Override
    public CommandeFournisseur getCommandeFournisseurById(Long id) {
        return null;
    }

    @Override
    public void createCommandeFournisseur(CommandeFournisseur request) {

    }
}

package com.ovd.gestionstock.services;

import com.ovd.gestionstock.models.CommandeFournisseur;

import java.util.List;

public interface CommandeFournisseurService {

    public List<CommandeFournisseur> getAllCommandeFournisseur();
    public void deleteCommandeFournisseur(Long id);

    public CommandeFournisseur getCommandeFournisseurById(Long id);

    public void createCommandeFournisseur(CommandeFournisseur request);
}

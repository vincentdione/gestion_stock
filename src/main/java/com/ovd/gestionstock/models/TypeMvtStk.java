package com.ovd.gestionstock.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public enum TypeMvtStk {
    ENTREE,
    SORTIE,
    CORRECTION_POS,
    CORRECTION_NEG,
}

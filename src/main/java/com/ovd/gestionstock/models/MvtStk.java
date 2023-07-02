package com.ovd.gestionstock.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@Entity
@Table(name = "MVTSTK")
@AllArgsConstructor
@NoArgsConstructor
public class MvtStk {

    @Id
    @GeneratedValue
    private Long id;
    private Instant dateMvt;
    private BigDecimal quantite;

    @ManyToOne
    @JoinColumn(name = "idArticle")
    private Article article;

    @Enumerated(EnumType.STRING)
    private TypeMvtStk typeMvtStk;

    @Enumerated(EnumType.STRING)
    private SourceMvt sourceMvt;

    private Long idEntreprise;


}

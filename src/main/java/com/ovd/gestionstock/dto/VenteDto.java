package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Fournisseur;
import com.ovd.gestionstock.models.Ventes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenteDto {

    private Long id;

    private String code;

    private Date dateVente;

    private String commentaire;

    private List<LigneVenteDto> ligneVentes;

    private Long idEntreprise;

    private ModePayementDto modePayement;

    public static VenteDto fromEntity(Ventes vente) {
        if (vente == null) {
            return null;
        }
        return VenteDto.builder()
                .id(vente.getId())
                .code(vente.getCode())
                .commentaire(vente.getCommentaire())
                .dateVente(vente.getDateVente())
                .idEntreprise(vente.getId())
                .modePayement(ModePayementDto.fromEntity(vente.getModePayement()))
                .build();
    }

    public static Ventes toEntity(VenteDto dto) {
        if (dto == null) {
            return null;
        }
        Ventes ventes = new Ventes();
        ventes.setId(dto.getId());
        ventes.setCode(dto.getCode());
        ventes.setCommentaire(dto.getCommentaire());
         ventes.setDateVente(dto.getDateVente());
        ventes.setIdEntreprise(dto.getIdEntreprise());
        ventes.setModePayement(ModePayementDto.toEntity(dto.getModePayement()));
        return ventes;
    }
}

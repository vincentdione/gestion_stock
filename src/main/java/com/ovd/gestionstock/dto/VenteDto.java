package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Ventes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenteDto {

    private Long id;
    private String code;
    private Date dateVente;
    private String commentaire;

    // Nouveaux champs
    private String nomClient;
    private String prenomClient;
    private String adresse;
    private String numero;

    private List<LigneVenteDto> ligneVentes;
    private Long idEntreprise;
    private ModePayementDto modePayement;

    // Champ calculé pour le montant total
    private BigDecimal montantTotal;

    public static VenteDto fromEntity(Ventes vente) {
        if (vente == null) {
            return null;
        }

        // Convertir les lignes de vente
        List<LigneVenteDto> ligneVenteDtos = null;
        if (vente.getLigneVentes() != null) {
            ligneVenteDtos = vente.getLigneVentes().stream()
                    .map(LigneVenteDto::fromEntity)
                    .collect(Collectors.toList());
        }

        // Utiliser la méthode getMontantTotal de l'entité si elle existe
        // Sinon calculer manuellement
        BigDecimal montantTotalCalculé = vente.getMontantTotal();

        return VenteDto.builder()
                .id(vente.getId())
                .code(vente.getCode())
                .commentaire(vente.getCommentaire())
                .dateVente(vente.getDateVente())
                // Nouveaux champs client
                .nomClient(vente.getNomClient())
                .prenomClient(vente.getPrenomClient())
                .adresse(vente.getAdresse())
                .numero(vente.getNumero())
                // Fin nouveaux champs
                .ligneVentes(ligneVenteDtos) // Ajout des lignes de vente
                .montantTotal(montantTotalCalculé) // Montant total
                .idEntreprise(vente.getIdEntreprise())
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
        // Nouveaux champs client
        ventes.setNomClient(dto.getNomClient());
        ventes.setPrenomClient(dto.getPrenomClient());
        ventes.setAdresse(dto.getAdresse());
        ventes.setNumero(dto.getNumero());
        // Fin nouveaux champs
        ventes.setIdEntreprise(dto.getIdEntreprise());
        ventes.setModePayement(ModePayementDto.toEntity(dto.getModePayement()));
        return ventes;
    }
}
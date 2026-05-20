package com.ovd.gestionstock.criteria;

import com.ovd.gestionstock.models.PeriodeRapport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RapportSearchCriteria {


    private PeriodeRapport periode;
    private Date dateDebut;
    private Date dateFin;
    private Boolean avecDetails;
    private Integer limitArticles; // Nombre d'articles à inclure dans le top
    private Long idEntreprise;

    // Méthode pour calculer les dates automatiquement
    public void calculerDates() {
        if (periode != null && periode != PeriodeRapport.PERSONNALISEE) {
            java.time.LocalDate aujourdHui = java.time.LocalDate.now();
            java.time.LocalDate debut = null;
            java.time.LocalDate fin = aujourdHui;

            switch (periode) {
                case HIER:
                    debut = aujourdHui.minusDays(1);
                    fin = aujourdHui.minusDays(1);
                    break;
                case SEMAINE:
                    debut = aujourdHui.minusDays(7);
                    break;
                case MOIS_EN_COURS:
                    debut = aujourdHui.withDayOfMonth(1);
                    break;
                case ANNEE_EN_COURS:
                    debut = aujourdHui.withDayOfYear(1);
                    break;
            }

            if (debut != null) {
                this.dateDebut = java.sql.Date.valueOf(debut);
                this.dateFin = java.sql.Date.valueOf(fin);
            }
        }
    }
}
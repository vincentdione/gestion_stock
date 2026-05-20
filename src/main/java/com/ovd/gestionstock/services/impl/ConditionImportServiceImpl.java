package com.ovd.gestionstock.services.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.ConditionAVImportDto;
import com.ovd.gestionstock.dto.UniteDto;
import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.ConditionAV;
import com.ovd.gestionstock.models.Unite;
import com.ovd.gestionstock.repositories.ArticleRepository;
import com.ovd.gestionstock.repositories.ConditionAVRepository;
import com.ovd.gestionstock.services.ConditionImportService;
import com.ovd.gestionstock.services.UniteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConditionImportServiceImpl implements ConditionImportService {

    private final ConditionAVRepository conditionAVRepository;
    private final ArticleRepository articleRepository;
    private final UniteService uniteService;
    private final TenantContext tenantContext;

    @Override
    @Transactional
    public List<ConditionAVImportDto> importConditionsFromCsv(MultipartFile file) throws IOException {
        List<ConditionAVImportDto> importedConditions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVReader csvReader = new CSVReader(br)) {

            // Lire les en-têtes
            String[] headers = csvReader.readNext();
            if (headers == null) {
                throw new RuntimeException("Le fichier CSV est vide");
            }

            // Mapper les en-têtes
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim().toLowerCase(), i);
            }

            // Vérifier les colonnes obligatoires
            List<String> requiredColumns = Arrays.asList("codearticle", "unite", "quantite", "prixunitaireht");
            for (String column : requiredColumns) {
                if (!headerMap.containsKey(column)) {
                    throw new RuntimeException("Colonne obligatoire manquante: " + column);
                }
            }

            // Lire les lignes de données
            String[] values;
            int lineNumber = 1;

            while ((values = csvReader.readNext()) != null) {
                lineNumber++;
                try {
                    ConditionAVImportDto conditionDto = processCsvRecord(values, headerMap, lineNumber);
                    if (conditionDto != null) {
                        ConditionAV savedCondition = saveCondition(conditionDto);
                        importedConditions.add(ConditionAVImportDto.fromEntity(savedCondition));

                        log.info("Traitement de la condition pour l'article: {} avec unité: {}",
                                conditionDto.getCodeArticle(), conditionDto.getUniteNom());
                    }
                } catch (Exception e) {
                    log.error("Erreur ligne {}: {}", lineNumber, e.getMessage(), e);
                    throw new RuntimeException("Erreur à la ligne " + lineNumber + ": " + e.getMessage(), e);
                }
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException("Erreur de validation CSV", e);
        }

        return importedConditions;
    }

    private ConditionAVImportDto processCsvRecord(String[] values, Map<String, Integer> headerMap, int lineNumber) {
        // Créer une fonction pour récupérer les valeurs
        Function<String, String> getValue = columnName -> {
            Integer index = headerMap.get(columnName.toLowerCase());
            if (index != null && index < values.length) {
                return values[index].trim();
            }
            return "";
        };

        // Extraire les données
        String codeArticle = getValue.apply("codeArticle");
        if (codeArticle.isEmpty()) {
            throw new RuntimeException("Le code article est requis à la ligne " + lineNumber);
        }

        String uniteNom = getValue.apply("unite");
        if (uniteNom.isEmpty()) {
            throw new RuntimeException("L'unité est requise pour l'article " + codeArticle + " à la ligne " + lineNumber);
        }

        BigDecimal quantite;
        try {
            String quantiteStr = getValue.apply("quantite");
            quantite = new BigDecimal(quantiteStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Quantité invalide pour l'article " + codeArticle + " à la ligne " + lineNumber, e);
        }

        BigDecimal prixUnitaireHt;
        try {
            String prixHtStr = getValue.apply("prixUnitaireHt");
            prixUnitaireHt = new BigDecimal(prixHtStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Prix unitaire HT invalide pour l'article " + codeArticle + " à la ligne " + lineNumber, e);
        }

        BigDecimal tauxTval = BigDecimal.ZERO;
        String tauxTvalStr = getValue.apply("tauxTval");
        if (!tauxTvalStr.isEmpty()) {
            try {
                tauxTval = new BigDecimal(tauxTvalStr);
            } catch (NumberFormatException e) {
                log.warn("Taux TVA invalide pour l'article {}, utilisation de 0: {}", codeArticle, e.getMessage());
            }
        }

        BigDecimal prixUnitaireTtc = BigDecimal.ZERO;
        String prixTtcStr = getValue.apply("prixUnitaireTtc");
        if (!prixTtcStr.isEmpty()) {
            try {
                prixUnitaireTtc = new BigDecimal(prixTtcStr);
            } catch (NumberFormatException e) {
                // Calculer automatiquement si possible
                if (prixUnitaireHt != null && tauxTval != null) {
                    prixUnitaireTtc = prixUnitaireHt.multiply(
                            BigDecimal.ONE.add(tauxTval.divide(new BigDecimal("100"))));
                }
            }
        }

        // Vérifier si l'article existe
        Long tenantId = tenantContext.getCurrentTenant();
        Article article = articleRepository.findByCodeArticleAndIdEntreprise(codeArticle, tenantId)
                .orElseThrow(() -> new RuntimeException("Article non trouvé: " + codeArticle + " à la ligne " + lineNumber));

        // Vérifier si la combinaison article+unité existe déjà
        boolean conditionExists = conditionAVRepository.existsByArticleAndUniteNom(article, uniteNom, tenantId);
        if (conditionExists) {
            log.warn("Condition existe déjà pour l'article {} avec unité {} - ignorée à la ligne {}",
                    codeArticle, uniteNom, lineNumber);
            return null;
        }
        UniteDto uniteDto = uniteService.getOrCreateUnite(uniteNom, uniteNom);
        return ConditionAVImportDto.importBuilder(codeArticle, uniteNom, quantite, prixUnitaireHt, tauxTval, prixUnitaireTtc);
    }

    private ConditionAV saveCondition(ConditionAVImportDto conditionDto) {
        Long tenantId = tenantContext.getCurrentTenant();

        Article article = articleRepository.findByCodeArticleAndIdEntreprise(
                        conditionDto.getCodeArticle(), tenantId)
                .orElseThrow(() -> new RuntimeException("Article non trouvé: " + conditionDto.getCodeArticle()));

        UniteDto uniteDto = uniteService.getOrCreateUnite(
                conditionDto.getUniteNom(), conditionDto.getUniteNom());
        Unite unite = UniteDto.toEntity(uniteDto);

        ConditionAV condition = ConditionAV.builder()
                .article(article)
                .unite(unite)
                .quantite(conditionDto.getQuantite())
                .prixUnitaireHt(conditionDto.getPrixUnitaireHt())
                .tauxTval(conditionDto.getTauxTval())
                .prixUnitaireTtc(conditionDto.getPrixUnitaireTtc())
                .idEntreprise(tenantId)
                .build();

        // Sauvegarder directement la condition
        ConditionAV savedCondition = conditionAVRepository.save(condition);

        // Ajouter à la collection de l'article (côté mémoire seulement)
        if (article.getConditions() == null) {
            article.setConditions(new ArrayList<>());
        }
        article.getConditions().add(savedCondition);

        log.info("Condition créée pour l'article {} avec unité {}",
                conditionDto.getCodeArticle(), conditionDto.getUniteNom());

        return savedCondition;
    }
}
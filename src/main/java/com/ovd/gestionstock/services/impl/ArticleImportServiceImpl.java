package com.ovd.gestionstock.services.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.ArticleCsvRepresentation;
import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.CategoryDto;
import com.ovd.gestionstock.dto.SousCategoryDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.repositories.ArticleRepository;
import com.ovd.gestionstock.services.ArticleImportService;
import com.ovd.gestionstock.services.CategoryService;
import com.ovd.gestionstock.services.SousCategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleImportServiceImpl implements ArticleImportService {

    private final ArticleServiceImpl articleService;
    private final SousCategoryService sousCategoryService;
    private final CategoryService categoryService;
    private final TenantContext tenantContext;
    private final ArticleRepository articleRepository;


    @Override
    public List<ArticleDto> importArticlesFromExcel(MultipartFile file) throws IOException {
        // Implémentation existante
        return List.of();
    }

    @Override
    public List<ArticleDto> importArticlesFromCsv(MultipartFile file) throws IOException {
        Set<ArticleCsvRepresentation> csvArticles = parseCsv(file);

        // Vérification initiale des doublons dans le fichier
        checkForDuplicateArticleCodes(csvArticles);

        // Vérification des articles existants en base
        Set<String> existingArticleCodes = getExistingArticleCodes(csvArticles);

        return csvArticles.stream()
                .filter(csv -> !existingArticleCodes.contains(csv.getCodeArticle()))
                .map(this::mapToArticleDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void checkForDuplicateArticleCodes(Set<ArticleCsvRepresentation> csvArticles) {
        Set<String> duplicateCodes = csvArticles.stream()
                .collect(Collectors.groupingBy(ArticleCsvRepresentation::getCodeArticle, Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        if (!duplicateCodes.isEmpty()) {
            throw new InvalidEntityException("Le fichier contient des codes articles en doublon: " + duplicateCodes,
                    ErrorCodes.ARTICLE_DUPLICATE_CODE_IN_FILE);
        }
    }

    private Set<String> getExistingArticleCodes(Set<ArticleCsvRepresentation> csvArticles) {
        List<String> codesToCheck = csvArticles.stream()
                .map(ArticleCsvRepresentation::getCodeArticle)
                .collect(Collectors.toList());

        return articleRepository.findAllByCodeArticleInAndIdEntreprise(
                        codesToCheck, tenantContext.getCurrentTenant())
                .stream()
                .map(Article::getCodeArticle)
                .collect(Collectors.toSet());
    }

    private Set<ArticleCsvRepresentation> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<ArticleCsvRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(ArticleCsvRepresentation.class);

            CsvToBean<ArticleCsvRepresentation> csvToBean =
                    new CsvToBeanBuilder<ArticleCsvRepresentation>(reader)
                            .withMappingStrategy(strategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();

            return csvToBean.parse().stream().collect(Collectors.toSet());
        }
    }

    private ArticleDto mapToArticleDto(ArticleCsvRepresentation csv) {
        try {



            // 1. Gérer la catégorie parente
            CategoryDto categoryDto = categoryService.getOrCreateCategory(
                    csv.getCategoryCode(),
                    "Catégorie " + csv.getCategoryCode()
            );

            if (categoryDto == null) {
                throw new EntityNotFoundException(
                        "La catégorie n'a pas pu être créée ou trouvée",
                        ErrorCodes.CATEGORY_NOT_FOUND
                );
            }

            // Gestion de la sous-catégorie
            SousCategoryDto sousCategoryDto = sousCategoryService.getOrCreateSousCategory(
                    csv.getSousCategoryCode(),
                    csv.getSousCategoryName(),
                    csv.getCategoryCode()
            );

            if (sousCategoryDto == null) {
                throw new EntityNotFoundException(
                        "La sous-catégorie n'a pas pu être créée ou trouvée",
                        ErrorCodes.SOUS_CATEGORY_NOT_FOUND
                );
            }

            // Créer et persister l'article
            ArticleDto articleDto =  ArticleDto.builder()
                    .codeArticle(csv.getCodeArticle())
                    .designation(csv.getDesignation())
                    .prixUnitaireHt(new BigDecimal(csv.getPrixUnitaireHt()))
                    .tauxTval(new BigDecimal(csv.getTauxTval()))
                    .prixUnitaireTtc(new BigDecimal(csv.getPrixUnitaireTtc()))
                    .photo(csv.getPhoto())
                    .sousCategoryDto(sousCategoryDto)
                    .idEntreprise(tenantContext.getCurrentTenant())
                    .build();
            return articleService.createArticle(articleDto);
        } catch (Exception e) {
            log.error("Erreur lors du mappage de l'article CSV: {}", e.getMessage());
            return null;
        }
    }
}
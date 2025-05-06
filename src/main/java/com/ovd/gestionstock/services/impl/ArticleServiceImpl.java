package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.dto.LigneCommandeFournisseurDto;
import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.LigneCommandeFournisseur;
import com.ovd.gestionstock.repositories.ArticleRepository;
import com.ovd.gestionstock.repositories.LigneCommandeClientRepository;
import com.ovd.gestionstock.repositories.LigneCommandeFournisseurRepository;
import com.ovd.gestionstock.repositories.LigneVenteRepository;
import com.ovd.gestionstock.services.ArticleService;
import com.ovd.gestionstock.services.TenantSecurityService;
import com.ovd.gestionstock.validators.ArticleValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final LigneVenteRepository ligneVenteRepository;
    private final LigneCommandeClientRepository ligneClientRepository;
    private final LigneCommandeFournisseurRepository ligneFournisseurRepository;
    private final TenantSecurityService tenantSecurityService;
    private final TenantContext tenantContext;


    @Override
    public List<ArticleDto> getAllArticle() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleDto> articleDto = articles.stream().map(ArticleDto::fromEntity).collect(Collectors.toList());
        return articleDto;
    }

    @Override
    public List<LigneVenteDto> findHistoriqueVente(Long idArticle) {
        return ligneVenteRepository.findAllByArticleId(idArticle).stream().map(LigneVenteDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeClientDto> findHistoriqueCommandeClient(Long idArticle) {
        return ligneClientRepository.findAllByArticleId(idArticle).stream().map(LigneCommandeClientDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Long idArticle) {
        return ligneFournisseurRepository.findAllByArticleId(idArticle).stream().map(LigneCommandeFournisseurDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<ArticleDto> findHistoriqueByIdSousCatergory(Long idCategory) {
        return articleRepository.findAllBySousCategoryId(idCategory).stream().map(ArticleDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteArticle(Long id) {
        if (id == null){
            log.info("ID article est introuvable");
            return;
        }
        articleRepository.deleteById(id);

    }

    @Override
    public ArticleDto getArticleById(Long id) {
        if (id == null) {
            log.info("ID est null");
            return null;
        }
        Optional<Article> article = articleRepository.findById(id);
        ArticleDto articleDto = ArticleDto.fromEntity(article.get());
        return  Optional.of(articleDto).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun article avec l'id  = " +id+ " n'a été trouvé",
                        ErrorCodes.ARTICLE_NOT_FOUND
                )
        );
    }

    @Override
    public ArticleDto createArticle(ArticleDto articleDto) {

        List<String> errors = ArticleValidator.validate(articleDto);

        if(!errors.isEmpty()){
            log.error("errors "+errors);
            throw  new InvalidEntityException("Les infos de l'article ne sont pas valides", ErrorCodes.ARTICLE_NOT_FOUND,errors);
        }

        Article article = ArticleDto.toEntity(articleDto);
        article.setIdEntreprise(tenantContext.getCurrentTenant());
        articleRepository.save(article);

        return ArticleDto.fromEntity(article);

    }
}

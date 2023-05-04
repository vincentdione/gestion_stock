package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.repositories.ArticleRepository;
import com.ovd.gestionstock.services.ArticleService;
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


    @Override
    public List<ArticleDto> getAllArticle() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleDto> articleDto = articles.stream().map(ArticleDto::fromEntity).collect(Collectors.toList());
        return articleDto;
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

        Article article = articleRepository.save(ArticleDto.toEntity(articleDto));

        return ArticleDto.fromEntity(article);

    }
}

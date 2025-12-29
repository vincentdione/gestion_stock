package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.ArticleDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleImportService {
    List<ArticleDto> importArticlesFromExcel(MultipartFile file) throws IOException;
    List<ArticleDto> importArticlesFromCsv(MultipartFile file) throws IOException;
}
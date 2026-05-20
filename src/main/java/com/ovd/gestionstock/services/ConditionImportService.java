package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.ConditionAVImportDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ConditionImportService {
    List<ConditionAVImportDto> importConditionsFromCsv(MultipartFile file) throws IOException;
}
package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    public String storeFile(MultipartFile file, String subDirectory, String customFileName) {
        try {
            // Créer le répertoire s'il n'existe pas
            Path uploadPath = Paths.get(uploadDir, subDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Utiliser le nom de fichier personnalisé
            Path filePath = uploadPath.resolve(customFileName);

            // Copier le fichier
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return subDirectory + "/" + customFileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + file.getOriginalFilename(), ex);
        }
    }

    // Gardez aussi l'ancienne méthode pour compatibilité
    public String storeFile(MultipartFile file, String subDirectory) {
        return storeFile(file, subDirectory, file.getOriginalFilename());
    }

    public void deleteFile(String filePath) {
        try {
            Path path = Paths.get(uploadDir, filePath);
            Files.deleteIfExists(path);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file " + filePath, ex);
        }
    }
}
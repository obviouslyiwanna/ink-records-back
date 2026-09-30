package com.inkrecords.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class UploadService {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp"
    );

    private final Path uploadRoot;

    public UploadService(@Value("#{environment['ink-records.upload-dir']}") String uploadDir) {
        this.uploadRoot = Path.of(uploadDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    void createUploadDirectory() throws IOException {
        Files.createDirectories(uploadRoot);
    }

    public Map<String, String> save(MultipartFile file) {
        if (file.isEmpty() || !ALLOWED_TYPES.contains(file.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "只支持 JPEG、PNG、GIF、WebP 图片");
        }

        String extension = extensionFor(file.getContentType());
        String filename = UUID.randomUUID() + extension;
        Path target = uploadRoot.resolve(filename).normalize();

        if (!target.startsWith(uploadRoot)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "非法文件名");
        }

        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return Map.of("url", "/uploads/" + filename);
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "图片保存失败", exception);
        }
    }

    private String extensionFor(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> throw new IllegalArgumentException("Unsupported image type");
        };
    }
}

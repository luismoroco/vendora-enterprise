package com.vendora.backend.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

  @Value("${app.upload.dir:uploads/images}")
  private String uploadDir;

  @Value("${app.base.url:http://localhost:8080}")
  private String baseUrl;

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
    try {
      // Validate file
      if (file.isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
      }

      // Validate file type
      String contentType = file.getContentType();
      if (contentType == null || !contentType.startsWith("image/")) {
        return ResponseEntity.badRequest().body(Map.of("error", "File must be an image"));
      }

      // Create upload directory if it doesn't exist
      Path uploadPath = Paths.get(uploadDir);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }

      // Generate unique filename
      String originalFilename = file.getOriginalFilename();
      String fileExtension = "";
      if (originalFilename != null && originalFilename.contains(".")) {
        fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
      }
      String filename = UUID.randomUUID().toString() + fileExtension;

      // Save file
      Path filePath = uploadPath.resolve(filename);
      Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

      // Generate URL
      String imageUrl = baseUrl + "/api/v1/images/" + filename;

      log.info("Image uploaded successfully: {}", filename);

      Map<String, String> response = new HashMap<>();
      response.put("url", imageUrl);
      response.put("filename", filename);

      return ResponseEntity.ok(response);

    } catch (IOException e) {
      log.error("Error uploading image", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", "Failed to upload image: " + e.getMessage()));
    }
  }

  @GetMapping("/{filename}")
  public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
    try {
      Path imagePath = Paths.get(uploadDir).resolve(filename);

      if (!Files.exists(imagePath)) {
        return ResponseEntity.notFound().build();
      }

      byte[] imageBytes = Files.readAllBytes(imagePath);

      // Determine content type
      String contentType = Files.probeContentType(imagePath);
      if (contentType == null) {
        contentType = "application/octet-stream";
      }

      return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .body(imageBytes);

    } catch (IOException e) {
      log.error("Error reading image: {}", filename, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @DeleteMapping("/{filename}")
  public ResponseEntity<Map<String, String>> deleteImage(@PathVariable String filename) {
    try {
      Path imagePath = Paths.get(uploadDir).resolve(filename);

      if (!Files.exists(imagePath)) {
        return ResponseEntity.notFound().build();
      }

      Files.delete(imagePath);
      log.info("Image deleted successfully: {}", filename);

      return ResponseEntity.ok(Map.of("message", "Image deleted successfully"));

    } catch (IOException e) {
      log.error("Error deleting image: {}", filename, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", "Failed to delete image: " + e.getMessage()));
    }
  }
}


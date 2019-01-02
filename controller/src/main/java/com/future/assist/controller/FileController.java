package com.future.assist.controller;

import com.future.assist.service.service_impl.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FileController {
    @Autowired
    private FileStorageService storageService;

    @PostMapping("/upload")
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) {
        String filename = storageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/download/")
                .path(filename)
                .toUriString();

        Map<String, String> response = new HashMap<>();
        response.put("file", fileDownloadUri);
        return response;
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity downloadFIle(@PathVariable String filename, HttpServletRequest request) {
        Resource resource = storageService.loadFileAsResource(filename);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (Exception ex) {
            contentType = "application/octet-stream";
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }


        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        contentType.equals("application/octet-stream") ? "attachment; filename=\"" : "inline; filename=\""
                                + resource.getFilename() + "\"")
                .body(resource);
    }
}

package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.FileStorageException;
import com.future.office_inventory_system.exception.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileStorageService {

    private static final String uploadDir = "./static";

    private Path fileStorageLocation;

    public FileStorageService() {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new FileStorageException("cannot create directory", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String[] originalName = StringUtils.cleanPath(file.getOriginalFilename()).split("\\.");
    
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    
        String dateString = format.format(new Date());
        String filename = dateString + (originalName.length - 1 == 0 ? "" : "." + originalName[originalName.length - 1]);
        
        try {
            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("could not store file " + filename, e);
        }

        return filename;
    }

    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
            else {
                throw new NotFoundException("File not found " + filename);
            }
        } catch (MalformedURLException e) {
            throw new NotFoundException("File not found " + filename, e);
        }
    }

}

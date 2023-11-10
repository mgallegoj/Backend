package com.app.requerimiento29.uploadAndDownload.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.requerimiento29.uploadAndDownload.service.UploadAndDownloadService;

@RestController
@RequestMapping("/api.dian-descuento/v1/file")
public class UploadAndDownloadController {

    @Autowired
    private final UploadAndDownloadService uploadAndDownloadService;

    public UploadAndDownloadController(UploadAndDownloadService uploadAndDownloadService) {
        this.uploadAndDownloadService = uploadAndDownloadService;
    }
    
    @PostMapping("/data/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        // The file must follow the next structure:
        // first the header and then the data following the header structure
        // TYPE,document,document_type,first_name,last_name,over_60,head_of_household,unemployed,city,address,stratum,tax,propertyType
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            String message = uploadAndDownloadService.processData(content);
            return ResponseEntity.ok("{\"message\":\"" + message + "\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{document}/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String document) {
       try {
        byte[] data = uploadAndDownloadService.downloadOwnerData(document);
        ByteArrayResource resource = new ByteArrayResource(data);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document + ".txt");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        return ResponseEntity.ok().headers(headers).body(resource);
                
       } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
       }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
       } 
    }

}

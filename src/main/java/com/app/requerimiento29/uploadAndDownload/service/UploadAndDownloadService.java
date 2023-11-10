package com.app.requerimiento29.uploadAndDownload.service;

public interface UploadAndDownloadService {
    String processData(String data);
    byte[] downloadOwnerData(String document);
}

package com.app.requerimiento29.uploadAndDownload.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Service
public class UploadAndDownloadServiceImp implements UploadAndDownloadService{
    
    @Value("${server.port}")
    private int port;
    
    private String URL;
    private String createUserURL;
    private String createPropertyURL;
    private String consultURL;
    
    @Autowired
    private final RestTemplate restTemplate;

    public UploadAndDownloadServiceImp(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void initUrls() {
        this.URL = "http://localhost:" + port + "/api.dian-descuento/v1";
        this.createUserURL = URL + "/inscripciones/owners";
        this.createPropertyURL = URL + "/inscripciones/properties";
        this.consultURL = URL + "/consults/";
    }

    @Override
    public byte[] downloadOwnerData(String document) {
        try {
            String url = consultURL + document + "/more";
            String data = restTemplate.getForObject(url, String.class);
            if (data != null) {
                ObjectMapper mapper = new ObjectMapper();
                // Habilitar la indenación para formateo bonito
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                
                // Convertir JSON a Objeto y luego a JSON formateado
                Object json = mapper.readValue(data, Object.class);
                String formattedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                
                // Retornar el JSON formateado como bytes
                return formattedJson.getBytes(StandardCharsets.UTF_8);
            } else {
                throw new RuntimeException("No se han encontrado datos para " + document);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON data", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los datos.", e);
        }
    }

    // Upload data to register
    @Override
    public String processData(String fileString) {
        Integer errorCounter = 0;
        List<String> lines = Arrays.asList(fileString.split("\r?\n"));
        //Processes the file line by line
        for(int i = 1; i < lines.size(); i++) {
            try {
                String line = lines.get(i);
                List<String> dataLine = Arrays.asList(line.split(","));
                if ("PROPIETARIO".equals(dataLine.get(0))) {
                    String document = dataLine.get(1);
                    String document_type =dataLine.get(2);
                    String first_name =dataLine.get(3);
                    String last_name =dataLine.get(4);
                    String over_60 =dataLine.get(5);
                    String head_of_household =dataLine.get(6);
                    String unemployed = dataLine.get(7);
                    String city =  dataLine.get(8);
                    String address =  dataLine.get(9);
                    Integer stratum =  Integer.parseInt(dataLine.get(10));
                    Double tax =  Double.parseDouble(dataLine.get(11));
                    String propertyType =  dataLine.get(12);
                    if (!createUserAPI(document, document_type, first_name,
                        last_name, over_60, head_of_household, unemployed,
                        city, address, stratum, tax, propertyType)){
                        errorCounter ++;
                    }
                } else if ("PROPIEDAD".equals(dataLine.get(0))) {
                    String city =  dataLine.get(8);
                    String address =  dataLine.get(9);
                    Integer stratum =  Integer.parseInt(dataLine.get(10));
                    Double tax =  Double.parseDouble(dataLine.get(11));
                    String propertyType =  dataLine.get(12);
                    String document =  dataLine.get(1);
                    if (!createPropertyAPI(city, address, stratum,
                        tax, propertyType, document)) {
                            errorCounter ++;
                        }
                } else {
                    errorCounter ++;
                }
            } catch (NumberFormatException e) {
                    errorCounter++;
                    continue;  // skip processing this line
                }
        }
        return errorCounter + " campos de propietarios o propiedades tenían un problema.";
    }
    
    // Api call to create user
    private Boolean createUserAPI(String document, String document_type, String first_name, String last_name, String over_60, String head_of_household, String unemployed, String city, String address, Integer stratum, Double tax, String propertyType) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("document", document);
            map.put("document_type", document_type);
            map.put("first_name", first_name);
            map.put("last_name", last_name);
            map.put("over_60", over_60);
            map.put("head_of_household", head_of_household);
            map.put("unenployed", unemployed);
            //Property data
            Map<String, Object> property = new HashMap<>();
            property.put("city", city);        
            property.put("address", address);
            property.put("stratum", stratum);
            property.put("tax", tax);
            property.put("propertyType", propertyType);   
            List<Map<String, Object>> propertiesList = new ArrayList <>();
            propertiesList.add(property);
            map.put("ownerProperties", propertiesList);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(createUserURL, requestEntity, String.class);
            return response.getStatusCode().is2xxSuccessful(); 
        } catch (RuntimeException e) {
            
            return false;
        }
    }
    // Api call to create property
    private Boolean createPropertyAPI(String city, String address, Integer stratum, Double tax, String propertyType, String document) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("city", city);        
            map.put("address", address);
            map.put("stratum", stratum);
            map.put("tax", tax);
            map.put("propertyType", propertyType);        
            //Owner data for registration
            Map<String, String> ownerDocument = new HashMap<>();
            ownerDocument.put("document", document);
            map.put("owner", ownerDocument);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(createPropertyURL, requestEntity, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (RuntimeException e) {
            return false;
        }
    }
    
}

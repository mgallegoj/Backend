package com.app.requerimiento29.consults.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.requerimiento29.consults.dto.OwnerDTO;
import com.app.requerimiento29.consults.dto.PropertyBasicDetailsDTO;
import com.app.requerimiento29.consults.dto.PropertyDetailsDTO;
import com.app.requerimiento29.consults.repository.PropertyConsultRepository;

@Service
public class ConsultServiceImp implements ConsultService{
    
    @Autowired
    private PropertyConsultRepository propertyConsultRepository;
	
	public ConsultServiceImp(PropertyConsultRepository propertyConsultRepository) {
		this.propertyConsultRepository = propertyConsultRepository;
	}

    // Gets all the owner data
    @Override
    public OwnerDTO ownerData(String ownerId) {
        OwnerDTO owner = propertyConsultRepository.findOwnerByDocument(ownerId);
        if (owner == null) {
            // Puedes manejarlo lanzando una excepción o devolviendo null
            throw new RuntimeException("Propietario no encontrado.");
        }
        List<PropertyDetailsDTO> properties  = propertyConsultRepository.findDetailsByDocument(ownerId);
        owner.setProperties(properties);
        return owner;
    }

    // Gets all the owner properties
    @Override
    public List<PropertyBasicDetailsDTO> findPropertiesByOwner(String ownerId) {
        return propertyConsultRepository.findPropertyDetailsByOwnerDocument(ownerId);
    }
}

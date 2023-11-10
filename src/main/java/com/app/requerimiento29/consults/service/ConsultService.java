package com.app.requerimiento29.consults.service;

import java.util.List;

import com.app.requerimiento29.consults.dto.OwnerDTO;
import com.app.requerimiento29.consults.dto.PropertyBasicDetailsDTO;

public interface ConsultService {
    OwnerDTO ownerData(String ownerId);
    List<PropertyBasicDetailsDTO> findPropertiesByOwner(String ownerId);
}

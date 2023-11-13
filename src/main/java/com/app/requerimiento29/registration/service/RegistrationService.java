package com.app.requerimiento29.registration.service;

import com.app.requerimiento29.registration.model.OwnerModel;
import com.app.requerimiento29.registration.model.PropertyModel;

public interface RegistrationService {
    OwnerModel createOwner(OwnerModel owner) throws IllegalArgumentException;
	OwnerModel getOwner(String ownerId);
	Boolean existsPropertyByCityAndAddress(String city, String address);
	PropertyModel createProperty(PropertyModel property);
}

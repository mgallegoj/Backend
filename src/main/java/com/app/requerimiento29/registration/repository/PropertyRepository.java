package com.app.requerimiento29.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.requerimiento29.registration.model.PropertyModel;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyModel, Long>{
	Boolean existsPropertyByCityAndAddress(String city, String address);
}
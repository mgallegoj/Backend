package com.app.requerimiento29.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.requerimiento29.registration.model.OwnerModel;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerModel, String>{
    
}

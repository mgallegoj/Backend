package com.app.requerimiento29.consults.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.requerimiento29.consults.dto.OwnerDTO;
import com.app.requerimiento29.consults.dto.PropertyBasicDetailsDTO;
import com.app.requerimiento29.consults.dto.PropertyDetailsDTO;
import com.app.requerimiento29.consults.model.PropertyConsultModel;

@Repository
public interface PropertyConsultRepository extends JpaRepository<PropertyConsultModel, Long> {

    @Query("SELECT new com.app.requerimiento29.consults.dto.OwnerDTO(o.documentType, o.document, o.firstName, o.lastName, o.over60, o.headOfHousehold, o.unemployed) FROM OwnerConsultModel o WHERE o.document = :ownerId")
    OwnerDTO findOwnerByDocument(@Param("ownerId") String ownerId);

    @Query("SELECT new com.app.requerimiento29.consults.dto.PropertyBasicDetailsDTO(p.city, p.address, d.deadlineDate, d.netPayment) FROM PropertyConsultModel p JOIN p.discount d WHERE p.owner.document = :ownerId")
    List<PropertyBasicDetailsDTO> findPropertyDetailsByOwnerDocument(@Param("ownerId") String ownerId);
    
    @Query("SELECT NEW com.app.requerimiento29.consults.dto.PropertyDetailsDTO(p.idProperty, p.city, p.address, p.stratum, p.requestDate, d.deadlineDate, d.netPayment, d.status) FROM PropertyConsultModel p JOIN p.discount d WHERE p.owner.document = :ownerId")
    List<PropertyDetailsDTO> findDetailsByDocument(@Param("ownerId") String ownerId);
    
}

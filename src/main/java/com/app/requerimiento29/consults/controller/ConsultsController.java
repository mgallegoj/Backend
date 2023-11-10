package com.app.requerimiento29.consults.controller;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.requerimiento29.consults.dto.OwnerDTO;
import com.app.requerimiento29.consults.dto.PropertyBasicDetailsDTO;
import com.app.requerimiento29.consults.service.ConsultService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api.dian-descuento/v1/consults")
public class ConsultsController {

    //Date when consults can be done
    private static final LocalDate consultStart = LocalDate.of(2023, Month.AUGUST,1);

    @Autowired
    private final ConsultService consultService;

    public ConsultsController(ConsultService consultService) {
        this.consultService = consultService;
    }

    // Gets all the owner data
    @GetMapping("/{document}/more")
    public ResponseEntity<?> moreOwnerData(@PathVariable String document) {
        try {
            OwnerDTO ownerDetails = consultService.ownerData(document);
            if (ownerDetails != null) {
                return new ResponseEntity<>(ownerDetails, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No se han encontrado datos para el documento dado.", HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Aviso: " + e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Se ha producido un error inesperado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Gets all the owner properties
    @GetMapping("/{document}")
    public ResponseEntity<?> findPropertiesByOwner(@PathVariable String document) {
        try {
            isConsultPeriod();
            List<PropertyBasicDetailsDTO> ownerDetails = consultService.findPropertiesByOwner(document);
            if (ownerDetails != null) {
                return new ResponseEntity<>(ownerDetails, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No se han encontrado datos para el documento dado.", HttpStatus.NO_CONTENT);
            }
        } catch (IllegalStateException e){
	    	return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	    }  catch (Exception e) {
            return new ResponseEntity<>("Se ha producido un error inesperado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void isConsultPeriod() {
        //LocalDate todayDate = LocalDate.of(2023, Month.JUNE,16);
        LocalDate todayDate = LocalDate.now();
        if (todayDate.isBefore(consultStart)) {
            throw new IllegalStateException("Lo sentimos, aún no se permiten consultas.");
        }
    }
}

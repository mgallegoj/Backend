package com.app.requerimiento29.validation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.requerimiento29.validation.service.ValidationService;

@RestController
@RequestMapping("/api.dian-descuento/v1/validation")
public class ValidationController {
    
    @Autowired
    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    // Update status to Pagado
    @PatchMapping("/{propertyId}")
    public ResponseEntity<String> patchOwner(@PathVariable String propertyId) {
        try {
            Long id = Long.parseLong(propertyId);
            validationService.validatePayment(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);            
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Formato de entrada no valido. Por favor, introduzca un numero valido.", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Execute batch processing
    @PatchMapping("/all")
    public ResponseEntity<String> executeBatches() {
        try {
            validationService.executeValidation();
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

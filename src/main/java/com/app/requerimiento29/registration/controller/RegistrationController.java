package com.app.requerimiento29.registration.controller;

import java.lang.annotation.AnnotationTypeMismatchException;
import java.time.LocalDate;
import java.time.Month;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.requerimiento29.registration.model.OwnerModel;
import com.app.requerimiento29.registration.model.PropertyModel;
import com.app.requerimiento29.registration.service.RegistrationService;

@RestController
@RequestMapping("/api.dian-descuento/v1/inscripciones")
public class RegistrationController {

	// Dates for the requirement
	// private static final LocalDate endRegistrations = LocalDate.of(2023, Month.JULY,20);
	//private static final LocalDate startRegistrations = LocalDate.of(2023, Month.JULY,1);

	private static final LocalDate endRegistrations = LocalDate.of(2023, Month.DECEMBER ,31);
	private static final LocalDate startRegistrations = LocalDate.of(2023, Month.JULY,1);
	
	@Autowired
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

	@PostMapping("/owners")
	public ResponseEntity<?> createOwner(@RequestBody OwnerModel owner) {
		try {
			isWithinRegistrationPeriod();
            OwnerModel newOwner = registrationService.createOwner(owner);
			return new ResponseEntity<>(newOwner, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (EntityExistsException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (IllegalStateException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("Se ha producido un error inesperado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/owners/{document}")
	public ResponseEntity<?> getOwner(@PathVariable String document) {
		try {
            OwnerModel owner = registrationService.getOwner(document);
	        return new ResponseEntity<>(owner, HttpStatus.OK);
	    } catch (EntityNotFoundException e){
	    	return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	    } catch (AnnotationTypeMismatchException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("Se ha producido un error inesperado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/properties")
	public ResponseEntity<?> createProperty(@RequestBody PropertyModel property) {
		try {
			isWithinRegistrationPeriod();
			PropertyModel newProperty = registrationService.createProperty(property);
            return new ResponseEntity<>(newProperty, HttpStatus.CREATED);
		} catch (IllegalArgumentException e){
	    	return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	    } catch (EntityNotFoundException e) {
	    	return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
			return new ResponseEntity<>("Se ha producido un error inesperado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    private void isWithinRegistrationPeriod() {
        //LocalDate todayDate = LocalDate.now();
		LocalDate todayDate = LocalDate.of(2023, Month.JULY ,13);
        if (todayDate.isBefore(startRegistrations) || todayDate.isAfter(endRegistrations)) {
            throw new IllegalStateException("Lo sentimos, ya no se aceptan más registros.");
        }
    }

}

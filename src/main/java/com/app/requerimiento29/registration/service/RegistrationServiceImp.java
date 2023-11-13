package com.app.requerimiento29.registration.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.requerimiento29.registration.model.OwnerModel;
import com.app.requerimiento29.registration.model.PropertyModel;
import com.app.requerimiento29.registration.repository.OwnerRepository;
import com.app.requerimiento29.registration.repository.PropertyRepository;

@Service
public class RegistrationServiceImp implements RegistrationService{

    @Value("${server.port}")
    private int port;

    private String discountApiURL;


    // Constants
    private static final double taxCoefficientStratum2 = 0.0035;
    private static final double taxCoefficientStratum4 = 0.006;
    private static final double taxCoefficientStratum6 = 0.011;
    private static final double taxCoefficientNoResidential = 0.01;

    @Autowired
    private final PropertyRepository propertyRepository;
    @Autowired
    private final OwnerRepository ownerRepository;
    @Autowired
    private final RestTemplate restTemplate;


    @PostConstruct
    public void initUrls() {
        this.discountApiURL = "http://localhost:" + port + "/api.dian-descuento/v1/discounts/discount_registration";
    }


	public RegistrationServiceImp(PropertyRepository propertyRepository, OwnerRepository ownerRepository, RestTemplateBuilder restTemplateBuilder) {
		this.propertyRepository = propertyRepository;
        this.ownerRepository = ownerRepository;
        this.restTemplate = restTemplateBuilder.build();
	}		 

    //Creates owner and property if owner is new, or if owner exists just creates property

    @Override
	public OwnerModel createOwner(OwnerModel owner) {
        //Do not want to update owner
        
		if (ownerRepository.existsById(owner.getDocument())) {
            owner.getOwnerProperties().get(0).setOwner(owner);
            PropertyModel newProperty = owner.getOwnerProperties().get(0);
            createProperty(newProperty);
            return owner;
        }
        if (!(isValidResponse(owner.getUnenployed()) && isValidResponse(owner.getHead_of_household()) && isValidResponse(owner.getOver_60()))) {
        	throw new IllegalArgumentException("La respuesta debe ser 'Si' o 'No'.");
        }
        if (!(isValidResponse(owner))) {
            throw new IllegalArgumentException("El tipo de documento debe ser 'CC', 'TI', 'CE', 'Pasaporte', 'VR' o 'RCN'.");
        }
    	isValidlen(owner.getDocument(), "document", 15);
    	isValidlen(owner.getFirst_name(), "first name", 15);
    	isValidlen(owner.getLast_name(), "last name", 15);
        OwnerModel newOwner = ownerRepository.save(owner);
        try {
            owner.getOwnerProperties().get(0).setOwner(owner);
            PropertyModel newProperty = owner.getOwnerProperties().get(0);
            createProperty(newProperty);            
        } catch (Exception e) {
            ownerRepository.delete(newOwner);
            throw new RuntimeException("No se ha podido registrar al propietario.");
        }
        return newOwner;
	}
	
	@Override
	public OwnerModel getOwner(String document) {
		isValidlen(document, "document", 15);
	    return ownerRepository.findById(document).orElseThrow(() -> new EntityNotFoundException("El usuario con documento: " + document + " no fue encontrado."));
	}

	@Override
	public PropertyModel createProperty(PropertyModel property) {
        LocalDate todayDate = LocalDate.now();
        //LocalDate todayDate = LocalDate.of(2023, Month.JULY ,13);
        String ownerDocument = property.getOwner().getDocument();
        OwnerModel existingOwner = getOwner(ownerDocument);

        property.setCity(property.getCity().toLowerCase());
	    property.setAddress(property.getAddress().toLowerCase());

        String lowerCaseCity = property.getCity();
        String lowerCaseAddress = property.getAddress();

		isValidlen(lowerCaseCity, "city", 20);
		isValidlen(lowerCaseAddress, "address", 30);

        if (property.getStratum() > 6 || property.getStratum() <= 0) {
            throw new IllegalArgumentException("Valor de estrato no válido.");
        }
        if (!isValidPropertyType(property.getPropertyType())) {
            throw new IllegalArgumentException("Tipo de propiedad no válida.");
        }
        if (existsPropertyByCityAndAddress(lowerCaseCity, lowerCaseAddress)) {
            throw new IllegalArgumentException("La propiedad " + lowerCaseCity + " " + lowerCaseAddress + " ya fue registrada.");
        }

	    int stratum = property.getStratum();
	    double tax = property.getTax();
	    double taxValue = 0;

        //Calculate the value of the property tax
        if ("Residencial".equals(property.getPropertyType())) {
            if (stratum <= 2) {
                taxValue = taxCoefficientStratum2*tax;
            } else if (stratum <= 4) {
                taxValue = taxCoefficientStratum4*tax;
            } else {
                taxValue = taxCoefficientStratum6*tax;
            }
        } else {
            taxValue = taxCoefficientNoResidential*tax;
        }

        property.setOwner(existingOwner);
        property.setTax(taxValue);
        property.setRequestDate(todayDate);
        //First saves the property
        PropertyModel newProperty = propertyRepository.save(property);
        Integer specialDiscount = 2;
        Integer strataDiscount = 1;

        if(newProperty.getOwner().getOver_60().equals("Si") || 
        newProperty.getOwner().getHead_of_household().equals("Si") || 
        newProperty.getOwner().getUnenployed().equals("Si")) {
            specialDiscount = 1;
        }

        if(stratum == 1 || stratum == 2) {
            strataDiscount = 3;
        } else if(stratum == 3 || stratum == 4) {
            strataDiscount = 2;
        } else {
            strataDiscount = 1;
        }

        //Api call to create the discount
        postDiscountToAPI(newProperty.getTax(), 
                        newProperty.getRequestDate(),
                        "Sin pagar", 
                        newProperty.getIdProperty(), 
                        newProperty.getOwner().getDocument(), 
                        strataDiscount, 
                        specialDiscount);
                        
        return newProperty;
	}
    
    @Override
    public Boolean existsPropertyByCityAndAddress(String city, String address) {
        return propertyRepository.existsPropertyByCityAndAddress(city.toLowerCase(), address.toLowerCase()) ;
    }

    //Api call
    private void postDiscountToAPI(Double NET_PAYMENT, LocalDate DEADLINE_DATE, String STATUS, Long ID_PROPERTY, String DOCUMENT, Integer ID_STRATUM_DISCOUNT, Integer ID_SPECIAL_DISCOUNT) {
        String url = discountApiURL;
        Map<String, Object> map = new HashMap<>();
        map.put("netPayment", NET_PAYMENT);
        map.put("deadlineDate", DEADLINE_DATE);
        map.put("status", STATUS);
        map.put("idProperty", ID_PROPERTY);
        map.put("document", DOCUMENT);

        // Add stratumDiscount and specialDiscount as nested maps
        Map<String, Integer> stratumDiscount = new HashMap<>();
        stratumDiscount.put("idStrataDiscount", ID_STRATUM_DISCOUNT);
        map.put("stratumDiscount", stratumDiscount);
        
        Map<String, Integer> specialDiscount = new HashMap<>();
        specialDiscount.put("idSpecialDiscount", ID_SPECIAL_DISCOUNT);
        map.put("specialDiscount", specialDiscount);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (response.getStatusCode() != HttpStatus.CREATED) {
            propertyRepository.deleteById(ID_PROPERTY);
            throw new RuntimeException("No se ha podido registrar el descuento.");
        }
    }

	private boolean isValidResponse(String yesno) {
	    return "No".equals(yesno) || "Si".equals(yesno);
	}
	
	private boolean isValidResponse(OwnerModel owner) {
	    return "CC".equals(owner.getDocument_type()) || 
                "TI".equals(owner.getDocument_type()) || 
                "CE".equals(owner.getDocument_type()) || 
                "Pasaporte".equals(owner.getDocument_type()) || 
                "VR".equals(owner.getDocument_type()) || 
                "RCN".equals(owner.getDocument_type());
	}
	
	private void isValidlen(String owner, String field, int num) {
	    if (owner != null && owner.length() > num)  {
	    	throw new IllegalArgumentException("El campo " + field + " tiene un problema.");
	    }
	}

    private boolean isValidPropertyType(String propertyType) {
	    return "Residencial".equals(propertyType) || "No residencial".equals(propertyType);
	}
}
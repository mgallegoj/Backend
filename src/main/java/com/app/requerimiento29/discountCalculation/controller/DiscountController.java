package com.app.requerimiento29.discountCalculation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.requerimiento29.discountCalculation.Model.DiscountRegistrationModel;
import com.app.requerimiento29.discountCalculation.service.DiscountService;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api.dian-descuento/v1/discounts")
public class DiscountController {

    @Autowired
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    // Generates discount registration
    @PostMapping("/discount_registration")
    public ResponseEntity<?> generateDiscount(@RequestBody DiscountRegistrationModel discountRegistration) {
        try {
            DiscountRegistrationModel newDiscount = discountService.generateDiscount(discountRegistration);
            return new ResponseEntity<>(newDiscount, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get owner discounts
    @GetMapping("/discount_registration/{document}")
    public ResponseEntity<?> getOwnerDiscounts(@PathVariable String document) {
        try {
            List<DiscountRegistrationModel> data = discountService.findAllByDocument(document);
            if(data.isEmpty()) {
                return new ResponseEntity<>("No se han encontrado descuentos para el documento proporcionado", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

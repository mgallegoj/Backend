package com.app.requerimiento29.discountCalculation.service;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.requerimiento29.discountCalculation.Model.DiscountRegistrationModel;
import com.app.requerimiento29.discountCalculation.Model.SpecialDiscountModel;
import com.app.requerimiento29.discountCalculation.Model.StrataDiscountModel;
import com.app.requerimiento29.discountCalculation.repository.DiscountRegistrationRepository;
import com.app.requerimiento29.discountCalculation.repository.SpecialDiscountRepository;
import com.app.requerimiento29.discountCalculation.repository.StrataDiscountRepository;

@Service
public class DiscountServiceImp implements DiscountService{
    @Autowired
    private final DiscountRegistrationRepository discountRepository;
    @Autowired
    private final SpecialDiscountRepository specialDiscountRepository;
    @Autowired
    private final StrataDiscountRepository strataDiscountRepository;
    
    public DiscountServiceImp(DiscountRegistrationRepository discountRepository, SpecialDiscountRepository specialDiscountRepository, StrataDiscountRepository strataDiscountRepository) {
        this.discountRepository = discountRepository;
        this.specialDiscountRepository = specialDiscountRepository;
        this.strataDiscountRepository = strataDiscountRepository;
    }

    // Generates discount registration
    @Override
    public DiscountRegistrationModel generateDiscount(DiscountRegistrationModel discountRegistration){
        String specialDiscountId = discountRegistration.getSpecialDiscount() != null ? discountRegistration.getSpecialDiscount().toString() : null;
        discountRegistration.setStatus("Sin pagar");
        
        if(specialDiscountId == null) {
            throw new IllegalArgumentException("El descuento especial es null.");
        }

        String stratumDiscountId = discountRegistration.getStratumDiscount() != null ? discountRegistration.getStratumDiscount().toString() : null;
        if(stratumDiscountId == null) {
            throw new IllegalArgumentException("El descuento por estrato es null.");
        }

        discountRegistration.setSpecialDiscount(getSpecialDiscountById(discountRegistration.getSpecialDiscount().getIdSpecialDiscount()));
        discountRegistration.setStratumDiscount(getStratumDiscountById(discountRegistration.getStratumDiscount().getIdStrataDiscount()));
        
        Double netPayment = discountRegistration.getNetPayment();
        // Convert to decimal
        Double stratumDiscountFactor = discountRegistration.getStratumDiscount().getStrataDiscount() / 100.0;
        Double specialDiscountFactor = discountRegistration.getSpecialDiscount().getSpecialDiscount() / 100.0;
        // Calculates the total discount
        Double totalDiscountFactor = 1 - (stratumDiscountFactor + specialDiscountFactor);
        // Apply discount to the tax
        netPayment = netPayment * totalDiscountFactor;
        discountRegistration.setNetPayment(netPayment);
        // Add 90 days to the registration date
        LocalDate newDateTime = discountRegistration.getDeadlineDate().plusDays(90);
        discountRegistration.setDeadlineDate(newDateTime);
        
        return discountRepository.save(discountRegistration);
    }

    // Get owner discounts
    @Override
    public List<DiscountRegistrationModel> findAllByDocument(String document) {
        return discountRepository.findByDocument(document);
    }

    @Override
    public SpecialDiscountModel getSpecialDiscountById(Integer id) {
        return specialDiscountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Descuento especial con id: " + id + " no fue encontrado."));
    }
    
    @Override
    public StrataDiscountModel getStratumDiscountById(Integer id) {
        return strataDiscountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Descuento por estrato con id: " + id + " no fue encontrado."));
    }

}
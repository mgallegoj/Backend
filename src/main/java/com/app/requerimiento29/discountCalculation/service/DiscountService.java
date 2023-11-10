package com.app.requerimiento29.discountCalculation.service;

import java.util.List;

import com.app.requerimiento29.discountCalculation.Model.DiscountRegistrationModel;
import com.app.requerimiento29.discountCalculation.Model.SpecialDiscountModel;
import com.app.requerimiento29.discountCalculation.Model.StrataDiscountModel;

public interface DiscountService {
    DiscountRegistrationModel generateDiscount(DiscountRegistrationModel discountModel);
    StrataDiscountModel getStratumDiscountById(Integer id);
    SpecialDiscountModel getSpecialDiscountById(Integer id);
    List<DiscountRegistrationModel> findAllByDocument(String document);
}

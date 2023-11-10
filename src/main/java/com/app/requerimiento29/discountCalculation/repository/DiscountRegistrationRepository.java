package com.app.requerimiento29.discountCalculation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.requerimiento29.discountCalculation.Model.DiscountRegistrationModel;

@Repository
public interface DiscountRegistrationRepository extends JpaRepository<DiscountRegistrationModel, Long>{
    List<DiscountRegistrationModel> findByDocument(String document);
}

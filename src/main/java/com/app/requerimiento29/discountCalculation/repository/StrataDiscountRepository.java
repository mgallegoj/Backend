package com.app.requerimiento29.discountCalculation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.requerimiento29.discountCalculation.Model.StrataDiscountModel;

@Repository
public interface StrataDiscountRepository extends JpaRepository<StrataDiscountModel, Integer>{
    
}

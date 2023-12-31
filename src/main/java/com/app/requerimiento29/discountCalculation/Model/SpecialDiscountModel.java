package com.app.requerimiento29.discountCalculation.Model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "PGP_SPECIAL_DISCOUNT")
public class SpecialDiscountModel {
    
    @Id
    @Column(name = "ID_SPECIAL_DISCOUNT", nullable = false)
    private Integer idSpecialDiscount;

    @Column(name = "SPECIAL_DISCOUNT", nullable = false)
    private Integer specialDiscount;

    @OneToMany(mappedBy = "specialDiscount")
    @JsonIgnore
    private List<DiscountRegistrationModel> specialDiscountRegistrations;

}

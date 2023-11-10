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
@Table(name = "PGP_STRATA_DISCOUNT")
public class StrataDiscountModel {
    @Id
    @Column(name = "ID_STRATUM_DISCOUNT", nullable = false)
    private Integer idStrataDiscount;

    @Column(name = "STRATUM_DISCOUNT", nullable = false)
    private Integer strataDiscount;
    
    @OneToMany(mappedBy = "stratumDiscount")
    @JsonIgnore
    private List<DiscountRegistrationModel> strataDiscountRegistrations;
}

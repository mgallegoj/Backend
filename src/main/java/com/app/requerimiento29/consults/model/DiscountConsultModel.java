package com.app.requerimiento29.consults.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "PGP_DISCOUNT_REGISTRATION")
public class DiscountConsultModel {
    @Id
    @Column(name = "ID_DISCOUNT_REGISTRATION")
    private Long id;

    @Column(name = "NET_PAYMENT")
    private Double netPayment;

    @Column(name = "DEADLINE_DATE")
    private LocalDate deadlineDate;

    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "ID_STRATUM_DISCOUNT")
    private Integer stratumDiscount;

    @Column(name = "ID_SPECIAL_DISCOUNT")
    private Integer specialDiscount;

    @OneToOne
    @JoinColumn(name = "ID_PROPERTY")
    private PropertyConsultModel property;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DOCUMENT")
    @JsonProperty(access = Access.READ_ONLY)
    private OwnerConsultModel discountOwner;

}

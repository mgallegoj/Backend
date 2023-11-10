package com.app.requerimiento29.discountCalculation.Model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
@Entity
@Table(name = "PGP_DISCOUNT_REGISTRATION")
public class DiscountRegistrationModel {
    @Id
    @Column(name="ID_DISCOUNT_REGISTRATION", nullable = false, updatable = false)
    @SequenceGenerator(
        name = "primary_sequence",
        sequenceName = "SEQ_DISCOUNT",
        allocationSize = 1,
        initialValue = 1
    )   
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "primary_sequence"
    )
    private Long id;

    @Column(name="NET_PAYMENT", nullable = false)
    private Double netPayment;

    @Column(name="DEADLINE_DATE", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadlineDate;

    @Column(name="STATUS", nullable = false, length = 10)
    private String status;

    @Column(name = "ID_PROPERTY", nullable = false, unique = true)
    private Long idProperty;

    @Column(name = "DOCUMENT", nullable = false)
    private String document;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_STRATUM_DISCOUNT", nullable = false)
    @JsonProperty(access = Access.READ_WRITE)
    private StrataDiscountModel stratumDiscount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_SPECIAL_DISCOUNT", nullable = false)
    @JsonProperty(access = Access.READ_WRITE)
    private SpecialDiscountModel specialDiscount;

}
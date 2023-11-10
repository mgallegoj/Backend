package com.app.requerimiento29.registration.model;

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
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name= "PGP_PROPERTY")
public class PropertyModel {

    @Id
    @Column(name="ID_PROPERTY", nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "SEQ_PROPERTY",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long idProperty;

    @Column(name="CITY", nullable = false, length = 20)
    private String city;

    @Column(name="ADDRESS", nullable = false, length = 30)
    private String address;

    @Column(name="STRATUM", nullable = false, length = 1)
    private Integer stratum;

    @Column(name="REQUEST_DATE", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestDate;

    @Column(name="TAX", nullable = false)
    private Double tax;

    @Column(name="PROPERTY_TYPE", nullable = false, length = 14)
    private String propertyType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="DOCUMENT")
    @JsonProperty(access = Access.READ_WRITE)
    private OwnerModel owner;

    public PropertyModel(Long idProperty, String city, String address, Integer stratum, Double tax, String propertyType, OwnerModel owner){
        this.idProperty = idProperty;
        this.city = city;
        this.address = address;
        this.stratum = stratum;
        this.tax = tax;
        this.propertyType = propertyType;
        this.owner = owner;
    }
}
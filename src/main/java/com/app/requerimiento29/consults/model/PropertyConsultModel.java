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
@Table(name = "PGP_PROPERTY")
public class PropertyConsultModel {
    
    @Id
    @Column(name = "ID_PROPERTY")
    private Long idProperty;

    @Column(name = "CITY")
    private String city;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "STRATUM")
    private Integer stratum;

    @Column(name = "REQUEST_DATE")
    private LocalDate requestDate;

    @Column(name = "TAX")
    private Double tax;

    @Column(name = "PROPERTY_TYPE")
    private String propertyType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DOCUMENT")
    @JsonProperty(access = Access.READ_ONLY)
    private OwnerConsultModel owner;
    
    @OneToOne(mappedBy = "property")
    private DiscountConsultModel discount;

}

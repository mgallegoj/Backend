package com.app.requerimiento29.consults.model;

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
@Table(name = "PGP_OWNER")
public class OwnerConsultModel {
    @Id
    @Column(name = "DOCUMENT")
    private String document;

    @Column(name = "DOCUMENT_TYPE")
    private String documentType;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "OVER_60")
    private String over60;

    @Column(name = "HEAD_OF_HOUSEHOLD")
    private String headOfHousehold;

    @Column(name = "UNENPLOYED")
    private String unemployed;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<PropertyConsultModel> properties;

    @OneToMany(mappedBy = "discountOwner")
    @JsonIgnore
    private List<DiscountConsultModel> discounts;
}

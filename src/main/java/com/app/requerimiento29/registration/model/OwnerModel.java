package com.app.requerimiento29.registration.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name= "PGP_OWNER")
public class OwnerModel {
    @Id
    @Column(name="DOCUMENT", nullable = false, updatable = false, length = 15)
    private String document;

    @Column(name="DOCUMENT_TYPE", nullable = false, length = 9)
    private String document_type;

    @Column(name="FIRST_NAME", nullable = false, length = 15)
    private String first_name;

    @Column(name="LAST_NAME", nullable = false, length = 15)
    private String last_name;

    @Column(name="OVER_60", nullable = false, length = 2)
    private String over_60;

    @Column(name="HEAD_OF_HOUSEHOLD", nullable = false, length = 2)
    private String head_of_household;

    @Column(name="UNENPLOYED", nullable = false, length = 2)
    private String unenployed;
    
    @JsonProperty(access = Access.WRITE_ONLY)
    @OneToMany(mappedBy="owner")
    private List<PropertyModel> ownerProperties;

}

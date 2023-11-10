package com.app.requerimiento29.consults.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OwnerDTO {
    
    private String documentType;
    private String document;
    private String firstName;
    private String lastName;
    private String over60;
    private String headOfHousehold;
    private String unemployed;
    private List<PropertyDetailsDTO> properties;

    public OwnerDTO(String documentType, String document, String firstName, String lastName, String over60, String headOfHousehold, String unemployed) {
        this.documentType = documentType;
        this.document = document;
        this.firstName = firstName;
        this.lastName = lastName;
        this.over60 = over60;
        this.headOfHousehold = headOfHousehold;
        this.unemployed = unemployed;
    }

}

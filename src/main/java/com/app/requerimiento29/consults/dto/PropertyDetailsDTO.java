package com.app.requerimiento29.consults.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDetailsDTO {
    
    private Long idProperty;
    private String city;
    private String address;
    private Integer stratum;
    private LocalDate requestDate;
    private LocalDate deadlineDate;
    private Double netPayment;
    private String status;
}

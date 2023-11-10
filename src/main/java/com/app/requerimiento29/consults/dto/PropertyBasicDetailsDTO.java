package com.app.requerimiento29.consults.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyBasicDetailsDTO {
    private String city;
    private String address;
    private LocalDate deadlineDate;
    private Double netPayment;
}

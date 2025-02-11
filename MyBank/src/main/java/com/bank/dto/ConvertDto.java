package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ConvertDto {

    private String fromCurrency;
    private String toCurrency;
    private double amount;
}

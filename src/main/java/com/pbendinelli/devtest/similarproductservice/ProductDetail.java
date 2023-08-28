package com.pbendinelli.devtest.similarproductservice;

import lombok.*;

import java.math.BigDecimal;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDetail {

    private String id;
    private String name;
    private BigDecimal price;
    private boolean availability;
}

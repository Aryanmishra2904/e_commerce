package com.aryan.e_commerce.order;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddress {

    private String name;
    private String phone;
    private String city;
    private String state;
    private String zip;
    private String line1;
}

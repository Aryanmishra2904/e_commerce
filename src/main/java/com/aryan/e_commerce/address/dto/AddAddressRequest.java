package com.aryan.e_commerce.address.dto;

import lombok.Data;

@Data
public class AddAddressRequest {

    private String fullName;
    private String phone;

    private String street;
    private String city;
    private String state;
    private String pincode;

    private Boolean isDefault;
}

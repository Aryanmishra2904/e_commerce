package com.aryan.e_commerce.address;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "addresses")
public class Address {

    @Id
    private String id;

    private String userId;

    private String fullName;
    private String phone;

    private String street;
    private String city;
    private String state;
    private String pincode;

    private Boolean isDefault;
}

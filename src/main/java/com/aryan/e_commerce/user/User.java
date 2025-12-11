package com.aryan.e_commerce.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank
    private String name;

    @Email
    @Indexed(unique = true)
    private String email;

    @Size(min = 6)
    private String password;

    // ⭐ Use ENUM not String
    private Role role;

    @NotBlank
    @Indexed(unique = true)
    @Pattern(regexp = "^[0-9]{10}$")
    private String phone;

    private String resetPasswordToken;
    private Instant resetPasswordExpiry;

    private List<RabbitConnectionDetails.Address> addresses;
}

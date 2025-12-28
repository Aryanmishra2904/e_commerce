package com.aryan.e_commerce.address.controller;

import com.aryan.e_commerce.address.Address;
import com.aryan.e_commerce.address.dto.AddAddressRequest;
import com.aryan.e_commerce.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/address")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public Address addAddress(
            @RequestBody AddAddressRequest request,
            Authentication authentication) {

        return addressService.addAddress(
                authentication.getName(), request
        );
    }

    @GetMapping
    public List<Address> getAddresses(Authentication authentication) {
        return addressService.getUserAddresses(authentication.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(
            @PathVariable String id,
            Authentication authentication) {

        addressService.deleteAddress(authentication.getName(), id);
    }
}

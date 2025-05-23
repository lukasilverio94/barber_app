package com.barbershop.controller;

import com.barbershop.dto.CustomerCreateDTO;
import com.barbershop.dto.CustomerDTO;
import com.barbershop.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerCreateDTO dto) {
        CustomerDTO newCustomer = customerService.createCustomer(dto);
        URI location = URI.create("/customers/" + newCustomer.id());
        return ResponseEntity.created(location).body(newCustomer);
    }


    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

}

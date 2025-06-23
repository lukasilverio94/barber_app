package com.barbershop.controller;

import com.barbershop.dto.CustomerCreateDTO;
import com.barbershop.dto.CustomerDTO;
import com.barbershop.dto.mappers.CustomerMapper;
import com.barbershop.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody @Valid  CustomerCreateDTO dto) {
        CustomerDTO newCustomer = customerService.createCustomer(dto);
        URI location = URI.create("/customers/" + newCustomer.id());
        return ResponseEntity.created(location).body(newCustomer);
    }


    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable UUID id) {
        CustomerDTO customerDTO = CustomerMapper.toDto(customerService.findCustomerByIdOrThrow(id));
        return ResponseEntity.ok(customerDTO);
    }

}

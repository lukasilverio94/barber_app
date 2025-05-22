package com.barbershop.dto.mappers;

import com.barbershop.dto.CustomerCreateDTO;
import com.barbershop.dto.CustomerDTO;
import com.barbershop.model.Customer;

public class CustomerMapper {

    public static CustomerDTO toDto(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail()
        );
    }

    public static Customer toEntity(CustomerCreateDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.name());
        customer.setPhone(dto.phone());
        customer.setEmail(dto.email());
        return customer;
    }

}

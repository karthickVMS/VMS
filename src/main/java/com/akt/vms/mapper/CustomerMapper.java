package com.akt.vms.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.akt.vms.dto.CustomerDTO;
import com.akt.vms.entity.Customer;




@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDTO toDto(Customer customer);
    Customer toEntity(CustomerDTO dto);
}
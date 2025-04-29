package com.akt.vms.mapper;



import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.akt.vms.dto.CategoryDTO;
import com.akt.vms.dto.VehicleDTO;
import com.akt.vms.entity.Category;
import com.akt.vms.entity.Vehicle;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
	VehicleMapper INSTANCE = Mappers.getMapper(VehicleMapper.class);

	VehicleDTO toDto(Vehicle vehicle);

	Vehicle toEntity(VehicleDTO vechicleDTO);

	CategoryDTO toDto(Category category);

	Category toEntity(CategoryDTO categoryDTO);

}

package com.akt.vms.mapper;

import org.springframework.stereotype.Component;

import com.akt.vms.dto.FuelFillingDTO;
import com.akt.vms.entity.FuelFilling;
import com.akt.vms.entity.Vehicle;

@Component
public class FuelFillingMapper {

	public static FuelFilling toEntity(FuelFillingDTO dto, Vehicle vehicle) {
		FuelFilling entity = new FuelFilling();
		entity.setVehicle(vehicle);
		entity.setFuelType(dto.getFuel_type());
		entity.setFuelQuantity(dto.getFuel_quantity());
		entity.setCost(dto.getCost());
		entity.setRefuelDateTime(dto.getRefuel_date_time());
		entity.setLocation(dto.getLocation());
		entity.setOdometerReading(dto.getOdometer_reading());
		return entity;
	}

	public static FuelFillingDTO toDTO(FuelFilling entity) {
		FuelFillingDTO dto = new FuelFillingDTO();
		dto.setVehicle_id(entity.getVehicle().getId());
		dto.setFuel_type(entity.getFuelType());
		dto.setFuel_quantity(entity.getFuelQuantity());
		dto.setCost(entity.getCost());
		dto.setRefuel_date_time(entity.getRefuelDateTime());
		dto.setLocation(entity.getLocation());
		dto.setOdometer_reading(entity.getOdometerReading());
		return dto;
	}
}

package com.akt.vms.mapper;

import com.akt.vms.dto.VehicleAssignDTO;
import com.akt.vms.entity.Driver;
import com.akt.vms.entity.Vehicle;
import com.akt.vms.entity.VehicleAssign;

public class VehicleAssignMapper {
	public static VehicleAssign toEntity(VehicleAssignDTO dto, Vehicle vehicle, Driver driver) {
		if (dto == null || vehicle == null || driver == null) {
			return null;
		}

		VehicleAssign assign = new VehicleAssign();
		assign.setVehicleAssignId(dto.getVehicleAssignId());

		assign.setRemarks(dto.getRemarks());
		assign.setVehicle(vehicle);
		assign.setDriver(driver);
		return assign;
	}

	public static VehicleAssignDTO toDTO(VehicleAssign assign) {
		if (assign == null || assign.getVehicle() == null) {
			return null;
		}

		VehicleAssignDTO dto = new VehicleAssignDTO();
		dto.setVehicleAssignId(assign.getVehicleAssignId());
		dto.setVehicleid(assign.getVehicle().getId()); // extract vehicle ID
		dto.setDriver_id(assign.getDriver().getDriverId());
		dto.setRemarks(assign.getRemarks());
		return dto;
	}
}

package com.akt.vms.mapper;

import java.time.Duration;

import com.akt.vms.dto.DriverDTO;
import com.akt.vms.dto.TripManagementDTO;
import com.akt.vms.dto.VehicleDTO;
import com.akt.vms.entity.Driver;
import com.akt.vms.entity.TripManagement;
import com.akt.vms.entity.Vehicle;

public class TripManagementMapper {

	public static TripManagementDTO toDTO(TripManagement tripManagement) {
		TripManagementDTO dto = new TripManagementDTO();
		dto.setTripManagementId(tripManagement.getTripManagementId());
		dto.setTripName(tripManagement.getTripName());
		dto.setStartLocation(tripManagement.getStartLocation());
		dto.setEndLocation(tripManagement.getEndLocation());
		dto.setStartTime(tripManagement.getStartTime());
		dto.setEndTime(tripManagement.getEndTime());
		dto.setStatus(tripManagement.getStatus());

		// Map Driver and Vehicle DTOs
		if (tripManagement.getDriver() != null) {
			DriverDTO driverDTO = new DriverDTO();
			driverDTO.setId(tripManagement.getDriver().getDriverId());
			driverDTO.setName(tripManagement.getDriver().getName());
			dto.setDriver(driverDTO);
		}

		if (tripManagement.getVehicle() != null) {
			VehicleDTO vehicleDTO = new VehicleDTO();
			vehicleDTO.setId(tripManagement.getVehicle().getId());
			vehicleDTO.setVehicleNumber(tripManagement.getVehicle().getVehicleNumber());
			dto.setVehicle(vehicleDTO);
		}

		return dto;
	}

	public static TripManagement toEntity(TripManagementDTO dto) {
		TripManagement tripManagement = new TripManagement();
		tripManagement.setTripManagementId(dto.getTripManagementId());
		tripManagement.setTripName(dto.getTripName());
		tripManagement.setStartLocation(dto.getStartLocation());
		tripManagement.setEndLocation(dto.getEndLocation());
		tripManagement.setStartTime(dto.getStartTime());
		tripManagement.setEndTime(dto.getEndTime());
		tripManagement.setStatus(dto.getStatus());

		// Map Driver and Vehicle entities if present
		if (dto.getDriver() != null) {
			Driver driver = new Driver();
			driver.setDriverId(dto.getDriver().getId());
			tripManagement.setDriver(driver);
		}

		if (dto.getVehicle() != null) {
			Vehicle vehicle = new Vehicle();
			vehicle.setId(dto.getVehicle().getId());
			tripManagement.setVehicle(vehicle);
		}

		return tripManagement;
	}

	public static long calculateDuration(TripManagement tripManagement) {
		if (tripManagement.getStartTime() != null && tripManagement.getEndTime() != null) {
			Duration duration = Duration.between(tripManagement.getStartTime(), tripManagement.getEndTime());
			return duration.toMinutes();
		}
		return 0;
	}

	// Implement a method for distance calculation (can be based on some API or
	// logic)
	public static double calculateDistance(String startLocation, String endLocation) {
		// Example: Use some geolocation API or custom logic to calculate the distance
		return 100.0; // Example static distance
	}
}

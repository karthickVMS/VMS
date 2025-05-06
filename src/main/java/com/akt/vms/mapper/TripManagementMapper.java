package com.akt.vms.mapper;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akt.vms.dto.TripManagementDTO;
import com.akt.vms.entity.TripManagement;
import com.akt.vms.repository.DriverRepository;
import com.akt.vms.repository.VehicleRepository;

@Component
public class TripManagementMapper {

	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private DriverMapper driverMapper;

	@Autowired
	private VehicleMapper vehicleMapper;

	public TripManagementDTO toDTO(TripManagement tripManagement) {
		TripManagementDTO dto = new TripManagementDTO();
		dto.setTripManagementId(tripManagement.getTripManagementId());
		dto.setTripName(tripManagement.getTripName());
		dto.setStartLocation(tripManagement.getStartLocation());
		dto.setEndLocation(tripManagement.getEndLocation());
		dto.setStartTime(tripManagement.getStartTime());
		dto.setEndTime(tripManagement.getEndTime());
		dto.setStatus(tripManagement.getStatus());

		if (tripManagement.getDriver() != null) {
			dto.setDriver(driverMapper.toDriverDTO(tripManagement.getDriver()));
		}

		if (tripManagement.getVehicle() != null) {
			dto.setVehicle(vehicleMapper.toDto(tripManagement.getVehicle()));
		}

		return dto;
	}

	public TripManagement toEntity(TripManagementDTO dto) {
		TripManagement tripManagement = new TripManagement();
		tripManagement.setTripManagementId(dto.getTripManagementId());
		tripManagement.setTripName(dto.getTripName());
		tripManagement.setStartLocation(dto.getStartLocation());
		tripManagement.setEndLocation(dto.getEndLocation());
		tripManagement.setStartTime(dto.getStartTime());
		tripManagement.setEndTime(dto.getEndTime());
		tripManagement.setStatus(dto.getStatus());

		tripManagement.setDriver(driverRepository.findById(dto.getDriverID()).get());
		tripManagement.setVehicle(vehicleRepository.findById(dto.getVehicleID()).get());

		return tripManagement;
	}

	public long calculateDuration(TripManagement tripManagement) {
		if (tripManagement.getStartTime() != null && tripManagement.getEndTime() != null) {
			Duration duration = Duration.between(tripManagement.getStartTime(), tripManagement.getEndTime());
			return duration.toMinutes();
		}
		return 0;
	}

	// Implement a method for distance calculation (can be based on some API or
	// logic)
	public double calculateDistance(String startLocation, String endLocation) {
		// Example: Use some geolocation API or custom logic to calculate the distance
		return 100.0; // Example static distance
	}
}

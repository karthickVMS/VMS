package com.akt.vms.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akt.vms.dto.DriverDTO;
import com.akt.vms.dto.RouteMappingDTO;
import com.akt.vms.dto.TripManagementDTO;
import com.akt.vms.dto.TripSummaryDTO;
import com.akt.vms.dto.VehicleDTO;
import com.akt.vms.entity.RouteMapping;
import com.akt.vms.entity.TripManagement;
import com.akt.vms.mapper.TripManagementMapper;
import com.akt.vms.repository.TripManagementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.transaction.Transactional;

@Service
public class TripManagementService {

	@Autowired
	private TripManagementRepository tripManagementRepository;

	@Autowired

	private TripManagementMapper tripManagementMapper;

	private static final Logger logger = LoggerFactory.getLogger(TripManagementService.class);

	@Transactional
	public List<TripManagementDTO> getAllTrips() {
		return tripManagementRepository.findAll().stream().map(tripManagementMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Transactional
	public TripManagementDTO createTrip(TripManagementDTO tripManagementDTO) {
		TripManagement tripManagement = tripManagementMapper.toEntity(tripManagementDTO);
		tripManagement.setStatus("PENDING");
		tripManagement = tripManagementRepository.save(tripManagement);
		return tripManagementMapper.toDTO(tripManagement);
	}

	@Transactional
	public TripManagementDTO startTrip(Long id) {
		TripManagement trip = tripManagementRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Trip not found"));

		if (!"PENDING".equals(trip.getStatus())) {
			throw new IllegalStateException("Trip cannot be started.");
		}

		trip.setStartTime(LocalDateTime.now());
		trip.setStatus("IN_PROGRESS");
		trip = tripManagementRepository.save(trip);

		return tripManagementMapper.toDTO(trip);
	}

	@Transactional
	public TripManagementDTO stopTrip(Long id) {
		TripManagement trip = tripManagementRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Trip not found"));

		if (!"IN_PROGRESS".equals(trip.getStatus())) {
			throw new IllegalStateException("Trip cannot be stopped.");
		}

		trip.setEndTime(LocalDateTime.now());
		trip.setStatus("COMPLETED");
		trip = tripManagementRepository.save(trip);

		return tripManagementMapper.toDTO(trip);
	}

	public TripSummaryDTO getTripSummary(Long id) {
		logger.info("Fetching trip summary for Trip ID: {}", id);
		TripManagement trip = tripManagementRepository.findById(id).orElseThrow(() -> {
			logger.error("Trip not found with ID: {}", id);
			return new IllegalArgumentException("Trip not found with ID: " + id);
		});
		logger.debug("Trip found: {}", trip.getTripName());

		long duration = tripManagementMapper.calculateDuration(trip);
		double distance = tripManagementMapper.calculateDistance(trip.getStartLocation(), trip.getEndLocation());

		// Initialize TripSummaryDTO
		TripSummaryDTO summary = new TripSummaryDTO();
		summary.setTripId(id);
		summary.setDuration(duration);
		summary.setDistance(distance);
		summary.setStatus(trip.getStatus());

		logger.debug("Trip duration: {}, distance: {}", duration, distance);

		TripManagementDTO tripDTO = tripManagementMapper.toDTO(trip);

		// Driver Mapping
		if (trip.getDriver() != null) {
			logger.debug("Mapping driver for trip ID {}", id);
			tripDTO.setDriverID(trip.getDriver().getDriverId());
			DriverDTO driverDTO = new DriverDTO();
			driverDTO.setId(trip.getDriver().getDriverId());
			driverDTO.setName(trip.getDriver().getName());
			driverDTO.setLicenseNum(trip.getDriver().getLicenseNum());
			driverDTO.setVehicleNum(trip.getDriver().getVehicleNum());
			driverDTO.setContactNum(trip.getDriver().getContactNum());
			driverDTO.setYearsOfExp(trip.getDriver().getYearsOfExp());
			driverDTO.setState(trip.getDriver().getState());
			// driverDTO.setAddress(trip.getDriver().getAddress());
			tripDTO.setDriver(driverDTO);
		} else {
			logger.warn("Driver not assigned for trip ID {}", id);
		}

		// Vehicle Mapping
		if (trip.getVehicle() != null) {
			logger.debug("Mapping vehicle for trip ID {}", id);

			tripDTO.setVehicleID(trip.getVehicle().getId());
			VehicleDTO vehicleDTO = new VehicleDTO();
			vehicleDTO.setId(trip.getVehicle().getId());
			vehicleDTO.setVehicleNumber(trip.getVehicle().getVehicleNumber());
			vehicleDTO.setModel(trip.getVehicle().getModel());
			vehicleDTO.setFuelType(trip.getVehicle().getFuelType());
			vehicleDTO.setInsurancePolicyNumber(trip.getVehicle().getInsurancePolicyNumber());
			vehicleDTO.setYearOfManufacturer(trip.getVehicle().getYearOfManufacturer());
			vehicleDTO.setRegistrationDate(trip.getVehicle().getRegistrationDate());
			// vehicleDTO.setCategoryId(trip.getVehicle().getCategoryId());
			// vehicleDTO.setCategory(trip.getVehicle().getCategory());
			tripDTO.setVehicle(vehicleDTO);
		} else {
			logger.warn("Vehicle not assigned for trip ID {}", id);
		}

		// Map all route mappings for the trip
		List<RouteMappingDTO> routeMappingDTOs = new ArrayList<>();
		if (trip.getRouteMappingList() != null) {
			logger.debug("Mapping route checkpoints for trip ID {}", id);
			for (RouteMapping mapping : trip.getRouteMappingList()) {
				RouteMappingDTO dto = new RouteMappingDTO();
				dto.setRoute_mapping_id(mapping.getRouteMappingId());
				dto.setTrip_management_id(trip.getTripManagementId());
				dto.setPoint_name(mapping.getPointName());
				dto.setReached_time(mapping.getReachedTime());
				dto.setStatus(mapping.getStatus());
				dto.setRemarks(mapping.getRemarks());

				// Set full TripManagementDTO(full trip information inside each route mapping)
				// dto.setTripManagementDTO(tripDTO);

				routeMappingDTOs.add(dto);
			}
		} else {
			logger.warn("No route mappings found for trip ID {}", id);
		}

		summary.setTripManagementDTO(tripDTO);
		summary.setRouteMappingList(routeMappingDTOs);

		logger.info("Successfully generated summary for Trip ID {}", id);
		return summary;
	}

}
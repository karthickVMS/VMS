package com.akt.vms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.akt.vms.dto.GpsDeviceInstallationDTO;
import com.akt.vms.entity.GpsDeviceInstallation;
import com.akt.vms.entity.Vehicle;
import com.akt.vms.mapper.GpsDeviceInstallationMapper;
import com.akt.vms.repository.GpsDeviceInstallationRepository;
import com.akt.vms.repository.VehicleRepository;

import jakarta.transaction.Transactional;

@Service
public class GpsDeviceInstallationService {

	@Autowired
	private GpsDeviceInstallationRepository gpsDeviceInstallationRepository;

	@Autowired
	private GpsDeviceInstallationMapper mapper;

	@Autowired
	private VehicleRepository vehicleRepository;

	private static final Logger logger = LoggerFactory.getLogger(GpsDeviceInstallationService.class);

	// @Transactional
	/*
	 * public ResponseEntity<GpsDeviceInstallation> getDeviceById(Long id) {
	 * logger.info("Fetching GPS device with ID: {}", id); return
	 * gpsDeviceInstallationRepository.findById(id).map(ResponseEntity::ok) //
	 * Return 200 OK with the device .orElseGet(() ->
	 * ResponseEntity.notFound().build()); // Return 404 Not Found if device is
	 * absent }
	 */
	@Transactional
	public GpsDeviceInstallationDTO getDeviceById(Long id) {
		logger.info("Fetching GPS device with ID: {}", id);
		GpsDeviceInstallation device = gpsDeviceInstallationRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found with ID: " + id));

		return mapper.toDTO(device);
	}

	@Transactional
	public GpsDeviceInstallationDTO installDevice(GpsDeviceInstallationDTO dto) {
		logger.info("Installing GPS device for vehicle ID: {}",
				dto.getVehicle() != null ? dto.getVehicle().getId() : "null");

		Long vehicleId = dto.getVehicle_id();
		if (vehicleId == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle ID is missing in request.");
		}

		Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found with ID: " + vehicleId));

		// Check if a GPS device is already installed
		if (gpsDeviceInstallationRepository.existsByVehicle(vehicle)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"A GPS device is already installed for this vehicle.");
		}

		GpsDeviceInstallation entity = mapper.toEntity(dto, vehicle);
		GpsDeviceInstallation saved = gpsDeviceInstallationRepository.save(entity);
		logger.info("GPS device installed with ID: {}", saved.getDeviceId());

		return mapper.toDTO(saved);
	}
}
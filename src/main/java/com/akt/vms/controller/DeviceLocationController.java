package com.akt.vms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akt.vms.dto.DeviceLocationDTO;
import com.akt.vms.dto.DeviceLocationResponseDTO;
import com.akt.vms.entity.DeviceLocation;
import com.akt.vms.service.DeviceLocationService;

@RestController
@RequestMapping("/api/location")
public class DeviceLocationController {

	@Autowired
	private DeviceLocationService service;

	@PostMapping("/device")
	public ResponseEntity<String> recordLocation(@RequestBody DeviceLocationDTO request) {

		try {
			DeviceLocation location = service.saveDeviceLocation(request);
			return ResponseEntity.status(HttpStatus.CREATED).body("Location recorded with ID: " + location.getId());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to record location");
		}
	}

	@GetMapping("/device/{deviceId}/locations")
	public ResponseEntity<DeviceLocationResponseDTO> getLocationsByDevice(@PathVariable Long deviceId) {
		try {
			// Fetch locations using the service
			DeviceLocationResponseDTO responseDTO = service.getLocationsForDevice(deviceId);
			if (responseDTO == null || responseDTO.getLocations().isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(responseDTO); // Return response DTO
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}
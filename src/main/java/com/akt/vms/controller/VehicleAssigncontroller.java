package com.akt.vms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akt.vms.dto.VehicleAssignDTO;
import com.akt.vms.service.VehicleAssignService;

@RestController
@RequestMapping("/driver/vehicle-assign")
public class VehicleAssigncontroller {
	private static final Logger logger = LoggerFactory.getLogger(VehicleAssigncontroller.class);

	private final VehicleAssignService vehicleAssignService;

	public VehicleAssigncontroller(VehicleAssignService vehicleAssignService) {
		this.vehicleAssignService = vehicleAssignService;
	}

	/**
	 * Create a new vehicle assignment.
	 */
	@PostMapping
	public ResponseEntity<VehicleAssignDTO> createVehicleAssign(@RequestBody VehicleAssignDTO dto) {
		logger.info("Creating new vehicle assignment");
		VehicleAssignDTO created = vehicleAssignService.createVehicleAssign(dto);
		return ResponseEntity.ok(created);
	}

	/**
	 * Get all vehicle assignments.
	 */
	@GetMapping
	public ResponseEntity<List<VehicleAssignDTO>> getAllVehicleAssigns() {
		logger.info("Fetching all vehicle assignments");
		List<VehicleAssignDTO> list = vehicleAssignService.getAllVehicleAssigns();
		return ResponseEntity.ok(list);
	}

	/**
	 * Get a specific vehicle assignment by ID.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<VehicleAssignDTO> getVehicleAssignById(@PathVariable Long id) {
		logger.info("Fetching vehicle assignment with ID: {}", id);
		VehicleAssignDTO dto = vehicleAssignService.getVehicleAssignById(id);
		if (dto == null) {
			logger.warn("Vehicle assignment not found for ID: {}", id);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

	/**
	 * Delete a vehicle assignment by ID.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteVehicleAssign(@PathVariable Long id) {
		logger.info("Deleting vehicle assignment with ID: {}", id);
		vehicleAssignService.deleteVehicleAssign(id);
		return ResponseEntity.noContent().build();
	}
}

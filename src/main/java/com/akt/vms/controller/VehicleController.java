package com.akt.vms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akt.vms.dto.VehicleDTO;
import com.akt.vms.entity.Vehicle;
import com.akt.vms.service.VehicleService;

/**
 * REST controller for managing vehicle-related operations. Handles HTTP
 * requests for creating, retrieving, updating, and deleting vehicles.
 */
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

	private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);
	private final VehicleService vehicleService;

	public VehicleController(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}

	/**
	 * Creating a new vehicle.
	 *
	 * @param vehicleDTO the vehicle details from the request body.
	 * @return created vehicle.
	 */
	@PostMapping
	public ResponseEntity<VehicleDTO> createvehicle(@RequestBody VehicleDTO vehicleDTO) {
		logger.info("Creating new Vehicle: {}", vehicleDTO);
		return ResponseEntity.ok(vehicleService.createVehicle(vehicleDTO));
	}

	/**
	 * Retrieve all vehicles.
	 *
	 * @return List of all vehicles.
	 */

	@GetMapping
	public ResponseEntity<List<VehicleDTO>> getAllvehicles() {
		logger.info("VehicleController :: getAllvehicles");
		return ResponseEntity.ok(vehicleService.getAllVehicles());
	}
	

	/**
	 * Deletes a vehicle by ID.
	 *
	 * @param id The Id of the vehicle to delete.
	 * @return No content response.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
		logger.info("Delete vehicle with ID: {}", id);
		vehicleService.deletevehicle(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Retrieves a vehicle by ID.
	 *
	 * @param id The Id of the vehicle.
	 * @return vehicle details.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
		logger.info("Fetch vehicle with ID: {}", id);
		VehicleDTO vehicleDTO = vehicleService.getVehicleById(id);
		if(vehicleDTO == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(vehicleDTO);
	}

	/**
	 * Endpoint to search vehicles using multiple filters in a single request. The
	 * request body accepts a VehicleDTO object with optional fields.
	 *
	 * @param dto VehicleDTO containing search filters
	 * @return List of matching Vehicle entities
	 */
	@PostMapping("/search")
	public ResponseEntity<List<Vehicle>> searchVehicles(@RequestBody VehicleDTO vehicleDTO) {
		logger.info("Received search request: {}", vehicleDTO);
		List<Vehicle> vehicles = vehicleService.searchVehicles(vehicleDTO);
		logger.info("Found {} vehicles matching the search", vehicles.size());
		return ResponseEntity.ok(vehicles);
	}

	/**
	 * Updates selected fields (model and fuelType) of a vehicle entity using a
	 * custom JPQL update query. This is triggered by a PUT request with vehicle ID
	 * in the path and updated fields as request parameters.
	 *
	 * @param id       The ID of the vehicle to update.
	 * @param model    The new model name to be set.
	 * @param fuelType The new fuel type to be set.
	 * @return ResponseEntity indicating success or failure of the update operation.
	 */
	@PutMapping("/{id}/custom-query-update")
	public ResponseEntity<String> updateSelectedFields(@PathVariable Long id, @RequestParam String model,
			@RequestParam String fuelType) {
		logger.info("Received request to update vehicle with ID: {}, model: {}, fuelType: {}", id, model, fuelType);

		boolean isUpdated = vehicleService.updateVehicleFields(id, model, fuelType);

		if (isUpdated) {
			logger.info("Vehicle with ID: {} updated successfully.", id);
			return ResponseEntity.ok("Vehicle updated successfully");
		} else {
			logger.warn("Vehicle with ID: {} not found. Update failed.", id);
			return ResponseEntity.ok("vehicle id not found");
		}

	}
}
package com.akt.vms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.akt.vms.dto.DriverDTO;
import com.akt.vms.dto.DriverFilterRequest;
import com.akt.vms.entity.Driver;
import com.akt.vms.service.DriverService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/driver")
public class DriverController {

	@Autowired

	private final DriverService driverService;

	// Logger for tracking application behavior
	private static final Logger logger = LoggerFactory.getLogger(DriverController.class);

	public DriverController(DriverService driverService) {
		this.driverService = driverService;
	}

	/**
	 * Creating a new driver detail
	 * 
	 * @param driverDTO passing driver details from the request body
	 * @return returns driverDTO object
	 */

	@PostMapping
	public ResponseEntity<DriverDTO> createDriver(@Valid @RequestBody DriverDTO driverDTO) {
		logger.info("Creating new driver: {}", driverDTO);
		logger.trace("createdriver() method called to store the data");
		return ResponseEntity.ok(driverService.createDriver(driverDTO)); // return new
		// ResponseEntity("Added...", HttpStatus.CREATED); // changing httpstatus code
		// from 200 to 201
	}

	/**
	 * Retrieving all the driver details
	 * 
	 * @return returns all driver details from driverService
	 */

	@GetMapping
	public ResponseEntity<List<DriverDTO>> getAllDrivers() {
		logger.info("DriverController :: getAlldrivers");
		logger.trace("getalldriver() method called to show all the driver data");
		logger.error("Driver detail not available", new RuntimeException("Error occured"));
		return ResponseEntity.ok(driverService.getAllDrivers());
	}

	/**
	 * @param id        is passed to get the particular driver detail
	 * @param driverDTO requests the details from driverDTO
	 * @return returns the updated value to driverDTO
	 */

	@PutMapping("/{id}")
	public ResponseEntity<DriverDTO> updateDriver(@PathVariable Long id, @RequestBody DriverDTO driverDTO) {
		logger.info("Update driver by id {}, new values are {}", id, driverDTO);
		logger.trace("updatedriver() method called to update the particular driver data");
		logger.error("Id not available", new RuntimeException("Error occured"));
		return ResponseEntity.ok(driverService.updateDriver(id, driverDTO));
	}

	/**
	 * @param id is passed to to get the particular driver detail
	 * @return returns no content of the particular id
	 */

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletedriver(@PathVariable Long id) {
		driverService.deleteDriver(id);
		logger.info("Delete driver by id {}", id);
		logger.trace("deletedriver() method called to delete the particular driver data");
		logger.error("Id not available", new RuntimeException("Error occured"));
		return ResponseEntity.noContent().build();
	}

	/**
	 * @param id is passed to to get the particular driver detail
	 * @return returns driver detail by Id
	 */

	@GetMapping("/{id}")
	public ResponseEntity<DriverDTO> getDriverById(@PathVariable Long id) {
		logger.info("DriverController :: getDriveryId");
		logger.trace("getDriverById() method called to show the particular driver data");
		logger.error("Driver detail not available", new RuntimeException("Error occured"));
		return ResponseEntity.ok(driverService.getDriverById(id));
	}

	/*
	 * @GetMapping("/{name}") public ResponseEntity<List<Driver>>
	 * getDriverByName(@PathVariable String name) {
	 * logger.info("Fetching drivers with name: {}", name); List<Driver> drivers =
	 * driverService.getDriverByName(name);
	 * 
	 * if (drivers.isEmpty()) { logger.warn("No drivers found with name: {}", name);
	 * return ResponseEntity.noContent().build(); }
	 * 
	 * logger.info("Found {} driver(s) with name: {}", drivers.size(), name); return
	 * ResponseEntity.ok(drivers); }
	 */

	/*
	 * * @RequestParam is used to extract query parameters from the request URL.
	 * required = false means that the query parameters are optional. If the client
	 * does not provide a value for a specific parameter, it will be null.
	 */

	@PostMapping("/specificationSearch")
	public ResponseEntity<List<Driver>> specificationFilterDrivers(@RequestBody DriverFilterRequest filterRequest) {
		logger.info("Received driver search request with filters: {}", filterRequest);

		List<Driver> drivers = driverService.getFilteredDriver(filterRequest);

		logger.info("Found {} drivers matching the criteria.", drivers.size());
		return ResponseEntity.ok(drivers);
	}

	/*
	 * @GetMapping("/search") public ResponseEntity<List<Driver>>
	 * searchDrivers(@RequestParam String searchvalue) { return
	 * ResponseEntity.ok(driverService.searchDrivers(searchvalue)); }
	 */
//
//	@GetMapping("/paginated")
//	public ResponseEntity<Page<DriverDTO>> getPaginatedDrivers(Pageable pageable) {
//		return ResponseEntity.ok(driverService.getPaginatedDrivers(pageable));
//	}

}

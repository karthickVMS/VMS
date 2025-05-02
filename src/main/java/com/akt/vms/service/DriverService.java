package com.akt.vms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.akt.vms.dto.CategoryDTO;
import com.akt.vms.dto.DriverDTO;
import com.akt.vms.dto.DriverFilterRequest;
import com.akt.vms.entity.Driver;
import com.akt.vms.mapper.CategoryMapper;
import com.akt.vms.mapper.DriverMapper;
import com.akt.vms.repository.DriverRepository;
import com.akt.vms.specification.DriverSpecifications;

@Service
public class DriverService {

	private static final Logger logger = LoggerFactory.getLogger(DriverService.class);

	@Autowired

	private final DriverRepository driverRepository;

	@Autowired
	private final DriverMapper driverMapper;

	public DriverService(DriverRepository driverRepository, DriverMapper driverMapper) {
		this.driverRepository = driverRepository;
		this.driverMapper = driverMapper;
	}

	/**
	 * Get all drivers from the database and convert them to DTOs.
	 * 
	 * @return List of DriverDTOs
	 */
	public List<DriverDTO> getAllDrivers() {
		return driverRepository.findAll().stream() // Fetch all Driver entities
				.map(DriverMapper::toDriverDTO) // Convert each entity to DTO
				.collect(Collectors.toList()); // Collect results into a list
	}

	/**
	 * Get a single driver by ID and convert it to a DTO. Returns null if not found
	 * (you may consider throwing an exception instead).
	 * 
	 * @param id - Driver ID
	 * @return DriverDTO or null
	 */
	public DriverDTO getDriverById(Long id) {
		return driverRepository.findById(id) // Try to find driver by ID
				.map(DriverMapper::toDriverDTO) // Convert to DTO if found
				.orElse(null); // Return null if not found
	}

	/**
	 * Create a new driver from the provided DTO.
	 * 
	 * @param driverDTO - Input DTO from client
	 * @return The saved driver as a DTO
	 */
	public DriverDTO createDriver(DriverDTO driverDTO) {
		Driver driver = driverMapper.toEntity(driverDTO); // Convert DTO to entity
		driver = driverRepository.save(driver); // Save to DB(driver object updated in the repository)
		return DriverMapper.toDriverDTO(driver); // Convert entity object to DTO
	}

	/**
	 * Update an existing driver using a given ID and new data from DriverDTO.
	 * 
	 * @param id        - ID of the driver to update
	 * @param driverDTO - New data to apply
	 * @return Updated driver as a DTO, or null if not found
	 */

	public DriverDTO updateDriver(Long id, DriverDTO driverDTO) {
		return driverRepository.findById(id).map(driver -> {
			driver.setName(driverDTO.getName());
			driver.setLicenseNum(driverDTO.getLicenseNum());
			driver.setVehicleNum(driverDTO.getVehicleNum());
			driver.setContactNum(driverDTO.getContactNum());
			driver.setYearsOfExp(driverDTO.getYearsOfExp());
			driver.setState(driverDTO.getState());
			Driver updatedDriver = driverRepository.save(driver);
			return driverMapper.toDriverDTO(updatedDriver);
		}).orElse(null);
	}

	/**
	 * Delete a driver by ID if it exists.
	 * 
	 * @param id - ID of the driver to delete
	 * @return true if deleted successfully, false if driver was not found
	 */
	public boolean deleteDriver(Long id) {
		if (driverRepository.existsById(id)) {
			driverRepository.deleteById(id);
			return true;
		}
		return false;

	}

	public List<Driver> searchDrivers(String name, String licenseNum, String state, Integer minExp) {
		Specification<Driver> spec = Specification.where(null); // start with empty spec

		if (name != null && !name.isEmpty()) {
			spec = spec.and(DriverSpecifications.hasName(name));
		}

		if (licenseNum != null && !licenseNum.isEmpty()) {
			spec = spec.and(DriverSpecifications.hasLicenseNum(licenseNum));
		}

		if (state != null && !state.isEmpty()) {
			spec = spec.and(DriverSpecifications.hasState(state));
		}

		if (minExp != null) {
			spec = spec.and(DriverSpecifications.hasYearsOfExpGreaterThan(minExp));
		}

		return driverRepository.findAll(spec);
	}

	public List<Driver> getFilteredDriver(DriverFilterRequest filterRequest) {

		logger.info("Filtering drivers with request: {}", filterRequest);

		Specification<Driver> spec = Specification.where(null);
// Add 'name' filter to the Specification if provided
		if (filterRequest.getName() != null && !filterRequest.getName().isEmpty()) {
			spec = spec.and(DriverSpecifications.hasName(filterRequest.getName()));
			logger.debug("Adding filter: name = {}", filterRequest.getName());
		}

		if (filterRequest.getLicenseNum() != null && !filterRequest.getLicenseNum().isEmpty()) {
			spec = spec.and(DriverSpecifications.hasLicenseNum(filterRequest.getLicenseNum()));
			logger.debug("Adding filter: licenseNum = {}", filterRequest.getLicenseNum());
		}

		if (filterRequest.getVehicleNum() != null && !filterRequest.getVehicleNum().isEmpty()) {
			spec = spec.and(DriverSpecifications.hasVehicleNum(filterRequest.getVehicleNum()));
			logger.debug("Adding filter: vehicleNum = {}", filterRequest.getVehicleNum());
		}

		if (filterRequest.getContactNum() != null && !filterRequest.getContactNum().isEmpty()) {
			spec = spec.and(DriverSpecifications.hasContactNum(filterRequest.getContactNum()));
			logger.debug("Adding filter: contactNum = {}", filterRequest.getContactNum());
		}

		if (filterRequest.getYearsOfExp() != null) {
			spec = spec.and(DriverSpecifications.hasYearsOfExpGreaterThan(filterRequest.getYearsOfExp()));
			logger.debug("Adding filter: yearsOfExp >= {}", filterRequest.getYearsOfExp());
		}

		if (filterRequest.getState() != null && !filterRequest.getState().isEmpty()) {
			spec = spec.and(DriverSpecifications.hasState(filterRequest.getState()));
			logger.debug("Adding filter: state = {}", filterRequest.getState());
		}

		// Use the constructed Specification to query the database
		List<Driver> drivers = driverRepository.findAll(spec);
		logger.info("Found {} drivers matching filters", drivers.size());

		return drivers;
	}

	/**
	 * Checks if a string is not null, not empty, and not just whitespace.
	 *
	 * @param value the string to check
	 * @return true if the string has non-whitespace content; false otherwise
	 */
	private boolean isNonEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

}

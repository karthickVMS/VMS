package com.akt.vms.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.akt.vms.dto.VehicleAssignDTO;
import com.akt.vms.entity.Driver;
import com.akt.vms.entity.TripManagement;
import com.akt.vms.entity.Vehicle;
import com.akt.vms.entity.VehicleAssign;
import com.akt.vms.mapper.VehicleAssignMapper;
import com.akt.vms.repository.DriverRepository;
import com.akt.vms.repository.TripManagementRepository;
import com.akt.vms.repository.VehicleAssignRepository;
import com.akt.vms.repository.VehicleRepository;

@Service
public class VehicleAssignService {

	private final DriverRepository driverRepository;
	private static final Logger logger = LoggerFactory.getLogger(VehicleAssignService.class);
	private final TripManagementRepository tripRepository;

	private final VehicleAssignRepository vehicleAssignRepository;
	private final VehicleRepository vehicleRepository;

	public VehicleAssignService(VehicleAssignRepository vehicleAssignRepository, VehicleRepository vehicleRepository,
			DriverRepository driverRepository, TripManagementRepository tripRepository) {
		this.vehicleAssignRepository = vehicleAssignRepository;
		this.vehicleRepository = vehicleRepository;
		this.driverRepository = driverRepository;
		this.tripRepository = tripRepository;
	}

	/**
	 * Create a new vehicle assignment.
	 *
	 * @param dto The data to assign a vehicle to a driver.
	 * @return The saved assignment as a DTO.
	 */
	public VehicleAssignDTO createVehicleAssign(VehicleAssignDTO dto) {
		logger.info("Creating new vehicle assignment");

		Optional<Vehicle> vehicleOpt = vehicleRepository.findById(dto.getVehicleid());
		Optional<Driver> driverOpt = driverRepository.findById(dto.getDriver_id());
		Optional<TripManagement> tripOpt = Optional.empty();

		if (dto.getTripManagementId() != null) {
			tripOpt = tripRepository.findById(dto.getTripManagementId());
			if (!tripOpt.isPresent()) {
				logger.error("Trip not found with ID: {}", dto.getTripManagementId());
				throw new RuntimeException("Trip not found with ID: " + dto.getTripManagementId());
			}
		}

		if (!vehicleOpt.isPresent()) {
			logger.error("Vehicle not found with ID: {}", dto.getVehicleid());
			throw new RuntimeException("Vehicle not found with ID: " + dto.getVehicleid());
		}

		if (!driverOpt.isPresent()) {
			logger.error("Driver not found with ID: {}", dto.getDriver_id());
			throw new RuntimeException("Driver not found with ID: " + dto.getDriver_id());
		}

		VehicleAssign entity = VehicleAssignMapper.toEntity(dto, vehicleOpt.get(), driverOpt.get(),
				tripOpt.orElse(null));
		VehicleAssign saved = vehicleAssignRepository.save(entity);
		logger.info("Vehicle assignment saved with ID: {}", saved.getVehicleAssignId());

		return VehicleAssignMapper.toDTO(saved);
	}

	/**
	 * Retrieve all vehicle assignments.
	 *
	 * @return List of assignment DTOs.
	 */
	@Cacheable("vehicleAssigns")
	public List<VehicleAssignDTO> getAllVehicleAssigns() {
		logger.info("Fetching all vehicle assignments");
		List<VehicleAssign> assigns = vehicleAssignRepository.findAll();
		logger.debug("Fetched {} assignments", assigns.size());
		return assigns.stream().map(VehicleAssignMapper::toDTO).collect(Collectors.toList());
	}

	/**
	 * Get a specific vehicle assignment by ID.
	 *
	 * @param id The assignment ID.
	 * @return The assignment DTO.
	 */
	@Cacheable("vehicleAssign")
	public VehicleAssignDTO getVehicleAssignById(Long id) {
		logger.info("Fetching vehicle assignment by ID: {}", id);
		return vehicleAssignRepository.findById(id).map(VehicleAssignMapper::toDTO).orElse(null);
	}

	/**
	 * Delete a vehicle assignment by ID.
	 *
	 * @param id The assignment ID to delete.
	 */
	@CacheEvict(value = { "vehicleAssigns", "vehicleAssign" }, allEntries = true)
	public void deleteVehicleAssign(Long id) {
		logger.info("Deleting vehicle assignment with ID: {}", id);
		vehicleAssignRepository.deleteById(id);
	}
}

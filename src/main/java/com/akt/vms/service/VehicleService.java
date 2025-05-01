package com.akt.vms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.akt.vms.dto.VehicleDTO;
import com.akt.vms.entity.Vehicle;
import com.akt.vms.mapper.VehicleMapper;
import com.akt.vms.repository.VehicleRepository;

@Service
public class VehicleService {

	private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);
	// automatically injects an instance of VehicleRepository
	private VehicleRepository vehicleRepository;// gives access to methods like findAll, save, delete, etc.

	public VehicleService(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}

	/**
	 * Create a new vehicle record in the database.
	 *
	 * @param vehicleDTO The vehicle data to insert.
	 * @return saved vehicle as a DTO.
	 */

	/**
	 * Creates a new vehicle record in the database.
	 *
	 * @param vehicleDTO The data transfer object containing vehicle details.
	 * @return The saved vehicle as a DTO, includes generated field ID.
	 */
	public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
		logger.info("Creating a new vehicle");
		Vehicle vehicleEntity = VehicleMapper.INSTANCE.toEntity(vehicleDTO);
		logger.debug("Vehicle entity created from DTO: {}", vehicleEntity);
		Vehicle savedEntity = vehicleRepository.save(vehicleEntity);
		logger.info("Vehicle saved with ID: {}", savedEntity.getId());
		return VehicleMapper.INSTANCE.toDto(savedEntity);

	}

	/**
	 * Retrieve all vehicles.
	 * 
	 *
	 * @return List of all vehicle DTOs.
	 */
	@Cacheable("vehicles")
	public List<VehicleDTO> getAllvehicles() {
		logger.info("VehicleService :: getAllvehicles");
		try {
			logger.debug("VehicleService, Debug");
			List<Vehicle> vehicleentity = vehicleRepository.findAll();
			logger.debug("Total vehicles fetched from DB: {}", vehicleentity.size());

			// Convert vehicle entity to DTO
			List<VehicleDTO> vehicleDTOs = vehicleentity.stream().map(VehicleMapper.INSTANCE::toDto)
					.collect(Collectors.toList());

			logger.info("Vehicle list conversion to DTO complete");
			return vehicleDTOs;
			// return
			// vehicleRepository.findAll().stream().map(VehicleMapper.INSTANCE::toDto).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("VehicleService Error in getAllvehicles methods:" + e.getMessage());
		}
		return null;
	}

	/**
	 * Retrieve a single vehicle by ID.
	 * 
	 *
	 * @param id vehicle ID.
	 * @return The vehicle DTO.
	 */

	@Cacheable("vehicle")
	public VehicleDTO getVehicleById(Long id) {

		logger.info("Fetch vehicle by ID: {}", id);
		return vehicleRepository.findById(id).map(VehicleMapper.INSTANCE::toDto).orElse(null);

	}

	/**
	 * Delete a vehicle by ID.
	 * 
	 *
	 * @param id The ID of the vehicle to delete.
	 */
	@CacheEvict(value = { "vehilcles", "vehicle" }, allEntries = true)
	public void deletevehicle(Long id) {
		vehicleRepository.deleteById(id);
		logger.info("Vehicle with ID {} deleted", id);
	}

	/**
	 * Searches for vehicles based on the non-null fields provided in the
	 * {@link VehicleDTO}.
	 *
	 * <p>
	 * This method supports flexible filtering by accepting a DTO with optional
	 * search fields. If all fields in the DTO are null, it returns all vehicles.
	 * Otherwise, it forwards the non-null fields to a custom repository method for
	 * filtered querying.
	 *
	 * <p>
	 * Logged messages help trace whether a full search or a filtered search is
	 * being executed.
	 *
	 * @param vehicleDTO the Data Transfer Object containing optional search
	 *                   criteria
	 * @return a list of {@link Vehicle} entities that match the search criteria
	 */
	public List<Vehicle> searchVehicles(VehicleDTO vehicleDTO) {
		logger.info("Searching vehicles with filters: {}", vehicleDTO);
		boolean isAllNull = vehicleDTO.getVehicleNumber() == null && vehicleDTO.getModel() == null
				&& vehicleDTO.getFuelType() == null && vehicleDTO.getInsurancePolicyNumber() == null
				&& vehicleDTO.getYearOfManufacturer() == null && vehicleDTO.getRegistrationDate() == null;

		if (isAllNull) {
			logger.info("All search fields are null. Returning all vehicles.");
			return vehicleRepository.findAll();
		}
		return vehicleRepository.searchVehicles(vehicleDTO.getVehicleNumber(), vehicleDTO.getModel(),
				vehicleDTO.getFuelType(), vehicleDTO.getInsurancePolicyNumber(), vehicleDTO.getYearOfManufacturer(),
				vehicleDTO.getRegistrationDate());
	}

	/**
	 * Updates the model and fuelType of a vehicle based on its ID using a custom
	 * JPQL update query.
	 *
	 * @param id       The ID of the vehicle to update.
	 * @param model    The new model name to be set.
	 * @param fuelType The new fuel type to be set.
	 * @return true if update is successful (i.e., at least one row is affected),
	 *         false otherwise.
	 */
	public boolean updateVehicleFields(Long id, String model, String fuelType) {
		logger.info(" to update vehicle with ID: {}, new model: {}, new fuelType: {}", id, model, fuelType);
		int updatedRows = vehicleRepository.updateModelAndFuelTypeById(id, model, fuelType);
		System.out.println("upated rows on request" + updatedRows);
		return updatedRows > 0;

	}

	/**
	 * Searches for vehicles using a case-insensitive keyword.
	 *
	 * <p>
	 * This method logs the search keyword and delegates the search operation to the
	 * repository layer. It converts the input value to lowercase to ensure a
	 * case-insensitive match.
	 *
	 * @param searchvalue the keyword or phrase to search for in vehicle records
	 * @return a list of {@link Vehicle} entities matching the search criteria
	 */
	public List<Vehicle> searchVehicles(String searchvalue) {
		logger.info("Initiating search for vehicles with value: {}", searchvalue);
		return vehicleRepository.searchVehicles(searchvalue.toLowerCase());

	}

}

package com.akt.vms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.akt.vms.dto.VehicleDTO;
import com.akt.vms.entity.Vehicle;
import com.akt.vms.repository.VehicleRepository;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {
	@Mock
	private VehicleRepository vehicleRepository;

	@InjectMocks
	private VehicleService vehicleService;

	private Vehicle vehicle;
	private VehicleDTO vehicleDTO;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle();
		vehicle.setId(1L);
		vehicle.setVehicleNumber("ABC1234");
		vehicle.setModel("Model X");
		vehicle.setFuelType("Petrol");
		vehicle.setInsurancePolicyNumber("INS123456");
		vehicle.setYearOfManufacturer(2020);

		vehicleDTO = new VehicleDTO();
		vehicleDTO.setVehicleNumber("ABC1234");
		vehicleDTO.setModel("Model X");
		vehicleDTO.setFuelType("Petrol");
		vehicleDTO.setInsurancePolicyNumber("INS123456");
		vehicleDTO.setYearOfManufacturer(2020);
	}

	/*
	 * Test case for creating a vehicle and verifying that the VehicleDTO is saved
	 * and returned.
	 * 
	 * - Mocks the vehicleRepository.save() method to simulate saving a vehicle. -
	 * Calls the createVehicle() service method to process the creation. - Asserts
	 * that the returned result is not null and checks the values in the VehicleDTO
	 * to ensure correctness. - Verifies that the save method of vehicleRepository
	 * is called once.
	 */
	@Test
	void createVehicle_ShouldSaveAndReturnVehicleDTO() {
		when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

		VehicleDTO result = vehicleService.createVehicle(vehicleDTO);

		assertNotNull(result);
		assertEquals("ABC1234", result.getVehicleNumber());
		assertEquals("Model X", result.getModel());
		assertEquals("Petrol", result.getFuelType());
		verify(vehicleRepository, times(1)).save(any(Vehicle.class));
	}

	/*
	 * Test case for retrieving all vehicles and checking that the result contains
	 * the expected VehicleDTOs.
	 * 
	 * - Mocks the vehicleRepository.findAll() method to return a list of vehicles.
	 * - Calls the getAllvehicles() service method to retrieve all vehicles. -
	 * Asserts that the result is not null and contains exactly one vehicle with the
	 * expected values. - Verifies that the findAll method of vehicleRepository is
	 * called once.
	 */
	@Test
	void getAllVehicles_ShouldReturnListOfVehicleDTOs() {
		List<Vehicle> vehicles = Arrays.asList(vehicle);
		when(vehicleRepository.findAll()).thenReturn(vehicles);

		List<VehicleDTO> result = vehicleService.getAllvehicles();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("ABC1234", result.get(0).getVehicleNumber());
		assertEquals("Model X", result.get(0).getModel());
		verify(vehicleRepository, times(1)).findAll();
	}

	/*
	 * Test case for retrieving a vehicle by ID when the vehicle exists.
	 * 
	 * - Mocks the vehicleRepository.findById() method to return a vehicle when the
	 * given ID is found. - Calls the getVehicleById() service method with the ID 1.
	 * - Asserts that the result is not null and contains the correct vehicle
	 * details. - Verifies that the findById method of vehicleRepository is called
	 * once.
	 */
	@Test
	void getVehicleById_ShouldReturnVehicleDTO_WhenVehicleExists() {
		when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

		VehicleDTO result = vehicleService.getVehicleById(1L);

		assertNotNull(result);
		assertEquals("ABC1234", result.getVehicleNumber());
		assertEquals("Model X", result.getModel());
		verify(vehicleRepository, times(1)).findById(1L);
	}

	/**
	 * Unit test for the deleteVehicle method in VehicleService.
	 *
	 * <p>
	 * This test verifies that when deleteVehicle is called with a given ID, the
	 * repository's deleteById method is invoked exactly once with the same ID.
	 */
	@Test
	void deleteVehicle_ShouldCallRepositoryDeleteById() {
		Long vehicleId = 1L;

		vehicleService.deletevehicle(vehicleId);

		verify(vehicleRepository, times(1)).deleteById(vehicleId);
	}

	/**
	 * Unit test for updateVehicleFields method when update is successful.
	 *
	 * <p>
	 * This test ensures that the service correctly calls the repository to update
	 * model and fuelType using a custom update query and returns true when the
	 * update affects at least one record.
	 */
	@Test
	void updateVehicleFields_ShouldReturnTrue_WhenUpdateIsSuccessful() {
		Long id = 1L;
		String model = "Model Y";
		String fuelType = "Electric";

		when(vehicleRepository.updateModelAndFuelTypeById(id, model, fuelType)).thenReturn(1);

		boolean result = vehicleService.updateVehicleFields(id, model, fuelType);

		assertTrue(result);
		verify(vehicleRepository, times(1)).updateModelAndFuelTypeById(id, model, fuelType);
	}

	/**
	 * Unit test for searchVehicles method in VehicleService.
	 *
	 * <p>
	 * This test ensures that the service converts the search input to lowercase,
	 * calls the repository method, and returns the expected list of matching
	 * vehicles.
	 */
	@Test
	void searchVehicles_ShouldReturnListOfVehicles() {
		String searchValue = "ABC";
		List<Vehicle> vehicles = List.of(new Vehicle(), new Vehicle());

		when(vehicleRepository.searchVehicles(searchValue.toLowerCase())).thenReturn(vehicles);

		List<Vehicle> result = vehicleService.searchVehicles(searchValue);

		assertEquals(2, result.size());
		verify(vehicleRepository, times(1)).searchVehicles("abc");
	}

}

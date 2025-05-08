package com.akt.vms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class VehicleAssignServiceTest {

	private VehicleAssignRepository vehicleAssignRepository;
	private VehicleRepository vehicleRepository;
	private DriverRepository driverRepository;
	private TripManagementRepository tripRepository;

	private VehicleAssignService vehicleAssignService;

	@BeforeEach
	public void setUp() {
		vehicleAssignRepository = mock(VehicleAssignRepository.class);
		vehicleRepository = mock(VehicleRepository.class);
		driverRepository = mock(DriverRepository.class);
		vehicleAssignService = new VehicleAssignService(vehicleAssignRepository, vehicleRepository, driverRepository, tripRepository);
	}

	@Test
	
	public void testCreateVehicleAssign_Success() {
	    // Prepare input DTO
	    VehicleAssignDTO dto = new VehicleAssignDTO();
	    dto.setVehicleid(1L);
	    dto.setDriver_id(2L);
	    dto.setTripManagementId(3L);
	    dto.setRemarks("Test Assignment");

	    // Prepare mock entities
	    Vehicle vehicle = new Vehicle();
	    vehicle.setId(1L);

	    Driver driver = new Driver();
	    driver.setDriverId(2L);

	    TripManagement trip = new TripManagement();
	    trip.setTripManagementId(3L);

	    VehicleAssign assignEntity = new VehicleAssign();
	    assignEntity.setVehicleAssignId(100L);
	    assignEntity.setVehicle(vehicle);
	    assignEntity.setDriver(driver);
	    assignEntity.setTrip(trip);
	    assignEntity.setRemarks("Test Assignment");

	    VehicleAssignDTO expectedDto = new VehicleAssignDTO();
	    expectedDto.setVehicleAssignId(100L);

	    // Mock repository responses
	    when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
	    when(driverRepository.findById(2L)).thenReturn(Optional.of(driver));
	    when(tripRepository.findById(3L)).thenReturn(Optional.of(trip));
	    when(vehicleAssignRepository.save(assignEntity)).thenReturn(assignEntity);

	    // Mock static mapper
	    try (MockedStatic<VehicleAssignMapper> mapperMock = Mockito.mockStatic(VehicleAssignMapper.class)) {
	        mapperMock.when(() -> VehicleAssignMapper.toEntity(dto, vehicle, driver, trip)).thenReturn(assignEntity);
	        mapperMock.when(() -> VehicleAssignMapper.toDTO(assignEntity)).thenReturn(expectedDto);

	        // Call the method
	        VehicleAssignDTO result = vehicleAssignService.createVehicleAssign(dto);

	        // Assertions
	        assertNotNull(result);
	        assertEquals(100L, result.getVehicleAssignId());

	        // Verify save was called
	        verify(vehicleAssignRepository).save(assignEntity);
	    }
	}

	@Test
	public void testCreateVehicleAssign_VehicleNotFound() {
		// Prepare input
		VehicleAssignDTO dto = new VehicleAssignDTO();
		dto.setVehicleid(1L);
		dto.setDriver_id(2L);

		// Mock vehicle not found
		when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

		// Expect exception
		RuntimeException ex = assertThrows(RuntimeException.class, () -> {
			vehicleAssignService.createVehicleAssign(dto);
		});

		assertEquals("Vehicle not found with ID: 1", ex.getMessage());
	}
}

package com.akt.vms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.akt.vms.dto.GpsDeviceInstallationDTO;
import com.akt.vms.dto.VehicleDTO;
import com.akt.vms.entity.GpsDeviceInstallation;
import com.akt.vms.entity.GpsDeviceInstallation.DeviceStatus;
import com.akt.vms.entity.Vehicle;
import com.akt.vms.mapper.GpsDeviceInstallationMapper;
import com.akt.vms.repository.GpsDeviceInstallationRepository;
import com.akt.vms.repository.VehicleRepository;

@ExtendWith(MockitoExtension.class)
public class GpsDeviceInstallationServiceTest {

	@Mock
	private GpsDeviceInstallationRepository gpsDeviceRepo;

	@Mock
	private VehicleRepository vehicleRepo;

	@Mock
	private GpsDeviceInstallationMapper mapper;

	@InjectMocks
	private GpsDeviceInstallationService gpsService;

	private Vehicle vehicle;
	private GpsDeviceInstallation device;
	private GpsDeviceInstallationDTO dto;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle();
		vehicle.setId(1L);

		device = new GpsDeviceInstallation();
		device.setDeviceId(100L);
		device.setVehicle(vehicle);
		device.setInstallationPerson("John");
		device.setInstallationDate(LocalDateTime.now());
		device.setDeviceStatus(DeviceStatus.ACTIVE);
		device.setSignalStrength(75.0);
		device.setLatitude(12.9716);
		device.setLongitude(77.5946);

		dto = new GpsDeviceInstallationDTO();
		dto.setDevice_id(100L);
		VehicleDTO vehicleDTO = new VehicleDTO();
		vehicleDTO.setId(1L);
		dto.setVehicle(vehicleDTO);
		dto.setInstallation_person("John");
		dto.setInstallation_date(LocalDateTime.now());
		dto.setDevice_status("ACTIVE");
		dto.setSignal_strength(75.0);
		dto.setLatitude(12.9716);
		dto.setLongitude(77.5946);
	}

	@Test
	void getDeviceById_ShouldReturnDto_WhenDeviceExists() {
		when(gpsDeviceRepo.findById(100L)).thenReturn(Optional.of(device));
		when(mapper.toDTO(device)).thenReturn(dto);

		GpsDeviceInstallationDTO result = gpsService.getDeviceById(100L);

		assertNotNull(result);
		assertEquals("John", result.getInstallation_person());
		verify(gpsDeviceRepo, times(1)).findById(100L);
	}

	@Test
	void getDeviceById_ShouldThrowException_WhenDeviceNotFound() {
		when(gpsDeviceRepo.findById(200L)).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> gpsService.getDeviceById(200L));
		verify(gpsDeviceRepo, times(1)).findById(200L);
	}

	@Test
	void installDevice_ShouldInstallSuccessfully() {
	    // Setup mocks
	    GpsDeviceInstallationDTO dto = mock(GpsDeviceInstallationDTO.class);
	    when(dto.getVehicle_id()).thenReturn(1L);  // Ensure vehicleId is provided

	    // Mock other dependencies
	    when(vehicleRepo.findById(1L)).thenReturn(Optional.of(vehicle));  // Mock vehicle found
	    when(gpsDeviceRepo.existsByVehicle(vehicle)).thenReturn(false);  // No existing device
	    when(mapper.toEntity(dto, vehicle)).thenReturn(device);  // Map DTO to device

	    // Set device_id to 100L before saving to ensure it's set correctly
	    device.setDeviceId(100L);  // Manually set device_id for test

	    // Mock save and DTO transformation
	    when(gpsDeviceRepo.save(device)).thenReturn(device);  // Mock save returning device
	    when(mapper.toDTO(device)).thenReturn(dto);  // Mock mapping to DTO for the result
	    when(dto.getDevice_id()).thenReturn(100L);
	    
	    // Call the service method
	    GpsDeviceInstallationDTO result = gpsService.installDevice(dto);

	    // Assertions
	    assertNotNull(result);
	    assertEquals(100L, result.getDevice_id());  // Ensure device ID is as expected
	    verify(vehicleRepo, times(1)).findById(1L);  // Ensure vehicle repo is queried once
	    verify(gpsDeviceRepo, times(1)).existsByVehicle(vehicle);  // Ensure device existence check
	    verify(gpsDeviceRepo, times(1)).save(device);  // Ensure save is called once
	}



	@Test
	void installDevice_ShouldThrowException_WhenVehicleIdMissing() {
		dto.setVehicle(null);

		assertThrows(ResponseStatusException.class, () -> gpsService.installDevice(dto));
		verify(vehicleRepo, never()).findById(anyLong());
		verify(gpsDeviceRepo, never()).save(any());
	}

	@Test
	void installDevice_ShouldThrowException_WhenVehicleNotFound() {
		GpsDeviceInstallationDTO dto = new GpsDeviceInstallationDTO();
		dto.setVehicle_id(1L);

		when(vehicleRepo.findById(1L)).thenReturn(Optional.empty());
		assertThrows(ResponseStatusException.class, () -> gpsService.installDevice(dto));

		verify(vehicleRepo, times(1)).findById(1L);
		verify(gpsDeviceRepo, never()).save(any());
	}

	@Test
	void installDevice_ShouldThrowException_WhenDeviceAlreadyExists() {

		Long vehicleId = 1L;

		// Mock vehicle repository to return a vehicle for ID 1
		when(vehicleRepo.findById(vehicleId)).thenReturn(Optional.of(vehicle));

		// Mock gpsDeviceInstallationRepository to return true (device exists for the
		// vehicle)
		when(gpsDeviceRepo.existsByVehicle(vehicle)).thenReturn(true);

		// Create a DTO with the correct vehicle ID
		GpsDeviceInstallationDTO dto = new GpsDeviceInstallationDTO();
		dto.setVehicle_id(vehicleId); // Ensure vehicleId is set to 1L

		// When: Trying to install the device should throw a ResponseStatusException
		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> gpsService.installDevice(dto));

		// Then: Verify the methods were called as expected
		verify(vehicleRepo, times(1)).findById(vehicleId); // Verify that findById was called with 1L
		verify(gpsDeviceRepo, times(1)).existsByVehicle(vehicle); // Verify existsByVehicle was called
		verify(gpsDeviceRepo, never()).save(any()); // Ensure that save was never called
	}

}

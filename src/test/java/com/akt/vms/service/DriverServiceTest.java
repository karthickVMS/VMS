package com.akt.vms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
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

import com.akt.vms.dto.DriverDTO;
import com.akt.vms.entity.Driver;
import com.akt.vms.mapper.DriverMapper;
import com.akt.vms.repository.DriverRepository;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {

	@Mock
	private DriverRepository driverRepository;

	@InjectMocks
	private DriverService driverService;

	private Driver driver;
	private DriverDTO driverDTO;

	@Mock
	private DriverMapper driverMapper; // Make sure this is mocked

	@BeforeEach
	void setUp() {
		driver = new Driver();
		driver.setDriverId(1L);
		driver.setName("Test Name");
		driver.setLicenseNum("Test License Number");
		driver.setVehicleNum("Test Vehicle Number");
		driver.setContactNum("Test Contact Number");
		driver.setYearsOfExp(100);
		driver.setState("Test State");

		driverDTO = new DriverDTO();
		driverDTO.setId(1L);
		driverDTO.setName("Test Name");
		driverDTO.setLicenseNum("Test License Number");
		driverDTO.setVehicleNum("Test Vehicle Number");
		driverDTO.setContactNum("Test Contact Number");
		driverDTO.setYearsOfExp(100);
		driverDTO.setState("Test State");
	}

	@Test
	void createDriver_ShouldSaveAndReturnDriverDTO() {
		// Arrange: input DTO
		DriverDTO inputDto = new DriverDTO();
		inputDto.setName("Jane Doe");
		inputDto.setLicenseNum("ABC123");

		// Arrange: entity converted from DTO
		Driver driverEntity = new Driver();
		driverEntity.setName("Jane Doe");
		driverEntity.setLicenseNum("ABC123");

		// Arrange: saved entity (with ID)
		Driver savedEntity = new Driver();
		savedEntity.setDriverId(1L);
		savedEntity.setName("Jane Doe");
		savedEntity.setLicenseNum("ABC123");

		// Arrange: output DTO from saved entity
		DriverDTO outputDto = new DriverDTO();
		outputDto.setName("Jane Doe");
		outputDto.setLicenseNum("ABC123");

		// Mock all steps
		when(driverMapper.toEntity(inputDto)).thenReturn(driverEntity);
		when(driverRepository.save(driverEntity)).thenReturn(savedEntity);
		when(driverMapper.toDriverDTO(savedEntity)).thenReturn(outputDto);

		// Act
		DriverDTO result = driverService.createDriver(inputDto);

		// Assert
		assertNotNull(result); // Line 73
		assertEquals("Jane Doe", result.getName());
		assertEquals("ABC123", result.getLicenseNum());
	}

	@Test
	void getAllDrivers_ShouldReturnListOfDriverDTOs() {
		// Arrange - create a mock Driver entity
		Driver driver = new Driver();
		driver.setName("Test Name");
		driver.setLicenseNum("Test License Number");

		// Mock repository to return a list containing the entity
		when(driverRepository.findAll()).thenReturn(List.of(driver));

		// Arrange - expected DTO result of mapping
		DriverDTO driverDTO = new DriverDTO();
		driverDTO.setName("Test Name");
		driverDTO.setLicenseNum("Test License Number");

		// Mock the mapper
		when(driverMapper.toDriverDTO(driver)).thenReturn(driverDTO);

		// Act
		List<DriverDTO> result = driverService.getAllDrivers();

		// Assert
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Test Name", result.get(0).getName());
		assertEquals("Test License Number", result.get(0).getLicenseNum());
	}

	@Test
	void getDriverById_ShouldReturnDriverDTO_WhenDriverExists() {
	    // Arrange
	    Long driverId = 1L;

	    Driver driver = new Driver();
	    driver.setDriverId(driverId);
	    driver.setName("Alice");
	    driver.setLicenseNum("XYZ789");

	    DriverDTO driverDTO = new DriverDTO();
	    driverDTO.setName("Alice");
	    driverDTO.setLicenseNum("XYZ789");

	    // Mock repository to return driver
	    when(driverRepository.findById(driverId)).thenReturn(Optional.of(driver));

	    // Mock mapper to return DTO
	    when(driverMapper.toDriverDTO(driver)).thenReturn(driverDTO);

	    // Act
	    DriverDTO result = driverService.getDriverById(driverId);

	    // Assert
	    assertNotNull(result);  // <-- Line 137 in your test
	    assertEquals("Alice", result.getName());
	}


	@Test
	void getDriverById_ShouldReturnNull_WhenDriverDoesNotExist() {
		when(driverRepository.findById(1L)).thenReturn(Optional.empty());

		DriverDTO result = driverService.getDriverById(1L);

		assertNull(result);
		verify(driverRepository, times(1)).findById(1L);
	}



	@Test
	void updateDriver_ShouldUpdateAndReturnDriverDTO_WhenDriverExists() {
	    // Arrange
	    Long driverId = 1L;

	    Driver existingDriver = new Driver();
	    existingDriver.setDriverId(driverId);
	    existingDriver.setName("Old Name");
	    existingDriver.setLicenseNum("OLD123");

	    DriverDTO updatedDto = new DriverDTO();
	    updatedDto.setName("New Name");
	    updatedDto.setLicenseNum("NEW123");

	    Driver updatedDriver = new Driver();
	    updatedDriver.setDriverId(driverId);
	    updatedDriver.setName("New Name");
	    updatedDriver.setLicenseNum("NEW123");

	    DriverDTO returnedDto = new DriverDTO();
	    returnedDto.setName("New Name");
	    returnedDto.setLicenseNum("NEW123");

	    when(driverRepository.findById(driverId)).thenReturn(Optional.of(existingDriver));
	    when(driverRepository.save(existingDriver)).thenReturn(updatedDriver);
	    when(driverMapper.toDriverDTO(updatedDriver)).thenReturn(returnedDto);

	    // Act
	    DriverDTO result = driverService.updateDriver(driverId, updatedDto);

	    // Assert
	    assertNotNull(result);  // Line 181 in your test
	    
	}

	@Test
	void updateDriver_ShouldReturnNull_WhenDriverDoesNotExist() {
		when(driverRepository.findById(1L)).thenReturn(Optional.empty());

		DriverDTO result = driverService.updateDriver(1L, driverDTO);

		assertNull(result);
		verify(driverRepository, times(1)).findById(1L);
		verify(driverRepository, never()).save(any(Driver.class));
	}

	@Test
	void deleteDriver_ShouldCallDeleteById() {
		// Arrange: mock existsById to return true so that deleteById is called
		when(driverRepository.existsById(1L)).thenReturn(true);
		doNothing().when(driverRepository).deleteById(1L);

		// Act
		boolean result = driverService.deleteDriver(1L);

		// Assert
		assertTrue(result);
		verify(driverRepository, times(1)).existsById(1L);
		verify(driverRepository, times(1)).deleteById(1L);
	}

}

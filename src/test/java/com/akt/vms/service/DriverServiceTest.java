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
		when(driverMapper.toEntity(driverDTO)).thenReturn(driver);
		when(driverRepository.save(driver)).thenReturn(driver);

		DriverDTO result = driverService.createDriver(driverDTO);

		assertNotNull(result);
		assertEquals("Test Name", result.getName());
		assertEquals("Test License Number", result.getLicenseNum());
		assertEquals("Test Vehicle Number", result.getVehicleNum());
		assertEquals("Test Contact Number", result.getContactNum());
		assertEquals(100, result.getYearsOfExp());
		assertEquals("Test State", result.getState());

		verify(driverRepository, times(1)).save(any(Driver.class));
	}

	@Test
	void getAllDrivers_ShouldReturnListOfDriverDTOs() {
		List<Driver> drivers = Arrays.asList(driver);
		when(driverRepository.findAll()).thenReturn(drivers);

		List<DriverDTO> result = driverService.getAllDrivers();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Test Name", result.get(0).getName());
		assertEquals("Test License Number", result.get(0).getLicenseNum());
		verify(driverRepository, times(1)).findAll();
	}

	@Test
	void getDriverById_ShouldReturnDriverDTO_WhenDriverExists() {
		when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

		DriverDTO result = driverService.getDriverById(1L);

		assertNotNull(result);
		assertEquals("Test Name", result.getName());
		assertEquals("Test License Number", result.getLicenseNum());
		verify(driverRepository, times(1)).findById(1L);
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
		when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
		when(driverRepository.save(any(Driver.class))).thenReturn(driver);

		DriverDTO updatedDTO = new DriverDTO();
		updatedDTO.setName("Updated Driver");
		updatedDTO.setLicenseNum("Updated LicenseNum");

		DriverDTO result = driverService.updateDriver(1L, updatedDTO);

		assertNotNull(result);
		assertEquals("Updated Driver", result.getName());
		assertEquals("Updated LicenseNum", result.getLicenseNum());
		verify(driverRepository, times(1)).findById(1L);
		verify(driverRepository, times(1)).save(any(Driver.class));
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

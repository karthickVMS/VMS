package com.akt.vms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.akt.vms.dto.DeviceLocationDTO;
import com.akt.vms.dto.DeviceLocationResponseDTO;
import com.akt.vms.entity.DeviceLocation;
import com.akt.vms.entity.GpsDeviceInstallation;
import com.akt.vms.mapper.DeviceLocationMapper;
import com.akt.vms.repository.DeviceLocationRepository;
import com.akt.vms.repository.GpsDeviceInstallationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeviceLocationServiceTest {

	@Mock
	private DeviceLocationRepository deviceLocationRepository;

	@Mock
	private GpsDeviceInstallationRepository gpsDeviceInstallationRepository;

	@Mock
	private DeviceLocationMapper deviceLocationMapper;

	@InjectMocks
	private DeviceLocationService deviceLocationService;

	private DeviceLocationDTO deviceLocationDTO;
	private GpsDeviceInstallation gpsDeviceInstallation;
	private DeviceLocation deviceLocation;

	@BeforeEach
	void setUp() {
		deviceLocationDTO = new DeviceLocationDTO();
		deviceLocationDTO.setDevice_id(1L);
		deviceLocationDTO.setLatitude(10.0);
		deviceLocationDTO.setLongitude(20.0);
		deviceLocationDTO.setTime(LocalDateTime.parse("2025-05-14T12:00:00"));

		gpsDeviceInstallation = new GpsDeviceInstallation();
		gpsDeviceInstallation.setDeviceId(1L);

		deviceLocation = new DeviceLocation();
		deviceLocation.setId(100L);
		deviceLocation.setLatitude(10.0);
		deviceLocation.setLongitude(20.0);
		deviceLocation.setTime(LocalDateTime.parse("2025-05-14T12:00:00"));
	}

	@Test
	void saveDeviceLocation_ShouldSaveAndReturnDeviceLocation_WhenDeviceExists() {
		when(gpsDeviceInstallationRepository.findById(1L)).thenReturn(Optional.of(gpsDeviceInstallation));
		when(deviceLocationMapper.toEntity(deviceLocationDTO, gpsDeviceInstallation)).thenReturn(deviceLocation);
		when(deviceLocationRepository.save(deviceLocation)).thenReturn(deviceLocation);
		when(gpsDeviceInstallationRepository.save(gpsDeviceInstallation)).thenReturn(gpsDeviceInstallation);

		DeviceLocation result = deviceLocationService.saveDeviceLocation(deviceLocationDTO);

		assertNotNull(result);
		assertEquals(100L, result.getId());
		assertEquals(10.0, result.getLatitude());
		assertEquals(20.0, result.getLongitude());

		verify(gpsDeviceInstallationRepository, times(1)).findById(1L);
		verify(deviceLocationRepository, times(1)).save(deviceLocation);
		verify(gpsDeviceInstallationRepository, times(1)).save(gpsDeviceInstallation);
	}

	@Test
	void saveDeviceLocation_ShouldThrowException_WhenDeviceDoesNotExist() {
		when(gpsDeviceInstallationRepository.findById(1L)).thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> deviceLocationService.saveDeviceLocation(deviceLocationDTO));

		assertEquals("Device with ID 1 not found", exception.getMessage());

		verify(gpsDeviceInstallationRepository, times(1)).findById(1L);
		verify(deviceLocationRepository, never()).save(any());
	}

	@Test
	void getLocationsForDevice_ShouldReturnResponseDTO_WhenLocationsExist() {
		List<DeviceLocation> locations = List.of(deviceLocation);
		DeviceLocationResponseDTO responseDTO = new DeviceLocationResponseDTO();

		when(deviceLocationRepository.findByDevice_DeviceId(1L)).thenReturn(locations);
		when(deviceLocationMapper.toDeviceLocationResponseDTO(locations)).thenReturn(responseDTO);

		DeviceLocationResponseDTO result = deviceLocationService.getLocationsForDevice(1L);

		assertNotNull(result);
		verify(deviceLocationRepository, times(1)).findByDevice_DeviceId(1L);
		verify(deviceLocationMapper, times(1)).toDeviceLocationResponseDTO(locations);
	}

	@Test
	void getLocationsForDevice_ShouldReturnNull_WhenNoLocationsExist() {
		when(deviceLocationRepository.findByDevice_DeviceId(1L)).thenReturn(Collections.emptyList());

		DeviceLocationResponseDTO result = deviceLocationService.getLocationsForDevice(1L);

		assertNull(result);
		verify(deviceLocationRepository, times(1)).findByDevice_DeviceId(1L);
		verify(deviceLocationMapper, never()).toDeviceLocationResponseDTO(any());
	}
}

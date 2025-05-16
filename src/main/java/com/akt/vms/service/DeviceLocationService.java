package com.akt.vms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akt.vms.dto.DeviceLocationDTO;
import com.akt.vms.dto.DeviceLocationResponseDTO;
import com.akt.vms.entity.DeviceLocation;
import com.akt.vms.entity.GpsDeviceInstallation;
import com.akt.vms.mapper.DeviceLocationMapper;
import com.akt.vms.repository.DeviceLocationRepository;
import com.akt.vms.repository.GpsDeviceInstallationRepository;

@Service
public class DeviceLocationService {

	@Autowired
	private DeviceLocationRepository deviceLocationRepository;

	@Autowired
	private GpsDeviceInstallationRepository gpsDeviceInstallationRepository;

	@Autowired
	private DeviceLocationMapper deviceLocationMapper;

	private static final Logger logger = LoggerFactory.getLogger(GpsDeviceInstallationService.class);

	public DeviceLocation saveDeviceLocation(DeviceLocationDTO request) {
		Long deviceId = request.getDevice_id();

		GpsDeviceInstallation device = gpsDeviceInstallationRepository.findById(deviceId).orElseThrow(() -> {
			logger.warn("Device with ID {} not found", deviceId);
			return new IllegalArgumentException("Device with ID " + deviceId + " not found");
		});

		DeviceLocation location = deviceLocationMapper.toEntity(request, device);

		location = deviceLocationRepository.save(location);

		device.setLastSignalCheck(location.getTime());
		device.setLatitude(location.getLatitude());
		device.setLongitude(location.getLongitude());
		gpsDeviceInstallationRepository.save(device);

		logger.info("Saved location with ID {} for device ID {}", location.getId(), deviceId);

		return location;
	}

	public DeviceLocationResponseDTO getLocationsForDevice(Long deviceId) {
		// Retrieve the list of device locations from the repository
		List<DeviceLocation> locations = deviceLocationRepository.findByDevice_DeviceId(deviceId);

		if (locations.isEmpty()) {
			return null;
		}

		return deviceLocationMapper.toDeviceLocationResponseDTO(locations);
	}
}
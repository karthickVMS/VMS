package com.akt.vms.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.akt.vms.dto.DeviceLocationDTO;
import com.akt.vms.dto.DeviceLocationResponseDTO;
import com.akt.vms.dto.GpsDeviceInstallationDTO;
import com.akt.vms.dto.VehicleDTO;
import com.akt.vms.entity.DeviceLocation;
import com.akt.vms.entity.GpsDeviceInstallation;
import com.akt.vms.entity.Vehicle;

@Component
public class DeviceLocationMapper {

	public DeviceLocation toEntity(DeviceLocationDTO dto, GpsDeviceInstallation device) {
		DeviceLocation location = new DeviceLocation();
		location.setDevice(device);
		location.setLocationId(dto.getLocation_id());
		location.setLatitude(dto.getLatitude());
		location.setLongitude(dto.getLongitude());
		location.setTime(dto.getTime());
		return location;
	}

	public DeviceLocationDTO toDTO(DeviceLocation location) {
		DeviceLocationDTO dto = new DeviceLocationDTO();
		dto.setDevice_id(location.getDevice().getDeviceId());
		dto.setLocation_id(location.getLocationId());
		dto.setLatitude(location.getLatitude());
		dto.setLongitude(location.getLongitude());
		dto.setTime(location.getTime());
		return dto;
	}

	public List<DeviceLocationDTO> toDTO(List<DeviceLocation> locations) {
		return locations.stream().map(this::toDTO).collect(Collectors.toList());
	}

	public DeviceLocationResponseDTO toDeviceLocationResponseDTO(List<DeviceLocation> locations) {
		DeviceLocationResponseDTO responseDTO = new DeviceLocationResponseDTO();
		if (locations == null || locations.isEmpty()) {
			return responseDTO;
		}

		responseDTO.setLocations(toDTO(locations));

		GpsDeviceInstallation device = locations.get(0).getDevice();
		responseDTO.setDevice(toDeviceDTO(device));

		return responseDTO;
	}

	private GpsDeviceInstallationDTO toDeviceDTO(GpsDeviceInstallation device) {
		GpsDeviceInstallationDTO dto = new GpsDeviceInstallationDTO();
		dto.setDevice_id(device.getDeviceId());
		dto.setVehicle(toVehicleDTO(device.getVehicle()));
		dto.setVehicle_id(device.getVehicle().getId());

		dto.setInstallation_person(device.getInstallationPerson());
		dto.setInstallation_date(device.getInstallationDate());
		dto.setDevice_status(device.getDeviceStatus().name());
		dto.setSignal_strength(device.getSignalStrength());
		dto.setLast_signal_check(device.getLastSignalCheck());
		dto.setLatitude(device.getLatitude());
		dto.setLongitude(device.getLongitude());
		dto.setRemarks(device.getRemarks());

		return dto;
	}

	private VehicleDTO toVehicleDTO(Vehicle vehicle) {
		if (vehicle == null)
			return null;

		VehicleDTO dto = new VehicleDTO();
		dto.setId(vehicle.getId());
		dto.setVehicleNumber(vehicle.getVehicleNumber());
		dto.setModel(vehicle.getModel());
		dto.setFuelType(vehicle.getFuelType());
		dto.setInsurancePolicyNumber(vehicle.getInsurancePolicyNumber());
		dto.setYearOfManufacturer(vehicle.getYearOfManufacturer());
		dto.setRegistrationDate(vehicle.getRegistrationDate());

		return dto;
	}
}

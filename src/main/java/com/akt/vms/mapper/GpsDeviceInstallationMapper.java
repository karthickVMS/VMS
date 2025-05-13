package com.akt.vms.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akt.vms.dto.GpsDeviceInstallationDTO;
import com.akt.vms.entity.GpsDeviceInstallation;
import com.akt.vms.entity.Vehicle;

@Component
public class GpsDeviceInstallationMapper {

	@Autowired
	private VehicleMapper vehicleMapper;

	public GpsDeviceInstallation toEntity(GpsDeviceInstallationDTO dto, Vehicle vehicle) {
		GpsDeviceInstallation entity = new GpsDeviceInstallation();
		entity.setDeviceId(dto.getDevice_id());
		entity.setVehicle(vehicle);
		entity.setInstallationPerson(dto.getInstallation_person());
		entity.setInstallationDate(dto.getInstallation_date());
		entity.setDeviceStatus(GpsDeviceInstallation.DeviceStatus.valueOf(dto.getDevice_status().name()));
		entity.setSignalStrength(dto.getSignal_strength());
		entity.setLastSignalCheck(dto.getLast_signal_check());
		// entity.setLatitude(dto.getLatitude());
		// entity.setLongitude(dto.getLongitude());
		entity.setRemarks(dto.getRemarks());
		return entity;
	}

	public GpsDeviceInstallationDTO toDTO(GpsDeviceInstallation entity) {
		GpsDeviceInstallationDTO dto = new GpsDeviceInstallationDTO();
		dto.setDevice_id(entity.getDeviceId());
		dto.setVehicle(vehicleMapper.toDto(entity.getVehicle())); // ✅ Maps to full vehicle object
		dto.setInstallation_person(entity.getInstallationPerson());
		dto.setInstallation_date(entity.getInstallationDate());
		dto.setDevice_status(GpsDeviceInstallationDTO.Device_status.valueOf(entity.getDeviceStatus().name()));
		dto.setSignal_strength(entity.getSignalStrength());
		dto.setLast_signal_check(entity.getLastSignalCheck());
		dto.setRemarks(entity.getRemarks());
		// dto.setLatitude(entity.getLatitude());
		// dto.setLongitude(entity.getLongitude());
		return dto;
	}
}

package com.akt.vms.dto;

import java.util.List;

public class DeviceLocationResponseDTO {

	private GpsDeviceInstallationDTO device;
	private List<DeviceLocationDTO> locations;

	public GpsDeviceInstallationDTO getDevice() {
		return device;
	}

	public void setDevice(GpsDeviceInstallationDTO device) {
		this.device = device;
	}

	public List<DeviceLocationDTO> getLocations() {
		return locations;
	}

	public void setLocations(List<DeviceLocationDTO> locations) {
		this.locations = locations;
	}

}

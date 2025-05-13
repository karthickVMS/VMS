package com.akt.vms.dto;

import java.time.LocalDateTime;

public class GpsDeviceInstallationDTO {

	private Long device_id;
	private Long vehicle_id;
	private VehicleDTO vehicle;
	private String installation_person;
	private LocalDateTime installation_date;
	private Device_status device_status;
	private Double signal_strength;
	private LocalDateTime last_signal_check;
	private Double latitude;
	private Double longitude;
	private String remarks;

	public enum Device_status {
		ACTIVE, INACTIVE, MALFUNCTIONING, REMOVED
	}

	public Long getVehicle_id() {
		return vehicle_id;
	}

	public void setVehicle_id(Long vehicle_id) {
		this.vehicle_id = vehicle_id;
	}

	public Long getDevice_id() {
		return device_id;
	}

	public void setDevice_id(Long device_id) {
		this.device_id = device_id;
	}

	public VehicleDTO getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleDTO vehicle) {
		this.vehicle = vehicle;
	}

	public String getInstallation_person() {
		return installation_person;
	}

	public void setInstallation_person(String installation_person) {
		this.installation_person = installation_person;
	}

	public LocalDateTime getInstallation_date() {
		return installation_date;
	}

	public void setInstallation_date(LocalDateTime installation_date) {
		this.installation_date = installation_date;
	}

	public Device_status getDevice_status() {
		return device_status;
	}

	public void setDevice_status(Device_status device_status) {
		this.device_status = device_status;
	}

	public Double getSignal_strength() {
		return signal_strength;
	}

	public void setSignal_strength(Double signal_strength) {
		this.signal_strength = signal_strength;
	}

	public LocalDateTime getLast_signal_check() {
		return last_signal_check;
	}

	public void setLast_signal_check(LocalDateTime last_signal_check) {
		this.last_signal_check = last_signal_check;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}

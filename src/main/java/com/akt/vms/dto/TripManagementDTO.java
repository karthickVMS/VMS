package com.akt.vms.dto;

import java.time.LocalDateTime;

public class TripManagementDTO {

	private Long tripManagementId;
	private String tripName;
	private String startLocation;
	private String endLocation;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String status;
	private DriverDTO driver;
	private VehicleDTO vehicle;

	public Long getTripManagementId() {
		return tripManagementId;
	}

	public void setTripManagementId(Long tripManagementId) {
		this.tripManagementId = tripManagementId;
	}

	public String getTripName() {
		return tripName;
	}

	public void setTripName(String tripName) {
		this.tripName = tripName;
	}

	public String getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}

	public String getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DriverDTO getDriver() {
		return driver;
	}

	public void setDriver(DriverDTO driver) {
		this.driver = driver;
	}

	public VehicleDTO getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleDTO vehicle) {
		this.vehicle = vehicle;
	}
}
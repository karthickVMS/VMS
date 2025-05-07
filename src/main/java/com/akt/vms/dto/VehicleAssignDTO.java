package com.akt.vms.dto;

public class VehicleAssignDTO {
	private Long vehicleAssignId;
	private Long vehicle_id;
	private Long driver_id;
	private String remarks;

	public Long getVehicleAssignId() {
		return vehicleAssignId;
	}

	public void setVehicleAssignId(Long vehicleAssignId) {
		this.vehicleAssignId = vehicleAssignId;
	}

	public Long getVehicleid() {
		return vehicle_id;
	}

	public void setVehicleid(Long vehicleid) {
		this.vehicle_id = vehicleid;
	}

	public Long getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(Long driver_id) {
		this.driver_id = driver_id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}

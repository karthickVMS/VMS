package com.akt.vms.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FuelFillingDTO {

	private Long vehicle_id;
	private String fuel_type;
	private Double fuel_quantity;
	private Double cost;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime refuel_date_time;
	private String location;
	private Integer odometer_reading;

	public Long getVehicle_id() {
		return vehicle_id;
	}

	public void setVehicle_id(Long vehicle_id) {
		this.vehicle_id = vehicle_id;
	}

	public String getFuel_type() {
		return fuel_type;
	}

	public void setFuel_type(String fuel_type) {
		this.fuel_type = fuel_type;
	}

	public Double getFuel_quantity() {
		return fuel_quantity;
	}

	public void setFuel_quantity(Double fuel_quantity) {
		this.fuel_quantity = fuel_quantity;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public LocalDateTime getRefuel_date_time() {
		return refuel_date_time;
	}

	public void setRefuel_date_time(LocalDateTime refuel_date_time) {
		this.refuel_date_time = refuel_date_time;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getOdometer_reading() {
		return odometer_reading;
	}

	public void setOdometer_reading(Integer odometer_reading) {
		this.odometer_reading = odometer_reading;
	}

}

package com.akt.vms.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FuelUsageReportDTO {

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Long vehicle_id;
	private LocalDateTime from_date;
	private LocalDateTime to_date;
	private double total_fuel_used;
	private double total_cost;
	private int distanc_traveled;
	private double efficiency;

	public Long getVehicle_id() {
		return vehicle_id;
	}

	public void setVehicle_id(Long vehicle_id) {
		this.vehicle_id = vehicle_id;
	}

	public LocalDateTime getFrom_date() {
		return from_date;
	}

	public void setFrom_date(LocalDateTime from_date) {
		this.from_date = from_date;
	}

	public LocalDateTime getTo_date() {
		return to_date;
	}

	public void setTo_date(LocalDateTime to_date) {
		this.to_date = to_date;
	}

	public double getTotal_fuel_used() {
		return total_fuel_used;
	}

	public void setTotal_fuel_used(double total_fuel_used) {
		this.total_fuel_used = total_fuel_used;
	}

	public double getTotal_cost() {
		return total_cost;
	}

	public void setTotal_cost(double total_cost) {
		this.total_cost = total_cost;
	}

	public int getDistanc_traveled() {
		return distanc_traveled;
	}

	public void setDistanc_traveled(int distanc_traveled) {
		this.distanc_traveled = distanc_traveled;
	}

	public double getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(double efficiency) {
		this.efficiency = efficiency;
	}

}

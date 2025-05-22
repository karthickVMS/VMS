package com.akt.vms.dto;

public class FuelMonthlyReportDTO {

	private Long vehicle_id;
	private String month;
	private String yearMonth; // Format: "2025-05"
	private Double total_fuel_used;
	private Integer distance_travelled;
	private Double fuel_efficiency;
	private Double total_cost;

	public Long getVehicle_id() {
		return vehicle_id;
	}

	public void setVehicle_id(Long vehicle_id) {
		this.vehicle_id = vehicle_id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public Double getTotal_fuel_used() {
		return total_fuel_used;
	}

	public void setTotal_fuel_used(Double total_fuel_used) {
		this.total_fuel_used = total_fuel_used;
	}

	public Integer getDistance_travelled() {
		return distance_travelled;
	}

	public void setDistance_travelled(Integer distance_travelled) {
		this.distance_travelled = distance_travelled;
	}

	public Double getFuel_efficiency() {
		return fuel_efficiency;
	}

	public void setFuel_efficiency(Double fuel_efficiency) {
		this.fuel_efficiency = fuel_efficiency;
	}

	public Double getTotal_cost() {
		return total_cost;
	}

	public void setTotal_cost(Double total_cost) {
		this.total_cost = total_cost;
	}

}

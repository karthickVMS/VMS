package com.akt.vms.entity;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fuel_filling_tbl")
@EntityListeners(AuditingEntityListener.class)
public class FuelFilling {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fuel_id")
	private Long fuelId;

	@ManyToOne
	@JoinColumn(name = "vehicle_id", nullable = false)
	private Vehicle vehicle;

	@Column(name = "fuel_type")
	private String fuelType;

	@Column(name = "fuel_quantity")
	private Double fuelQuantity;

	@Column(name = "cost")
	private Double cost;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "refuel_datetime")
	private LocalDateTime refuelDateTime;

	@Column(name = "location")
	private String location;

	@Column(name = "odometer_reading")
	private Integer odometerReading;

	public Long getFuelId() {
		return fuelId;
	}

	public void setFuelId(Long fuelId) {
		this.fuelId = fuelId;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public Double getFuelQuantity() {
		return fuelQuantity;
	}

	public void setFuelQuantity(Double fuelQuantity) {
		this.fuelQuantity = fuelQuantity;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public LocalDateTime getRefuelDateTime() {
		return refuelDateTime;
	}

	public void setRefuelDateTime(LocalDateTime refuelDateTime) {
		this.refuelDateTime = refuelDateTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getOdometerReading() {
		return odometerReading;
	}

	public void setOdometerReading(Integer odometerReading) {
		this.odometerReading = odometerReading;
	}

}

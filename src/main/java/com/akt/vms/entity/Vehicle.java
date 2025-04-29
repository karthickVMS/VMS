package com.akt.vms.entity;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicledetails")
@EntityListeners(AuditingEntityListener.class)
public class Vehicle extends Auditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String vehicleNumber;
	private String model;
	private String fuelType;
	private String insurancePolicyNumber;
	private Integer yearOfManufacturer;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "categoryid", referencedColumnName = "id")
	private Category category;
	/*
	 * @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval =
	 * true) private List<VehiclePurchase> purchases = new ArrayList<>();
	 */

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setYearOfManufacturer(Integer yearOfManufacturer) {
		this.yearOfManufacturer = yearOfManufacturer;
	}

	private LocalDate registrationDate;

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	@Column(nullable = false, unique = true)
	public String getInsurancePolicyNumber() {
		return insurancePolicyNumber;
	}

	public void setInsurancePolicyNumber(String insurancePolicyNumber) {
		this.insurancePolicyNumber = insurancePolicyNumber;
	}

	public Integer getYearOfManufacturer() {
		return yearOfManufacturer;
	}

	@Column(nullable = false, unique = true)
	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

}

package com.akt.vms.entity;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicle_gps_device_tbl")
@EntityListeners(AuditingEntityListener.class)
public class GpsDeviceInstallation extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "device_id")
	private Long deviceId;

	@OneToOne
	@JoinColumn(name = "vehicle_id", referencedColumnName = "id", unique = true, nullable = false)
	private Vehicle vehicle;

	@Column(name = "installation_person", nullable = false)
	private String installationPerson;

	@Column(name = "installation_date", nullable = false)
	private LocalDateTime installationDate;

	@Column(name = "device_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private DeviceStatus deviceStatus;

	@Column(name = "signal_strength")
	private Double signalStrength;

	@Column(name = "last_signal_check")
	private LocalDateTime lastSignalCheck;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	private String remarks;

	public enum DeviceStatus {
		ACTIVE, INACTIVE, MALFUNCTIONING, REMOVED
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getInstallationPerson() {
		return installationPerson;
	}

	public void setInstallationPerson(String installationPerson) {
		this.installationPerson = installationPerson;
	}

	public LocalDateTime getInstallationDate() {
		return installationDate;
	}

	public void setInstallationDate(LocalDateTime installationDate) {
		this.installationDate = installationDate;
	}

	public DeviceStatus getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(DeviceStatus deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public Double getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(Double signalStrength) {
		this.signalStrength = signalStrength;
	}

	public LocalDateTime getLastSignalCheck() {
		return lastSignalCheck;
	}

	public void setLastSignalCheck(LocalDateTime lastSignalCheck) {
		this.lastSignalCheck = lastSignalCheck;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
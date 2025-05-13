package com.akt.vms.entity;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "route_mapping_tbl")
@EntityListeners(AuditingEntityListener.class)
public class RouteMapping extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "route_mapping_id")
	private Long routeMappingId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trip_management_id")
	private TripManagement tripManagement;

	@Column(name = "point_name")
	private String pointName;

	@Column(name = "reached_time")
	private LocalDateTime reachedTime;

	@Column(name = "remarks")
	private String remarks;

	public enum Status {
		PENDING, REACHED, MISSED
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status;

	public Long getRouteMappingId() {
		return routeMappingId;
	}

	public void setRouteMappingId(Long routeMappingId) {
		this.routeMappingId = routeMappingId;
	}

	public TripManagement getTripManagement() {
		return tripManagement;
	}

	public void setTripManagement(TripManagement tripManagement) {
		this.tripManagement = tripManagement;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public LocalDateTime getReachedTime() {
		return reachedTime;
	}

	public void setReachedTime(LocalDateTime reachedTime) {
		this.reachedTime = reachedTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}

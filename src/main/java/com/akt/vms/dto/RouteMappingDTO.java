package com.akt.vms.dto;

import java.time.LocalDateTime;

import com.akt.vms.entity.RouteMapping.Status;
import com.akt.vms.entity.TripManagement;

public class RouteMappingDTO {

	private Long route_mapping_id;
	private Long trip_management_id;
	private String point_name;
	private LocalDateTime reached_time;
	private Status status;
	private String remarks;

	private TripManagementDTO tripManagementDTO;

	private TripManagement tripManagement;

	public TripManagement getTripManagement() {
		return tripManagement;
	}

	public void setTripManagement(TripManagement tripManagement) {
		this.tripManagement = tripManagement;
	}

	public TripManagementDTO getTripManagementDTO() {
		return tripManagementDTO;
	}

	public void setTripManagementDTO(TripManagementDTO tripManagementDTO) {
		this.tripManagementDTO = tripManagementDTO;
	}

	public Long getRoute_mapping_id() {
		return route_mapping_id;
	}

	public void setRoute_mapping_id(Long route_mapping_id) {
		this.route_mapping_id = route_mapping_id;
	}

	public Long getTrip_management_id() {
		return trip_management_id;
	}

	public void setTrip_management_id(Long trip_management_id) {
		this.trip_management_id = trip_management_id;
	}

	public String getPoint_name() {
		return point_name;
	}

	public void setPoint_name(String point_name) {
		this.point_name = point_name;
	}

	public LocalDateTime getReached_time() {
		return reached_time;
	}

	public void setReached_time(LocalDateTime reached_time) {
		this.reached_time = reached_time;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
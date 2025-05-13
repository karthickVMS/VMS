package com.akt.vms.dto;

import java.util.ArrayList;
import java.util.List;

public class TripSummaryDTO {

	private Long tripId;
	private long duration; // In minutes
	private double distance; // In kilometers
	private String status;
	public TripManagementDTO tripManagementDTO;
	public List<RouteMappingDTO> routeMappingList = new ArrayList<>();

	
	public TripManagementDTO getTripManagementDTO() {
		return tripManagementDTO;
	}

	public void setTripManagementDTO(TripManagementDTO tripManagementDTO) {
		this.tripManagementDTO = tripManagementDTO;
	}

	public List<RouteMappingDTO> getRouteMappingList() {
		return routeMappingList;
	}

	public void setRouteMappingList(List<RouteMappingDTO> routeMappingList) {
		this.routeMappingList = routeMappingList;
	}

	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

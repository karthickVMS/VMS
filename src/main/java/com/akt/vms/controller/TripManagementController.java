package com.akt.vms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akt.vms.dto.TripManagementDTO;
import com.akt.vms.dto.TripSummaryDTO;
import com.akt.vms.service.TripManagementService;

@RestController
@RequestMapping("/api/trips")
public class TripManagementController {

	@Autowired
	private TripManagementService tripManagementService;

	@GetMapping
	public List<TripManagementDTO> getAllTrips() {
	    return tripManagementService.getAllTrips();
	}

	@PostMapping
	public TripManagementDTO createTrip(@RequestBody TripManagementDTO tripManagementDTO) {
		return tripManagementService.createTrip(tripManagementDTO);
	}

	@PutMapping("/{id}/start")
	public TripManagementDTO startTrip(@PathVariable Long id) {
		return tripManagementService.startTrip(id);
	}

	@PutMapping("/{id}/stop")
	public TripManagementDTO stopTrip(@PathVariable Long id) {
		return tripManagementService.stopTrip(id);
	}

	@GetMapping("/{id}/summary")
	public TripSummaryDTO getTripSummary(@PathVariable Long id) {
		return tripManagementService.getTripSummary(id);
	}
}
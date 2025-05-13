package com.akt.vms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akt.vms.dto.RouteMappingDTO;
import com.akt.vms.entity.RouteMapping;
import com.akt.vms.mapper.RouteMappingMapper;
import com.akt.vms.service.RouteMappingService;

@RestController
@RequestMapping("/api/routes")
public class RouteMappingController {

	@Autowired
	private RouteMappingService routeMappingService;

	@Autowired
	private RouteMappingMapper routeMappingMapper;

	// Create a route mapping using trip ID from path
	@PostMapping("/trip/{tripId}")
	public ResponseEntity<RouteMappingDTO> createRouteWithTripId(@PathVariable Long tripId,
			@RequestBody RouteMappingDTO dto) {

		RouteMapping saved = routeMappingService.createRouteMapping(dto, tripId);
		RouteMappingDTO response = routeMappingMapper.toDTO(saved);
		return ResponseEntity.ok(response);
	}

	// Update a route mapping by ID
	@PutMapping("/{id}")
	public ResponseEntity<RouteMappingDTO> updateRouteMapping(@PathVariable Long id, @RequestBody RouteMappingDTO dto) {

		RouteMapping updated = routeMappingService.updateRouteMapping(id, dto);
		return ResponseEntity.ok(routeMappingMapper.toDTO(updated));
	}

	// Delete a route mapping by ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRouteMapping(@PathVariable Long id) {
		routeMappingService.deleteRouteMapping(id);
		return ResponseEntity.noContent().build();
	}
}

package com.akt.vms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akt.vms.dto.RouteMappingDTO;
import com.akt.vms.entity.RouteMapping;
import com.akt.vms.mapper.RouteMappingMapper;
import com.akt.vms.repository.RouteMappingRepository;

import jakarta.transaction.Transactional;

@Service
public class RouteMappingService {

	@Autowired
	private RouteMappingRepository routeMappingRepository;

	@Autowired
	private RouteMappingMapper routeMappingMapper;

	// Create a new RouteMapping using tripId
	@Transactional
	public RouteMapping createRouteMapping(RouteMappingDTO dto, Long tripId) {
		RouteMapping entity = routeMappingMapper.toEntity(dto);
		return routeMappingRepository.save(entity);
	}

	// Update existing RouteMapping
	@Transactional
	public RouteMapping updateRouteMapping(Long id, RouteMappingDTO dto) {
		RouteMapping existing = routeMappingRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("RouteMapping not found with ID: " + id));

		routeMappingMapper.updateEntityFromDTO(dto, existing);
		return routeMappingRepository.save(existing);
	}

	// Delete a RouteMapping by ID
	@Transactional
	public void deleteRouteMapping(Long id) {
		if (!routeMappingRepository.existsById(id)) {
			throw new RuntimeException("RouteMapping not found with ID: " + id);
		}
		routeMappingRepository.deleteById(id);
	}

}

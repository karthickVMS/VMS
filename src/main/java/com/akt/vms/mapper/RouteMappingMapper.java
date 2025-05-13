package com.akt.vms.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akt.vms.dto.RouteMappingDTO;
import com.akt.vms.entity.RouteMapping;
import com.akt.vms.repository.TripManagementRepository;

@Component
public class RouteMappingMapper {

	@Autowired
	private TripManagementRepository tripManagementRepository;
	
	@Autowired
	private TripManagementMapper tripManagementMapper;

	/**
	 * Converts a DTO to a RouteMapping entity. Associates the entity with the given
	 * TripManagement.
	 */
	public RouteMapping toEntity(RouteMappingDTO dto) {
		if (dto == null) {
			throw new IllegalArgumentException("DTO and Trip must not be null");
		}
		RouteMapping entity = new RouteMapping();

		entity.setRouteMappingId(dto.getRoute_mapping_id());
		entity.setPointName(dto.getPoint_name());
		entity.setStatus(dto.getStatus());
		entity.setReachedTime(dto.getReached_time());
		entity.setRemarks(dto.getRemarks());

		entity.setTripManagement(tripManagementRepository.findById(dto.getTrip_management_id()).get());
		return entity;

	}

	/**
	 * Converts a RouteMapping entity to its corresponding DTO.
	 */
	public RouteMappingDTO toDTO(RouteMapping entity) {
		if (entity == null || entity.getTripManagement() == null) {
			throw new IllegalArgumentException("Entity and its trip must not be null");
		}

		RouteMappingDTO dto = new RouteMappingDTO();
		dto.setRoute_mapping_id(entity.getRouteMappingId());
		dto.setPoint_name(entity.getPointName());
		dto.setStatus(entity.getStatus());
		dto.setReached_time(entity.getReachedTime());
		dto.setRemarks(entity.getRemarks());
		
		if (entity.getTripManagement() != null) {
			dto.setTrip_management_id(entity.getTripManagement().getTripManagementId());
			dto.setTripManagementDTO(tripManagementMapper.toDTO(entity.getTripManagement()));
		}
		return dto;
	}

	/**
	 * Updates an existing RouteMapping entity with values from a DTO. Does not
	 * update the trip association.
	 */
	public void updateEntityFromDTO(RouteMappingDTO dto, RouteMapping entity) {
		if (dto == null || entity == null) {
			throw new IllegalArgumentException("DTO and Entity must not be null");
		}

		entity.setPointName(dto.getPoint_name());
		entity.setStatus(dto.getStatus());
		entity.setReachedTime(dto.getReached_time());
		entity.setRemarks(dto.getRemarks());
	}
}

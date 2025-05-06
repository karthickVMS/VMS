package com.akt.vms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akt.vms.dto.TripManagementDTO;
import com.akt.vms.dto.TripSummaryDTO;
import com.akt.vms.entity.TripManagement;
import com.akt.vms.mapper.TripManagementMapper;
import com.akt.vms.repository.TripManagementRepository;

import jakarta.transaction.Transactional;

@Service
public class TripManagementService {

	@Autowired
	private TripManagementRepository tripManagementRepository;

	@Autowired
	private TripManagementMapper tripManagementMapper;

	@Transactional
	public List<TripManagementDTO> getAllTrips() {
		return tripManagementRepository.findAll().stream().map(tripManagementMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Transactional
	public TripManagementDTO createTrip(TripManagementDTO tripManagementDTO) {
		TripManagement tripManagement = tripManagementMapper.toEntity(tripManagementDTO);
		tripManagement.setStatus("PENDING");
		tripManagement = tripManagementRepository.save(tripManagement);
		return tripManagementMapper.toDTO(tripManagement);
	}

	@Transactional
	public TripManagementDTO startTrip(Long id) {
		TripManagement trip = tripManagementRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Trip not found"));

		if (!"PENDING".equals(trip.getStatus())) {
			throw new IllegalStateException("Trip cannot be started.");
		}

		trip.setStartTime(LocalDateTime.now());
		trip.setStatus("IN_PROGRESS");
		trip = tripManagementRepository.save(trip);

		return tripManagementMapper.toDTO(trip);
	}

	@Transactional
	public TripManagementDTO stopTrip(Long id) {
		TripManagement trip = tripManagementRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Trip not found"));

		if (!"IN_PROGRESS".equals(trip.getStatus())) {
			throw new IllegalStateException("Trip cannot be stopped.");
		}

		trip.setEndTime(LocalDateTime.now());
		trip.setStatus("COMPLETED");
		trip = tripManagementRepository.save(trip);

		return tripManagementMapper.toDTO(trip);
	}

	public TripSummaryDTO getTripSummary(Long id) {
		TripManagement trip = tripManagementRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Trip not found with ID: " + id));

		long duration = tripManagementMapper.calculateDuration(trip);
		double distance = tripManagementMapper.calculateDistance(trip.getStartLocation(), trip.getEndLocation());

		// Create and return TripSummaryDTO
		TripSummaryDTO summary = new TripSummaryDTO();
		summary.setTripId(id);
		summary.setDuration(duration);
		summary.setDistance(distance);
		summary.setStatus(trip.getStatus());

		return summary;
	}
}
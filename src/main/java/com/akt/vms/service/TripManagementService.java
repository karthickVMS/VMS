package com.akt.vms.service;

import java.time.LocalDateTime;

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

	@Transactional
	public TripManagementDTO createTrip(TripManagementDTO tripManagementDTO) {
		TripManagement tripManagement = TripManagementMapper.toEntity(tripManagementDTO);
		tripManagement.setStatus("PENDING");
		tripManagement = tripManagementRepository.save(tripManagement);
		return TripManagementMapper.toDTO(tripManagement);
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

		return TripManagementMapper.toDTO(trip);
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

		return TripManagementMapper.toDTO(trip);
	}

	public TripSummaryDTO getTripSummary(Long id) {
		TripManagement trip = tripManagementRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Trip not found"));

		long duration = TripManagementMapper.calculateDuration(trip);
		double distance = TripManagementMapper.calculateDistance(trip.getStartLocation(), trip.getEndLocation());

		TripSummaryDTO summary = new TripSummaryDTO();
		summary.setTripId(id);
		summary.setDuration(duration);
		summary.setDistance(distance);
		summary.setStatus(trip.getStatus());

		return summary;
	}
}
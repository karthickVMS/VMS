package com.akt.vms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import com.akt.vms.dto.TripManagementDTO;
import com.akt.vms.dto.TripSummaryDTO;
import com.akt.vms.entity.TripManagement;
import com.akt.vms.mapper.TripManagementMapper;
import com.akt.vms.repository.TripManagementRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TripManagementServiceTest {

	@Mock
	private TripManagementRepository tripManagementRepository;

	@Mock
	private TripManagementMapper tripManagementMapper;

	@InjectMocks
	private TripManagementService tripManagementService;

	private TripManagement tripEntity;
	private TripManagementDTO tripDTO;

	@BeforeEach
	void setUp() {
		tripEntity = new TripManagement();
		tripEntity.setTripManagementId(1L);
		tripEntity.setStatus("PENDING");

		tripDTO = new TripManagementDTO();
		tripDTO.setTripManagementId(1L);
	}

	@Test
	void createTrip_ShouldSaveAndReturnTripDTO() {
		TripManagementDTO tripDTO = new TripManagementDTO();
		tripDTO.setTripManagementId(1L);

		TripManagement tripEntity = new TripManagement();
		tripEntity.setTripManagementId(1L);
		tripEntity.setStatus("PENDING");

		// Mock mapper behavior
		when(tripManagementMapper.toEntity(any(TripManagementDTO.class))).thenReturn(tripEntity);
		when(tripManagementMapper.toDTO(any(TripManagement.class))).thenReturn(tripDTO);

		// Mock repository behavior
		when(tripManagementRepository.save(any(TripManagement.class))).thenReturn(tripEntity);

		// Call service method
		TripManagementDTO result = tripManagementService.createTrip(tripDTO);

		// Verify and assert
		assertNotNull(result);
		assertEquals(1L, result.getTripManagementId());
		verify(tripManagementRepository, times(1)).save(any(TripManagement.class));
	}

	@Test
	void startTrip_ShouldThrowException_WhenTripIsNotPending() {
		tripEntity.setStatus("COMPLETED");
		when(tripManagementRepository.findById(1L)).thenReturn(Optional.of(tripEntity));

		assertThrows(IllegalStateException.class, () -> {
			tripManagementService.startTrip(1L);
		});

		verify(tripManagementRepository, times(1)).findById(1L);
		verify(tripManagementRepository, never()).save(any());
	}

	@Test
	void stopTrip_ShouldUpdateStatusAndEndTime_WhenTripIsInProgress() {
		tripEntity.setStatus("IN_PROGRESS");

		TripManagement updatedTrip = new TripManagement();
		updatedTrip.setTripManagementId(1L);
		updatedTrip.setStatus("COMPLETED");
		updatedTrip.setEndTime(LocalDateTime.now());

		when(tripManagementRepository.findById(1L)).thenReturn(Optional.of(tripEntity));
		when(tripManagementRepository.save(any())).thenReturn(updatedTrip);

		try (MockedStatic<TripManagementMapper> mockedStatic = mockStatic(TripManagementMapper.class)) {
			mockedStatic.when(() -> tripManagementMapper.toDTO(any())).thenReturn(tripDTO);

			TripManagementDTO result = tripManagementService.stopTrip(1L);

			assertNotNull(result);
			verify(tripManagementRepository).findById(1L);
			verify(tripManagementRepository).save(any());
		}
	}

	@Test
	void startTrip_ShouldUpdateStatusAndStartTime_WhenTripIsPending() {
		// Initialize tripEntity
		tripEntity.setStatus("PENDING");
		tripEntity.setTripManagementId(1L);
		tripEntity.setStartTime(LocalDateTime.now().minusMinutes(10));

		// New updated trip object for after save
		TripManagement updatedTrip = new TripManagement();
		updatedTrip.setTripManagementId(1L);
		updatedTrip.setStatus("IN_PROGRESS");
		updatedTrip.setStartTime(LocalDateTime.now());

		// Mock repository calls
		when(tripManagementRepository.findById(1L)).thenReturn(Optional.of(tripEntity));
		when(tripManagementRepository.save(any(TripManagement.class))).thenReturn(updatedTrip);

		// Mock static methods with mockStatic
		try (MockedStatic<TripManagementMapper> mockedStatic = mockStatic(TripManagementMapper.class)) {
			// Ensure toDTO returns the correct DTO with the updated status
			TripManagementDTO tripDTO = new TripManagementDTO();
			tripDTO.setTripManagementId(1L);
			tripDTO.setStatus("IN_PROGRESS"); // Mock status here
			mockedStatic.when(() -> tripManagementMapper.toDTO(any(TripManagement.class))).thenReturn(tripDTO);

			// Call the service method
			TripManagementDTO result = tripManagementService.startTrip(1L);

			// Assertions to verify the outcome
			assertNotNull(result);
			assertEquals(1L, result.getTripManagementId()); // Trip ID should match
			assertEquals("IN_PROGRESS", result.getStatus()); // Status should be updated to "IN_PROGRESS"

			// Verify interactions with repository
			verify(tripManagementRepository, times(1)).findById(1L);
			verify(tripManagementRepository, times(1)).save(any(TripManagement.class));
		}
	}

	@Test
	void stopTrip_ShouldThrowException_WhenTripIsNotInProgress() {
		tripEntity.setStatus("PENDING");
		when(tripManagementRepository.findById(1L)).thenReturn(Optional.of(tripEntity));

		assertThrows(IllegalStateException.class, () -> {
			tripManagementService.stopTrip(1L);
		});

		verify(tripManagementRepository, times(1)).findById(1L);
		verify(tripManagementRepository, never()).save(any());
	}

	@Test
	void getTripSummary_ShouldReturnTripSummaryDTO_WhenTripExists() {
		tripEntity.setTripManagementId(1L);
		tripEntity.setStartTime(LocalDateTime.now().minusHours(2));
		tripEntity.setEndTime(LocalDateTime.now());
		tripEntity.setStatus("COMPLETED");
		tripEntity.setStartLocation("A");
		tripEntity.setEndLocation("B");

		when(tripManagementRepository.findById(1L)).thenReturn(Optional.of(tripEntity));

		try (MockedStatic<TripManagementMapper> mockedStatic = mockStatic(TripManagementMapper.class)) {
			mockedStatic.when(() -> tripManagementMapper.calculateDuration(tripEntity)).thenReturn(7200L);
			mockedStatic.when(() -> tripManagementMapper.calculateDistance("A", "B")).thenReturn(150.0);

			TripSummaryDTO result = tripManagementService.getTripSummary(1L);

			assertNotNull(result);
			assertEquals(1L, result.getTripId());
			assertEquals("COMPLETED", result.getStatus());
			assertEquals(7200L, result.getDuration());
			assertEquals(150.0, result.getDistance());

			verify(tripManagementRepository, times(1)).findById(1L);
		}
	}

	@Test
	void getTripSummary_ShouldThrowException_WhenTripDoesNotExist() {
		when(tripManagementRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> {
			tripManagementService.getTripSummary(1L);
		});

		verify(tripManagementRepository, times(1)).findById(1L);
	}
}

package com.akt.vms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.akt.vms.dto.TripManagementDTO;
import com.akt.vms.dto.TripSummaryDTO;
import com.akt.vms.service.TripManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
public class TripManagementControllerTest implements WebMvcConfigurer {

	@Mock
	private TripManagementService tripManagementService;

	@InjectMocks
	private TripManagementController tripManagementController;

	private MockMvc mockMvc;
	private TripManagementDTO tripDTO;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule()); // ✅ Register module for Java 8 time types

		tripDTO = new TripManagementDTO();
		tripDTO.setTripManagementId(1L);
		tripDTO.setStatus("PENDING");
		tripDTO.setStartTime(LocalDateTime.now());
		tripDTO.setEndTime(LocalDateTime.now().plusHours(2));

		mockMvc = MockMvcBuilders.standaloneSetup(tripManagementController).build();
	}

	@Test
	void createTrip_ShouldReturnCreatedTrip() throws Exception {
		when(tripManagementService.createTrip(any(TripManagementDTO.class))).thenReturn(tripDTO);

		mockMvc.perform(post("/api/trips").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(tripDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.tripManagementId").value(1L)).andExpect(jsonPath("$.status").value("PENDING"));

		verify(tripManagementService, times(1)).createTrip(any(TripManagementDTO.class));
	}

	@Test
	void startTrip_ShouldReturnUpdatedTrip() throws Exception {
		tripDTO.setStatus("IN_PROGRESS");
		when(tripManagementService.startTrip(eq(1L))).thenReturn(tripDTO);

		mockMvc.perform(put("/api/trips/1/start")).andExpect(status().isOk())
				.andExpect(jsonPath("$.tripManagementId").value(1L))
				.andExpect(jsonPath("$.status").value("IN_PROGRESS"));

		verify(tripManagementService, times(1)).startTrip(eq(1L));
	}

	@Test
	void stopTrip_ShouldReturnUpdatedTrip() throws Exception {
		tripDTO.setStatus("COMPLETED"); // ✅ Set expected value here
		when(tripManagementService.stopTrip(eq(1L))).thenReturn(tripDTO);

		mockMvc.perform(put("/api/trips/1/stop")) // ✅ Also ensure this is PUT not POST
				.andExpect(status().isOk()).andExpect(jsonPath("$.tripManagementId").value(1L))
				.andExpect(jsonPath("$.status").value("COMPLETED"));

		verify(tripManagementService, times(1)).stopTrip(eq(1L));
	}

	@Test
	void getTripSummary_ShouldReturnSummary() throws Exception {
		TripSummaryDTO summaryDTO = new TripSummaryDTO();
		summaryDTO.setTripId(1L);
		summaryDTO.setStatus("COMPLETED");
		summaryDTO.setDuration(7200L);
		summaryDTO.setDistance(150.0);

		when(tripManagementService.getTripSummary(eq(1L))).thenReturn(summaryDTO);

		mockMvc.perform(get("/api/trips/1/summary")).andExpect(status().isOk())
				.andExpect(jsonPath("$.tripId").value(1L)).andExpect(jsonPath("$.status").value("COMPLETED"))
				.andExpect(jsonPath("$.duration").value(7200)).andExpect(jsonPath("$.distance").value(150.0));

		verify(tripManagementService, times(1)).getTripSummary(eq(1L));
	}
}

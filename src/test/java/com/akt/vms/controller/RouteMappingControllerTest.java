package com.akt.vms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType; // Ensure this import is present
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.akt.vms.dto.RouteMappingDTO;
import com.akt.vms.entity.RouteMapping;
import com.akt.vms.mapper.RouteMappingMapper;
import com.akt.vms.service.RouteMappingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
public class RouteMappingControllerTest implements WebMvcConfigurer {

	@Mock
	private RouteMappingService routeMappingService;

	@InjectMocks
	private RouteMappingController routeMappingController;

	@Mock
	private RouteMappingMapper routeMappingMapper;
	private MockMvc mockMvc;
	private RouteMappingDTO routeMappingDTO;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule()); // Register module for Java 8 time types

		routeMappingDTO = new RouteMappingDTO();
		routeMappingDTO.setPoint_name("Point X");

		mockMvc = MockMvcBuilders.standaloneSetup(routeMappingController).build();
	}

	@Test
	void createRouteWithTripId_ShouldReturnCreatedRouteMapping() throws Exception {
		Long tripId = 1L;

		RouteMappingDTO outputDto = new RouteMappingDTO();
		outputDto.setPoint_name("Checkpoint A");

		// Mock service and mapper behavior
		when(routeMappingService.createRouteMapping(any(), eq(tripId))).thenReturn(new RouteMapping());
		when(routeMappingMapper.toDTO(any())).thenReturn(outputDto);

		// Prepare JSON payload
		String jsonPayload = """
				    {
				        "point_name": "Checkpoint A"
				    }
				""";

		// Corrected test method with proper imports and method usage
		mockMvc.perform(post("/api/routes/trip/{tripId}", tripId) // Corrected path
				.contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andExpect(status().isOk()) // Expect
																											// status OK
																											// (200)
				.andExpect(jsonPath("$.point_name").value("Checkpoint A")) // Assert the response content
				.andDo(print()); // Print the response for debugging
	}

	@Test
	void updateRouteMapping_ShouldReturnUpdatedRouteMapping() throws Exception {
		// Create an updated entity with "Updated Point"
		RouteMapping updatedEntity = new RouteMapping();
		updatedEntity.setPointName("Updated Point");

		// Mock the service method to return the updated entity
		when(routeMappingService.updateRouteMapping(eq(1L), any(RouteMappingDTO.class))).thenReturn(updatedEntity);

		// Mock the mapper to return the updated DTO
		RouteMappingDTO updatedDTO = new RouteMappingDTO();
		updatedDTO.setPoint_name("Updated Point");
		when(routeMappingMapper.toDTO(any(RouteMapping.class))).thenReturn(updatedDTO);

		// Perform the test request
		mockMvc.perform(put("/api/routes/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(routeMappingDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.point_name").value("Updated Point")); // Assert the updated value

		// Verify that the service method was called
		verify(routeMappingService, times(1)).updateRouteMapping(eq(1L), any(RouteMappingDTO.class));
	}

	@Test
	void deleteRouteMapping_ShouldReturnNoContent() throws Exception {
		doNothing().when(routeMappingService).deleteRouteMapping(eq(1L));

		mockMvc.perform(delete("/api/routes/1")).andExpect(status().isNoContent());

		verify(routeMappingService, times(1)).deleteRouteMapping(eq(1L));
	}
}

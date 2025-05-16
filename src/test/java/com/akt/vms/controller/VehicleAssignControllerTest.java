package com.akt.vms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

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

import com.akt.vms.dto.VehicleAssignDTO;
import com.akt.vms.service.VehicleAssignService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class VehicleAssignControllerTest implements WebMvcConfigurer{

	@Mock
	private VehicleAssignService vehicleAssignService;

	@InjectMocks
	private VehicleAssigncontroller vehicleAssigncontroller;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;
	private VehicleAssignDTO vehicleAssignDTO;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();

		vehicleAssignDTO = new VehicleAssignDTO();
		vehicleAssignDTO.setVehicleid(200L);
		vehicleAssignDTO.setDriver_id(100L);
		vehicleAssignDTO.setVehicleAssignId(1L);

		mockMvc = MockMvcBuilders.standaloneSetup(vehicleAssigncontroller).build();
	}

	
	@Test
	void createVehicleAssign_ShouldReturnCreatedAssignment() throws Exception {
	    VehicleAssignDTO dto = new VehicleAssignDTO();
	    dto.setVehicleAssignId(1L);
	    dto.setDriver_id(100L);
	    dto.setVehicleid(200L);

	    // Mock the service to return the DTO with the correct fields
	    when(vehicleAssignService.createVehicleAssign(any(VehicleAssignDTO.class))).thenReturn(dto);

	    // Perform the POST request
	    mockMvc.perform(post("/driver/vehicle-assign")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(dto)))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.vehicleAssignId").value(1L))
	            .andExpect(jsonPath("$.driver_id").value(100L))  
	            .andExpect(jsonPath("$.vehicleid").value(200L));

	    // Verify the service method was called
	    verify(vehicleAssignService, times(1)).createVehicleAssign(any(VehicleAssignDTO.class));
	}

	@Test
	void getAllVehicleAssigns_ShouldReturnList() throws Exception {
	    // Create mock DTO
	    VehicleAssignDTO dto = new VehicleAssignDTO();
	    dto.setDriver_id(100L); // Ensure these values are set correctly
	    dto.setVehicleid(200L);

	    // Mock service method to return a list containing the DTO
	    when(vehicleAssignService.getAllVehicleAssigns()).thenReturn(List.of(dto));

	    // Perform GET request and assert response
	    mockMvc.perform(get("/driver/vehicle-assign"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$[0].driver_id").value(100L)) // Adjust if necessary
	            .andExpect(jsonPath("$[0].vehicleid").value(200L));

	    // Verify service method was called once
	    verify(vehicleAssignService, times(1)).getAllVehicleAssigns();
	}

	@Test
	void getVehicleAssignById_ShouldReturnAssignment() throws Exception {
		when(vehicleAssignService.getVehicleAssignById(1L)).thenReturn(vehicleAssignDTO);

		mockMvc.perform(get("/driver/vehicle-assign/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.vehicleAssignId").value(1L)).andExpect(jsonPath("$.driver_id").value(100L))
				.andExpect(jsonPath("$.vehicleid").value(200L));

		verify(vehicleAssignService, times(1)).getVehicleAssignById(1L);
	}

	@Test
	void getVehicleAssignById_ShouldReturnNotFound() throws Exception {
		when(vehicleAssignService.getVehicleAssignById(999L)).thenReturn(null);

		mockMvc.perform(get("/driver/vehicle-assign/999")).andExpect(status().isNotFound());

		verify(vehicleAssignService, times(1)).getVehicleAssignById(999L);
	}

	@Test
	void deleteVehicleAssign_ShouldReturnNoContent() throws Exception {
		mockMvc.perform(delete("/driver/vehicle-assign/1")).andExpect(status().isNoContent());

		verify(vehicleAssignService, times(1)).deleteVehicleAssign(1L);
	}
}

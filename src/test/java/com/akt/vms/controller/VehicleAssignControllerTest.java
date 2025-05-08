package com.akt.vms.controller;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.awt.PageAttributes.MediaType;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.akt.vms.dto.VehicleAssignDTO;
import com.akt.vms.service.VehicleAssignService;

public class VehicleAssignControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Mock
	private VehicleAssignService vehicleAssignService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetAllVehicleAssigns() throws Exception {
		// Prepare mock data
		VehicleAssignDTO dto1 = new VehicleAssignDTO();
		dto1.setVehicleAssignId(1L);
		VehicleAssignDTO dto2 = new VehicleAssignDTO();
		dto2.setVehicleAssignId(2L);

		when(vehicleAssignService.getAllVehicleAssigns()).thenReturn(List.of(dto1, dto2));

		// Perform GET request
		mockMvc.perform(get("/driver/vehicle-assign")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].vehicleAssignId").value(1))
				.andExpect(jsonPath("$[1].vehicleAssignId").value(2));

		// Verify service method was called
		verify(vehicleAssignService, times(1)).getAllVehicleAssigns();
	}

	@Test
	public void testGetVehicleAssignById_Found() throws Exception {
		// Prepare mock data
		VehicleAssignDTO dto = new VehicleAssignDTO();
		dto.setVehicleAssignId(1L);

		when(vehicleAssignService.getVehicleAssignById(1L)).thenReturn(dto);

		// Perform GET request
		mockMvc.perform(get("/driver/vehicle-assign/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.vehicleAssignId").value(1));

		// Verify service method was called
		verify(vehicleAssignService, times(1)).getVehicleAssignById(1L);
	}

	@Test
	public void testGetVehicleAssignById_NotFound() throws Exception {
		// Mock not found
		when(vehicleAssignService.getVehicleAssignById(999L)).thenReturn(null);

		// Perform GET request
		mockMvc.perform(get("/driver/vehicle-assign/999")).andExpect(status().isNotFound());

		// Verify service method was called
		verify(vehicleAssignService, times(1)).getVehicleAssignById(999L);
	}

	@Test
	public void testDeleteVehicleAssign() throws Exception {
		// Perform DELETE request
		mockMvc.perform(delete("/driver/vehicle-assign/1")).andExpect(status().isNoContent());

		// Verify service method was called
		verify(vehicleAssignService, times(1)).deleteVehicleAssign(1L);
	}
}

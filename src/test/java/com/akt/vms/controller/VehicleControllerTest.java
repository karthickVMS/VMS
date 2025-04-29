package com.akt.vms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.akt.vms.dto.VehicleDTO;
import com.akt.vms.entity.Vehicle;
import com.akt.vms.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class VehicleControllerTest implements WebMvcConfigurer{

	@Mock
	private VehicleService vehicleService;

	@InjectMocks
	private VehicleController vehicleController;

	private MockMvc mockMvc;
	private VehicleDTO vehicleDTO;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();

		vehicleDTO = new VehicleDTO();
		vehicleDTO.setVehicleNumber("ABC1234");
		vehicleDTO.setModel("Model X");
		vehicleDTO.setFuelType("Petrol");
		vehicleDTO.setInsurancePolicyNumber("INS123456");
		vehicleDTO.setYearOfManufacturer(2020);
		
		// Configure MockMvc with PageableHandlerMethodArgumentResolver
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
	}

	/*
	 * Test case for creating a vehicle through the controller and verifying that
	 * the response returns the correct VehicleDTO.
	 * 
	 * - Mocks the vehicleService.createVehicle() method to return a vehicleDTO. -
	 * Performs a POST request to the /vehicles endpoint with the vehicleDTO as the
	 * request body. - Verifies the response status is OK and checks that the
	 * vehicleNumber and model are returned in the response. - Verifies that the
	 * createVehicle method of vehicleService was called once.
	 */
	@Test
	void createVehicle_ShouldReturnCreatedVehicleDTO() throws Exception {
		when(vehicleService.createVehicle(any(VehicleDTO.class))).thenReturn(vehicleDTO);

		mockMvc.perform(post("/api/vehicles").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(vehicleDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.vehicleNumber").value("ABC1234")).andExpect(jsonPath("$.model").value("Model X"))
				.andDo(print());

		verify(vehicleService, times(1)).createVehicle(any(VehicleDTO.class));
	}

	/*
	 * Test case for retrieving all vehicles through the controller and verifying
	 * that the response returns a list of VehicleDTOs.
	 * 
	 * - Mocks the vehicleService.getAllvehicles() method to return a list of
	 * vehicleDTOs. - Performs a GET request to the /vehicles endpoint to fetch all
	 * vehicles. - Verifies the response status is OK and checks that the list has
	 * one vehicle with the expected vehicleNumber. - Verifies that the
	 * getAllvehicles method of vehicleService was called once.
	 */
	@Test
	void getAllVehicles_ShouldReturnListOfVehicleDTOs() throws Exception {
		List<VehicleDTO> vehicles = Arrays.asList(vehicleDTO);
		when(vehicleService.getAllvehicles()).thenReturn(vehicles);

		mockMvc.perform(get("/api/vehicles").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(1)).andExpect(jsonPath("$[0].vehicleNumber").value("ABC1234"))
				.andDo(print());

		verify(vehicleService, times(1)).getAllvehicles();
	}
	@Test
	void deleteVehicle_ShouldReturnNoContent() throws Exception {
	    doNothing().when(vehicleService).deletevehicle(1L);

	    mockMvc.perform(delete("/api/vehicles/1"))
	           .andExpect(status().isNoContent())
	           .andDo(print());

	    verify(vehicleService, times(1)).deletevehicle(1L);
	}
	@Test
	void getVehicleById_ShouldReturnVehicleDTO_WhenVehicleExists() throws Exception {
	    when(vehicleService.getVehicleById(1L)).thenReturn(vehicleDTO);

	    mockMvc.perform(get("/api/vehicles/1").contentType(MediaType.APPLICATION_JSON))
	           .andExpect(status().isOk())
	           .andExpect(jsonPath("$.vehicleNumber").value("ABC1234"))
	           .andExpect(jsonPath("$.model").value("Model X"))
	           .andDo(print());

	    verify(vehicleService, times(1)).getVehicleById(1L);
	}
	@Test
	void searchVehicles_ShouldReturnListOfMatchingVehicles() throws Exception {
	    List<Vehicle> vehicles = Arrays.asList(new Vehicle());
	    when(vehicleService.searchVehicles(any(VehicleDTO.class))).thenReturn(vehicles);

	    mockMvc.perform(post("/api/vehicles/search")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(vehicleDTO)))
	           .andExpect(status().isOk())
	           .andExpect(jsonPath("$.length()").value(1))
	           .andDo(print());

	    verify(vehicleService, times(1)).searchVehicles(any(VehicleDTO.class));
	}
	@Test
	void updateSelectedFields_ShouldReturnSuccessMessage_WhenVehicleIsUpdated() throws Exception {
	    when(vehicleService.updateVehicleFields(1L, "Model S", "Electric")).thenReturn(true);

	    mockMvc.perform(put("/api/vehicles/1/custom-query-update")
	            .param("model", "Model S")
	            .param("fuelType", "Electric"))
	           .andExpect(status().isOk())
	           .andExpect(content().string("Vehicle updated successfully"))
	           .andDo(print());

	    verify(vehicleService, times(1)).updateVehicleFields(1L, "Model S", "Electric");
	}
}

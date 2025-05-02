package com.akt.vms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.akt.vms.dto.DriverDTO;
import com.akt.vms.service.DriverService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class DriverControllerTest implements WebMvcConfigurer {

	@Mock
	private DriverService driverService;

	@InjectMocks
	private DriverController driverController;

	private MockMvc mockMvc;
	private DriverDTO driverDTO;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {

		objectMapper = new ObjectMapper();
		driverDTO = new DriverDTO();
		driverDTO.setId(1L);
		driverDTO.setName("Test Name");
		driverDTO.setLicenseNum("Test License Number");
		driverDTO.setVehicleNum("Test Vehicle Number");
		driverDTO.setContactNum("Test Contact Number");
		driverDTO.setYearsOfExp(100);
		driverDTO.setState("Test State");

		// Configure MockMvc with PageableHandlerMethodArgumentResolver
		mockMvc = MockMvcBuilders.standaloneSetup(driverController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
	}

	@Test
	void createDriver_ShouldReturnCreatedDriver() throws Exception {
		when(driverService.createDriver(any(DriverDTO.class))).thenReturn(driverDTO);

		mockMvc.perform(post("/driver").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(driverDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("Test Name"))
				.andExpect(jsonPath("$.licenseNum").value("Test License Number"))
				.andExpect(jsonPath("$.vehicleNum").value("Test Vehicle Number"))
				.andExpect(jsonPath("$.contactNum").value("Test Contact Number"))
				.andExpect(jsonPath("$.yearsOfExp").value(100)).andExpect(jsonPath("$.state").value("Test State"));

		verify(driverService, times(1)).createDriver(any(DriverDTO.class));
	}

	@Test
	void updateDriver_ShouldReturnUpdatedDriver() throws Exception {
		when(driverService.updateDriver(eq(1L), any(DriverDTO.class))).thenReturn(driverDTO);

		DriverDTO requestDTO = new DriverDTO();
		requestDTO.setName("Test Name");
		requestDTO.setLicenseNum("Test License Number");
		requestDTO.setVehicleNum("Test Vehicle Number");
		requestDTO.setContactNum("Test Contact Number");
		requestDTO.setYearsOfExp(100);
		requestDTO.setState("Test State");

		mockMvc.perform(put("/driver/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("Test Name"))
				.andExpect(jsonPath("$.licenseNum").value("Test License Number"))
				.andExpect(jsonPath("$.vehicleNum").value("Test Vehicle Number"))
				.andExpect(jsonPath("$.contactNum").value("Test Contact Number"))
				.andExpect(jsonPath("$.yearsOfExp").value(100)).andExpect(jsonPath("$.state").value("Test State"));

		verify(driverService, times(1)).updateDriver(eq(1L), any(DriverDTO.class));
	}

	@Test
	void deleteDriver_ShouldReturnOk() throws Exception {
		when(driverService.deleteDriver(1L)).thenReturn(true);

		mockMvc.perform(delete("/driver/1")).andExpect(status().isNoContent()); // ✅ Expect 204

		verify(driverService, times(1)).deleteDriver(1L);
	}

	@Test
	void deleteDriver_ShouldReturnNoContent() throws Exception {
		doNothing().when(driverService).deleteDriver(1L);

		mockMvc.perform(delete("/driver/1")).andExpect(status().isNoContent());

		verify(driverService, times(1)).deleteDriver(1L);
	}

	@Test
	void getDriverById_ShouldReturnDriver() throws Exception {
		when(driverService.getDriverById(1L)).thenReturn(driverDTO);

		mockMvc.perform(get("/driver/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.name").value("Test Name"))
				.andExpect(jsonPath("$.licenseNum").value("Test License Number"))
				.andExpect(jsonPath("$.vehicleNum").value("Test Vehicle Number"))
				.andExpect(jsonPath("$.contactNum").value("Test Contact Number"))
				.andExpect(jsonPath("$.yearsOfExp").value(100)).andExpect(jsonPath("$.state").value("Test State"));

		verify(driverService, times(1)).getDriverById(1L);
	}

	@Test
	void getAllDrivers_ShouldReturnDriverList() throws Exception {
		List<DriverDTO> drivers = Arrays.asList(driverDTO);
		when(driverService.getAllDrivers()).thenReturn(drivers);

		mockMvc.perform(get("/driver")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].name").value("Test Name"))
				.andExpect(jsonPath("$[0].licenseNum").value("Test License Number"))
				.andExpect(jsonPath("$[0].vehicleNum").value("Test Vehicle Number"))
				.andExpect(jsonPath("$[0].contactNum").value("Test Contact Number"))
				.andExpect(jsonPath("$[0].yearsOfExp").value(100))
				.andExpect(jsonPath("$[0].state").value("Test State"));

		verify(driverService, times(1)).getAllDrivers();
	}
}
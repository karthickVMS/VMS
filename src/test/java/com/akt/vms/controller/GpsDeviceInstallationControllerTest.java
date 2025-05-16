package com.akt.vms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.akt.vms.dto.GpsDeviceInstallationDTO;
import com.akt.vms.dto.VehicleDTO;
import com.akt.vms.service.DeviceLocationService;
import com.akt.vms.service.GpsDeviceInstallationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class GpsDeviceInstallationControllerTest implements WebMvcConfigurer {

	@Mock
	private GpsDeviceInstallationService service; // Mock service layer

	@InjectMocks
	private GpsDeviceInstallationController gpsDeviceInstallationController;
	private MockMvc mockMvc;
	private GpsDeviceInstallationDTO dto;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		objectMapper = new ObjectMapper();
		dto = new GpsDeviceInstallationDTO();
		dto.setDevice_id(1L);
		VehicleDTO vehicleDTO = new VehicleDTO();
		vehicleDTO.setId(100L);
		dto.setVehicle(vehicleDTO);
		dto.setInstallation_person("John Doe");
		dto.setInstallation_date(LocalDateTime.now());
		dto.setDevice_status("ACTIVE");
		dto.setSignal_strength(80.5);
		dto.setLatitude(12.9716);
		dto.setLongitude(77.5946);
		dto.setRemarks("Installed correctly.");

		mockMvc = MockMvcBuilders.standaloneSetup(gpsDeviceInstallationController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
	}

	@Test
	void getDevice_ShouldReturnDevice_WhenExists() throws Exception {
		when(service.getDeviceById(1L)).thenReturn(dto); // Mock service behavior

		mockMvc.perform(MockMvcRequestBuilders.get("/api/gps-devices/1")).andExpect(status().isOk()) // Check for OK
																										// status
				.andExpect(jsonPath("$.device_id").value(1L)) // Check device_id
				.andExpect(jsonPath("$.installation_person").value("John Doe")) // Check installation_person
				.andExpect(jsonPath("$.device_status").value("ACTIVE")); // Check device status

		verify(service, times(1)).getDeviceById(1L); // Verify service method was called
	}

	@Test
	void installDevice_ShouldReturnCreatedDevice() throws Exception {
		when(service.installDevice(any())).thenReturn(dto); // Mock service behavior

		mockMvc.perform(MockMvcRequestBuilders.post("/api/gps-devices/install").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))) // Send DTO as JSON body
				.andExpect(status().isCreated()) // Expect 201 Created
				.andExpect(jsonPath("$.device_id").value(1L)) // Check device_id
				.andExpect(jsonPath("$.vehicle.id").value(100L)) // Check vehicle id
				.andExpect(jsonPath("$.installation_person").value("John Doe")); // Check installation_person

		verify(service, times(1)).installDevice(any()); // Verify service method was called
	}

	@Test
	void installDevice_ShouldReturnBadRequest_WhenDtoIsInvalid() throws Exception {
		// Modify DTO to be invalid (e.g., a required field is missing or invalid)
		dto.setInstallation_person(""); // Assuming this field is required and cannot be empty

		mockMvc.perform(MockMvcRequestBuilders.post("/api/gps-devices/install").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))) // Send invalid DTO as JSON body
				.andExpect(status().isBadRequest()) // Expect 400 Bad Request
				.andExpect(jsonPath("$.installation_person").value("must not be empty")); // Check validation error
																							// message
	}
}

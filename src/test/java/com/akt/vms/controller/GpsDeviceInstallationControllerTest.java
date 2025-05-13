package com.akt.vms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.akt.vms.dto.GpsDeviceInstallationDTO;
import com.akt.vms.dto.VehicleDTO;
import com.akt.vms.service.GpsDeviceInstallationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GpsDeviceInstallationController.class)
public class GpsDeviceInstallationControllerTest implements WebMvcConfigurer {

	@Mock
    private GpsDeviceInstallationService service;
	
	@InjectMocks
	private GpsDeviceInstallationController gpsDeviceInstallationController;
	
	@Mock
    private MockMvc mockMvc;
	private GpsDeviceInstallationDTO gpsDeviceInstallationDTO;
    private ObjectMapper objectMapper;
    private GpsDeviceInstallationDTO dto;

    @BeforeEach
    void setup() {
        dto = new GpsDeviceInstallationDTO();
        dto.setDevice_id(1L);
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(100L);
        dto.setVehicle(vehicleDTO);
        dto.setInstallation_person("John Doe");
        dto.setInstallation_date(LocalDateTime.now());
        dto.setDevice_status(GpsDeviceInstallationDTO.Device_status.ACTIVE);
        dto.setSignal_strength(80.5);
        dto.setLatitude(12.9716);
        dto.setLongitude(77.5946);
        dto.setRemarks("Installed correctly.");
    }

    @Test
    void getDevice_ShouldReturnDevice_WhenExists() throws Exception {
        when(service.getDeviceById(1L)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/gps-devices/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.device_id").value(1L))
            .andExpect(jsonPath("$.installation_person").value("John Doe"))
            .andExpect(jsonPath("$.device_status").value("ACTIVE"));

        verify(service, times(1)).getDeviceById(1L);
    }

    @Test
    void installDevice_ShouldReturnCreatedDevice() throws Exception {
        when(service.installDevice(any())).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/gps-devices/install")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.device_id").value(1L))
            .andExpect(jsonPath("$.vehicle.id").value(100L))
            .andExpect(jsonPath("$.installation_person").value("John Doe"));

        verify(service, times(1)).installDevice(any());
    }

}

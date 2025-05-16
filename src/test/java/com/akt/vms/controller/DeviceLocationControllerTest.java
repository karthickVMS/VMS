package com.akt.vms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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

import com.akt.vms.dto.DeviceLocationDTO;
import com.akt.vms.dto.DeviceLocationResponseDTO;
import com.akt.vms.dto.GpsDeviceInstallationDTO;
import com.akt.vms.entity.DeviceLocation;
import com.akt.vms.service.DeviceLocationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class DeviceLocationControllerTest implements WebMvcConfigurer {

    @Mock
    private DeviceLocationService deviceLocationService;

    @InjectMocks
    private DeviceLocationController deviceLocationController;

    private MockMvc mockMvc;
    private DeviceLocationDTO deviceLocationDTO;
    private ObjectMapper objectMapper;
    private DeviceLocation deviceLocation;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Setting up DeviceLocationDTO
        deviceLocationDTO = new DeviceLocationDTO();
        deviceLocationDTO.setDevice_id(1L);
        deviceLocationDTO.setLatitude(12.34);
        deviceLocationDTO.setLongitude(56.78);

        // Setting up DeviceLocation entity (mocked return value)
        deviceLocation = new DeviceLocation();
        deviceLocation.setId(1L);
        deviceLocation.setLatitude(12.34);
        deviceLocation.setLongitude(56.78);

        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(deviceLocationController).build();
    }

    @Test
    void recordLocation_ShouldReturnCreatedLocation() throws Exception {
        when(deviceLocationService.saveDeviceLocation(any(DeviceLocationDTO.class))).thenReturn(deviceLocation);

        mockMvc.perform(post("/api/location/device")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceLocationDTO)))
                .andExpect(status().isCreated())
                // Check if the response body contains a substring like "Location recorded with ID"
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.containsString("Location recorded with ID")));

        verify(deviceLocationService, times(1)).saveDeviceLocation(any(DeviceLocationDTO.class));
    }


    @Test
    void recordLocation_ShouldReturnNotFound_WhenInvalidData() throws Exception {
        when(deviceLocationService.saveDeviceLocation(any(DeviceLocationDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid device ID"));

        mockMvc.perform(post("/api/location/device")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceLocationDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Invalid device ID"));

        verify(deviceLocationService, times(1)).saveDeviceLocation(any(DeviceLocationDTO.class));
    }

    @Test
    void recordLocation_ShouldReturnInternalError_WhenServiceFails() throws Exception {
        when(deviceLocationService.saveDeviceLocation(any(DeviceLocationDTO.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/location/device")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceLocationDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("Failed to record location"));

        verify(deviceLocationService, times(1)).saveDeviceLocation(any(DeviceLocationDTO.class));
    }

    @Test
    void getLocationsByDevice_ShouldReturnLocations() throws Exception {
        DeviceLocationResponseDTO mockResponse = new DeviceLocationResponseDTO();
        mockResponse.setDevice(new GpsDeviceInstallationDTO()); // Mock device DTO
        List<DeviceLocationDTO> mockLocations = new ArrayList<>();
        DeviceLocationDTO location = new DeviceLocationDTO();
        location.setLatitude(12.34);
        location.setLongitude(56.78);
        mockLocations.add(location);
        mockResponse.setLocations(mockLocations);

        when(deviceLocationService.getLocationsForDevice(eq(1L))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/location/device/1/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locations[0].latitude").value(12.34))
                .andExpect(jsonPath("$.locations[0].longitude").value(56.78));

        verify(deviceLocationService, times(1)).getLocationsForDevice(eq(1L));
    }

    @Test
    void getLocationsByDevice_ShouldReturnNotFound_WhenNoLocations() throws Exception {
        when(deviceLocationService.getLocationsForDevice(eq(1L))).thenReturn(null);

        mockMvc.perform(get("/api/location/device/1/locations"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());

        verify(deviceLocationService, times(1)).getLocationsForDevice(eq(1L));
    }

    @Test
    void getLocationsByDevice_ShouldReturnInternalError_WhenServiceFails() throws Exception {
        when(deviceLocationService.getLocationsForDevice(eq(1L))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/location/device/1/locations"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").doesNotExist());

        verify(deviceLocationService, times(1)).getLocationsForDevice(eq(1L));
    }
}

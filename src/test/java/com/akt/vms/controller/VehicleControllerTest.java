package com.akt.vms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import com.akt.vms.dto.VehicleDTO;
import com.akt.vms.entity.Vehicle;
import com.akt.vms.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;

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

/**
 * Unit tests for {@link VehicleController}.
 * Uses Mockito and MockMvc to simulate HTTP requests and validate controller behavior.
 */
@ExtendWith(MockitoExtension.class)
public class VehicleControllerTest implements WebMvcConfigurer {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    private MockMvc mockMvc;
    private VehicleDTO vehicleDTO;
    private ObjectMapper objectMapper;

    /**
     * Sets up test data and configures MockMvc.
     */
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        vehicleDTO = new VehicleDTO();
        vehicleDTO.setVehicleNumber("ABC1234");
        vehicleDTO.setModel("Model X");
        vehicleDTO.setFuelType("Petrol");
        vehicleDTO.setInsurancePolicyNumber("INS123456");
        vehicleDTO.setYearOfManufacturer(2020);

        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    /**
     * Test for creating a vehicle.
     * - Simulates POST /api/vehicles
     * - Mocks vehicleService.createVehicle()
     * - Verifies status 200 OK and response body
     */
    @Test
    void createVehicle_ShouldReturnCreatedVehicleDTO() throws Exception {
        when(vehicleService.createVehicle(any(VehicleDTO.class))).thenReturn(vehicleDTO);

        mockMvc.perform(post("/api/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleNumber").value("ABC1234"))
                .andExpect(jsonPath("$.model").value("Model X"));

        verify(vehicleService, times(1)).createVehicle(any(VehicleDTO.class));
    }

    /**
     * Test for retrieving all vehicles.
     * - Simulates GET /api/vehicles
     * - Mocks vehicleService.getAllvehicles()
     * - Verifies list size and content
     */
    @Test
    void getAllVehicles_ShouldReturnListOfVehicleDTOs() throws Exception {
        List<VehicleDTO> vehicles = Arrays.asList(vehicleDTO);
        when(vehicleService.getAllvehicles()).thenReturn(vehicles);

        mockMvc.perform(get("/api/vehicles").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].vehicleNumber").value("ABC1234"));

        verify(vehicleService, times(1)).getAllvehicles();
    }

    /**
     * Test for retrieving a vehicle by ID (success case).
     * - Simulates GET /api/vehicles/{id}
     * - Verifies status 200 OK and response content
     */
    @Test
    void getVehicleById_ShouldReturnVehicleDTO_WhenVehicleExists() throws Exception {
        when(vehicleService.getVehicleById(1L)).thenReturn(vehicleDTO);

        mockMvc.perform(get("/api/vehicles/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleNumber").value("ABC1234"))
                .andExpect(jsonPath("$.model").value("Model X"));

        verify(vehicleService, times(1)).getVehicleById(1L);
    }

    /**
     * Test for retrieving a vehicle by ID (not found case).
     * - Simulates GET /api/vehicles/{id}
     * - Verifies status 404 Not Found
     */
    @Test
    void testGetVehicleById_NotFound() throws Exception {
        when(vehicleService.getVehicleById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/vehicles/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test for deleting a vehicle by ID.
     * - Simulates DELETE /api/vehicles/{id}
     * - Verifies status 204 No Content
     */
    @Test
    void deleteVehicle_ShouldReturnNoContent() throws Exception {
        doNothing().when(vehicleService).deletevehicle(1L);

        mockMvc.perform(delete("/api/vehicles/1"))
                .andExpect(status().isNoContent());

        verify(vehicleService, times(1)).deletevehicle(1L);
    }

    /**
     * Test for searching vehicles based on filters.
     * - Simulates POST /api/vehicles/search
     * - Verifies status 200 OK and returned list
     */
    @Test
    void searchVehicles_ShouldReturnListOfMatchingVehicles() throws Exception {
        List<Vehicle> vehicles = Arrays.asList(new Vehicle());
        when(vehicleService.searchVehicles(any(VehicleDTO.class))).thenReturn(vehicles);

        mockMvc.perform(post("/api/vehicles/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(vehicleService, times(1)).searchVehicles(any(VehicleDTO.class));
    }

    /**
     * Test for updating model and fuelType via custom JPQL update (success case).
     * - Simulates PUT /api/vehicles/{id}/custom-query-update
     * - Verifies status 200 OK and success message
     */
    @Test
    void updateSelectedFields_ShouldReturnSuccessMessage_WhenVehicleIsUpdated() throws Exception {
        when(vehicleService.updateVehicleFields(1L, "Model S", "Electric")).thenReturn(true);

        mockMvc.perform(put("/api/vehicles/1/custom-query-update")
                .param("model", "Model S")
                .param("fuelType", "Electric"))
                .andExpect(status().isOk())
                .andExpect(content().string("Vehicle updated successfully"));

        verify(vehicleService, times(1)).updateVehicleFields(1L, "Model S", "Electric");
    }

    /**
     * Test for updating model and fuelType via custom JPQL update (failure case).
     * - Simulates PUT /api/vehicles/{id}/custom-query-update
     * - Verifies status 200 OK and error message
     */
    @Test
    void testUpdateSelectedFields_Failure() throws Exception {
        when(vehicleService.updateVehicleFields(1L, "ModelX", "Electric")).thenReturn(false);

        mockMvc.perform(put("/api/vehicles/1/custom-query-update")
                .param("model", "ModelX")
                .param("fuelType", "Electric"))
                .andExpect(status().isOk())
                .andExpect(content().string("vehicle id not found"));

        verify(vehicleService, times(1)).updateVehicleFields(1L, "ModelX", "Electric");
    }
}

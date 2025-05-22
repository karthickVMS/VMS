package com.akt.vms.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
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

import com.akt.vms.dto.FuelFillingDTO;
import com.akt.vms.dto.FuelMonthlyReportDTO;
import com.akt.vms.dto.FuelUsageReportDTO;
import com.akt.vms.service.FuelAlertService;
import com.akt.vms.service.FuelFillingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
public class FuelFillingControllerTest implements WebMvcConfigurer {

	@Mock
	private FuelFillingService fuelFillingService;

	@Mock
	private FuelAlertService fuelAlertService;

	@InjectMocks
	private FuelFillingController fuelFillingController;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;
	private FuelFillingDTO dto;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		dto = new FuelFillingDTO();
		dto.setVehicle_id(1L);
		dto.setFuel_type("Diesel");
		dto.setFuel_quantity(40.0);
		dto.setCost(4000.0);
		dto.setRefuel_date_time(LocalDateTime.of(2025, 5, 16, 10, 0));
		dto.setLocation("Bharat Petroleum");
		dto.setOdometer_reading(80500);

		mockMvc = MockMvcBuilders.standaloneSetup(fuelFillingController).build();
	}

	@Test
	void logFuel_ShouldReturnSuccessMessage() throws Exception {
		mockMvc.perform(post("/api/fuel/log").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk())
				.andExpect(content().string("Fuel entry logged successfully."));

		verify(fuelFillingService, times(1)).logFuelEntry(any(FuelFillingDTO.class));
	}

	@Test
	void getFuelEntries_ShouldReturnFuelList() throws Exception {
		when(fuelFillingService.getFuelEntriesByVehicle(1L)).thenReturn(List.of(dto));

		mockMvc.perform(get("/api/fuel/vehicle/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].vehicle_id", is(1))).andExpect(jsonPath("$[0].fuel_type", is("Diesel")))
				.andExpect(jsonPath("$[0].fuel_quantity", is(40.0))).andExpect(jsonPath("$[0].cost", is(4000.0)))
				.andExpect(jsonPath("$[0].location", is("Bharat Petroleum")))
				.andExpect(jsonPath("$[0].odometer_reading", is(80500)));

		verify(fuelFillingService, times(1)).getFuelEntriesByVehicle(1L);
	}

	@Test
	void getFuelEfficiency_ShouldReturnCorrectValue() throws Exception {
		when(fuelFillingService.calculateFuelEfficiency(1L)).thenReturn(10.5);

		mockMvc.perform(get("/api/fuel/efficiency/1")).andExpect(status().isOk()).andExpect(content().string("10.5"));

		verify(fuelFillingService, times(1)).calculateFuelEfficiency(1L);
	}

	@Test
	void checkFuelAlerts_ShouldReturnAlertsMap() throws Exception {
		when(fuelAlertService.checkFuelLevels(2L)).thenReturn("Fuel level is normal.");
		when(fuelAlertService.checkFuelEfficiency(2L)).thenReturn("Fuel efficiency is within acceptable limits.");
		when(fuelAlertService.checkHighFuelConsumption(2L)).thenReturn("Fuel consumption is within normal range.");

		mockMvc.perform(post("/api/fuel/alerts/2")).andExpect(status().isOk())
				.andExpect(jsonPath("$.fuelLevel").value("Fuel level is normal."))
				.andExpect(jsonPath("$.fuelEfficiency").value("Fuel efficiency is within acceptable limits."))
				.andExpect(jsonPath("$.fuelConsumption").value("Fuel consumption is within normal range."));

		verify(fuelAlertService, times(1)).checkFuelLevels(2L);
		verify(fuelAlertService, times(1)).checkFuelEfficiency(2L);
		verify(fuelAlertService, times(1)).checkHighFuelConsumption(2L);
	}

	@Test
	void getFuelReport_ShouldReturnUsageReport() throws Exception {
		FuelUsageReportDTO report = new FuelUsageReportDTO();
		report.setVehicle_id(1L);
		report.setTotal_cost(3000.0);
		report.setTotal_fuel_used(150.0);
		report.setDistanc_traveled(1200);
		report.setEfficiency(8.0);

		when(fuelFillingService.generateFuelUsageReport(any(Long.class), any(LocalDateTime.class),
				any(LocalDateTime.class))).thenReturn(report);

		mockMvc.perform(
				get("/api/fuel/report/1").param("from", "2025-05-01T00:00:00").param("to", "2025-05-31T23:59:59"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.vehicle_id", is(1)))
				.andExpect(jsonPath("$.total_cost", is(3000.0)))
				.andExpect(jsonPath("$.total_fuel_used", is(150.0)))
				.andExpect(jsonPath("$.distanc_traveled", is(1200)))
				.andExpect(jsonPath("$.efficiency", is(8.0)));
	}

	@Test
	void getMonthlyFuelReport_ShouldReturnList() throws Exception {
		FuelMonthlyReportDTO monthlyReport = new FuelMonthlyReportDTO();
		monthlyReport.setMonth("May");
		monthlyReport.setTotal_fuel_used(100.0);
		monthlyReport.setTotal_cost(9500.0);
		monthlyReport.setFuel_efficiency(9.5);

		when(fuelFillingService.getMonthlyReport(1L)).thenReturn(List.of(monthlyReport));

		mockMvc.perform(get("/api/fuel/report/monthly/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].month", is("May")))
				.andExpect(jsonPath("$[0].total_fuel_used", is(100.0)))
				.andExpect(jsonPath("$[0].total_cost", is(9500.0)))
				.andExpect(jsonPath("$[0].fuel_efficiency", is(9.5)));
	}

	@Test
	void downloadMonthlyFuelReportCSV_ShouldReturnSuccess() throws Exception {
		// This is a basic test to check response headers — actual content test may
		// require integration testing.
		mockMvc.perform(get("/api/fuel/report/monthly/csv/1")).andExpect(status().isOk())
				.andExpect(content().contentType("text/csv")).andExpect(content().string("")); // Mock doesn't write
																								// content here
	}
}

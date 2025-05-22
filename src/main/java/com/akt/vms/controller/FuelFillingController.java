package com.akt.vms.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akt.vms.dto.FuelFillingDTO;
import com.akt.vms.dto.FuelMonthlyReportDTO;
import com.akt.vms.dto.FuelUsageReportDTO;
import com.akt.vms.service.FuelAlertService;
import com.akt.vms.service.FuelFillingService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/fuel")
public class FuelFillingController {

	@Autowired
	private FuelFillingService service;

	@Autowired
	private FuelAlertService fuelAlertService;

	@PostMapping("/log")
	public String logFuel(@RequestBody FuelFillingDTO dto) {
		service.logFuelEntry(dto);
		return "Fuel entry logged successfully.";
	}

	@GetMapping("/vehicle/{vehicleId}")
	public List<FuelFillingDTO> getFuelEntries(@PathVariable Long vehicleId) {
		return service.getFuelEntriesByVehicle(vehicleId);
	}

	@GetMapping("/efficiency/{vehicleId}")
	public double getFuelEfficiency(@PathVariable Long vehicleId) {
		return service.calculateFuelEfficiency(vehicleId);
	}

	@PostMapping("/alerts/{vehicleId}")
	public ResponseEntity<Map<String, String>> checkFuelAlerts(@PathVariable Long vehicleId) {
		Map<String, String> result = new HashMap<>();
		result.put("fuelLevel", fuelAlertService.checkFuelLevels(vehicleId));
		result.put("fuelEfficiency", fuelAlertService.checkFuelEfficiency(vehicleId));
		result.put("fuelConsumption", fuelAlertService.checkHighFuelConsumption(vehicleId));

		return ResponseEntity.ok(result);
	}

	@GetMapping("/report/{vehicleId}")
	public ResponseEntity<FuelUsageReportDTO> getFuelReport(@PathVariable Long vehicleId,
			@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

		FuelUsageReportDTO report = service.generateFuelUsageReport(vehicleId, from, to);
		return ResponseEntity.ok(report);
	}

	@GetMapping("/report/monthly/{vehicleId}")
	public List<FuelMonthlyReportDTO> getMonthlyFuelReport(@PathVariable Long vehicleId) {
		return service.getMonthlyReport(vehicleId);
	}
	
	@GetMapping("/report/monthly/csv/{vehicleId}")
	public void downloadMonthlyFuelReportCSV(@PathVariable Long vehicleId, HttpServletResponse response)
	        throws IOException {

	    // Set response type and headers for CSV download
	    response.setContentType("text/csv");
	    response.setHeader("Content-Disposition", "attachment; filename=\"monthly-fuel-report.csv\"");

	    // Delegate to service to write CSV content directly to response
	    service.writeMonthlyFuelReportCSV(vehicleId, response.getWriter());
	}
}

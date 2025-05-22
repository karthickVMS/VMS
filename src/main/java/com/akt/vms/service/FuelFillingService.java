package com.akt.vms.service;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akt.vms.dto.FuelFillingDTO;
import com.akt.vms.dto.FuelMonthlyReportDTO;
import com.akt.vms.dto.FuelUsageReportDTO;
import com.akt.vms.entity.FuelFilling;
import com.akt.vms.entity.Vehicle;
import com.akt.vms.mapper.FuelFillingMapper;
import com.akt.vms.mapper.FuelMonthlyReportMapper;
import com.akt.vms.mapper.FuelReportMapper;
import com.akt.vms.repository.FuelFillingRepository;
import com.akt.vms.repository.VehicleRepository;

@Service
public class FuelFillingService {

	@Autowired
	private FuelFillingRepository fuelRepo;

	@Autowired
	private VehicleRepository vehicleRepo;

	/**
	 * Logs a new fuel entry for a vehicle using data from the provided DTO
	 *
	 * @param dto Data transfer object containing fuel filling details
	 */

	public void logFuelEntry(FuelFillingDTO dto) {
		Vehicle vehicle = vehicleRepo.findById(dto.getVehicle_id())
				.orElseThrow(() -> new RuntimeException("Vehicle not found"));
		FuelFilling entry = FuelFillingMapper.toEntity(dto, vehicle);
		fuelRepo.save(entry);
	}

	/**
	 * Retrieves a list of fuel entries for a specific vehicle
	 *
	 * @param vehicleId ID of the vehicle
	 * @return List of FuelFillingDTO objects
	 */

	public List<FuelFillingDTO> getFuelEntriesByVehicle(Long vehicleId) {
		return fuelRepo.findByVehicleId(vehicleId).stream().map(FuelFillingMapper::toDTO).collect(Collectors.toList());
	}

	/**
	 * Calculates the fuel efficiency for a vehicle based on two fuel entries
	 *
	 * @param vehicleId ID of the vehicle
	 * @return Fuel efficiency (distance traveled per unit of fuel)
	 */

	public double calculateFuelEfficiency(Long vehicleId) {
		List<FuelFilling> fuelFillings = fuelRepo.findByVehicleId(vehicleId);

		// Ensure there are at least two refueling events
		if (fuelFillings.size() < 2) {
			throw new IllegalArgumentException("Not enough data to calculate fuel efficiency.");
		}

		// Sorting the fuel filling records based on refuel date in descending order
		fuelFillings.sort(Comparator.comparing(FuelFilling::getRefuelDateTime).reversed());

		// Get the first and second fuel fillings after sorting in descending order
		FuelFilling first = fuelFillings.get(0); // Most recent refuel
		FuelFilling second = fuelFillings.get(1); // Second most recent refuel

		double distanceTraveled = first.getOdometerReading() - second.getOdometerReading();

		// Calculate the total fuel consumed between the first and second refueling
		double totalFuelConsumed = first.getFuelQuantity();

		// Check for invalid values
		if (distanceTraveled <= 0 || totalFuelConsumed <= 0) {
			throw new IllegalArgumentException("Invalid odometer readings or fuel data.");
		}

		// Calculate and return the fuel efficiency
		return distanceTraveled / totalFuelConsumed;
	}

	public FuelUsageReportDTO generateFuelUsageReport(Long vehicleId, LocalDateTime from, LocalDateTime to) {
		List<FuelFilling> fuelFillings = fuelRepo
				.findByVehicleIdAndRefuelDateTimeBetweenOrderByRefuelDateTimeAsc(vehicleId, from, to);

		if (fuelFillings.size() < 2) {
			throw new IllegalArgumentException("Not enough data to generate report.");
		}

		return FuelReportMapper.toReportDTO(vehicleId, from, to, fuelFillings);
	}

	public List<FuelMonthlyReportDTO> getMonthlyReport(Long vehicleId) {
		List<FuelFilling> fillings = fuelRepo.findByVehicleId(vehicleId);
		return FuelMonthlyReportMapper.mapToMonthlyReport(fillings);
	}

	public void writeMonthlyFuelReportCSV(Long vehicleId, PrintWriter writer) {
		// Define CSV header
		final String HEADER = "Vehicle ID,Month,Fuel Used (L),Distance (KM),Efficiency (KM/L),Fuel Cost ($)";
		writer.println(HEADER);

		// Fetch and process fuel entries into monthly report
		List<FuelMonthlyReportDTO> report = FuelMonthlyReportMapper
				.mapToMonthlyReport(fuelRepo.findByVehicleId(vehicleId));

		// Write each row to the CSV
		report.forEach(dto -> writer.printf("%d,%s,%.2f,%d,%.2f,%.2f%n", dto.getVehicle_id(), dto.getMonth(),
				dto.getTotal_fuel_used(), dto.getDistance_travelled(), dto.getFuel_efficiency(), dto.getTotal_cost()));

		writer.flush(); // Ensure all data is written to the response
	}

}

package com.akt.vms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akt.vms.entity.FuelFilling;
import com.akt.vms.repository.FuelFillingRepository;

@Service
public class FuelAlertService {

	private static final double FUEL_EFFICIENCY_THRESHOLD = 5.0; // e.g., km/l or mpg
	private static final double FUEL_CONSUMPTION_THRESHOLD_MULTIPLIER = 1.2;

	@Autowired
	private FuelFillingRepository fuelFillingRepository;

	@Autowired
	private NotificationService notificationService;

	/**
	 * Checks current fuel level status. Note: This is a placeholder method. Actual
	 * logic can be added if tank size and current level are known.
	 *
	 * @param vehicleId ID of the vehicle
	 * @return Fuel level status message
	 */

	public String checkFuelLevels(Long vehicleId) {
		// Placeholder example: You can implement actual fuel level logic if fuel tank
		// size is known
		return "Fuel level is normal.";
	}

	/**
	 * Checks whether the fuel efficiency of the vehicle is below the defined
	 * threshold.
	 *
	 * @param vehicleId ID of the vehicle
	 * @return Message indicating efficiency status
	 */
	public String checkFuelEfficiency(Long vehicleId) {
		List<FuelFilling> fuelFillings = fuelFillingRepository.findByVehicleId(vehicleId);

		double avgEfficiency = fuelFillings.stream()
				.mapToDouble(filling -> filling.getOdometerReading() / filling.getFuelQuantity()).average().orElse(0.0);

		if (avgEfficiency < FUEL_EFFICIENCY_THRESHOLD) {
			notificationService.sendFuelAlert(vehicleId, "Fuel efficiency is below the expected threshold.");
			return "Fuel efficiency is below the expected threshold.";
		}
		return "Fuel efficiency is within acceptable limits.";
	}

	/**
	 * Checks if the most recent fuel filling indicates unusually high fuel
	 * consumption.
	 *
	 * @param vehicleId ID of the vehicle
	 * @return Message indicating consumption status
	 */
	public String checkHighFuelConsumption(Long vehicleId) {
		List<FuelFilling> fuelFillings = fuelFillingRepository.findByVehicleId(vehicleId);
		if (fuelFillings.size() < 1)
			return "No fuel data available.";

		// Get the most recent fuel filling record
		FuelFilling lastFuelFilling = fuelFillings.get(fuelFillings.size() - 1);
		double average = averageFuelConsumption(vehicleId);

		if (lastFuelFilling.getFuelQuantity() > (average * FUEL_CONSUMPTION_THRESHOLD_MULTIPLIER)) {
			notificationService.sendFuelAlert(vehicleId, "Fuel consumption is higher than usual.");
			return "Fuel consumption is higher than usual.";
		}
		return "Fuel consumption is within normal range.";
	}

	/**
	 * Calculates the average fuel consumption for the vehicle.
	 *
	 * @param vehicleId ID of the vehicle
	 * @return Average fuel quantity per filling
	 */
	private double averageFuelConsumption(Long vehicleId) {
		List<FuelFilling> fuelFillings = fuelFillingRepository.findByVehicleId(vehicleId);
		return fuelFillings.stream().mapToDouble(FuelFilling::getFuelQuantity).average().orElse(0.0);
	}
}
package com.akt.vms.mapper;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.akt.vms.dto.FuelMonthlyReportDTO;
import com.akt.vms.entity.FuelFilling;

public class FuelMonthlyReportMapper {

	public static List<FuelMonthlyReportDTO> mapToMonthlyReport(List<FuelFilling> fillings) {
		// Group by vehicle ID + YearMonth
		Map<String, List<FuelFilling>> grouped = fillings.stream().collect(
				Collectors.groupingBy(f -> f.getVehicle().getId() + "-" + YearMonth.from(f.getRefuelDateTime())));

		List<FuelMonthlyReportDTO> reportList = new ArrayList<>();

		for (Map.Entry<String, List<FuelFilling>> entry : grouped.entrySet()) {
			List<FuelFilling> monthFillings = entry.getValue();

			// Sort by refuel date within the month
			monthFillings.sort(Comparator.comparing(FuelFilling::getRefuelDateTime));

			// Extract YearMonth and Vehicle ID
			YearMonth yearMonth = YearMonth.from(monthFillings.get(0).getRefuelDateTime());
			Long vehicleId = monthFillings.get(0).getVehicle().getId();

			// Total fuel and cost
			double totalFuel = monthFillings.stream().mapToDouble(FuelFilling::getFuelQuantity).sum();
			double totalCost = monthFillings.stream().mapToDouble(FuelFilling::getCost).sum();

			// Calculate distance if at least 2 readings
			int distance = 0;
			if (monthFillings.size() >= 2) {
				distance = monthFillings.get(monthFillings.size() - 1).getOdometerReading()
						- monthFillings.get(0).getOdometerReading();
			}

			double efficiency = totalFuel > 0 ? distance / totalFuel : 0;

			FuelMonthlyReportDTO dto = new FuelMonthlyReportDTO();
			dto.setVehicle_id(vehicleId);
			dto.setMonth(yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
			dto.setYearMonth(yearMonth.toString()); // e.g., "2025-05"
			dto.setTotal_fuel_used(Math.round(totalFuel * 100.0) / 100.0);
			dto.setDistance_travelled(distance);
			dto.setFuel_efficiency(Math.round(efficiency * 100.0) / 100.0);
			dto.setTotal_cost(Math.round(totalCost * 100.0) / 100.0);

			reportList.add(dto);
		}

		// Sort the report list by YearMonth for correct chronological order
		reportList.sort(Comparator.comparing(FuelMonthlyReportDTO::getYearMonth));

		return reportList;
	}
}

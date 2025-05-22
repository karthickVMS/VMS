package com.akt.vms.mapper;

import java.time.LocalDateTime;
import java.util.List;

import com.akt.vms.dto.FuelUsageReportDTO;
import com.akt.vms.entity.FuelFilling;

public class FuelReportMapper {

	public static FuelUsageReportDTO toReportDTO(Long vehicleId, LocalDateTime from, LocalDateTime to,
			List<FuelFilling> fuelFillings) {
		double totalFuel = fuelFillings.stream().mapToDouble(FuelFilling::getFuelQuantity).sum();
		double totalCost = fuelFillings.stream().mapToDouble(FuelFilling::getCost).sum();

		int startOdo = fuelFillings.get(0).getOdometerReading();
		int endOdo = fuelFillings.get(fuelFillings.size() - 1).getOdometerReading();
		int distance = endOdo - startOdo;

		double efficiency = totalFuel > 0 ? distance / totalFuel : 0;

		FuelUsageReportDTO dto = new FuelUsageReportDTO();
		dto.setVehicle_id(vehicleId);
		dto.setFrom_date(from);
		dto.setTo_date(to);
		dto.setTotal_fuel_used(totalFuel);
		dto.setTotal_cost(totalCost);
		dto.setDistanc_traveled(distance);
		dto.setEfficiency(efficiency);

		return dto;
	}

}

package com.akt.vms.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akt.vms.entity.FuelFilling;

public interface FuelFillingRepository extends JpaRepository<FuelFilling, Long> {

	List<FuelFilling> findByVehicleId(Long vehicleId);

	List<FuelFilling> findByVehicleIdAndRefuelDateTimeBetweenOrderByRefuelDateTimeAsc(Long vehicleId,
			LocalDateTime from, LocalDateTime to);
}

package com.akt.vms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akt.vms.entity.GpsDeviceInstallation;
import com.akt.vms.entity.Vehicle;

public interface GpsDeviceInstallationRepository extends JpaRepository<GpsDeviceInstallation, Long> {

	boolean existsByVehicle(Vehicle vehicle);

}

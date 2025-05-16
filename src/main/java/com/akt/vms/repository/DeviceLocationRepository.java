package com.akt.vms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akt.vms.entity.DeviceLocation;

public interface DeviceLocationRepository extends JpaRepository<DeviceLocation, Long> {

	List<DeviceLocation> findByDevice_DeviceId(Long deviceId);

}
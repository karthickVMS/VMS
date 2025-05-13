package com.akt.vms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akt.vms.dto.GpsDeviceInstallationDTO;
import com.akt.vms.service.GpsDeviceInstallationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/gps-devices")
public class GpsDeviceInstallationController {

	@Autowired
	private GpsDeviceInstallationService service;

	@GetMapping("/{id}")
	public ResponseEntity<GpsDeviceInstallationDTO> getDevice(@PathVariable Long id) {
		GpsDeviceInstallationDTO dto = service.getDeviceById(id);
		return ResponseEntity.ok(dto);
	}

	@PostMapping("/install")
	public ResponseEntity<GpsDeviceInstallationDTO> installDevice(@Valid @RequestBody GpsDeviceInstallationDTO dto) {
		GpsDeviceInstallationDTO result = service.installDevice(dto);
		return ResponseEntity.status(201).body(result); // 201 Created
	}

}

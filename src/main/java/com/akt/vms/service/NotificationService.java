package com.akt.vms.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	public void sendFuelAlert(Long vehicleId, String message) {
		// Send an email or SMS to the vehicle owner or fleet manager
		System.out.println("Alert for vehicle " + vehicleId + ": " + message);
	}
}

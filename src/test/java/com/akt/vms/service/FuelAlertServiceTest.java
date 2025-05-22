package com.akt.vms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.akt.vms.entity.FuelFilling;
import com.akt.vms.repository.FuelFillingRepository;

@ExtendWith(MockitoExtension.class)
public class FuelAlertServiceTest {

    @Mock
    private FuelFillingRepository fuelFillingRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private FuelAlertService fuelAlertService;

    private FuelFilling entry1, entry2;

    @BeforeEach
    void setUp() {
        entry1 = new FuelFilling();
        entry1.setOdometerReading(10000);
        entry1.setFuelQuantity(20.0);
        entry1.setRefuelDateTime(LocalDateTime.of(2025, 5, 14, 10, 0));

        entry2 = new FuelFilling();
        entry2.setOdometerReading(10400);
        entry2.setFuelQuantity(25.0);
        entry2.setRefuelDateTime(LocalDateTime.of(2025, 5, 15, 10, 0));
    }

    @Test
    void checkFuelEfficiency_ShouldTriggerAlert_WhenEfficiencyLow() {
        entry1.setOdometerReading(100); // Low mileage
        entry2.setOdometerReading(120);

        when(fuelFillingRepository.findByVehicleId(1L)).thenReturn(List.of(entry1, entry2));

        String result = fuelAlertService.checkFuelEfficiency(1L);

        assertEquals("Fuel efficiency is below the expected threshold.", result);
        verify(notificationService).sendFuelAlert(eq(1L), contains("below the expected threshold"));
    }

    @Test
    void checkFuelEfficiency_ShouldReturnNormalMessage_WhenEfficiencyOK() {
        when(fuelFillingRepository.findByVehicleId(1L)).thenReturn(List.of(entry1, entry2));

        String result = fuelAlertService.checkFuelEfficiency(1L);

        assertEquals("Fuel efficiency is within acceptable limits.", result);
        verify(notificationService, never()).sendFuelAlert(anyLong(), anyString());
    }

    @Test
    void checkHighFuelConsumption_ShouldTriggerAlert_WhenConsumptionHigh() {
        entry2.setFuelQuantity(60.0); // 20 + 60 average = 40, threshold = 40 * 1.2 = 48, 60 > 48

        when(fuelFillingRepository.findByVehicleId(1L)).thenReturn(List.of(entry1, entry2));

        String result = fuelAlertService.checkHighFuelConsumption(1L);

        assertEquals("Fuel consumption is higher than usual.", result);
        verify(notificationService).sendFuelAlert(eq(1L), contains("higher than usual"));
    }

    @Test
    void checkHighFuelConsumption_ShouldReturnNormalMessage_WhenConsumptionNormal() {
        when(fuelFillingRepository.findByVehicleId(1L)).thenReturn(List.of(entry1, entry2));

        String result = fuelAlertService.checkHighFuelConsumption(1L);

        assertEquals("Fuel consumption is within normal range.", result);
        verify(notificationService, never()).sendFuelAlert(anyLong(), anyString());
    }

    @Test
    void checkFuelLevels_ShouldReturnNormalMessage() {
        String result = fuelAlertService.checkFuelLevels(1L);
        assertEquals("Fuel level is normal.", result);
    }
}

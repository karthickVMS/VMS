package com.akt.vms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.akt.vms.dto.FuelFillingDTO;
import com.akt.vms.dto.FuelMonthlyReportDTO;
import com.akt.vms.dto.FuelUsageReportDTO;
import com.akt.vms.entity.FuelFilling;
import com.akt.vms.entity.Vehicle;
import com.akt.vms.mapper.FuelFillingMapper;
import com.akt.vms.repository.FuelFillingRepository;
import com.akt.vms.repository.VehicleRepository;

@ExtendWith(MockitoExtension.class)
public class FuelFillingServiceTest {

	@Mock
	private FuelFillingRepository fuelFillingRepository;

	@Mock
	private VehicleRepository vehicleRepository;

	@InjectMocks
	private FuelFillingService fuelFillingService;

	private FuelFillingDTO fuelFillingDTO;
	private Vehicle vehicle;
	private FuelFilling fuelFilling;

	@BeforeEach
	void setUp() {
		fuelFillingDTO = new FuelFillingDTO();
		fuelFillingDTO.setVehicle_id(1L);
		fuelFillingDTO.setFuel_type("Diesel");
		fuelFillingDTO.setFuel_quantity(30.0);
		fuelFillingDTO.setCost(3000.0);
		fuelFillingDTO.setRefuel_date_time(LocalDateTime.parse("2025-05-16T10:00:00"));
		fuelFillingDTO.setLocation("IOCL Station");
		fuelFillingDTO.setOdometer_reading(80000);

		vehicle = new Vehicle();
		vehicle.setId(1L);

		fuelFilling = FuelFillingMapper.toEntity(fuelFillingDTO, vehicle);
	}

	@Test
	void logFuelEntry_ShouldSaveEntry_WhenVehicleExists() {
		when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

		fuelFillingService.logFuelEntry(fuelFillingDTO);

		verify(vehicleRepository, times(1)).findById(1L);
		verify(fuelFillingRepository, times(1)).save(any(FuelFilling.class));
	}

	@Test
	void logFuelEntry_ShouldThrowException_WhenVehicleNotFound() {
		when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> fuelFillingService.logFuelEntry(fuelFillingDTO));

		assertEquals("Vehicle not found", exception.getMessage());
		verify(fuelFillingRepository, never()).save(any());
	}

	@Test
	void getFuelEntriesByVehicle_ShouldReturnDTOList() {
		when(fuelFillingRepository.findByVehicleId(1L)).thenReturn(List.of(fuelFilling));

		List<FuelFillingDTO> result = fuelFillingService.getFuelEntriesByVehicle(1L);

		assertEquals(1, result.size());
		assertEquals("Diesel", result.get(0).getFuel_type());
		verify(fuelFillingRepository, times(1)).findByVehicleId(1L);
	}

	@Test
	void calculateFuelEfficiency_ShouldThrowException_WhenLessThanTwoEntries() {
		when(fuelFillingRepository.findByVehicleId(1L)).thenReturn(List.of(fuelFilling));

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> fuelFillingService.calculateFuelEfficiency(1L));

		assertEquals("Not enough data to calculate fuel efficiency.", ex.getMessage());
		verify(fuelFillingRepository, times(1)).findByVehicleId(1L);
	}

	@Test
	void calculateFuelEfficiency_ShouldReturnCorrectValue_WhenValidEntriesExist() {
		FuelFilling earlier = new FuelFilling();
		earlier.setOdometerReading(10000);
		earlier.setRefuelDateTime(LocalDateTime.parse("2025-05-14T10:00:00"));
		earlier.setFuelQuantity(20.0);

		FuelFilling later = new FuelFilling();
		later.setOdometerReading(10400);
		later.setRefuelDateTime(LocalDateTime.parse("2025-05-15T10:00:00"));
		later.setFuelQuantity(25.0);

		List<FuelFilling> mockEntries = new ArrayList<>(List.of(earlier, later));
		when(fuelFillingRepository.findByVehicleId(1L)).thenReturn(mockEntries);

		double efficiency = fuelFillingService.calculateFuelEfficiency(1L);

		assertEquals(16.0, efficiency, 0.001); // 400 / 45 = 8.888...
	}
	

	@Test
	void calculateFuelEfficiency_ShouldThrow_WhenDistanceOrFuelIsInvalid() {
	    FuelFilling recent = new FuelFilling();
	    recent.setRefuelDateTime(LocalDateTime.of(2025, 5, 17, 10, 0));
	    recent.setOdometerReading(10000);
	    recent.setFuelQuantity(0.0);

	    FuelFilling previous = new FuelFilling();
	    previous.setRefuelDateTime(LocalDateTime.of(2025, 5, 16, 10, 0));
	    previous.setOdometerReading(10000);
	    previous.setFuelQuantity(30.0);

	    when(fuelFillingRepository.findByVehicleId(1L)).thenReturn(new ArrayList<>(List.of(recent, previous)));

	    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
	            () -> fuelFillingService.calculateFuelEfficiency(1L));

	    assertEquals("Invalid odometer readings or fuel data.", ex.getMessage());
	}


    @Test
    void generateFuelUsageReport_ShouldReturnValidReport() {
        FuelFilling entry1 = new FuelFilling();
        entry1.setOdometerReading(10000);
        entry1.setFuelQuantity(20.0);
        entry1.setCost(2000.0);
        entry1.setRefuelDateTime(LocalDateTime.of(2025, 5, 1, 10, 0));

        FuelFilling entry2 = new FuelFilling();
        entry2.setOdometerReading(10400);
        entry2.setFuelQuantity(25.0);
        entry2.setCost(2500.0);
        entry2.setRefuelDateTime(LocalDateTime.of(2025, 5, 10, 10, 0));

        when(fuelFillingRepository.findByVehicleIdAndRefuelDateTimeBetweenOrderByRefuelDateTimeAsc(
                eq(1L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(List.of(entry1, entry2));

        FuelUsageReportDTO report = fuelFillingService.generateFuelUsageReport(
                1L,
                LocalDateTime.of(2025, 5, 1, 0, 0),
                LocalDateTime.of(2025, 5, 31, 23, 59));

        assertNotNull(report);
        assertEquals(1L, report.getVehicle_id());
    }

    @Test
    void generateFuelUsageReport_ShouldThrow_WhenInsufficientData() {
        when(fuelFillingRepository.findByVehicleIdAndRefuelDateTimeBetweenOrderByRefuelDateTimeAsc(
                eq(1L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(List.of(fuelFilling));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> fuelFillingService.generateFuelUsageReport(1L,
                        LocalDateTime.of(2025, 5, 1, 0, 0),
                        LocalDateTime.of(2025, 5, 31, 23, 59)));

        assertEquals("Not enough data to generate report.", ex.getMessage());
    }

    @Test
    void getMonthlyReport_ShouldReturnList() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);

        FuelFilling filling = new FuelFilling();
        filling.setRefuelDateTime(LocalDateTime.of(2025, 5, 10, 10, 0));
        filling.setFuelQuantity(100.0);
        filling.setOdometerReading(1000);
        filling.setCost(9000.0);
        filling.setVehicle(vehicle);

        when(fuelFillingRepository.findByVehicleId(1L)).thenReturn(List.of(filling));

        List<FuelMonthlyReportDTO> result = fuelFillingService.getMonthlyReport(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    void writeMonthlyFuelReportCSV_ShouldWriteCorrectCSV() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);  // Ensure the Vehicle ID is set

        FuelFilling filling = new FuelFilling();
        filling.setRefuelDateTime(LocalDateTime.of(2025, 5, 10, 10, 0));
        filling.setFuelQuantity(100.0);
        filling.setOdometerReading(1000);
        filling.setCost(9000.0);
        filling.setVehicle(vehicle);

        when(fuelFillingRepository.findByVehicleId(1L)).thenReturn(List.of(filling));

        // Use StringWriter and PrintWriter to capture the generated CSV output
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        fuelFillingService.writeMonthlyFuelReportCSV(1L, pw);

        String output = sw.toString();
        System.out.println("Generated CSV Output: \n" + output);

        assertTrue(output.contains("100.0"));
        assertTrue(output.contains("1"));  // Vehicle ID should be present
    }


    @Test
    void writeMonthlyFuelReportCSV_ShouldGenerateCSVContent() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);  // Set vehicle ID

        FuelFilling filling = new FuelFilling();
        filling.setRefuelDateTime(LocalDateTime.of(2025, 5, 10, 10, 0));
        filling.setFuelQuantity(100.0);
        filling.setOdometerReading(1000);
        filling.setCost(9000.0);
        filling.setVehicle(vehicle);  // Set the vehicle for FuelFilling

        when(fuelFillingRepository.findByVehicleId(1L)).thenReturn(List.of(filling));  // Ensure a non-null list is returned

        // Use StringWriter and PrintWriter to capture the generated CSV output
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        fuelFillingService.writeMonthlyFuelReportCSV(1L, pw);

        String output = sw.toString();
        System.out.println("Generated CSV Output: \n" + output);  // Debug: print the CSV output

        assertTrue(output.contains("Vehicle ID,Month,Fuel Used (L),Distance (KM),Efficiency (KM/L),Fuel Cost ($)"));
        assertTrue(output.contains("100.0"));  // Fuel quantity in the CSV
        assertTrue(output.contains("100.0"));   // Odometer reading in the CSV
        assertTrue(output.contains("9000.0")); // Cost in the CSV
    }
}

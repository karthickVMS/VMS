package com.akt.vms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akt.vms.entity.Vehicle;

@Repository

public interface VehicleRepository
		extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
	Vehicle findByModelAndFuelType(String model, String fuelType);

////query to find a Vehicle by model and fuel type
//	@Query("select v from Vehicle v where v.model=?1 and v.fuelType=?2")
//	Vehicle getByModelAndFuelType(String model, String fuelType);

//Native SQL query to find a Vehicle by model and fuel type (using actual table name)
	@Query(value = "select * from vehicledetails where model=?1 and fuelType=?2", nativeQuery = true)
	Vehicle getByModelAndFuelType_1(String model, String fuelType);

	// find vehicles with no insurance policy
	@Query("SELECT v FROM Vehicle v WHERE v.insurancePolicyNumber IS NULL")
	List<Vehicle> findVehiclesWithoutInsurance();

	// find vehicles where model starts/ends with a specific value
	@Query("SELECT v FROM Vehicle v WHERE v.model LIKE CONCAT(:prefix, '%')")
	List<Vehicle> findByModelStartingWith(@Param("prefix") String prefix);

	@Query("SELECT v FROM Vehicle v WHERE v.model LIKE CONCAT('%', :suffix)")
	List<Vehicle> findByModelEndingWith(@Param("suffix") String suffix);

	// to get distinct fuel types
	@Query("SELECT DISTINCT v.fuelType FROM Vehicle v")
	List<String> findDistinctFuelTypes();

	// find vehicles where vehicle number is not null and model is not empty
	@Query("SELECT v FROM Vehicle v WHERE v.vehicleNumber IS NOT NULL AND TRIM(v.model) <> ''")
	List<Vehicle> findValidVehicleNumbers();
	// vehicles with the most common model
//	@Query (SELECT v FROM Vehicle v WHERE v.model = (
//        SELECT v2.model FROM Vehicle v2 
//        GROUP BY v2.model 
//	ORDER BY COUNT(v2.model) DESC
//        LIMIT 1))

//update query to modify vehicle model and fuel type by id
	@Modifying // Marks this query as an update operation
	@Transactional // Ensures the update occurs within a transaction
	@Query("UPDATE Vehicle v SET v.model = :model, v.fuelType = :fuelType WHERE v.id = :id")
	int updateModelAndFuelTypeById(Long id, String model, String fuelType);

	/**
	 * Custom query to search vehicle details by multiple fields. Any field can be
	 * null, and the query adapts accordingly.
	 */
	@Query("SELECT v FROM Vehicle v " + "WHERE (:vehicleNumber IS NULL OR v.vehicleNumber = :vehicleNumber) "
			+ "AND (:model IS NULL OR v.model = :model) " + "AND (:fuelType IS NULL OR v.fuelType = :fuelType) "
			+ "AND (:insurancePolicyNumber IS NULL OR v.insurancePolicyNumber = :insurancePolicyNumber) "
			+ "AND (:yearOfManufacturer IS NULL OR v.yearOfManufacturer = :yearOfManufacturer) "
			+ "AND (:registrationDate IS NULL OR v.registrationDate = :registrationDate)")
	List<Vehicle> searchVehicles(@Param("vehicleNumber") String vehicleNumber, @Param("model") String model,
			@Param("fuelType") String fuelType, @Param("insurancePolicyNumber") String insurancePolicyNumber,
			@Param("yearOfManufacturer") Integer yearOfManufacturer,
			@Param("registrationDate") LocalDate registrationDate);

	/**
	 * Custom search query to search vehicles based on multiple fields. It searches
	 * case-insensitively using LIKE for partial matches.
	 */
	@Query("SELECT v FROM Vehicle v WHERE " + "LOWER(v.vehicleNumber) LIKE (CONCAT('%', :searchvalue, '%')) OR "
			+ "LOWER(v.model) LIKE (CONCAT('%', :searchvalue, '%')) OR "
			+ "LOWER(v.fuelType) LIKE (CONCAT('%', :searchvalue, '%')) OR "
			+ "LOWER(v.insurancePolicyNumber) LIKE (CONCAT('%', :searchvalue, '%')) OR "
			+ "CAST(v.yearOfManufacturer AS string) LIKE %:searchvalue% OR "
			+ "CAST(v.id AS string) LIKE %:searchvalue%")
	List<Vehicle> searchVehicles(@Param("searchvalue") String searchvalue);
}

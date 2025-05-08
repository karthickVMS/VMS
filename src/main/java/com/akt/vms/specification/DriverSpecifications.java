package com.akt.vms.specification;

import org.springframework.data.jpa.domain.Specification;

import com.akt.vms.entity.Driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverSpecifications {

	private static final Logger logger = LoggerFactory.getLogger(DriverSpecifications.class);

	/*
	 * Creates a specification to filter drivers by exact name match.
	 * 
	 * @param name the name to match
	 * 
	 * @return a Specification that checks if the driver's name matches the given name
	 */
	public static Specification<Driver> hasName(String name) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
	}

	public static Specification<Driver> hasLicenseNum(String licenseNum) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("licenseNum")), "%" + licenseNum.toLowerCase() + "%");
	}

	public static Specification<Driver> hasVehicleNum(String vehicleNum) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("vehicleNum")), "%" + vehicleNum.toLowerCase() + "%");
	}

	public static Specification<Driver> hasContactNum(String contactNum) {
		return (root, query, cb) -> cb.equal(root.get("contactNum"), contactNum);

	}

	/**
	 * Filters drivers where the state name contains the given value, ignoring case.
	 */
	public static Specification<Driver> hasState(String state) {
		return (root, query, cb) -> {
			logger.debug("Applying filter: state contains '{}'", state);
			return cb.like(cb.lower(root.get("state")), "%" + state.toLowerCase() + "%");
		};
	}

	public static Specification<Driver> hasYearsOfExpGreaterThan(Integer yearsOfExp) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("yearsOfExp"), yearsOfExp);

	}

	public static Specification<Driver> hasLicenseTypeName(String typeName) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("licenseType").get("categoryName")),
				"%" + typeName.toLowerCase() + "%");

	}

}

package com.akt.vms.specification;

import org.springframework.data.jpa.domain.Specification;

import com.akt.vms.entity.Vehicle;



public class VehicleSpecification {
	public static Specification<Vehicle> getvehicleSpecification(String searchValue) {
        return (root, query, cb) -> {
            if (searchValue == null || searchValue.trim().isEmpty()) {
                return cb.conjunction();
            }

            String likevalue = "%" + searchValue.toLowerCase() + "%";

            return cb.or(
                cb.like(cb.lower(root.get("vehicleNumber")), likevalue),
                cb.like(cb.lower(root.get("model")), likevalue),
                cb.like(cb.lower(root.get("fuelType")), likevalue),
                cb.like(cb.lower(root.get("insurancePolicyNumber")), likevalue),
                cb.like(cb.lower(cb.function("str", String.class, root.get("yearOfManufacturer"))), likevalue),
                cb.like(cb.lower(cb.function("str", String.class, root.get("id"))), likevalue)
            );
        };
    }
}

package com.akt.vms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.akt.vms.entity.TripManagement;

@Repository
public interface TripManagementRepository extends JpaRepository<TripManagement, Long> {

}

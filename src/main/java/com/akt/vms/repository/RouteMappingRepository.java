package com.akt.vms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.akt.vms.entity.RouteMapping;

@Repository
public interface RouteMappingRepository extends JpaRepository<RouteMapping, Long> {

}

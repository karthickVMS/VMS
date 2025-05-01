package com.akt.vms.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.akt.vms.entity.Customer;



@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer>  {
	
	
	
	@Query("SELECT c FROM Customer c WHERE c.customerName = :customerName AND c.city = :city")
	Optional<Customer> findByCustomerNameAndCity(@Param("customerName") String customerName, @Param("city") String city);
	

    Optional<Customer> getByCustomerEmailAndCity(String customerEmail, String city);
    
    @Query("UPDATE Customer c SET c.customerEmail = :email WHERE c.id = :id")
    void updateCustomerEmail(@Param("id") Long id, @Param("email") String email);
    
    List<Customer> findByCustomerNameLike(String namePattern);
    
    List<Customer> findByCustomerNameContaining(String name);
    @Query("SELECT c FROM Customer c WHERE c.customerName = :customerName OR c.city = :city")
    List<Customer> findByCustomerNameOrCity(@Param("customerName") String customerName, @Param("city") String city);

    @Query("SELECT c FROM Customer c WHERE c.customerName NOT LIKE :namePattern")
    List<Customer> findByCustomerNameNotLike(@Param("namePattern") String namePattern);
    @Query("SELECT c FROM Customer c WHERE c.customerName LIKE CONCAT(:prefix, '%')")
    List<Customer> findByCustomerNameStartingWithCustom(@Param("prefix") String prefix);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.customerName) = LOWER(:customerName)")
    List<Customer> findByCustomerNameIgnoreCaseCustom(@Param("customerName") String customerName);


	Object searchCustomers(String lowerCase);

}

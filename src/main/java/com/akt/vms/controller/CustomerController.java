package com.akt.vms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akt.vms.dto.CustomerDTO;
import com.akt.vms.entity.Customer;
import com.akt.vms.service.CustomerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	// logging information, warnings, and errors to assist in debugging and
	// monitoring Java applications.
	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	
	@GetMapping("/search")
    public CustomerDTO findByCustomerNameAndCity(
            @RequestParam String customerName,
            @RequestParam String city) {
        return customerService.findByCustomerNameAndCity(customerName, city);
    }
	
	@PutMapping("/{id}/email")
	public ResponseEntity<CustomerDTO> updateCustomerEmail(@PathVariable Long id, @RequestBody Map<String, String> updateData) {
	    String newEmail = updateData.get("customerEmail");
	    CustomerDTO updatedCustomer = customerService.updateCustomerEmail(id, newEmail);
	    return ResponseEntity.ok(updatedCustomer);
	}
	@GetMapping("/like-search")
	public ResponseEntity<List<CustomerDTO>> searchCustomersByNameLike(@RequestParam String name) {
	    List<CustomerDTO> customers = customerService.findCustomersByNameLike(name);
	    return ResponseEntity.ok(customers);
	}
	
    @GetMapping("/contain")
    public List<Customer> getCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String city
    ) {
        return customerService.getCustomers(name, email, city);
    }
	
	@GetMapping("/contains")
	public ResponseEntity<List<CustomerDTO>> searchCustomersByNameContaining(@RequestParam String name) {
	    List<CustomerDTO> customers = customerService.findCustomersByNameContaining(name);
	    return ResponseEntity.ok(customers);
	}
	
	
	@GetMapping("api/search")
    public CustomerDTO getByCustomerEmailAndCity(
            @RequestParam String customerEmail,
            @RequestParam String city) {
        return customerService.getByCustomerEmailAndCity(customerEmail, city);
    }

	/**
	 * Create a new customer in the customers table.
	 * 
	 * @param customerDTO - the customer data to be created.
	 * @return ResponseEntity containing the created customer.
	 */
	
	@PostMapping
	public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
	    logger.info("Creating new Customer: {}", customerDTO);
	    CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
	    return ResponseEntity
	            .status(HttpStatus.CREATED) // HTTP 201
	            .body(customerDTO);
	}
	/*
	 * @PostMapping public ResponseEntity<CustomerDTO> createCustomer(@RequestBody
	 * CustomerDTO customerDTO) { logger.info("Creating new Customer: {}",
	 * customerDTO); return
	 * ResponseEntity.ok(customerService.createCustomer(customerDTO)); }
	 */

	/**
	 * Get all customers from the customers table.
	 * 
	 * @return ResponseEntity containing a list of all customers.
	 */
	@GetMapping
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
		logger.info("CustomerController Fetching all customers from the service.");
		return ResponseEntity.ok(customerService.getAllCustomers());
	}

	/**
	 * Update the customer with the given ID from the customers table. using 2
	 * parameters
	 * 
	 * @param id          - the ID of the customer to be updated.
	 * @param customerDTO - the updated customer data.
	 * @return ResponseEntity containing the updated CustomerDTO.
	 */

	@PutMapping("/{id}")
	public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
	    logger.info("Updating customer with ID: {}", id);
	    CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);

	    return ResponseEntity
	            .accepted() // sets status to 202 Accepted
	            .body(updatedCustomer);
	}

	/*
	 * @PutMapping("/{id}") public ResponseEntity<CustomerDTO>
	 * updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
	 * logger.info("Updating customer with ID: {}", id); return
	 * ResponseEntity.ok(customerService.updateCustomer(id, customerDTO)); }
	 */
	/**
	 * Delete the customer with the given ID from the customers table.
	 * 
	 * @param id - the ID of the customer to be deleted.
	 * @return success or not.
	 */
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
	    logger.info("delete customer with ID: {}", id);

	    customerService.deleteCustomer(id); // performs deletion immediately

	    // 204 No Content: successful request, nothing to return
	    return ResponseEntity.noContent().build();
	}
	/**
	 * Get the customer with the given ID from the customers table.
	 * 
	 * @param id - the ID of the customer.
	 * @return ResponseEntity containing the customer.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
	    logger.info("Fetching customer with ID: {}", id);
	    
	    // Assuming the service returns null or throws an exception if the customer is not found
	    CustomerDTO customerDTO = customerService.getCustomerById(id);
	    
	    if (customerDTO == null) {
	        // Return 409 Conflict if a business conflict occurs (customer exists but cannot be retrieved)
	        return ResponseEntity
	                .status(HttpStatus.CONFLICT) // HTTP 409
	                .body(null); // Optionally, you can send an error message body
	    }
	    
	    return ResponseEntity.ok(customerDTO); // HTTP 200 OK if customer is found
	}
	/*
	 * @GetMapping("/{id}") public ResponseEntity<CustomerDTO>
	 * getCustomerById(@PathVariable Long id) {
	 * logger.info("Fetching customer with ID: {}", id); return
	 * ResponseEntity.ok(customerService.getCustomerById(id)); }
	 */
	
	  @GetMapping("/paginated") public ResponseEntity<Page<CustomerDTO>> getPaginatedCustomers(Pageable pageable) 
	  { return
	  ResponseEntity.ok(customerService.getPaginatedCustomers(pageable)); 
	  }
	  
	  @PostMapping("/search")
		public ResponseEntity<List<Customer>> searchCustomers(@RequestBody CustomerDTO customerDTO) {
			logger.info("Received search request: {}", customerDTO);
			List<Customer> customers = customerService.searchCustomers(customerDTO);
			logger.info("Found {} customers matching the search", customers.size());
			return ResponseEntity.ok(customers);
		}

	 
}

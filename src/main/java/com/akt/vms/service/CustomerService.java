package com.akt.vms.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.akt.vms.dto.CustomerDTO;
import com.akt.vms.entity.Customer;
import com.akt.vms.mapper.CustomerMapper;
import com.akt.vms.repository.CustomerRepository;
import com.akt.vms.specifications.CustomerSpecification;

import jakarta.persistence.criteria.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomerService {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);  // Initialize logger
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    /**
     * Create a new customer record.
     * @param customerDTO - The customerDTO containing the customer fields.
     * @return The created CustomerDTO.
     */
    //Create records
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
    	logger.info("create a new customer: {}", customerDTO);
    	// convert customer dto object to customer entity object
    	Customer customer = CustomerMapper.INSTANCE.toEntity(customerDTO);
    	logger.info("Converted customer DTO to entity: {}", customer);
    	//to store customer entity to customer repository
    	

    	customer = customerRepository.save(customer);
    	logger.info("Customer entity saved to repository with ID: {}", customer.getId());

    	//to convert saved entity into dto object 
        return CustomerMapper.INSTANCE.toDto(customer);
    }
    
    /**
     * Retrieve all customers from the database and return them as CustomerDTOs.
     * @return A list of CustomerDTOs.
     */
    
    public List<CustomerDTO> getAllCustomers() {
    	logger.info("Request to fetch all customers from the database.");
    	List<CustomerDTO> customerDTOList = new ArrayList<>();
    	
        // Retrieve all customers from the repository
        List<Customer> customers = customerRepository.findAll();
        
        //Map each customer to a CustomerDTO
       
        for (Customer customer : customers) {
            CustomerDTO customerDTO = CustomerMapper.INSTANCE.toDto(customer);
            customerDTOList.add(customerDTO);
        }
        logger.info("Successfully fetched customers from the database.");
        //Return the list of CustomerDTOs
        return customerDTOList;
    }
    //Get all records
    //@Cacheable("categories")
    // get all list of customer data through CustomerDTO
    /*public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }*/
    public CustomerDTO getCustomerById(Long id) {
    	 logger.info("Request to fetch customer with ID: {}", id);
        // Retrieve the customer from the repository by ID
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        
        // Check if the customer exists
        if (optionalCustomer.isPresent()) {
            // Customer found, map it to CustomerDTO
            Customer customer = optionalCustomer.get();
            logger.info("Customer with ID: {} found.", id);
            return CustomerMapper.INSTANCE.toDto(customer);
        } else {
            // Customer not found, return null
            System.out.println("Customer with ID " + id + " not found.");
            logger.warn("Customer with ID: {} not found.", id);
            return null;
        }
    }
    
    public CustomerDTO findByCustomerNameAndCity(String customerName, String city) {
    	Optional<Customer> optCustomer = customerRepository.findByCustomerNameAndCity(customerName, city);
    	// Check if the customer exists
        if (optCustomer.isPresent()) {
            // Customer found, map it to CustomerDTO
            Customer customer = optCustomer.get();
            logger.info("Customer with Name and city: {} found.", customerName, city);
            return CustomerMapper.INSTANCE.toDto(customer);
        } else {
            // Customer not found, return null
            
            logger.warn("Customer with Name and city: {} not found.", customerName, city);
            return null;
        }
               
    }
    
    public CustomerDTO getByCustomerEmailAndCity(String customerEmail, String city) {
    	Optional<Customer> optCustomer2 = customerRepository.getByCustomerEmailAndCity(customerEmail, city);
    	// Check if the customer exists
        if (optCustomer2.isPresent()) {
            // Customer found, map it to CustomerDTO
            Customer customer = optCustomer2.get();
            logger.info("Customer with Email and city: {} found.", customerEmail, city);
            return CustomerMapper.INSTANCE.toDto(customer);
        } else {
            // Customer not found, return null
            
            logger.warn("Customer with Email and city: {} not found.", customerEmail, city);
            return null;
        }
               
    }

    //Get record using id
    //@Cacheable("category")
    /*public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(CustomerMapper.INSTANCE::toDto)
                .orElse(null);
    }*/
    //Edit the created records
    //@CacheEvict(value = "categories", allEntries = true)
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
    	logger.info("Received request to update customer with ID: {}", id);
    	Customer customer =customerRepository.findById(id).get();
       if (customer!=null) {
            customer.setCustomerName(customerDTO.getCustomerName());
            customer.setCustomerNumber(customerDTO.getCustomerNumber());
            customer.setCustomerEmail(customerDTO.getCustomerEmail());
            customer.setCustomerDob(customerDTO.getCustomerDob());
            customer.setCity(customerDTO.getCity());
            
          //to store customer entity to customer repository
            customer = customerRepository.save(customer);
            logger.info("Customer with ID: {} updated successfully.", id);
          //to convert entity to dto object 
            return CustomerMapper.INSTANCE.toDto(customer);
        } return(null);
    }
    
    
    public List<Customer> getCustomers(String name, String email, String city) {
        Specification<Customer> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and(CustomerSpecification.hasName(name));
        }

        if (email != null && !email.isEmpty()) {
            spec = spec.and(CustomerSpecification.hasEmail(email));
        }

        if (city != null && !city.isEmpty()) {
            spec = spec.and(CustomerSpecification.isFromCity(city));
        }

        return customerRepository.findAll(spec);
    }
    public List<CustomerDTO> findCustomersByNameContaining(String name) {
        List<Customer> customers = customerRepository.findByCustomerNameContaining(name);
        return customers.stream()
                        .map(CustomerMapper.INSTANCE::toDto)
                        .collect(Collectors.toList());
    }
   
    public CustomerDTO updateCustomerEmail(Long id, String newEmail) {
        logger.info("Updating email for customer ID: {}", id);
        
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
        
        customer.setCustomerEmail(newEmail);
        customer = customerRepository.save(customer);
        
        logger.info("Customer email updated successfully for ID: {}", id);
        
        return CustomerMapper.INSTANCE.toDto(customer);
    }
    public List<CustomerDTO> findCustomersByNameLike(String name) {
        
        String pattern = "%" + name + "%";
        List<Customer> customers = customerRepository.findByCustomerNameLike(pattern);
        return customers.stream()
                        .map(CustomerMapper.INSTANCE::toDto)
                        .collect(Collectors.toList());
    }
    public void deleteCustomer(Long id) {
    	logger.info("Request to delete customer with ID: {}", id);
        // Check if the customer with the given ID exists
        boolean customerExists = customerRepository.existsById(id);
        
        // If the customer exists, delete it
        if (customerExists) {
            // Delete the customer by ID from the repository
            customerRepository.deleteById(id);
            logger.info("Customer with ID: {} has been deleted successfully.", id);
            System.out.println("Customer with ID " + id + " has been deleted.");
        } else {
        	logger.warn("Customer with ID: {} does not exist in the database.", id);
            System.out.println("Customer with ID " + id + " does not exist.");
        }
    }
    //Delete the created record using id
    //@CacheEvict(value = {"categories", "category"}, allEntries = true)
    /*public void deleteCustomer(Long id) {
    	//Delete one existing customer entity from customer Repository
    	customerRepository.deleteById(id);
    }*/
   //paging and sorting
    public Page<CustomerDTO> getPaginatedCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(CustomerMapper.INSTANCE::toDto);
    }
    
    public List<Customer> searchCustomers(CustomerDTO customerDTO) {
        // Example: search by name, email, and city if provided
        String name = customerDTO.getCustomerName();
        String email = customerDTO.getCustomerEmail();
        String city = customerDTO.getCity();

        // Example using a JPA Specification, QueryDSL, or custom repo method
        // Here, we assume a simple repository method or filtering
        return customerRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("customerName")), "%" + name.toLowerCase() + "%"));
            }
            if (email != null && !email.isEmpty()) {
                predicate = cb.and(predicate, cb.equal(cb.lower(root.get("customerEmail")), email.toLowerCase()));
            }
            if (city != null && !city.isEmpty()) {
                predicate = cb.and(predicate, cb.equal(cb.lower(root.get("city")), city.toLowerCase()));
            }

            return predicate;
        });
    }

}


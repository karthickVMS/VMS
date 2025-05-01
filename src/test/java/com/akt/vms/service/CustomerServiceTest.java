package com.akt.vms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.akt.vms.dto.CustomerDTO;
import com.akt.vms.entity.Customer;
import com.akt.vms.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    private static final Long CUSTOMER_ID = 1L;
    private static final String NAME = "John";
    private static final String EMAIL = "john@example.com";
    private static final String CITY = "mdu";
    private static final String PHONE = "8888877777";
    private static final String DOB = "22/08/2000";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setCustomerName(NAME);
        customer.setCustomerEmail(EMAIL);
        customer.setCustomerNumber(PHONE);
        customer.setCustomerDob(DOB);
        customer.setCity(CITY);

        customerDTO = new CustomerDTO();
        customerDTO.setId(CUSTOMER_ID);
        customerDTO.setCustomerName(NAME);
        customerDTO.setCustomerEmail(EMAIL);
        customerDTO.setCustomerNumber(PHONE);
        customerDTO.setCustomerDob(DOB);
        customerDTO.setCity(CITY);
    }

    @Test
    void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.createCustomer(customerDTO);

        assertNotNull(result);
        assertEquals(NAME, result.getCustomerName());
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(NAME, result.get(0).getCustomerName());
    }

    @Test
    void testGetCustomerById_Found() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));

        CustomerDTO result = customerService.getCustomerById(CUSTOMER_ID);

        assertNotNull(result);
        assertEquals(NAME, result.getCustomerName());
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        CustomerDTO result = customerService.getCustomerById(CUSTOMER_ID);

        assertNull(result);
    }

    @Test
    void testUpdateCustomerEmail() {
        String newEmail = "newemail@example.com";
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.updateCustomerEmail(CUSTOMER_ID, newEmail);

        assertNotNull(result);
        assertEquals(newEmail, result.getCustomerEmail());
    }

    @Test
    void testFindByCustomerNameAndCity() {
        when(customerRepository.findByCustomerNameAndCity(NAME, CITY)).thenReturn(Optional.of(customer));

        CustomerDTO result = customerService.findByCustomerNameAndCity(NAME, CITY);

        assertNotNull(result);
        assertEquals(NAME, result.getCustomerName());
    }

    @Test
    void testUpdateCustomer() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.updateCustomer(CUSTOMER_ID, customerDTO);

        assertNotNull(result);
        assertEquals(NAME, result.getCustomerName());
    }

    @Test
    void testGetCustomers_WithAllParams() {
    	when(customerRepository.findAll(any(Specification.class))).thenReturn(List.of(customer));

        List<Customer> result = customerService.getCustomers(NAME, EMAIL, CITY);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(customerRepository).findAll(any(Specification.class));
    }

    @Test
    void testDeleteCustomer() {
        when(customerRepository.existsById(CUSTOMER_ID)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(CUSTOMER_ID);

        customerService.deleteCustomer(CUSTOMER_ID);

        verify(customerRepository, times(1)).deleteById(CUSTOMER_ID);
    }

    @Test
    void testFindCustomersByNameLike() {
        String nameFragment = "Joh";
        when(customerRepository.findByCustomerNameLike("%" + nameFragment + "%")).thenReturn(List.of(customer));

        List<CustomerDTO> result = customerService.findCustomersByNameLike(nameFragment);

        assertEquals(1, result.size());
        assertEquals(NAME, result.get(0).getCustomerName());
    }

    @Test
    void testGetPaginatedCustomers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> page = new PageImpl<>(List.of(customer));

        when(customerRepository.findAll(pageable)).thenReturn(page);

        Page<CustomerDTO> result = customerService.getPaginatedCustomers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testSearchCustomers_ByNameOnly() {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerName(NAME);

        when(customerRepository.findAll(any(Specification.class))).thenReturn(List.of(customer));

        List<Customer> result = customerService.searchCustomers(dto);

        assertEquals(1, result.size());
        assertEquals(NAME, result.get(0).getCustomerName());
    }

    @Test
    void testSearchCustomers_ByAllFields() {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerName(NAME);
        dto.setCustomerEmail(EMAIL);
        dto.setCity(CITY);

        when(customerRepository.findAll(any(Specification.class))).thenReturn(List.of(customer));

        List<Customer> result = customerService.searchCustomers(dto);

        assertEquals(1, result.size());
        assertEquals(EMAIL, result.get(0).getCustomerEmail());
    }

    @Test
    void testSearchCustomers_WithEmptyFields() {
        CustomerDTO dto = new CustomerDTO(); // all fields null

        when(customerRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        List<Customer> result = customerService.searchCustomers(dto);

        assertTrue(result.isEmpty());
    }
}

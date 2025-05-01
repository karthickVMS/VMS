


package com.akt.vms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;

import com.akt.vms.dto.CustomerDTO;
import com.akt.vms.entity.Customer;
import com.akt.vms.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private static final Long CUSTOMER_ID = 1L;
    private static final String BASE_URL = "/api/customers";
    private CustomerDTO customerDTO;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        customerDTO = createSampleCustomerDTO();
    }

    private CustomerDTO createSampleCustomerDTO() {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerName("John");
        dto.setCustomerNumber("8888877777");
        dto.setCustomerEmail("john@example.com");
        dto.setCustomerDob("22/08/2000");
        dto.setCity("mdu");
        return dto;
    }

    private String asJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    void getCustomerById_shouldReturnCustomer_whenFound() throws Exception {
        when(customerService.getCustomerById(CUSTOMER_ID)).thenReturn(customerDTO);

        mockMvc.perform(get(BASE_URL + "/{id}", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John"));
    }

    @Test
    void getCustomerById_shouldReturnConflict_whenNotFound() throws Exception {
        when(customerService.getCustomerById(CUSTOMER_ID)).thenReturn(null);

        mockMvc.perform(get(BASE_URL + "/{id}", CUSTOMER_ID))
                .andExpect(status().isConflict());
    }

    @Test
    void createCustomer_shouldReturnCreatedCustomer() throws Exception {
        when(customerService.createCustomer(any())).thenReturn(customerDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("John"));
    }

    @Test
    void updateCustomer_shouldReturnUpdatedCustomer() throws Exception {
        when(customerService.updateCustomer(eq(CUSTOMER_ID), any())).thenReturn(customerDTO);

        mockMvc.perform(put(BASE_URL + "/{id}", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(customerDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.customerEmail").value("john@example.com"));
    }

    @Test
    void deleteCustomer_shouldReturnNoContent_whenDeleted() throws Exception {
        doNothing().when(customerService).deleteCustomer(CUSTOMER_ID);

        mockMvc.perform(delete(BASE_URL + "/{id}", CUSTOMER_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void findCustomerByNameAndCity_shouldReturnCustomer() throws Exception {
        when(customerService.findByCustomerNameAndCity("John", "mdu")).thenReturn(customerDTO);

        mockMvc.perform(get(BASE_URL + "/search")
                        .param("customerName", "John")
                        .param("city", "mdu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("mdu"));
    }

    @Test
    void updateCustomerEmail_shouldReturnUpdatedCustomer() throws Exception {
        String updatedEmail = "newjohn@example.com";
        Map<String, String> updateData = Map.of("customerEmail", updatedEmail);

        customerDTO.setCustomerEmail(updatedEmail);
        when(customerService.updateCustomerEmail(CUSTOMER_ID, updatedEmail)).thenReturn(customerDTO);

        mockMvc.perform(put(BASE_URL + "/{id}/email", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerEmail").value(updatedEmail));
    }

    @Test
    void getAllCustomers_shouldReturnCustomerList() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(customerDTO));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("John"));
    }

    @Test
    void testSearchCustomers_Success() throws Exception {
        CustomerDTO requestDTO = new CustomerDTO();
        requestDTO.setCustomerName("John");
        requestDTO.setCustomerEmail("john@example.com");
        requestDTO.setCity("mdu");

        Customer customer = new Customer();
        customer.setCustomerName("John");
        customer.setCustomerEmail("john@example.com");
        customer.setCity("mdu");

        when(customerService.searchCustomers(any(CustomerDTO.class)))
                .thenReturn(List.of(customer));

        mockMvc.perform(post(BASE_URL + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("John"))
                .andExpect(jsonPath("$[0].customerEmail").value("john@example.com"))
                .andExpect(jsonPath("$[0].city").value("mdu"));
    }

    @Test
    void testSearchCustomers_NoResults() throws Exception {
        CustomerDTO requestDTO = new CustomerDTO();
        requestDTO.setCustomerName("Unknown");

        when(customerService.searchCustomers(any(CustomerDTO.class)))
                .thenReturn(List.of());

        mockMvc.perform(post(BASE_URL + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

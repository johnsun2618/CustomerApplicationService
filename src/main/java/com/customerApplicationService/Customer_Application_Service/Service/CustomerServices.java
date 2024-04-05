package com.customerApplicationService.Customer_Application_Service.Service;

import com.customerApplicationService.Customer_Application_Service.Repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.customerApplicationService.Customer_Application_Service.Entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;


import java.util.Arrays;
import java.util.List;
import java.util.Map;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServices {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;

//    @Value() - to assign default values to variables and method arguments
    @Value("${sunbasedata.api.auth.url}")
    private String authUrl;

    @Value("${sunbasedata.api.data.url}")
    private String dataUrl;


//     Adds a new customer to the database.
    public Customer addCustomer(Customer customer) throws Exception{
        try {
            return customerRepository.save(customer);
        }
        catch (Exception e){
            throw new Exception("Something went wrong please check then try again");
        }
    }


//     Fetch customer by their ID.
    public Customer getCustomerById(int customerId) throws  Exception{

        Optional<Customer> customer = customerRepository.findById(customerId);

        if(customer.isEmpty()){
            throw new Exception("Customer not found");
        }
        return customer.get();
    }

//     Fetch all customers with pagination.
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

//     Searches for customers based on a search term.
    public List<Customer> searchCustomers(String searchTerm) {
        return customerRepository.searchCustomers(searchTerm);
    }

//     Updates details of an existing customer.
    public Customer updateCustomerDetails(int cutomerId, Customer updateCustomer) throws Exception {

        try {
            Customer existingCustomer  =  customerRepository.findById(cutomerId).orElse(null);

            if(existingCustomer != null){
                existingCustomer.setFirst_name(updateCustomer.getFirst_name());
                existingCustomer.setLast_name(updateCustomer.getLast_name());
                existingCustomer.setCity(updateCustomer.getCity());
                existingCustomer.setEmail(updateCustomer.getEmail());
                existingCustomer.setAddress(updateCustomer.getAddress());
                existingCustomer.setState(updateCustomer.getState());
                existingCustomer.setStreet(updateCustomer.getStreet());
                existingCustomer.setPhone(updateCustomer.getPhone());

                return customerRepository.save(existingCustomer);
            }
            else {
                return null;
            }
        }
        catch (Exception e){
            throw new Exception("Customer not found");
        }
    }

//     Deletes a customer by their ID.
    public void deleteCustomerById(int customerId) throws Exception {

        try {
            Optional<Customer> customer = customerRepository.findById(customerId);

            if(customer.isEmpty()){
                throw new Exception("Customer not found");
            }
            else {
                customerRepository.deleteById(customerId);
            }

        } catch (EmptyResultDataAccessException e) {
            throw new Exception("Invalid id: " + customerId, e);
        }
    }

    private List<Customer> fetchCustomerList(String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);

        try {
            ResponseEntity<Customer[]> response = restTemplate.exchange(dataUrl, HttpMethod.GET, new HttpEntity<>(headers), Customer[].class);

            return Arrays.asList(response.getBody());
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                throw new RuntimeException("Unauthorized access to customer list");
            } else if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new RuntimeException("Customer list not found");
            }
            throw e;
        }
    }

    public List<Customer> addDataCustomer(){
        String apiUrl = "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
        HttpHeaders headers = new HttpHeaders();
        String bearerToken = "dGVzdEBzdW5iYXNlZGF0YS5jb206VGVzdEAxMjM=";
        headers.set("Authorization", "Bearer "+ bearerToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Customer[]> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Customer[].class);

        Customer[] objects = response.getBody();
        for(Customer cust : objects){
            customerRepository.save(cust);
        }
        return List.of(objects);

    }


//     Helper method to convert a list of maps to a list of objects of the specified type
    private <T> List<T> convertToList(List<Map<String, Object>> list, Class<T> targetType) {
        return list.stream()
                .map(map -> new ObjectMapper().convertValue(map, targetType))
                .collect(Collectors.toList());
    }


    private void saveOrUpdateCustomers(List<Customer> customers) {
        customers.forEach(customerRepository::save);
    }
}

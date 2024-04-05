package com.customerApplicationService.Customer_Application_Service.Controller;

import com.customerApplicationService.Customer_Application_Service.Entity.Customer;
import com.customerApplicationService.Customer_Application_Service.Service.CustomerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/home")
//http://localhost:8080/home
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerServices customerServices;

//     Endpoint for creating a new customer.
//    http://localhost:8080/home/api/create
    @PostMapping("/api/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) throws Exception{
        try{
            Customer customerDetails = customerServices.addCustomer(customer);
            return ResponseEntity.ok(customerDetails);
        }
        catch (Exception e){
            return ResponseEntity.status(500).build();
        }
    }

//     Endpoint for retrieving a paginated list of customers.
//    http://localhost:8080/home/api/getlist
    @GetMapping("/api/getlist")
    public ResponseEntity<Page<Customer>> getList(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) throws Exception {
        try {
            Page<Customer> customerPage = customerServices.getAllCustomers(PageRequest.of(page, size));
            return ResponseEntity.ok(customerPage);
        } catch (Exception e) {
            throw new Exception("Internal Server Issue");
        }
    }

//     Endpoint for retrieving customer details by ID.
//    http://localhost:8080/home/api/CustomerById/1 or 2 or 3 etc
    @GetMapping("/api/CustomerById/{customerId}")
    public ResponseEntity findById(@PathVariable int customerId) throws Exception{
        try{
            return ResponseEntity.ok(customerServices.getCustomerById(customerId));
        }
        catch (Exception e){
//            throw new Exception("Customer Not Found");
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    Endpoint for searching customers by a searchTerm.
//    http://localhost:8080/home/api/customers/search
    @GetMapping("/api/customers/search")
    public List<Customer> searchCustomers(@RequestParam String searchTerm) {
        return customerServices.searchCustomers(searchTerm);
    }

//    http://localhost:8080/home/create/api/customers/search/sunbase
    @GetMapping("/api/customers/search/sunbase")
    public List<Customer> searchCustomers() {
        return customerServices.addDataCustomer();
    }


//     Endpoint for updating customer details by ID.
//    http://localhost:8080/home/api/updateDetails/1 or 2 or 3 etc
    @PutMapping("/api/updateDetails/{id}")
    public ResponseEntity<Customer> updateCustomerDetails(
            @PathVariable int id,
            @RequestBody Customer updateCustomer) {
        try {
            Customer updatedCustomer = customerServices.updateCustomerDetails(id, updateCustomer);

            if (updatedCustomer != null) {
                return ResponseEntity.ok(updatedCustomer);
            } else {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//     Endpoint for deleting a customer by ID.
//    http://localhost:8080/home/api/deleteById/1 or 2 or 3 etc
    @DeleteMapping("/api/deleteById/{customerId}")
    public ResponseEntity deleteById(@PathVariable int customerId) {
        try {
            customerServices.deleteCustomerById(customerId);
            return ResponseEntity.ok("Customer deleted from DB");
        } catch (Exception e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}

package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.CustomerDetailsBO;
import com.example.demo.bo.VendorDetailsBO;
import com.example.demo.services.CustomerDataService;

@CrossOrigin(origins = "*")
@RestController(value="/customer")
public class CustomerController {
	
	@Autowired
	private CustomerDataService customerDataService;
	
	@GetMapping(path="/customer")
	public List<CustomerDetailsBO> getAllCustomers(){
		return customerDataService.getAllCustomersDetails();
	}
	
	@PostMapping(path="/add-new-customer")
	public String addNewCustomer(@RequestBody CustomerDetailsBO customerBO){
		return customerDataService.addNewCustomer(customerBO);
	}
	
	@GetMapping(path="get-customer-list")
	public List<CustomerDetailsBO> getVendorList(){
		return customerDataService.getCustomerList();
	}
	
	@GetMapping(path="/get-customer-details")
	public List<CustomerDetailsBO> getVendorDetails(){
		return customerDataService.getCustomerDetails();
	}
}

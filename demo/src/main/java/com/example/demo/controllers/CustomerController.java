package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.CustomerDetailsBO;
import com.example.demo.services.CustomerDataService;

@RestController(value="/customer")
public class CustomerController {
	
	@Autowired
	private CustomerDataService customerDataService;
	
	@GetMapping(path="/customer")
	public List<CustomerDetailsBO> getAllCustomers(){
		return customerDataService.getAllCustomersDetails();
	}
}

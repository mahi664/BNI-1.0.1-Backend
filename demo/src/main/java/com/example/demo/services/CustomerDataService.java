package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.bo.CustomerDetailsBO;

@Service
public class CustomerDataService {

	public List<CustomerDetailsBO> getAllCustomersDetails() {
		ArrayList<CustomerDetailsBO> customerList = new ArrayList<>();
		
		CustomerDetailsBO customerDetailsBO = new CustomerDetailsBO();
		customerDetailsBO.setCustomerId("201900001");
		customerDetailsBO.setCustomerName("Mahesh Ghuge");
		customerDetailsBO.setAddress("Ghotan");
		customerList.add(customerDetailsBO);
		
		customerDetailsBO = new CustomerDetailsBO();
		customerDetailsBO.setCustomerId("201900002");
		customerDetailsBO.setCustomerName("Bharat Gadhe");
		customerDetailsBO.setAddress("Shevgaon");
		customerList.add(customerDetailsBO);
		
		customerDetailsBO = new CustomerDetailsBO();
		customerDetailsBO.setCustomerId("201900003");
		customerDetailsBO.setCustomerName("Aditya Dikshit");
		customerDetailsBO.setAddress("Nashik");
		customerList.add(customerDetailsBO);
		
		return customerList;
	}

}

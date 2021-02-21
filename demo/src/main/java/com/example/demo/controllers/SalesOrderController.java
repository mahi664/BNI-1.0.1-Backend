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
import com.example.demo.services.SalesOrderService;

@CrossOrigin(origins = "*")
@RestController
public class SalesOrderController {
	
	@Autowired
	private SalesOrderService salesOrderService;
	
	@PostMapping(path="/add-new-sales-order")
	public String addNewSalesOrder(@RequestBody CustomerDetailsBO customerDetBO){
		return salesOrderService.addNewSalesOrder(customerDetBO);
	}
	
	@GetMapping(path="/get-all-sales-invoices")
	public List<CustomerDetailsBO> getSalesInvoices(){
		return salesOrderService.getSalesInvoices();
	}
	
	@GetMapping(path="/get-next-invoice-id")
	public int getNextInvoiceId(){
		return salesOrderService.getNextInvoiceId();
	}
}

package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.PurchaseDetailsBO;
import com.example.demo.bo.VendorDetailsBO;
import com.example.demo.services.PurchaseOrderService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class PurchaseOrderController {
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@PostMapping(path="/add-new-purchase-invoice")
	public String addNewPurchaseOrder(@RequestBody VendorDetailsBO vendorDetBO){
		return purchaseOrderService.addNewPurchaseOrder(vendorDetBO);
	}
}

package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.PurchaseDetailsBO;
import com.example.demo.services.PurchaseOrderService;

@RestController
public class PurchaseOrderController {
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@PostMapping(path="/add-new-purchase-order")
	public String addNewPurchaseOrder(@RequestBody List<PurchaseDetailsBO> purchaseBOs){
		return purchaseOrderService.addNewPurchaseOrder(purchaseBOs);
	}
}

package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.VendorDetailsBO;
import com.example.demo.services.VendorService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class VendorController {
	
	@Autowired
	private VendorService vendorService;
	
	@PostMapping(path="/add-new-vendor")
	public String addNewVendor(@RequestBody VendorDetailsBO vendorDetBO){
		return vendorService.addNewVendor(vendorDetBO);
	}
	
	@GetMapping(path="/get-vendor-details")
	public List<VendorDetailsBO> getVendorDetails(){
		return vendorService.getVendorDetails();
	}
	
	@GetMapping(path="get-vendor-list")
	public List<VendorDetailsBO> getVendorList(){
		return vendorService.getVendorList();
	}
}

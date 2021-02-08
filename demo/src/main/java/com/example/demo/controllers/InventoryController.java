package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.bo.GstDetailsBO;
import com.example.demo.bo.ProductDisplayDetailsBO;
import com.example.demo.bo.ProductDetailsBO;
import com.example.demo.services.InventoryService;

@CrossOrigin(origins = "*")
@RestController
public class InventoryController {
	
	@Autowired
	InventoryService inventoryService;
	
//	@Autowired
//	StorageService storageService;
	
	@PostMapping(path="/add-new-product")
	public String addNewProduct(@RequestBody ProductDetailsBO pBO){
		List<ProductDetailsBO> productList = new ArrayList<>();
		productList.add(pBO);
		return inventoryService.addNewProduct(productList);
	}
	
//	@PostMapping(path="/add-new-products")
//	public String addNewProducts(@RequestParam MultipartFile fileName) throws FileNotFoundException{
//		storageService.store(fileName);
//		return inventoryService.addNewProducts(fileName.getOriginalFilename());
//	}
	
	@PostMapping(path="/update-products")
	public String updateProducts(@RequestBody List<ProductDetailsBO> productList){
		return inventoryService.updateProducts(productList);
	}
	
	@PostMapping(path="/add-gst-rate")
	public String addGstDetails(@RequestBody GstDetailsBO gstDet) {
		return inventoryService.addGstDetails(gstDet);
	}
	
	@GetMapping(path="/get-gst-rates")
	public List<GstDetailsBO> getGstRates(){
		return inventoryService.getGstRates();
		
	}
	
	@GetMapping(path="/get-inventory-details")
	public List<ProductDisplayDetailsBO> getInventoryDetails(){
		return inventoryService.getInventoryDetails();
	}
	
	@GetMapping(path="/get-product-names")
	public List<String> getProductNames(){
		return inventoryService.getProductNames();
	}
	
	@GetMapping(path="/get-batch-numbers")
	public List<String> getBatchNos(){
		return inventoryService.getBatchNos();
	}
	
	@GetMapping(path="/get-product-gst-map")
	public Map<String, Double> getProductGstMap(){
		return inventoryService.getProductGstMap();
	}
	
	@GetMapping(path="/get-product-name-id-map")
	public Map<String, Integer> getProductToIdMap(){
		return inventoryService.getProductToIdMap();
	}
	
	@PostMapping(path="/get-product-obj")
	public ProductDetailsBO getProductObj(@RequestBody String batchNo){
		return inventoryService.getProductObj(batchNo);
	}
}

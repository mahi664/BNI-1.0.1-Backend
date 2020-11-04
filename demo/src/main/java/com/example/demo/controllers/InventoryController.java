package com.example.demo.controllers;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.bo.ProductDetailsBO;
import com.example.demo.services.InventoryService;
import com.example.demo.storage.StorageService;

@RestController
public class InventoryController {
	
	@Autowired
	InventoryService inventoryService;
	
	@Autowired
	StorageService storageService;
	
	@PostMapping(path="/add-new-product")
	public String addNewProduct(@RequestBody ProductDetailsBO pBO){
		List<ProductDetailsBO> productList = new ArrayList<>();
		productList.add(pBO);
		return inventoryService.addNewProduct(productList);
	}
	
	@PostMapping(path="/add-new-products")
	public String addNewProducts(@RequestParam MultipartFile fileName) throws FileNotFoundException{
		storageService.store(fileName);
		return inventoryService.addNewProducts(fileName.getOriginalFilename());
	}
	
	@PostMapping(path="/update-products")
	public String updateProducts(@RequestBody List<ProductDetailsBO> productList){
		return inventoryService.updateProducts(productList);
	}
}

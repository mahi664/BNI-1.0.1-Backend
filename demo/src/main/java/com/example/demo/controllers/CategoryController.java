package com.example.demo.controllers;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.bo.CategoryDetailsBO;
import com.example.demo.services.CategoryService;
import com.example.demo.storage.StorageService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class CategoryController {

	@Autowired
	StorageService storageService;
	
	@Autowired
	CategoryService categoryService;
	
	@PostMapping(path="/add-new-category")
	public ResponseEntity<String> addNewCategory(@RequestBody CategoryDetailsBO catBO){
		List<CategoryDetailsBO> categoryList = new ArrayList<>();
		categoryList.add(catBO);
		String retMsg = categoryService.addNewCategory(categoryList);
		return new ResponseEntity<String>(retMsg, HttpStatus.OK);
	}
	
	@PostMapping(path="/add-new-categories-test")
	public String addNewCategories(@RequestParam String fileName) throws FileNotFoundException{
		return categoryService.addNewCategories(fileName);
	}
	
	@PostMapping(path="/add-new-categories")
	public String addNewCategories(@RequestParam MultipartFile fileName) throws FileNotFoundException{
		storageService.store(fileName);
		return categoryService.addNewCategories(fileName.getOriginalFilename());
	}
	
	@PostMapping(path="/update-category")
	public String updateCategory(@RequestBody List<CategoryDetailsBO> categoryList){
		return categoryService.updateCategories(categoryList);
	}
	
	@PostMapping(path="/delete-category")
	public String deleteCategory(@RequestBody List<Integer> categoryIds){
		return categoryService.deleteCategories(categoryIds);
	}
	
	@GetMapping(path="/categories")
	public List<CategoryDetailsBO> getAllCategories(){
		return categoryService.getAllCategories();
	}
	
	@PostMapping(path="/quick-search-category")
	public List<CategoryDetailsBO> getCategoriesOnQuickSearch(@RequestBody String filterParam){
		return categoryService.getQuickSearchedCategory(filterParam);
	}
}

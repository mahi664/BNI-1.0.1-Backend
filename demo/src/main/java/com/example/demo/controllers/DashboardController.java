package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.DashboardStatisticsBO;
import com.example.demo.services.DashboardService;

@CrossOrigin(origins="*")
@RestController
public class DashboardController {
	
	@Autowired
	private DashboardService dashboardService;
	
	@GetMapping(path="/getDashboardStatistics")
	public DashboardStatisticsBO getDashboardStatistics(){
		return dashboardService.getDashboardStatistics();
	}
}

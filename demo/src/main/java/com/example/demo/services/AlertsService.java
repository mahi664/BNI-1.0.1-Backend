package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.utils.Constants;

@Service
public class AlertsService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public String getAlert(String alertType, String alert) {
		String retString = Constants.EMPTY_STRING;
		retString+=alertType+":";
		String alertDiscription = getAlertDiscription(alert);
		retString+=alertDiscription;
		return retString;
	}

	private String getAlertDiscription(String alert) {
		String query = "select ALERT_DISCRIPTION from alerts where ALERT_NAME = ?";
		return jdbcTemplate.queryForObject(query, String.class, alert);
	}
	
	
}

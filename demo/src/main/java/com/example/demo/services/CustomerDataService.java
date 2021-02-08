package com.example.demo.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import com.example.demo.bo.CustomerDetailsBO;
import com.example.demo.bo.VendorDetailsBO;

@Service
public class CustomerDataService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<CustomerDetailsBO> getAllCustomersDetails() {
		ArrayList<CustomerDetailsBO> customerList = new ArrayList<>();
		
//		CustomerDetailsBO customerDetailsBO = new CustomerDetailsBO();
//		customerDetailsBO.setCustomerId("201900001");
//		customerDetailsBO.setCustomerName("Mahesh Ghuge");
//		customerDetailsBO.setAddress("Ghotan");
//		customerList.add(customerDetailsBO);
//		
//		customerDetailsBO = new CustomerDetailsBO();
//		customerDetailsBO.setCustomerId("201900002");
//		customerDetailsBO.setCustomerName("Bharat Gadhe");
//		customerDetailsBO.setAddress("Shevgaon");
//		customerList.add(customerDetailsBO);
//		
//		customerDetailsBO = new CustomerDetailsBO();
//		customerDetailsBO.setCustomerId("201900003");
//		customerDetailsBO.setCustomerName("Aditya Dikshit");
//		customerDetailsBO.setAddress("Nashik");
//		customerList.add(customerDetailsBO);
		
		return customerList;
	}

	public String addNewCustomer(CustomerDetailsBO customerBO) {
		int customerId = getMaxCustomerId();
		customerBO.setCustomerId(++customerId);
		int ret = insertCustomerDet(customerBO);
		if(ret<=0)
			return "Error in adding new customer details";
		return "Customer details added successfully";
	}

	private int getMaxCustomerId() {
		String query = "SELECT MAX(CUSTOMER_ID) FROM CUSTOMER_DET";
		String customerId = jdbcTemplate.queryForObject(query, String.class);
		if (customerId == null)
			return 0;
		return Integer.parseInt(customerId);
	}

	private int insertCustomerDet(CustomerDetailsBO customerBO) {
		String query = "INSERT INTO CUSTOMER_DET(CUSTOMER_ID,NAME,PHONE,EMAIL,VILLAGE,CITY,DISTRICT,"
				+ " STATE,PIN_CODE,UID_NO,GST_NO,PAN_NO) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		return jdbcTemplate.update(query, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, customerBO.getCustomerId());
				ps.setString(2, customerBO.getCustomerName());
				ps.setString(3, customerBO.getPhone());
				ps.setString(4, customerBO.getEmail());
				ps.setString(5, customerBO.getVillage());
				ps.setString(6, customerBO.getCity());
				ps.setString(7, customerBO.getDistrict());
				ps.setString(8, customerBO.getState());
				ps.setString(9, customerBO.getPinCode());
				ps.setString(10, customerBO.getUidNo());
				ps.setString(11, customerBO.getGstNo());
				ps.setString(12, customerBO.getPanNo());
			}
			
		});
	}
	
	public List<CustomerDetailsBO> getCustomerList() {
		String query = "SELECT CUSTOMER_ID,NAME FROM CUSTOMER_DET";
		return jdbcTemplate.query(query, new ResultSetExtractor<List<CustomerDetailsBO>>(){

			@Override
			public List<CustomerDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<CustomerDetailsBO> customerList = new ArrayList<>();
				while(rs.next()){
					CustomerDetailsBO vendorBO = new CustomerDetailsBO();
					vendorBO.setCustomerId(rs.getInt(1));
					vendorBO.setCustomerName(rs.getString(2));
					customerList.add(vendorBO);
				}
				return customerList;
			}
			
		});
	}
}

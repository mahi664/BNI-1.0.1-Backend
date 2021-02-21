package com.example.demo.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import com.example.demo.bo.CustomerDetailsBO;
import com.example.demo.bo.InvoiceDetailsBO;
import com.example.demo.bo.PurchaseDetailsBO;
import com.example.demo.bo.VendorDetailsBO;
import com.example.demo.utils.DateUtils;

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
			return "ERROR:Error in adding new customer details";
		return "SUCCESS:Customer details added successfully";
	}

	private int getMaxCustomerId() {
		String query = "SELECT MAX(CUSTOMER_ID) FROM customer_det";
		String customerId = jdbcTemplate.queryForObject(query, String.class);
		if (customerId == null)
			return 0;
		return Integer.parseInt(customerId);
	}

	private int insertCustomerDet(CustomerDetailsBO customerBO) {
		String query = "INSERT INTO customer_det(CUSTOMER_ID,NAME,PHONE,EMAIL,VILLAGE,CITY,DISTRICT,"
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
		String query = "SELECT CUSTOMER_ID,NAME FROM customer_det";
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

	public List<CustomerDetailsBO> getCustomerDetails() {
		Map<Integer, CustomerDetailsBO> customerId2CustomerDetM = new HashMap<>();
		getCustomerBasicDetails(customerId2CustomerDetM);
		getCustomerPurchaseReceipts(customerId2CustomerDetM);
		return new ArrayList<>(customerId2CustomerDetM.values());
	}

	private void getCustomerPurchaseReceipts(Map<Integer, CustomerDetailsBO> customerId2CustomerDetM) {
		String query = "select ORDER_ID,CUSTOMER_ID,DATE_SKEY,DUE_AMT,TOTAL_AMT from order_customer_map";
		jdbcTemplate.query(query, new ResultSetExtractor<Void>() {

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					InvoiceDetailsBO salesOrdBO = new InvoiceDetailsBO();
					salesOrdBO.setInvoiceId("RC-"+rs.getString(1));
					salesOrdBO.setInvoiceDateSkey(rs.getInt(3));
					salesOrdBO.setInvoiceDate(DateUtils.getDate(salesOrdBO.getInvoiceDateSkey()));
					salesOrdBO.setDueAmt(rs.getDouble(4));
					salesOrdBO.setTotalAmt(rs.getDouble(5));
					salesOrdBO.setPaidAmt(salesOrdBO.getTotalAmt() - salesOrdBO.getDueAmt());

					List<InvoiceDetailsBO> purchaseOrdeL = customerId2CustomerDetM.get(rs.getInt(2)).getInvoices();
					if (purchaseOrdeL == null)
						purchaseOrdeL = new ArrayList<>();
					purchaseOrdeL.add(salesOrdBO);
					customerId2CustomerDetM.get(rs.getInt(2)).setInvoices(purchaseOrdeL);
					customerId2CustomerDetM.get(rs.getInt(2)).setTotalDueAmt(customerId2CustomerDetM.get(rs.getInt(2)).getTotalDueAmt()+salesOrdBO.getDueAmt());
				}
				return null;
			}

		});
	}

	private void getCustomerBasicDetails(Map<Integer, CustomerDetailsBO> customerId2CustomerDetM) {
		String query = "select CUSTOMER_ID,NAME,CITY,DISTRICT,STATE,PHONE,EMAIL,UID_NO,VILLAGE,PIN_CODE, GST_NO,PAN_NO "+ 
					   "from customer_det";
		 jdbcTemplate.query(query, new ResultSetExtractor<Void>(){

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					CustomerDetailsBO customerBO = new CustomerDetailsBO();
					customerBO.setCustomerId(rs.getInt(1));
					customerBO.setCustomerName(rs.getString(2));
					customerBO.setCity(rs.getString(3));
					customerBO.setDistrict(rs.getString(4));
					customerBO.setState(rs.getString(5));
					customerBO.setPhone(rs.getString(6));
					customerBO.setEmail(rs.getString(7));
					customerBO.setUidNo(rs.getString(8));
					customerBO.setVillage(rs.getString(9));
					customerBO.setPinCode(rs.getString(10));
					customerBO.setGstNo(rs.getString(11));
					customerBO.setPanNo(rs.getString(12));
					
					customerId2CustomerDetM.put(customerBO.getCustomerId(), customerBO);
				}
				
				return null;
			}
			
		});
	}
}

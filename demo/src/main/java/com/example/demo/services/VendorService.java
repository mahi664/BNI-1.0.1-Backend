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

import com.example.demo.bo.VendorDetailsBO;

@Service
public class VendorService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public String addNewVendor(VendorDetailsBO vendorDetBO) {
		//TODO: 1. Check Vendor is existing. 2. Fetch next vendor Id to use.
		int vendorId = getMaxVendorId();
		vendorDetBO.setVendorId(++vendorId);
		int ret = insertVendorDets(vendorDetBO);
		if(ret==1)
			return "Vendor Details Added Successfully!!";
		return "Error in adding Vendor details!!";
	}

	private int getMaxVendorId() {
		String query = "SELECT MAX(VENDOR_ID) FROM VENDOR_DET";
		String vendorId = jdbcTemplate.queryForObject(query, String.class);
		if (vendorId == null)
			return 0;
		return Integer.parseInt(vendorId);
	}

	private int insertVendorDets(VendorDetailsBO vendorDetBO) {
		String query = "INSERT INTO VENDOR_DET (VENDOR_ID,VENDOR_NAME,CITY,DISTRICT,STATE,PHONE,"+
						"EMAIL,GST_NO,UID_NO,VILLAGE,PIN_CODE,PAN_NO) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		return jdbcTemplate.update(query, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setInt(1, vendorDetBO.getVendorId());
				ps.setString(2, vendorDetBO.getVendorName());
				ps.setString(3, vendorDetBO.getCity());
				ps.setString(4, vendorDetBO.getDistrict());
				ps.setString(5, vendorDetBO.getState());
				ps.setString(6, vendorDetBO.getPhone());
				ps.setString(7, vendorDetBO.getEmail());
				ps.setString(8, vendorDetBO.getGstNo());
				ps.setString(9, vendorDetBO.getUidNo());
				ps.setString(10, vendorDetBO.getVillage());
				ps.setString(11, vendorDetBO.getPinCode());
				ps.setString(12, vendorDetBO.getPanNo());
			}
			
		});
	}

	public List<VendorDetailsBO> getVendorDetails() {
		String query = "select VENDOR_ID,VENDOR_NAME,CITY,DISTRICT,STATE,PHONE,EMAIL,UID_NO,VILLAGE,"+
				"PIN_CODE, GST_NO,PAN_NO from vendor_det";
		return jdbcTemplate.query(query, new ResultSetExtractor<List<VendorDetailsBO>>(){

			@Override
			public List<VendorDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<VendorDetailsBO> vendorDetailsL = new ArrayList<>();
				while(rs.next()){
					VendorDetailsBO vendorBO = new VendorDetailsBO();
					vendorBO.setVendorId(rs.getInt(1));
					vendorBO.setVendorName(rs.getString(2));
					vendorBO.setCity(rs.getString(3));
					vendorBO.setDistrict(rs.getString(4));
					vendorBO.setState(rs.getString(5));
					vendorBO.setPhone(rs.getString(6));
					vendorBO.setEmail(rs.getString(7));
					vendorBO.setUidNo(rs.getString(8));
					vendorBO.setVillage(rs.getString(9));
					vendorBO.setPinCode(rs.getString(10));
					vendorBO.setGstNo(rs.getString(11));
					vendorBO.setPanNo(rs.getString(12));
					
					vendorDetailsL.add(vendorBO);
				}
				return vendorDetailsL;
			}
			
		});
	}

}

package com.example.demo.services;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import com.example.demo.bo.ProductDetailsBO;
import com.example.demo.bo.PurchaseDetailsBO;

@Service
public class PurchaseOrderService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public String addNewPurchaseOrder(List<PurchaseDetailsBO> purchaseBOs) {
		for(int i=0;i<purchaseBOs.size();i++){
			insertVendorInvoiceMap(purchaseBOs.get(i).getVendorId(),purchaseBOs.get(i).getInvoiceId(),purchaseBOs.get(i).getInvoiceDate());
			int[] ret = insertPurchaseOrder(purchaseBOs.get(i).getInvoiceId(),purchaseBOs.get(i).getProductsList());
			if (ret.length <= 0)
				return "Error in inserting purchase order";
		}
		return "Purchase order added successfully";

	}

	private int insertVendorInvoiceMap(String vendorId, String invoiceId, int invoiceDate) {
		String query = "INSERT INTO VENDOR_RECEIPT_MAP(VENDOR_ID,INVOICE_ID,DATE_SKEY) "+
						"VALUES(?,?,?)";
		return jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, vendorId);
				ps.setString(2, invoiceId);
				ps.setInt(3, invoiceDate);
			}
		});
	}

	private int[] insertPurchaseOrder(String invoiceId,List<ProductDetailsBO> productsList) {
		String query = "INSERT INTO PURCHASE_ORDER_DET(PRODUCT_ID,INVOICE_ID,BATCH_NO,QUANTITY,PRICE,"
				+ "COST,GST,MFG_DATE,EXP_DATE) VALUES(?,?,?,?,?,?,?,?,?)";
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productsList.get(i).getProductId());
				ps.setString(2, invoiceId);
				ps.setString(3, productsList.get(i).getBatchNo());
				ps.setInt(4, productsList.get(i).getQuantity());
				ps.setDouble(5, productsList.get(i).getPrice());
				ps.setDouble(6, productsList.get(i).getCost());
				ps.setDouble(7, productsList.get(i).getSgst());
				ps.setDate(8, new Date(productsList.get(i).getMfgDate().getTime()));
				ps.setDate(9, new Date(productsList.get(i).getExpDate().getTime()));
			}

			@Override
			public int getBatchSize() {
				return productsList.size();
			}
		});
	}


}

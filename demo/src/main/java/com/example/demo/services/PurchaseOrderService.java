package com.example.demo.services;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import com.example.demo.bo.ProductDetailsBO;
import com.example.demo.bo.PurchaseDetailsBO;
import com.example.demo.bo.VendorDetailsBO;
import com.example.demo.utils.DateUtils;

@Service
public class PurchaseOrderService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private InventoryService inventoryService;

	@Transactional
	public String addNewPurchaseOrder(VendorDetailsBO vendorDetBO) {
//		try {
			int ret = insertVendorReceipt(vendorDetBO.getVendorId(), vendorDetBO.getInvoices().get(0));
			if(ret<=0){
				return "Error in saving new purchase order!";
			}
			int batRet[] = insertPurchaseOrderDetails(vendorDetBO.getInvoices().get(0).getInvoiceId(),vendorDetBO.getInvoices().get(0).getProductsList()); 
			if(batRet.length<=0)
				return "Error in saving new purchase order!";
			Map<Integer, Integer> product2AvailStockM = inventoryService.getProductAvailStockMap(vendorDetBO.getInvoices().get(0).getProductsList());
			updateProdToAvailStock(product2AvailStockM,vendorDetBO.getInvoices().get(0).getProductsList());
			batRet = inventoryService.updateProductStockMap(product2AvailStockM);
			return "New purchase order saved successfully!";
//		} catch (Exception e) {
//			e.printStackTrace();
//			
//			return "Error in saving new purchase order!";
//		}
		
	}

	private void updateProdToAvailStock(Map<Integer, Integer> product2AvailStockM,
			List<ProductDetailsBO> productsList) {
		for(ProductDetailsBO prodBO: productsList){
			int val = product2AvailStockM.get(prodBO.getProductId())+prodBO.getQuantity();
			product2AvailStockM.replace(prodBO.getProductId(), val);
		}
		
	}

	private int[] insertPurchaseOrderDetails(String invoiceId, List<ProductDetailsBO> productsList) {
		String query="INSERT INTO PURCHASE_ORDER_DET(PRODUCT_ID,INVOICE_ID,BATCH_NO,QUANTITY,COST,GST,EXP_DATE,IN_STOCK) "+
				" VALUES(?,?,?,?,?,?,?,?)";
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productsList.get(i).getProductId());
				ps.setString(2, invoiceId);
				ps.setString(3, productsList.get(i).getBatchNo());
				ps.setInt(4, productsList.get(i).getQuantity());
				ps.setDouble(5, productsList.get(i).getCost());
				ps.setDouble(6, productsList.get(i).getGst());
				ps.setDate(7, DateUtils.getSqlDate(productsList.get(i).getExpDate()));
				ps.setInt(8, productsList.get(i).getQuantity());
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return productsList.size();
			}
		});
		
	}


	private int insertVendorReceipt(int vendorId, PurchaseDetailsBO purchaseDetailsBO) {
		String query = "INSERT INTO VENDOR_RECEIPT_MAP (VENDOR_ID,INVOICE_ID,DATE_SKEY,DUE_AMT,TOTAL_AMT,DISCOUNT,PAYMENT_TYPE,CHEQUE_NO,GST_AMT,ACC_NO) "+
						"VALUES(?,?,?,?,?,?,?,?,?,?)";
		return jdbcTemplate.update(query, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, vendorId);
				ps.setString(2, purchaseDetailsBO.getInvoiceId());
				ps.setInt(3, DateUtils.dateSkey(purchaseDetailsBO.getInvoiceDate()));
				ps.setDouble(4, purchaseDetailsBO.getDueAmt());
				ps.setDouble(5, purchaseDetailsBO.getTotalAmt());
				ps.setDouble(6, purchaseDetailsBO.getDiscount());
				ps.setString(7, purchaseDetailsBO.getPaymentType());
				ps.setString(8, purchaseDetailsBO.getChequeNo());
				ps.setDouble(9, purchaseDetailsBO.getGstAmt());
				ps.setString(10,purchaseDetailsBO.getAccNo());
			}
			
		});
	} 


}

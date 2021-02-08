package com.example.demo.services;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
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

	private void updateProdToAvailStock(Map<Integer, Integer> product2AvailStockM,List<ProductDetailsBO> productsList) {
		for(ProductDetailsBO prodBO: productsList){
			if(product2AvailStockM.get(prodBO.getProductId())==null)
				product2AvailStockM.put(prodBO.getProductId(), 0);
			int val = product2AvailStockM.get(prodBO.getProductId()) + prodBO.getQuantity();
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

	public List<VendorDetailsBO> getAllPurchaseOrders() {
		Map<String, VendorDetailsBO> invoiceId2VendorDetM = new HashMap<>();
		getVendorReceiptMap(invoiceId2VendorDetM);
		getProductsForInvoices(invoiceId2VendorDetM);
		return new ArrayList<>(invoiceId2VendorDetM.values());
	}

	private void getProductsForInvoices(Map<String, VendorDetailsBO> invoiceId2VendorDetM) {
		String query = "select a.INVOICE_ID,a.PRODUCT_ID,b.PRODUCT_NAME,a.BATCH_NO,a.QUANTITY,a.COST,a.GST,a.EXP_DATE "+
						"from purchase_order_det a, product_det b "
						+ "where a.PRODUCT_ID = b.PRODUCT_ID";
		jdbcTemplate.query(query, new ResultSetExtractor<Void>(){

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					String invoiceID = rs.getString(1);
					if(invoiceId2VendorDetM.containsKey(invoiceID)){
						VendorDetailsBO vendorBO = invoiceId2VendorDetM.get(invoiceID);
						if(vendorBO!=null){
							List<PurchaseDetailsBO>invoices = vendorBO.getInvoices();
							if(invoices!=null && invoices.get(0)!=null){
								PurchaseDetailsBO invoiceBO = invoices.get(0);
								List<ProductDetailsBO> productsL = invoiceBO.getProductsList();
								if(productsL==null)
									productsL = new ArrayList<>();
								ProductDetailsBO prodBO = new ProductDetailsBO();
								prodBO.setProductId(rs.getInt(2));
								prodBO.setProductName(rs.getString(3));
								prodBO.setBatchNo(rs.getString(4));
								prodBO.setQuantity(rs.getInt(5));
								prodBO.setCost(rs.getDouble(6));
								prodBO.setGst(rs.getDouble(7));
								prodBO.setExpDate(new java.util.Date(rs.getDate(8).getTime()));
								prodBO.setTotal(getTotalValue(prodBO));
								productsL.add(prodBO);
								invoiceBO.setProductsList(productsL);
							}
						}
					}
				}
				return null;
			}

			private double getTotalValue(ProductDetailsBO prodBO) {
				double total = prodBO.getQuantity() * prodBO.getCost();
				total = total + total*(prodBO.getGst()/100);
				return total;
			}
			
		});
		
	}

	private void getVendorReceiptMap(Map<String, VendorDetailsBO> invoiceId2VendorDetM) {
		String query = "SELECT a.VENDOR_ID,a.VENDOR_NAME,b.INVOICE_ID,b.DATE_SKEY,b.DUE_AMT,b.TOTAL_AMT,b.DISCOUNT,"+
						"b.PAYMENT_TYPE,b.CHEQUE_NO,b.GST_AMT,b.ACC_NO "+
						"from vendor_det a, vendor_receipt_map b "+
						"where a.VENDOR_ID = b.VENDOR_ID";
		jdbcTemplate.query(query, new ResultSetExtractor<Void>(){

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				VendorDetailsBO vendorBO = null;
				while(rs.next()){
					String invoiceId = rs.getString(3);
					if(invoiceId2VendorDetM.get(invoiceId)==null)
						invoiceId2VendorDetM.put(invoiceId, new VendorDetailsBO());
					vendorBO = invoiceId2VendorDetM.get(invoiceId);
					if(vendorBO!=null){
						vendorBO.setVendorId(rs.getInt(1));
						vendorBO.setVendorName(rs.getString(2));
						List<PurchaseDetailsBO> invoices = vendorBO.getInvoices();
						if(invoices==null)
							invoices = new ArrayList<>();
						PurchaseDetailsBO invoice = invoices.size()<=0?null:invoices.get(0);
						if(invoice==null)
							invoice = new PurchaseDetailsBO();
						invoice.setInvoiceId(invoiceId);
						invoice.setInvoiceDate(DateUtils.getDate(rs.getInt(4)));
						invoice.setDueAmt(rs.getDouble(5));
						invoice.setTotalAmt(rs.getDouble(6));
						invoice.setDiscount(rs.getDouble(7));
						invoice.setPaymentType(rs.getString(8));
						invoice.setChequeNo(rs.getString(9));
						invoice.setGstAmt(rs.getDouble(10));
						invoice.setAccNo(rs.getString(11));
						
						invoices.add(invoice);
						vendorBO.setInvoices(invoices);
					}
				}
				return null;
			}
			
		});
		
	} 


}

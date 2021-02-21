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

import com.example.demo.bo.CustomerDetailsBO;
import com.example.demo.bo.InvoiceDetailsBO;
import com.example.demo.bo.ProductDetailsBO;
import com.example.demo.bo.PurchaseDetailsBO;
import com.example.demo.bo.VendorDetailsBO;
import com.example.demo.utils.DateUtils;

@Service
public class SalesOrderService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private InventoryService inventoryService;
	
	@Transactional
	public String addNewSalesOrder(CustomerDetailsBO customerDetBO) {
		int batRet[] = insertSalesOrderDetails(customerDetBO.getInvoices().get(0).getInvoiceId(),customerDetBO.getInvoices().get(0).getProductsList()); 
		if(batRet.length<=0)
			return "ERROR:Error in saving sales order details!";
		int ret = insertCustomerReceiptMap(customerDetBO.getCustomerId(), customerDetBO.getInvoices().get(0));
		if(ret<=0){
			return "ERROR:Error in adding Customer Receipt Map!";
		}
		Map<Integer, Integer> product2AvailStockM = inventoryService.getProductAvailStockMap(customerDetBO.getInvoices().get(0).getProductsList());
		updateProdToAvailStock(product2AvailStockM,customerDetBO.getInvoices().get(0).getProductsList());
		batRet = inventoryService.updateProductStockMap(product2AvailStockM);
		if(batRet.length<=0)
			return "ERROR:Error in updating product stock map!";
		Map<String,Integer> batchNo2AvailStockM = inventoryService.getBatchNoAvailStockMap(customerDetBO.getInvoices().get(0).getProductsList());
		updateBatchNoToAvailStock(batchNo2AvailStockM,customerDetBO.getInvoices().get(0).getProductsList());
		batRet = inventoryService.updateBatchStockMap(batchNo2AvailStockM);
		if(batRet.length<=0)
			return "ERROR:Error in updating batch no stock!";
		return "SUCCESS:New sales order saved successfully!";
	}

	private void updateBatchNoToAvailStock(Map<String, Integer> batchNo2AvailStockM,List<ProductDetailsBO> productsList) {
		for(ProductDetailsBO prodBO: productsList){
			int val = batchNo2AvailStockM.get(prodBO.getBatchNo())-prodBO.getQuantity();
			batchNo2AvailStockM.replace(prodBO.getBatchNo(), val);
		}
	}

	private void updateProdToAvailStock(Map<Integer, Integer> product2AvailStockM,List<ProductDetailsBO> productsList) {
		for(ProductDetailsBO prodBO: productsList){
			int val = product2AvailStockM.get(prodBO.getProductId())-prodBO.getQuantity();
			product2AvailStockM.replace(prodBO.getProductId(), val);
		}
		
	}

	private int[] insertSalesOrderDetails(String invoiceId, List<ProductDetailsBO> productsList) {
		String query="INSERT INTO order_det(PRODUCT_ID,ORDER_ID,BATCH_NO,QUANTITY,RATE,GST,EXP_DATE) "+
				" VALUES(?,?,?,?,?,?,?)";
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productsList.get(i).getProductId());
				ps.setString(2, invoiceId);
				ps.setString(3, productsList.get(i).getBatchNo());
				ps.setInt(4, productsList.get(i).getQuantity());
				ps.setDouble(5, productsList.get(i).getCost());
				ps.setDouble(6, productsList.get(i).getGst());
				ps.setDate(7, new Date(productsList.get(i).getExpDate().getTime()));
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return productsList.size();
			}
		});
	}

	private int insertCustomerReceiptMap(int customerId, InvoiceDetailsBO invoiceDetailsBO) {
		String query = "INSERT INTO order_customer_map  (CUSTOMER_ID, ORDER_ID,DATE_SKEY,DUE_AMT,TOTAL_AMT,DISCOUNT,PAYMENT_TYPE,CHEQUE_NO,GST_AMT,ACC_NO) "+
				"VALUES(?,?,?,?,?,?,?,?,?,?)";
		return jdbcTemplate.update(query, new PreparedStatementSetter(){
		
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, customerId);
				ps.setInt(2, Integer.parseInt(invoiceDetailsBO.getInvoiceId()));
				ps.setInt(3, DateUtils.dateSkey(invoiceDetailsBO.getInvoiceDate()));
				ps.setDouble(4, invoiceDetailsBO.getDueAmt());
				ps.setDouble(5, invoiceDetailsBO.getTotalAmt());
				ps.setDouble(6, invoiceDetailsBO.getDiscount());
				ps.setString(7, invoiceDetailsBO.getPaymentType());
				ps.setString(8, invoiceDetailsBO.getChequeNo());
				ps.setDouble(9, invoiceDetailsBO.getGstAmt());
				ps.setString(10,invoiceDetailsBO.getAccNo());
			}
			
		});
	}

	public List<CustomerDetailsBO> getSalesInvoices() {
		Map<String, CustomerDetailsBO> invoiceId2CustomerDetM = new HashMap<>();
		getCustomerReceiptMap(invoiceId2CustomerDetM);
		getProductsForInvoices(invoiceId2CustomerDetM);
		return new ArrayList<>(invoiceId2CustomerDetM.values());
	}

	private void getProductsForInvoices(Map<String, CustomerDetailsBO> invoiceId2CustomerDetM) {
		String query = "select a.ORDER_ID,a.PRODUCT_ID,b.PRODUCT_NAME,a.BATCH_NO,a.QUANTITY,a.RATE,a.GST,a.EXP_DATE "+
						"from order_det a, product_det b "+
						"where a.PRODUCT_ID = b.PRODUCT_ID";
		jdbcTemplate.query(query, new ResultSetExtractor<Void>() {

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					String invoiceID = "RC-"+rs.getInt(1);
					if (invoiceId2CustomerDetM.containsKey(invoiceID)) {
						CustomerDetailsBO customerBO = invoiceId2CustomerDetM.get(invoiceID);
						if (customerBO != null) {
							List<InvoiceDetailsBO> invoices = customerBO.getInvoices();
							if (invoices != null && invoices.get(0) != null) {
								InvoiceDetailsBO invoiceBO = invoices.get(0);
								List<ProductDetailsBO> productsL = invoiceBO.getProductsList();
								if (productsL == null)
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
				total = total + total * (prodBO.getGst() / 100);
				return total;
			}

		});

	}

	private void getCustomerReceiptMap(Map<String, CustomerDetailsBO> invoiceId2CustomerDetM) {
		String query = "select a.CUSTOMER_ID,a.NAME,b.ORDER_ID,b.DATE_SKEY,b.DUE_AMT,b.TOTAL_AMT,b.DISCOUNT,"+
					   "b.PAYMENT_TYPE,b.CHEQUE_NO,b.GST_AMT,b.ACC_NO "+
					   "from customer_det a, order_customer_map b "+
					   "where a.CUSTOMER_ID = b.CUSTOMER_ID";
		jdbcTemplate.query(query, new ResultSetExtractor<Void>() {

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				CustomerDetailsBO customerBO = null;
				while (rs.next()) {
					String invoiceId ="RC-"+rs.getInt(3);
					if (invoiceId2CustomerDetM.get(invoiceId) == null)
						invoiceId2CustomerDetM.put(invoiceId, new CustomerDetailsBO());
					customerBO = invoiceId2CustomerDetM.get(invoiceId);
					if (customerBO != null) {
						customerBO.setCustomerId(rs.getInt(1));
						customerBO.setCustomerName(rs.getString(2));
						List<InvoiceDetailsBO> invoices = customerBO.getInvoices();
						if (invoices == null)
							invoices = new ArrayList<>();
						InvoiceDetailsBO invoice = invoices.size() <= 0 ? null : invoices.get(0);
						if (invoice == null)
							invoice = new InvoiceDetailsBO();
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
						customerBO.setInvoices(invoices);
					}
				}
				return null;
			}

		});
	}

	public int getNextInvoiceId() {
		String query = "select max(ORDER_ID) from order_customer_map";
		return jdbcTemplate.queryForObject(query, Integer.class);
	}

}

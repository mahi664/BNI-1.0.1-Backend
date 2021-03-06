package com.example.demo.services;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bo.GstDetailsBO;
import com.example.demo.bo.InventoryBO;
import com.example.demo.bo.ProductDisplayDetailsBO;
import com.example.demo.bo.ProductDetailsBO;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;
import com.example.demo.utils.FileUploadUtils;

@Service
public class InventoryService {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CategoryService categoryService;

	@Autowired
	FileUploadUtils fileUplaod;

	@Transactional
	public String addNewProduct(List<ProductDetailsBO> productList) {
		int productId = getMaxProductId();
		Map<String, String> catName2IdMap = categoryService.getCategoryIdToNameMap();
		for (ProductDetailsBO prodBO : productList) {
			String errorMessage = validateProdDetBeforeInsert(prodBO,catName2IdMap);
			if(errorMessage!=null)
				return errorMessage;
			prodBO.setProductId(++productId);
		}

		int[] ret = insertProduct(productList);
		if (ret.length <= 0)
			return "ERROR:Error in adding products Basic details!!";
		Date effDate = new Date();
		ret = insertPoductStcokMap(productList,effDate);
		if(ret.length<=0)
			return "ERROR:Error in adding Product stock map";
		ret = insertProductToCategoryMap(productList, catName2IdMap, effDate);
		if (ret.length <= 0)
			return "ERROR:Error in adding products category!!";
		ret = insertProdToSGSTMap(productList, effDate);
		if (ret.length <= 0)
			return "ERROR:Error in adding products SGST details!!";
		ret = insertProdToCGSTMap(productList, effDate);
		if (ret.length <= 0)
			return "ERROR:Error in adding products CGST details!!";
		ret = insertProdToIGSTMap(productList, effDate);
		if (ret.length <= 0)
			return "ERROR:Error in adding products IGST details!!";
//		ret = insertProdToDiscountMap(productList, effDate);
//		if (ret.length <= 0)
//			return "Error in adding products discount details!!";
		return "SUCCESS:Products added successfully";
	}

	private int[] insertPoductStcokMap(List<ProductDetailsBO> productList, Date effDate) {
		String query = "INSERT INTO product_stock_map(PRODUCT_ID,AVAIL_STOCK,LAST_UPDATE_TIME,LAST_USER) "+
						"VALUES(?,?,?,?)";
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productList.get(i).getProductId());
				ps.setInt(2,0);
				ps.setDate(3, DateUtils.getSqlDate(effDate));
				ps.setString(4, null);
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return productList.size();
			}
		});
	}

	private String validateProdDetBeforeInsert(ProductDetailsBO prodBO, Map<String, String> catName2IdMap) {
		List<String> productNames = getProductNames();
		Map<String, List<Double>> gst2RatesMap = populateGSTRates();
		if (productNames.contains(prodBO.getProductName()))
			return "ERROR:Error in adding Product. Duplicate for " + prodBO.getProductName();
		if (!catName2IdMap.containsKey(prodBO.getCategoryName()))
			return "ERROR:Error in adding Product. Invalid Category Name: " + prodBO.getCategoryName();
//		if (prodBO.getCgst() != 0 && !gst2RatesMap.get(Constants.CGST).contains(prodBO.getCgst()))
//			return "Error in adding Product. Invalid CGST Rate: " + prodBO.getCgst();
//		if (prodBO.getSgst() != 0 && !gst2RatesMap.get(Constants.SGST).contains(prodBO.getSgst()))
//			return "Error in adding Product. Invalid SGST Rate: " + prodBO.getSgst();
		if (prodBO.getGst() != 0 && !gst2RatesMap.get(Constants.GST).contains(prodBO.getGst()))
			return "ERROR:Error in adding Product. Invalid GST Rate: " + prodBO.getIgst();
		if (prodBO.getUnit().equals(""))
			return "ERROR:Error in adding Product. Unit should not be blank";
		return null;
	}

	private int[] insertProdToSGSTMap(List<ProductDetailsBO> productList, Date effDate) {
		String query = "INSERT INTO product_sgst_map (PRODUCT_ID,GST_RATE,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME) "+
						Constants.VALUESFORINSERT;
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productList.get(i).getProductId());
				ps.setDouble(2, productList.get(i).getGst()/2);
				ps.setInt(3, DateUtils.dateSkey(effDate));
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
				ps.setDate(5, new java.sql.Date(new Date().getTime()));
			}

			@Override
			public int getBatchSize() {
				return productList.size();
			}
		});
	}
	
	private int[] insertProdToCGSTMap(List<ProductDetailsBO> productList, Date effDate) {
		String query = "INSERT INTO product_cgst_map (PRODUCT_ID,GST_RATE,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME) "+
						Constants.VALUESFORINSERT;
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productList.get(i).getProductId());
				ps.setDouble(2, productList.get(i).getGst());
				ps.setInt(3, DateUtils.dateSkey(effDate));
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
				ps.setDate(5, new java.sql.Date(new Date().getTime()));
			}

			@Override
			public int getBatchSize() {
				return productList.size();
			}
		});
	}

	private int[] insertProdToIGSTMap(List<ProductDetailsBO> productList, Date effDate) {
		String query = "INSERT INTO product_igst_map (PRODUCT_ID,GST_RATE,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME) "+
						Constants.VALUESFORINSERT;
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productList.get(i).getProductId());
				ps.setDouble(2, productList.get(i).getIgst());
				ps.setInt(3, DateUtils.dateSkey(effDate));
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
				ps.setDate(5, new java.sql.Date(new Date().getTime()));
			}

			@Override
			public int getBatchSize() {
				return productList.size();
			}
		});
	}
	
	private int[] insertProdToDiscountMap(List<ProductDetailsBO> productList, Date effDate) {
		String query = "INSERT INTO product_discount_map(PRODUCT_ID,DISCOUNT,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME)"
				+ Constants.VALUESFORINSERT;
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productList.get(i).getProductId());
				ps.setDouble(2, productList.get(i).getDiscount());
				ps.setInt(3, DateUtils.dateSkey(effDate));
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
				ps.setDate(5, new java.sql.Date(new Date().getTime()));
			}

			@Override
			public int getBatchSize() {

				return productList.size();
			}
		});
	}

	private Map<String, List<Double>> populateGSTRates() {
		Map<String, List<Double>> gstRatesMap = new HashMap<>();

		String query = "SELECT GST_RATE FROM gst_rates";
		List<Double> list = jdbcTemplate.queryForList(query, Double.class);
		gstRatesMap.put(Constants.GST, list);

//		query = "SELECT GST_RATE FROM SGST_RATES";
//		list = jdbcTemplate.queryForList(query, Double.class);
//		gstRatesMap.put(Constants.SGST, list);
//
//		query = "SELECT GST_RATE FROM IGST_RATES";
//		list = jdbcTemplate.queryForList(query, Double.class);
//		gstRatesMap.put(Constants.IGST, list);

		return gstRatesMap;
	}

	private int[] insertProductToCategoryMap(List<ProductDetailsBO> productList, Map<String, String> catName2IdMap,
			Date effDate) {
		String query = "INSERT INTO product_category_map(PRODUCT_ID,CATEGORY_ID,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME)"
				+ Constants.VALUESFORINSERT;
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productList.get(i).getProductId());
				ps.setString(2, catName2IdMap.get(productList.get(i).getCategoryName()));
				ps.setInt(3, DateUtils.dateSkey(effDate));
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
				ps.setDate(5, new java.sql.Date(new Date().getTime()));
			}

			@Override
			public int getBatchSize() {
				return productList.size();
			}
		});
	}

	private int[] insertProduct(List<ProductDetailsBO> productList) {
		String query = "INSERT INTO product_det(PRODUCT_ID,PRODUCT_NAME,DISP_NAME,PRODUCT_DESC,UNIT,"
				+ "MRP,MANUFACTURER,SELLING_PRICE,PACKAGING)"
				+ "VALUES(?,?,?,?,?,?,?,?,?)";
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productList.get(i).getProductId());
				ps.setString(2, productList.get(i).getProductName());
				ps.setString(3, productList.get(i).getDisplayName());
				ps.setString(4, productList.get(i).getDescription());
				ps.setString(5, productList.get(i).getUnit());
				ps.setDouble(6, productList.get(i).getMrp());
				ps.setString(7, productList.get(i).getManufacturer());
				ps.setDouble(8, productList.get(i).getSellingPrice());
				ps.setString(9, productList.get(i).getPackaging());
			}

			@Override
			public int getBatchSize() {
				return productList.size();
			}
		});
	}

	public List<String> getProductNames() {
		String query = "SELECT PRODUCT_NAME FROM product_det";
		return jdbcTemplate.queryForList(query, String.class);
	}
	
	public List<String> getBatchNos() {
		String query = "SELECT DISTINCT(BATCH_NO) FROM purchase_order_det WHERE"
				+ " EXP_DATE>=? AND IN_STOCK>0";
//		return jdbcTemplate.queryForList(query, String.class);
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setDate(1, new java.sql.Date(new Date().getTime()));
				
			}
		}, new ResultSetExtractor<List<String>>() {

			@Override
			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<String> retList = new ArrayList<>();
				while(rs.next()){
					retList.add(rs.getString(1));
				}
				return retList;
			}
		});
	}

	private int getMaxProductId() {
		String query = "SELECT MAX(PRODUCT_ID) FROM product_det";
		String catId = jdbcTemplate.queryForObject(query, String.class);
		if (catId == null)
			return 0;
		return Integer.parseInt(catId);
	}

	@Transactional
	public String addNewProducts(String fileName) throws FileNotFoundException {
		List<ProductDetailsBO> productList = fileUplaod.getProductListFromFile(fileName);
		return addNewProduct(productList);
	}

	@Transactional
	public String updateProducts(List<ProductDetailsBO> productList) {
		for (ProductDetailsBO prodBO : productList) {
			udateRequiredProductFields(prodBO);
		}
		return "Product Details updated successfully";
	}

	private void udateRequiredProductFields(ProductDetailsBO prodBO) {
		Map<String, String> catName2IdM = categoryService.getCategoryIdToNameMap();
		if (prodBO.getProductName() != null || prodBO.getDisplayName() != null || 
				prodBO.getDescription() != null || prodBO.getUnit()!=null || prodBO.getMrp()!=0 ||
				prodBO.getSellingPrice()!=0 || prodBO.getManufacturer()!=null || prodBO.getPackaging()!=null){
				
			updateProductBasicDetails(prodBO);
		}
			
		if (prodBO.getCategoryName() != null) {
			updateProductCategory(prodBO.getProductId());
			List<ProductDetailsBO> tempProdL = new ArrayList<>();
			tempProdL.add(prodBO);
			insertProductToCategoryMap(tempProdL, catName2IdM, new Date());
		}
		if (prodBO.getSellingPrice() != 0) {
			updateProductPrice(prodBO.getProductId());
			insertProductPrice(prodBO);
		}
		if (prodBO.getCost() != 0) {
			updateProductCost(prodBO.getProductId());
			insetProductCost(prodBO);
		}
		if (prodBO.getCgst() != 0) {
			updateProductCGSTMap(prodBO.getProductId());
			List<ProductDetailsBO> prodList = new ArrayList<>();
			prodList.add(prodBO);
			insertProdToCGSTMap(prodList, new Date());
		}
		if (prodBO.getSgst() != 0) {
			updateProductSGSTMap(prodBO.getProductId());
			List<ProductDetailsBO> prodList = new ArrayList<>();
			prodList.add(prodBO);
			insertProdToSGSTMap(prodList, new Date());
		}
		if (prodBO.getIgst() != 0) {
			updateProductIGSTMap(prodBO.getProductId());
			List<ProductDetailsBO> prodList = new ArrayList<>();
			prodList.add(prodBO);
			insertProdToIGSTMap(prodList, new Date());
		}
		if (prodBO.getDiscount() != 0) {
			updateProductDiscount(prodBO.getProductId());
			List<ProductDetailsBO> prodList = new ArrayList<>();
			prodList.add(prodBO);
			insertProdToDiscountMap(prodList, new Date());
		}
	}

	private int updateProductDiscount(int productId) {
		String query="UPDATE product_discount_map SET END_DATE_SKEY=?,LAST_UPDATE_TIME=?";
		query+=Constants.PIDANDENDDATEWHERECLAUSE;
		return jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, DateUtils.dateSkey(new Date()));
				ps.setDate(2, new java.sql.Date(new Date().getTime()));
				ps.setInt(3, productId);
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
			}
		});
	}

	private int updateProductCGSTMap(int productId) {
		String query="UPDATE product_cgst_map SET END_DATE_SKEY=?,LAST_UPDATE_TIME=?";
		query+=Constants.PIDANDENDDATEWHERECLAUSE;
		return jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, DateUtils.dateSkey(new Date()));
				ps.setDate(2, new java.sql.Date(new Date().getTime()));
				ps.setInt(3, productId);
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
			}
		});
	}
	
	private int updateProductSGSTMap(int productId) {
		String query="UPDATE product_sgst_map SET END_DATE_SKEY=?,LAST_UPDATE_TIME=?";
		query+=Constants.PIDANDENDDATEWHERECLAUSE;
		return jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, DateUtils.dateSkey(new Date()));
				ps.setDate(2, new java.sql.Date(new Date().getTime()));
				ps.setInt(3, productId);
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
			}
		});
	}
	
	private int updateProductIGSTMap(int productId) {
		String query="UPDATE product_igst_map SET END_DATE_SKEY=?,LAST_UPDATE_TIME=?";
		query+=Constants.PIDANDENDDATEWHERECLAUSE;
		return jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, DateUtils.dateSkey(new Date()));
				ps.setDate(2, new java.sql.Date(new Date().getTime()));
				ps.setInt(3, productId);
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
			}
		});
	}

	private int insetProductCost(ProductDetailsBO prodBO) {
		String query = "INSERT INTO product_cost_map(PRODUCT_ID,COST,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME)"
				+ Constants.VALUESFORINSERT;
		return jdbcTemplate.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, prodBO.getProductId());
				ps.setDouble(2, prodBO.getCost());
				ps.setInt(3, DateUtils.dateSkey(new Date()));
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
				ps.setDate(5, new java.sql.Date(new Date().getTime()));
			}
		});

	}

	private int updateProductCost(int productId) {
		String query = "UPDATE product_cost_map SET END_DATE_SKEY=?,LAST_UPDATE_TIME=? ";
		query+=Constants.PIDANDENDDATEWHERECLAUSE;
		return jdbcTemplate.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, DateUtils.dateSkey(new Date()));
				ps.setDate(2, new java.sql.Date(new Date().getTime()));
				ps.setInt(3, productId);
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
			}
		});

	}

	private int insertProductPrice(ProductDetailsBO prodBO) {
		String query = "INSERT INTO product_price_map(PRODUCT_ID,PRICE,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME)"
				+ Constants.VALUESFORINSERT;
		return jdbcTemplate.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, prodBO.getProductId());
				ps.setDouble(2, prodBO.getSellingPrice());
				ps.setInt(3, DateUtils.dateSkey(new Date()));
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
				ps.setDate(5, new java.sql.Date(new Date().getTime()));
			}
		});

	}

	private int updateProductPrice(int productId) {
		String query = "UPDATE product_price_map SET END_DATE_SKEY=?,LAST_UPDATE_TIME=? ";
		query+=Constants.PIDANDENDDATEWHERECLAUSE;
		return jdbcTemplate.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, DateUtils.dateSkey(new Date()));
				ps.setDate(2, new java.sql.Date(new Date().getTime()));
				ps.setInt(3, productId);
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
			}
		});

	}

	private int updateProductCategory(int productId) {
		String query = "UPDATE product_category_map SET END_DATE_SKEY=?,LAST_UPDATE_TIME=? ";
		query+=Constants.PIDANDENDDATEWHERECLAUSE;
		return jdbcTemplate.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, DateUtils.dateSkey(new Date()));
				ps.setDate(2, new java.sql.Date(new Date().getTime()));
				ps.setInt(3, productId);
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
			}
		});
	}

	private int updateProductBasicDetails(ProductDetailsBO prodBO) {
		String query = getUpdateStatementForProdBaseDet(prodBO);
		query += " WHERE PRODUCT_ID=?";

		return jdbcTemplate.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				int paramIndx = 1;
				if (prodBO.getProductName() != null)
					ps.setString(paramIndx++, prodBO.getProductName());
				if (prodBO.getDisplayName() != null)
					ps.setString(paramIndx++, prodBO.getDisplayName());
				if (prodBO.getDescription() != null)
					ps.setString(paramIndx++, prodBO.getDescription());
				if (prodBO.getUnit() != null)
					ps.setString(paramIndx++, prodBO.getUnit());
				if(prodBO.getMrp()!=0)
					ps.setDouble(paramIndx++, prodBO.getMrp());
				if(prodBO.getSellingPrice()!=0)
					ps.setDouble(paramIndx++, prodBO.getSellingPrice());
				if(prodBO.getManufacturer()!=null)
					ps.setString(paramIndx++, prodBO.getManufacturer());
				if(prodBO.getPackaging()!=null)
					ps.setString(paramIndx++, prodBO.getPackaging());
				ps.setInt(paramIndx, prodBO.getProductId());
			}
		});
	}

	private String getUpdateStatementForProdBaseDet(ProductDetailsBO prodBO) {
		String query = "UPDATE product_det SET ";
		if (prodBO.getProductName() != null)
			query += "PRODUCT_NAME=?";
		if (prodBO.getDisplayName() != null) {
			if (prodBO.getProductName() != null)
				query += ",";
			query += "DISP_NAME=?";
		}
		if (prodBO.getDescription() != null) {
			if (prodBO.getProductName() != null || prodBO.getDisplayName() != null)
				query += ",";
			query += "PRODUCT_DESC=?";
		}
		if (prodBO.getUnit() != null) {
			if (prodBO.getProductName() != null || prodBO.getDisplayName() != null || prodBO.getDescription() != null)
				query += ",";
			query += "UNIT=?";
		}
		if(prodBO.getMrp()!=0){
			if (prodBO.getProductName() != null || prodBO.getDisplayName() != null || 
					prodBO.getDescription() != null || prodBO.getUnit()!=null)
				query+=",";
			query+="MRP=?";
		}
		if(prodBO.getSellingPrice()!=0){
			if (prodBO.getProductName() != null || prodBO.getDisplayName() != null || 
					prodBO.getDescription() != null || prodBO.getUnit()!=null || prodBO.getMrp()!=0)
				query+=",";
			query+="SELLING_PRICE=?";
		}
		if(prodBO.getManufacturer()!=null){
			if (prodBO.getProductName() != null || prodBO.getDisplayName() != null || 
					prodBO.getDescription() != null || prodBO.getUnit()!=null || prodBO.getMrp()!=0
					|| prodBO.getSellingPrice()!=0)
				query+=",";
			query+="MANUFACTURER=?";
		}
		if(prodBO.getPackaging()!=null){
			if (prodBO.getProductName() != null || prodBO.getDisplayName() != null || 
					prodBO.getDescription() != null || prodBO.getUnit()!=null || prodBO.getMrp()!=0
					|| prodBO.getSellingPrice()!=0 || prodBO.getManufacturer()!=null)
				query+=",";
			query+="PACKAGING=?";
		}
		return query;
	}

	public String addGstDetails(GstDetailsBO gstDet) {
		int ret = insertGstRates(gstDet);
		if(ret<=0)
			return "Error in adding GST rate!";
		return "GST rate added successfully!";
	}

	private int insertGstRates(GstDetailsBO gstDet) {
		String query = "INSERT INTO gst_rates (GST_RATE,NAME) VALUES(?,?)";
		
		return jdbcTemplate.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setDouble(1, gstDet.getGstRate());
				ps.setString(2, gstDet.getGstName());
				
			}
			
		});
	}

	public List<GstDetailsBO> getGstRates() {
		String query = "SELECT NAME,GST_RATE FROM gst_rates";
		return jdbcTemplate.query(query, new ResultSetExtractor<List<GstDetailsBO>>() {
			List<GstDetailsBO> retList = new ArrayList<>();
			@Override
			public List<GstDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()) {
					GstDetailsBO gstBo = new GstDetailsBO();
					gstBo.setGstName(rs.getString(1));
					gstBo.setGstRate(rs.getDouble(2));
					retList.add(gstBo);
				}
				return retList;
			}
			
		});
	}

	public List<ProductDisplayDetailsBO> getInventoryDetails() {
		Map<Integer,ProductDisplayDetailsBO> productId2DetMap = new HashMap<>();
		populateProdutBasicDetails(productId2DetMap);
		populateInventoyDetailsForproduct(productId2DetMap);
		populateProdcutAvailStockMap(productId2DetMap);
		populateProductGstMap(productId2DetMap);
		return new ArrayList<>( productId2DetMap.values());
	}

	private void populateProductGstMap(Map<Integer, ProductDisplayDetailsBO> productId2DetMap) {
		String query = "SELECT PRODUCT_ID,GST_RATE FROM product_cgst_map";
		jdbcTemplate.query(query, new ResultSetExtractor<Void>(){

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					productId2DetMap.get(rs.getInt(1)).setGst(rs.getDouble(2));
				}
				return null;
			}
			
		});
		
	}

	private void populateProdcutAvailStockMap(Map<Integer, ProductDisplayDetailsBO> productId2DetMap) {
		String query="SELECT PRODUCT_ID,AVAIL_STOCK FROM product_stock_map";
		jdbcTemplate.query(query, new ResultSetExtractor<Void>(){

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					productId2DetMap.get(rs.getInt(1)).setInStock(rs.getInt(2));
				}
				return null;
			}
			
		});
		
	}

	private void populateInventoyDetailsForproduct(Map<Integer, ProductDisplayDetailsBO> productId2DetMap) {
		String query =  "SELECT B.PRODUCT_ID, B.BATCH_NO,B.QUANTITY,B.PRICE,B.COST,B.GST,B.MFG_DATE,B.EXP_DATE,B.IN_STOCK \r\n" + 
				"FROM product_det A, purchase_order_det B \r\n" + 
				"WHERE A.PRODUCT_ID = B.PRODUCT_ID AND B.IN_STOCK>0";
		jdbcTemplate.query(query, new ResultSetExtractor<Void>() {

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()) {
					if(!productId2DetMap.containsKey(rs.getInt(1)))
						continue;
					ProductDisplayDetailsBO prodDispObj = productId2DetMap.get(rs.getInt(1));
					if(prodDispObj!=null) {
						InventoryBO inventoryObj = new InventoryBO();
						inventoryObj.setBatchNo(rs.getString(2));
						inventoryObj.setTotalQty(rs.getDouble(3));
						inventoryObj.setSellingPrice(rs.getDouble(4));
						inventoryObj.setPurchasedCost(rs.getDouble(5));
						inventoryObj.setGst(rs.getDouble(6));
						inventoryObj.setMfgDate(rs.getDate(7));
						inventoryObj.setExpDate(rs.getDate(8));
						inventoryObj.setInStock(rs.getDouble(9));
						setExpiryStatus(inventoryObj);
						List<InventoryBO> inventoryDetObj = prodDispObj.getInventories();
						if(inventoryDetObj == null)
							inventoryDetObj = new ArrayList<>();
						inventoryDetObj.add(inventoryObj);
						prodDispObj.setInventories(inventoryDetObj);
					}
				}
				return null;
			}
			
		});
	}
	
	private void setExpiryStatus(InventoryBO inventoryObj) {
		Date currDate = new Date();
		int diff = DateUtils.dateSkey(inventoryObj.getExpDate()) - DateUtils.dateSkey(currDate);
		if(diff<0){
			inventoryObj.setExpStatus(Constants.EXPIRED);
		}
		else if(diff<=30){
			inventoryObj.setExpStatus(Constants.ABOUT_TO_EXPIRE);
		}
		else{
			inventoryObj.setExpStatus(Constants.EMPTY_STRING);
		}
	}

	private void populateProdutBasicDetails(Map<Integer, ProductDisplayDetailsBO> productId2DetMap) {
		String query = "SELECT A.PRODUCT_ID,A.PRODUCT_NAME,A.PRODUCT_DESC,A.UNIT,A.MRP,"
				+ "A.MANUFACTURER, A.SELLING_PRICE, A.PACKAGING,B.NAME\r\n" + 
				"FROM product_det A, category_det B, product_category_map C\r\n" + 
				"WHERE A.PRODUCT_ID = C.PRODUCT_ID AND B.CATEGORY_ID = C.CATEGORY_ID";
		jdbcTemplate.query(query, new ResultSetExtractor<Void>() {

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()) {
					int productId = rs.getInt(1);
					if(!productId2DetMap.containsKey(productId)) {
						productId2DetMap.put(productId,new ProductDisplayDetailsBO());
					}
					
					ProductDisplayDetailsBO prodDispDet = productId2DetMap.get(productId);
					prodDispDet.setProductId(productId);
					prodDispDet.setProductName(rs.getString(2));
					prodDispDet.setDescription(rs.getString(3));
					prodDispDet.setUnit(rs.getString(4));
					prodDispDet.setMrp(rs.getDouble(5));
					prodDispDet.setManufacturer(rs.getString(6));
					prodDispDet.setSellingPrice(rs.getDouble(7));
					prodDispDet.setPackaging(rs.getString(8));
					prodDispDet.setCategory(rs.getString(9));
				}
				return null;
			}
			
		});
			
	}

	public Map<String, Double> getProductGstMap() {
		String query="SELECT A.PRODUCT_NAME,B.GST_RATE\r\n" + 
				"FROM product_det A, product_cgst_map B \r\n" + 
				"WHERE A.PRODUCT_ID = B.PRODUCT_ID";
		return jdbcTemplate.query(query, new ResultSetExtractor<Map<String, Double>>(){
			
			Map<String, Double> prodName2GstMap = new HashMap<>();
			@Override
			public Map<String, Double> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()) {
					prodName2GstMap.put(rs.getString(1), rs.getDouble(2));
				}
				return prodName2GstMap;
			}
			
		});
	}

	public Map<String, Integer> getProductToIdMap() {
		String query="SELECT PRODUCT_ID, PRODUCT_NAME " + 
				"FROM product_det";
		return jdbcTemplate.query(query, new ResultSetExtractor<Map<String, Integer>>(){
			
			Map<String, Integer> prodName2IdMap = new HashMap<>();
			@Override
			public Map<String, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()) {
					prodName2IdMap.put(rs.getString(2), rs.getInt(1));
				}
				return prodName2IdMap;
			}
			
		});
	}
	
	public ProductDetailsBO getProductObj(String batchNo){
		String query = "SELECT B.PRODUCT_ID,B.EXP_DATE,A.PRODUCT_NAME,A.PACKAGING,A.SELLING_PRICE,"
				+ "A.UNIT,A.MANUFACTURER,C.GST_RATE "
				+ "FROM product_det A,purchase_order_det B,product_cgst_map C "
				+ "WHERE A.PRODUCT_ID = B.PRODUCT_ID AND B.PRODUCT_ID=C.PRODUCT_ID AND "
				+ "A.PRODUCT_ID=C.PRODUCT_ID AND B.BATCH_NO=?";
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, batchNo);
			}
		}, new ResultSetExtractor<ProductDetailsBO>() {

			@Override
			public ProductDetailsBO extractData(ResultSet rs) throws SQLException, DataAccessException {
				ProductDetailsBO prodBO = new ProductDetailsBO();
				while(rs.next()){
					prodBO.setProductId(rs.getInt(1));
					prodBO.setExpDate(new Date(rs.getDate(2).getTime()));
					prodBO.setProductName(rs.getString(3));
					prodBO.setPackaging(rs.getString(4));
					prodBO.setSellingPrice(rs.getDouble(5));
					prodBO.setUnit(rs.getString(6));
					prodBO.setManufacturer(rs.getString(7));
					prodBO.setGst(rs.getDouble(8));
				}
				return prodBO;
			}
		});
	}
	
	public int[] updateProductStockMap(Map<Integer, Integer> product2AvailStockM) {
		String query = "UPDATE product_stock_map SET AVAIL_STOCK=?,LAST_UPDATE_TIME=?,LAST_USER=? "+
						" WHERE PRODUCT_ID=?";
		
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			List<Integer>prodIdList = new ArrayList<>(product2AvailStockM.keySet());
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, product2AvailStockM.get(prodIdList.get(i)));
				ps.setDate(2, DateUtils.getSqlDate(new Date()));
				ps.setString(3, null);
				ps.setInt(4, prodIdList.get(i));
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return product2AvailStockM.size();
			}
		});
	}

	public Map<Integer, Integer> getProductAvailStockMap(List<ProductDetailsBO> productsList) {
		String query="SELECT PRODUCT_ID,AVAIL_STOCK FROM product_stock_map "+
					" WHERE PRODUCT_ID IN"+getInClause(productsList.size());
		return jdbcTemplate.query(query, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				for(int i=1;i<=productsList.size();i++){
					ps.setInt(i, productsList.get(i-1).getProductId());
				}
				
			}
			
		}, new ResultSetExtractor<Map<Integer,Integer>>() {

			@Override
			public Map<Integer, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<Integer, Integer> prod2AvailStockM = new HashMap<>();
				while(rs.next()){
					prod2AvailStockM.put(rs.getInt(1), rs.getInt(2));
				}
				return prod2AvailStockM;
			}
		});
	}

	private String getInClause(int size) {
		String retString = "(";
		for(int i=0;i<size;i++){
			if(i!=0)
				retString+=",";
			retString+="?";
		}
		retString+=")";
		return retString;
	}

	public Map<String, Integer> getBatchNoAvailStockMap(List<ProductDetailsBO> productsList) {
		String query="SELECT BATCH_NO,IN_STOCK FROM purchase_order_det "+
				" WHERE BATCH_NO IN"+getInClause(productsList.size());
		return jdbcTemplate.query(query, new PreparedStatementSetter(){
	
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				for(int i=1;i<=productsList.size();i++){
					ps.setString(i, productsList.get(i-1).getBatchNo());
				}
				
			}
			
		}, new ResultSetExtractor<Map<String,Integer>>() {
	
			@Override
			public Map<String, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, Integer> prod2AvailStockM = new HashMap<>();
				while(rs.next()){
					prod2AvailStockM.put(rs.getString(1), rs.getInt(2));
				}
				return prod2AvailStockM;
			}
		});
	}

	public int[] updateBatchStockMap(Map<String, Integer> product2AvailStockM) {
		String query = "UPDATE purchase_order_det SET IN_STOCK=?"+
				" WHERE BATCH_NO=?";

		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			List<String>prodIdList = new ArrayList<>(product2AvailStockM.keySet());
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, product2AvailStockM.get(prodIdList.get(i)));
				ps.setString(2, prodIdList.get(i));
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return product2AvailStockM.size();
			}
		});
	}

}

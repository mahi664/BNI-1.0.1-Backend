package com.example.demo.services;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
			return "Error in adding products Basic details!!";
		Date effDate = new Date();
		ret = insertProductToCategoryMap(productList, catName2IdMap, effDate);
		if (ret.length <= 0)
			return "Error in adding products category!!";
		ret = insertProdToSGSTMap(productList, effDate);
		if (ret.length <= 0)
			return "Error in adding products SGST details!!";
		ret = insertProdToCGSTMap(productList, effDate);
		if (ret.length <= 0)
			return "Error in adding products CGST details!!";
		ret = insertProdToIGSTMap(productList, effDate);
		if (ret.length <= 0)
			return "Error in adding products IGST details!!";
		ret = insertProdToDiscountMap(productList, effDate);
		if (ret.length <= 0)
			return "Error in adding products discount details!!";
		return "Products added successfully";
	}

	private String validateProdDetBeforeInsert(ProductDetailsBO prodBO, Map<String, String> catName2IdMap) {
		List<String> productNames = getProductNames();
		Map<String, List<Double>> gst2RatesMap = populateGSTRates();
		if (productNames.contains(prodBO.getProductName()))
			return "Error in adding Product. Duplicate for " + prodBO.getProductName();
		if (!catName2IdMap.containsKey(prodBO.getCategoryName()))
			return "Error in adding Product. Invalid Category Name: " + prodBO.getCategoryName();
		if (prodBO.getCgst() != 0 && !gst2RatesMap.get(Constants.CGST).contains(prodBO.getCgst()))
			return "Error in adding Product. Invalid CGST Rate: " + prodBO.getCgst();
		if (prodBO.getSgst() != 0 && !gst2RatesMap.get(Constants.SGST).contains(prodBO.getSgst()))
			return "Error in adding Product. Invalid SGST Rate: " + prodBO.getSgst();
		if (prodBO.getIgst() != 0 && !gst2RatesMap.get(Constants.IGST).contains(prodBO.getIgst()))
			return "Error in adding Product. Invalid IGST Rate: " + prodBO.getIgst();
		if (prodBO.getUnit().equals(""))
			return "Error in adding Product. Unit should not be blank";
		return null;
	}

	private int[] insertProdToSGSTMap(List<ProductDetailsBO> productList, Date effDate) {
		String query = "INSERT INTO PRODUCT_SGST_MAP (PRODUCT_ID,GST_RATE,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME) "+
						Constants.VALUESFORINSERT;
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productList.get(i).getProductId());
				ps.setDouble(2, productList.get(i).getSgst());
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
		String query = "INSERT INTO PRODUCT_CGST_MAP (PRODUCT_ID,GST_RATE,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME) "+
						Constants.VALUESFORINSERT;
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productList.get(i).getProductId());
				ps.setDouble(2, productList.get(i).getCgst());
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
		String query = "INSERT INTO PRODUCT_IGST_MAP (PRODUCT_ID,GST_RATE,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME) "+
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
		String query = "INSERT INTO PRODUCT_DISCOUNT_MAP(PRODUCT_ID,DISCOUNT,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME)"
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

		String query = "SELECT GST_RATE FROM CGST_RATES";
		List<Double> list = jdbcTemplate.queryForList(query, Double.class);
		gstRatesMap.put(Constants.CGST, list);

		query = "SELECT GST_RATE FROM SGST_RATES";
		list = jdbcTemplate.queryForList(query, Double.class);
		gstRatesMap.put(Constants.SGST, list);

		query = "SELECT GST_RATE FROM IGST_RATES";
		list = jdbcTemplate.queryForList(query, Double.class);
		gstRatesMap.put(Constants.IGST, list);

		return gstRatesMap;
	}

	private int[] insertProductToCategoryMap(List<ProductDetailsBO> productList, Map<String, String> catName2IdMap,
			Date effDate) {
		String query = "INSERT INTO PRODUCT_CATEGORY_MAP(PRODUCT_ID,CATEGORY_ID,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME)"
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
		String query = "INSERT INTO PRODUCT_DET(PRODUCT_ID,PRODUCT_NAME,DISP_NAME,PRODUCT_DESC,UNIT)"
				+ Constants.VALUESFORINSERT;
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productList.get(i).getProductId());
				ps.setString(2, productList.get(i).getProductName());
				ps.setString(3, productList.get(i).getDisplayName());
				ps.setString(4, productList.get(i).getDescription());
				ps.setString(5, productList.get(i).getUnit());
			}

			@Override
			public int getBatchSize() {
				return productList.size();
			}
		});
	}

	private List<String> getProductNames() {
		String query = "SELECT PRODUCT_NAME FROM PRODUCT_DET";
		return jdbcTemplate.queryForList(query, String.class);
	}

	private int getMaxProductId() {
		String query = "SELECT MAX(PRODUCT_ID) FROM PRODUCT_DET";
		String catId = jdbcTemplate.queryForObject(query, String.class);
		if (catId == null)
			return 0;
		return Integer.parseInt(catId.split("_")[1]);
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
		if (prodBO.getProductName() != null || prodBO.getDisplayName() != null || prodBO.getDescription() != null)
			updateProductBasicDetails(prodBO);
		if (prodBO.getCategoryName() != null) {
			updateProductCategory(prodBO.getProductId());
			List<ProductDetailsBO> tempProdL = new ArrayList<>();
			tempProdL.add(prodBO);
			insertProductToCategoryMap(tempProdL, catName2IdM, new Date());
		}
		if (prodBO.getPrice() != 0) {
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
		String query="UPDATE PRODUCT_DISCOUNT_MAP SET END_DATE_SKEY=?,LAST_UPDATE_TIME=?";
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
		String query="UPDATE PRODUCT_CGST_MAP SET END_DATE_SKEY=?,LAST_UPDATE_TIME=?";
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
		String query="UPDATE PRODUCT_SGST_MAP SET END_DATE_SKEY=?,LAST_UPDATE_TIME=?";
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
		String query="UPDATE PRODUCT_IGST_MAP SET END_DATE_SKEY=?,LAST_UPDATE_TIME=?";
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
		String query = "INSERT INTO PRODUCT_COST_MAP(PRODUCT_ID,COST,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME)"
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
		String query = "UPDATE PRODUCT_COST_MAP SET END_DATE_SKEY=?,LAST_UPDATE_TIME=? ";
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
		String query = "INSERT INTO PRODUCT_PRICE_MAP(PRODUCT_ID,PRICE,EFF_DATE_SKEY,END_DATE_SKEY,LAST_UPDATE_TIME)"
				+ Constants.VALUESFORINSERT;
		return jdbcTemplate.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, prodBO.getProductId());
				ps.setDouble(2, prodBO.getPrice());
				ps.setInt(3, DateUtils.dateSkey(new Date()));
				ps.setInt(4, DateUtils.dateSkey(Constants.MAX_DATE));
				ps.setDate(5, new java.sql.Date(new Date().getTime()));
			}
		});

	}

	private int updateProductPrice(int productId) {
		String query = "UPDATE PRODUCT_PRICE_MAP SET END_DATE_SKEY=?,LAST_UPDATE_TIME=? ";
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
		String query = "UPDATE PRODUCT_CATEGORY_MAP SET END_DATE_SKEY=?,LAST_UPDATE_TIME=? ";
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
				ps.setInt(paramIndx, prodBO.getProductId());
			}
		});
	}

	private String getUpdateStatementForProdBaseDet(ProductDetailsBO prodBO) {
		String query = "UPDATE PRODUCT_DET SET ";
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
			query += "PRODUCT_DESC=?";
		}
		return query;
	}

}

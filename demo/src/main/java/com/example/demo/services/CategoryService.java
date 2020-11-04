package com.example.demo.services;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bo.CategoryDetailsBO;
import com.example.demo.utils.FileUploadUtils;

@Service
public class CategoryService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	FileUploadUtils fileUtils;
	
	public String addNewCategory(List<CategoryDetailsBO> categoryList) {
		
		int categoryId = getMaxCatId();
		List<String> categoryNames = getCategoryNames();
		for(CategoryDetailsBO catBO : categoryList){
			if(categoryNames.contains(catBO.getCategoryName()))
				return "Error in uploading categories. Duplicate for "+catBO.getCategoryName();
			catBO.setCategoryId(++categoryId);
		}
		
		int[] ret = insertCategory(categoryList);
		if(ret.length<=0)
			return "Error in uploading categories!!";
		return "Categories uploaded successfully";
	}

	private int[] insertCategory(List<CategoryDetailsBO> categoryList) {
		String query = "INSERT INTO CATEGORY_DET(CATEGORY_ID,NAME,DISP_NAME,CATEGORY_DESC)"+ 
				   "VALUES(?,?,?,?)";
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, categoryList.get(i).getCategoryId());
				ps.setString(2, categoryList.get(i).getCategoryName());
				ps.setString(3, categoryList.get(i).getCategoryDispName());
				ps.setString(4, categoryList.get(i).getCategoryDescription());
			}
			
			@Override
			public int getBatchSize() {
				return categoryList.size();
			}
		});
	}

	private int getMaxCatId() {
		String query = "SELECT MAX(CATEGORY_ID) FROM CATEGORY_DET";
		Integer catId =  jdbcTemplate.queryForObject(query, Integer.class);
		if(catId==null)
			return 0;
		return catId.intValue();
	}
	
	private List<String> getCategoryNames() {
		String query = "SELECT NAME FROM CATEGORY_DET";
		return jdbcTemplate.queryForList(query, String.class);
	}

	public String addNewCategories(String fileName) throws FileNotFoundException {
		List<CategoryDetailsBO> categoryList = fileUtils.getCategoriesFromFile(fileName);
		return addNewCategory(categoryList);
	}

	@Transactional
	public String updateCategories(List<CategoryDetailsBO> categoryList) {
		List<String> categoryNames = getCategoryNames();
		for(CategoryDetailsBO catBO : categoryList){
			if(categoryNames.contains(catBO.getCategoryName()))
				return "Duplicate entry for category name : "+catBO.getCategoryName();
			updateCategory(catBO);
		}
		return "Categories updated successfully";
	}

	@Transactional
	public int updateCategory(CategoryDetailsBO catBO) {
		String query = getUpdateQuery(catBO);
		query+="WHERE CATEGORY_ID=?";
		return jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				int paramIndx=1;
				if(catBO.getCategoryName()!=null)
					ps.setString(paramIndx++, catBO.getCategoryName());
				if(catBO.getCategoryDispName()!=null)
					ps.setString(paramIndx++, catBO.getCategoryDispName());
				if(catBO.getCategoryDescription()!=null)
					ps.setString(paramIndx++, catBO.getCategoryDescription());
				ps.setInt(paramIndx, catBO.getCategoryId());
			}
		});
	}

	private String getUpdateQuery(CategoryDetailsBO catBO) {
		String query = "UPDATE CATEGORY_DET SET ";
		if(catBO.getCategoryName()!=null)
			query+="NAME=?";
		if(catBO.getCategoryDispName()!=null){
			if(catBO.getCategoryName()!=null) query+=",";
			query+="DISP_NAME=?";
		}
		if(catBO.getCategoryDescription()!=null){
			if(catBO.getCategoryName()!=null || catBO.getCategoryDispName()!=null) query+=",";
			query+="CATEGORY_DESC=? ";
		}
		return query;
	}

	public String deleteCategories(List<Integer> categoryIds) {
		String query = "DELETE FROM CATEGORY_DET WHERE CATEGORY_ID=?";
		int[] ret = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, categoryIds.get(i));
			}
			
			@Override
			public int getBatchSize() {
				return categoryIds.size();
			}
		});
		if(ret.length<=0)
			return "Error in deleting Categories";
		return "Categories Deleted Successgully";
	}

	public Map<String, String> getCategoryIdToNameMap() {
		String query = "SELECT CATEGORY_ID,NAME FROM CATEGORY_DET";
		return jdbcTemplate.query(query, new ResultSetExtractor<Map<String, String>>(){

			@Override
			public Map<String, String> extractData(ResultSet rs) throws SQLException {
				Map<String, String> catName2IdMap = new HashMap<>();
				while(rs.next()){
					catName2IdMap.put(rs.getString(2), rs.getString(1));
				}
				return catName2IdMap;
			}
			
		});
	}

	public List<CategoryDetailsBO> getAllCategories() {
		String query = "SELECT * FROM CATEGORY_DET";
		return jdbcTemplate.query(query, new ResultSetExtractor<List<CategoryDetailsBO>>() {
			List<CategoryDetailsBO> categoryList = new ArrayList<>();
			@Override
			public List<CategoryDetailsBO> extractData(ResultSet rs) throws SQLException {
				while(rs.next()){
					CategoryDetailsBO catBO = new CategoryDetailsBO();
					catBO.setCategoryId(rs.getInt(1));
					catBO.setCategoryName(rs.getString(2));
					catBO.setCategoryDispName(rs.getString(3));
					catBO.setCategoryDescription(rs.getString(4));
					categoryList.add(catBO);
				}
				return categoryList;
			}
		});
	}

	public List<CategoryDetailsBO> getQuickSearchedCategory(String filterParam) {
		String query = "SELECT * FROM CATEGORY_DET "+
						"WHERE CATEGORY_ID LIKE CONCAT('%',?,'%') OR NAME LIKE CONCAT('%',?,'%') OR DISP_NAME LIKE CONCAT('%',?,'%') OR CATEGORY_DESC LIKE CONCAT('%',?,'%')";
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, filterParam);
				ps.setString(2, filterParam);
				ps.setString(3, filterParam);
				ps.setString(4, filterParam);
			}
		}, new ResultSetExtractor<List<CategoryDetailsBO>>() {

			@Override
			public List<CategoryDetailsBO> extractData(ResultSet rs) throws SQLException {
				List<CategoryDetailsBO> categoryList = new ArrayList<>();
				while(rs.next()){
					CategoryDetailsBO catBO = new CategoryDetailsBO();
					catBO.setCategoryId(rs.getInt(1));
					catBO.setCategoryName(rs.getString(2));
					catBO.setCategoryDispName(rs.getString(3));
					catBO.setCategoryDescription(rs.getString(4));
					categoryList.add(catBO);
				}
				return categoryList;
			}
		});
	}
}

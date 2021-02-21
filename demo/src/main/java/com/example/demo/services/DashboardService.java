package com.example.demo.services;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import com.example.demo.bo.DashboardStatisticsBO;
import com.example.demo.utils.DateUtils;

@Service
public class DashboardService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate; 

	public DashboardStatisticsBO getDashboardStatistics() {
		DashboardStatisticsBO dashboardStatsBO = new DashboardStatisticsBO();
		getTotalSalesAmt(dashboardStatsBO);
		getTotalPuchaseAmt(dashboardStatsBO);
		getTotalClients(dashboardStatsBO);
		grtPendingSalesAmt(dashboardStatsBO);
		getPendingPurchaseAmt(dashboardStatsBO);
		getTotalGstAmt(dashboardStatsBO);
		getTotalVendors(dashboardStatsBO);
		getExpiredProducts(dashboardStatsBO);
		return dashboardStatsBO;
	}

	private void getExpiredProducts(DashboardStatisticsBO dashboardStatsBO) {
		String query = "select sum(IN_STOCK) totalExpiredProducts from purchase_order_det where EXP_DATE<?";
		jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setDate(1, new Date(new java.util.Date().getTime()));
			}
		}, new ResultSetExtractor<Void>() {

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					dashboardStatsBO.setExpiredProducts(rs.getDouble(1));
				}
				return null;
			}
		});
	}

	private void getTotalVendors(DashboardStatisticsBO dashboardStatsBO) {
		String query = "select count(*) totalVendor from vendor_det";
		Double totalVendor = jdbcTemplate.queryForObject(query, Double.class);
		dashboardStatsBO.setTotalVendors(totalVendor.doubleValue());
	}

	private void getTotalGstAmt(DashboardStatisticsBO dashboardStatsBO) {
		String query = "select sum(GST_AMT) totalGstAmt from order_customer_map";
		Double totalGstAmt = jdbcTemplate.queryForObject(query, Double.class);
		dashboardStatsBO.setTotalGstAmt(totalGstAmt.doubleValue());
	}

	private void getPendingPurchaseAmt(DashboardStatisticsBO dashboardStatsBO) {
		String query = "select sum(DUE_AMT) pendingPurchaseAmt from vendor_receipt_map";
		Double pendingPurchaseAmt = jdbcTemplate.queryForObject(query, Double.class);
		dashboardStatsBO.setPendingPurchaseAmt(pendingPurchaseAmt.doubleValue());
	}

	private void grtPendingSalesAmt(DashboardStatisticsBO dashboardStatsBO) {
		String query = "select sum(DUE_AMT) pendingSalesAmt from order_customer_map";
		Double pendingSalesAmt = jdbcTemplate.queryForObject(query, Double.class);
		dashboardStatsBO.setPendingSaledAmt(pendingSalesAmt.doubleValue());
	}

	private void getTotalClients(DashboardStatisticsBO dashboardStatsBO) {
		String query = "select count(*) totalClients from customer_det";
		Double totalClients = jdbcTemplate.queryForObject(query, Double.class);
		dashboardStatsBO.setTotalClients(totalClients.doubleValue());
	}

	private void getTotalPuchaseAmt(DashboardStatisticsBO dashboardStatsBO) {
		String query = "select sum(TOTAL_AMT) purchaseAmt from vendor_receipt_map";
		Double totalPurchaseAmt = jdbcTemplate.queryForObject(query, Double.class);
		dashboardStatsBO.setTotalPurchaseAmt(totalPurchaseAmt.doubleValue());
	}

	private void getTotalSalesAmt(DashboardStatisticsBO dashboardStatsBO) {
		String query = "select sum(TOTAL_AMT) salesAmt from order_customer_map";
		Double totalSaleAmt = jdbcTemplate.queryForObject(query, Double.class);
		dashboardStatsBO.setTotalSaleAmt(totalSaleAmt.doubleValue());
	}

}

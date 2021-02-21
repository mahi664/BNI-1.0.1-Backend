package com.example.demo.bo;

public class DashboardStatisticsBO {
	
	private double totalSaleAmt;
	private double totalClients;
	private double pendingSalesAmt;
    private double pendingPurchaseAmt;
    private double totalGstAmt;
    private double totalPurchaseAmt;
    private double totalVendors;
    private double expiredProducts;
    
	public double getTotalSaleAmt() {
		return totalSaleAmt;
	}
	public void setTotalSaleAmt(double totalSaleAmt) {
		this.totalSaleAmt = totalSaleAmt;
	}
	public double getTotalClients() {
		return totalClients;
	}
	public void setTotalClients(double totalClients) {
		this.totalClients = totalClients;
	}
	public double getPendingSaledAmt() {
		return pendingSalesAmt;
	}
	public void setPendingSaledAmt(double pendingSaledAmt) {
		this.pendingSalesAmt = pendingSaledAmt;
	}
	public double getPendingPurchaseAmt() {
		return pendingPurchaseAmt;
	}
	public void setPendingPurchaseAmt(double pendingPurchaseAmt) {
		this.pendingPurchaseAmt = pendingPurchaseAmt;
	}
	public double getTotalGstAmt() {
		return totalGstAmt;
	}
	public void setTotalGstAmt(double totalGstAmt) {
		this.totalGstAmt = totalGstAmt;
	}
	public double getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}
	public void setTotalPurchaseAmt(double totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}
	public double getTotalVendors() {
		return totalVendors;
	}
	public void setTotalVendors(double totalVendors) {
		this.totalVendors = totalVendors;
	}
	public double getExpiredProducts() {
		return expiredProducts;
	}
	public void setExpiredProducts(double expiredProducts) {
		this.expiredProducts = expiredProducts;
	}
	@Override
	public String toString() {
		return "DashboardStatisticsBO [totalSaleAmt=" + totalSaleAmt + ", totalClients=" + totalClients
				+ ", pendingSaledAmt=" + pendingSalesAmt + ", pendingPurchaseAmt=" + pendingPurchaseAmt
				+ ", totalGstAmt=" + totalGstAmt + ", totalPurchaseAmt=" + totalPurchaseAmt + ", totalVendors="
				+ totalVendors + ", expiredProducts=" + expiredProducts + "]";
	}
    
    
}

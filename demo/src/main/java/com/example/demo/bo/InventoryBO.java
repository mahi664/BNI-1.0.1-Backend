package com.example.demo.bo;

import java.util.Date;

public class InventoryBO {
	private String batchNo;
	private double totalQty;
	private double sellingPrice;
	private double purchasedCost;
	private double gst;
	private Date mfgDate;
	private Date expDate;
	private double inStock;
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public double getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(double totalQty) {
		this.totalQty = totalQty;
	}
	public double getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public double getPurchasedCost() {
		return purchasedCost;
	}
	public void setPurchasedCost(double purchasedCost) {
		this.purchasedCost = purchasedCost;
	}
	public double getGst() {
		return gst;
	}
	public void setGst(double gst) {
		this.gst = gst;
	}
	public Date getMfgDate() {
		return mfgDate;
	}
	public void setMfgDate(Date mfgDate) {
		this.mfgDate = mfgDate;
	}
	public Date getExpDate() {
		return expDate;
	}
	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}
	public double getInStock() {
		return inStock;
	}
	public void setInStock(double inStock) {
		this.inStock = inStock;
	}
	
	
}

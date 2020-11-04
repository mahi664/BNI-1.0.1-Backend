package com.example.demo.bo;

import java.util.Date;

public class ProductDetailsBO extends CategoryDetailsBO {
	private 	int 		productId;
	private 	String 		productName;
	private 	String 		displayName;
	private 	String 		description;
	private 	double		cost;
	private		double		price;
	private 	double		discount;
	private 	int 		quantity;
	private 	double		cgst;
	private 	double 		sgst;
	private 	double 		igst;
	private 	String 		unit;
	private 	String 		batchNo;
	private 	Date 		mfgDate;
	private		Date		expDate;
	
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getCgst() {
		return cgst;
	}
	public void setCgst(double cgst) {
		this.cgst = cgst;
	}
	public double getSgst() {
		return sgst;
	}
	public void setSgst(double sgst) {
		this.sgst = sgst;
	}
	public double getIgst() {
		return igst;
	}
	public void setIgst(double igst) {
		this.igst = igst;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
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
	
	
	
}

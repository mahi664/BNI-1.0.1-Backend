package com.example.demo.bo;

import java.util.Date;

public class ProductDetailsBO extends CategoryDetailsBO {
	private 	int 		productId;
	private 	String 		productName;
	private 	String 		displayName;
	private 	String 		description;
	private 	double		cost;
	private		double		sellingPrice;
	private 	double		discount;
	private 	int 		quantity;
	private 	double		cgst;
	private 	double 		sgst;
	private 	double 		igst;
	private 	double 		gst;
	private 	String 		unit;
	private 	String 		batchNo;
	private 	Date 		mfgDate;
	private		Date		expDate;
	private		double 		mrp;
	private 	String 		manufacturer;
	private 	String 		packaging;
	private 	double 		total;
	
	
	
	
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public double getMrp() {
		return mrp;
	}
	public void setMrp(double mrp) {
		this.mrp = mrp;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getPackaging() {
		return packaging;
	}
	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}
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
	public double getGst() {
		return gst;
	}
	public void setGst(double gst) {
		this.gst = gst;
	}
	@Override
	public String toString() {
		return "ProductDetailsBO [productId=" + productId + ", productName=" + productName + ", displayName="
				+ displayName + ", description=" + description + ", cost=" + cost + ", sellingPrice=" + sellingPrice
				+ ", discount=" + discount + ", quantity=" + quantity + ", cgst=" + cgst + ", sgst=" + sgst + ", igst="
				+ igst + ", gst=" + gst + ", unit=" + unit + ", batchNo=" + batchNo + ", mfgDate=" + mfgDate
				+ ", expDate=" + expDate + ", mrp=" + mrp + ", manufacturer=" + manufacturer + ", packaging="
				+ packaging + ", total=" + total + ", getTotal()=" + getTotal() + ", getSellingPrice()="
				+ getSellingPrice() + ", getMrp()=" + getMrp() + ", getManufacturer()=" + getManufacturer()
				+ ", getPackaging()=" + getPackaging() + ", getBatchNo()=" + getBatchNo() + ", getProductId()="
				+ getProductId() + ", getProductName()=" + getProductName() + ", getDisplayName()=" + getDisplayName()
				+ ", getDescription()=" + getDescription() + ", getCost()=" + getCost() + ", getDiscount()="
				+ getDiscount() + ", getQuantity()=" + getQuantity() + ", getCgst()=" + getCgst() + ", getSgst()="
				+ getSgst() + ", getIgst()=" + getIgst() + ", getUnit()=" + getUnit() + ", getMfgDate()=" + getMfgDate()
				+ ", getExpDate()=" + getExpDate() + ", getGst()=" + getGst() + ", getCategoryId()=" + getCategoryId()
				+ ", getCategoryName()=" + getCategoryName() + ", getCategoryDispName()=" + getCategoryDispName()
				+ ", getCategoryDescription()=" + getCategoryDescription() + ", toString()=" + super.toString()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}
	
	
	
}

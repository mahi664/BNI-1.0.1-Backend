package com.example.demo.bo;


import java.util.List;

public class VendorDetailsBO {
	private 	int 		vendorId;
	private 	String 		vendorName;
	private 	String 		city;
	private 	String 		district;
	private 	String 		state;
	private 	String 		phone;
	private 	String 		email;
	private 	String 		gstNo;
	private 	String 		uidNo;
	private 	String 		village;
	private 	String 		pinCode;
	private 	String 		panNo;
	private		double		totalDueAmt = 0;
	
	private 	List<PurchaseDetailsBO> invoices = null;
	
	
	
	
	public double getTotalDueAmt() {
		return totalDueAmt;
	}
	public void setTotalDueAmt(double totalDueAmt) {
		this.totalDueAmt = totalDueAmt;
	}
	public List<PurchaseDetailsBO> getInvoices() {
		return invoices;
	}
	public void setInvoices(List<PurchaseDetailsBO> invoices) {
		this.invoices = invoices;
	}
	public String getUidNo() {
		return uidNo;
	}
	public void setUidNo(String uidNo) {
		this.uidNo = uidNo;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	
	public int getVendorId() {
		return vendorId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGstNo() {
		return gstNo;
	}
	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}
	@Override
	public String toString() {
		return "VendorDetailsBO [vendorId=" + vendorId + ", vendorName=" + vendorName + ", city=" + city + ", district="
				+ district + ", state=" + state + ", phone=" + phone + ", email=" + email + ", gstNo=" + gstNo
				+ ", uidNo=" + uidNo + ", village=" + village + ", pinCode=" + pinCode + ", panNo=" + panNo + "]";
	}
	
	
	
}

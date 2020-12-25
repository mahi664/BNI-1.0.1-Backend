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
	private 	int 		gstNo;
	
	private 	List<InvoiceDetailsBO>	invoices =	null;
	
	
	public List<InvoiceDetailsBO> getInvoices() {
		return invoices;
	}
	public void setInvoices(List<InvoiceDetailsBO> invoices) {
		this.invoices = invoices;
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
	public int getGstNo() {
		return gstNo;
	}
	public void setGstNo(int gstNo) {
		this.gstNo = gstNo;
	}
	@Override
	public String toString() {
		return "VendorDetailsBO [vendorId=" + vendorId + ", vendorName=" + vendorName + ", city=" + city + ", district="
				+ district + ", state=" + state + ", phone=" + phone + ", email=" + email + ", gstNo=" + gstNo
				+ ", invoices=" + invoices + "]";
	}
	
	
	
}

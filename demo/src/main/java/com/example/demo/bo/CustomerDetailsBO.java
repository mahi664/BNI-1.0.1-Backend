package com.example.demo.bo;

import java.util.ArrayList;
import java.util.List;

public class CustomerDetailsBO {
	private 	int						customerId;
	private		String						customerName;
	private		String						phone;
	private 	String 						email;
	private 	String 						village;
	private 	String 						city;
	private 	String 						district;
	private 	String 						state;
	private 	String 						pinCode;
	private 	String 						uidNo;
	private 	String 						gstNo;
	private 	String 						panNo;
	private 	double						totalDueAmt = 0;
	
	//List of Invoices belongs to this Customer
	private 	List<InvoiceDetailsBO>	invoices =	null;
	
	
	
	
	public double getTotalDueAmt() {
		return totalDueAmt;
	}

	public void setTotalDueAmt(double totalDueAmt) {
		this.totalDueAmt = totalDueAmt;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
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

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getUidNo() {
		return uidNo;
	}

	public void setUidNo(String uidNo) {
		this.uidNo = uidNo;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

//	public void setCustomerInvoicesList(ArrayList<InvoiceDetailsBO> customerInvoicesList) {
//		this.customerInvoicesList = customerInvoicesList;
//	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public List<InvoiceDetailsBO> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<InvoiceDetailsBO> invoices) {
		this.invoices = invoices;
	}

	@Override
	public String toString() {
		return "CustomerDetailsBO [customerId=" + customerId + ", customerName=" + customerName + ", phone=" + phone
				+ ", email=" + email + ", village=" + village + ", city=" + city + ", district=" + district + ", state="
				+ state + ", pinCode=" + pinCode + ", uidNo=" + uidNo + ", gstNo=" + gstNo + ", panNo=" + panNo
				+ ", invoices=" + invoices + ", getVillage()=" + getVillage() + ", getCity()=" + getCity()
				+ ", getDistrict()=" + getDistrict() + ", getState()=" + getState() + ", getPinCode()=" + getPinCode()
				+ ", getUidNo()=" + getUidNo() + ", getGstNo()=" + getGstNo() + ", getPanNo()=" + getPanNo()
				+ ", getCustomerId()=" + getCustomerId() + ", getCustomerName()=" + getCustomerName() + ", getPhone()="
				+ getPhone() + ", getEmail()=" + getEmail() + ", getInvoices()=" + getInvoices() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
	
	
}

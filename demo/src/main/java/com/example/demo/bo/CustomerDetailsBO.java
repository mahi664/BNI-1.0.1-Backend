package com.example.demo.bo;

import java.util.ArrayList;
import java.util.List;

public class CustomerDetailsBO {
	private 	String						customerId;
	private		String						customerName;
	private		String						phone;
	private 	String 						address;
	private 	String 						email;
	
	//List of Invoices belongs to this Customer
	private 	ArrayList<InvoiceDetailsBO>	customerInvoicesList =	null;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<InvoiceDetailsBO> getCustomerInvoicesList() {
		return customerInvoicesList;
	}

	public void setCustomerInvoicesList(List<InvoiceDetailsBO> customerInvoicesList) {
		this.customerInvoicesList = (ArrayList<InvoiceDetailsBO>) customerInvoicesList;
	}
	
	
}

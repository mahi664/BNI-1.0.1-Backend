package com.example.demo.bo;

import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailsBO {
	private 	String 							invoiceId;
	private 	int 							dateSkey;
	private	    String							paymentStatus;
	private 	double							totalPrice;
	private 	double							paid;
	private		double							unpaid;
	
	//List of products belongs to this Invoice
	private 	ArrayList<ProductDetailsBO>		products = 		null;

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public int getDateSkey() {
		return dateSkey;
	}

	public void setDateSkey(int dateSkey) {
		this.dateSkey = dateSkey;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getPaid() {
		return paid;
	}

	public void setPaid(double paid) {
		this.paid = paid;
	}

	public double getUnpaid() {
		return unpaid;
	}

	public void setUnpaid(double unpaid) {
		this.unpaid = unpaid;
	}

	public ArrayList<ProductDetailsBO> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<ProductDetailsBO> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "InvoiceDetailsBO [invoiceId=" + invoiceId + ", dateSkey=" + dateSkey + ", paymentStatus="
				+ paymentStatus + ", totalPrice=" + totalPrice + ", paid=" + paid + ", unpaid=" + unpaid + ", products="
				+ products + "]";
	}

	
}

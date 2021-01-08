package com.example.demo.bo;

import java.util.List;

public class PurchaseDetailsBO extends VendorDetailsBO {
	private 	int 							invoiceDate;
	private 	String 							invoiceId;
	private 	double 							totalAmt;
	private 	double 							paidAmt;
	
	private 	List<ProductDetailsBO>		productsList = 		null;
	
	public double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public double getPaidAmt() {
		return paidAmt;
	}

	public void setPaidAmt(double paidAmt) {
		this.paidAmt = paidAmt;
	}


	public int getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(int invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public List<ProductDetailsBO> getProductsList() {
		return productsList;
	}

	public void setProductsList(List<ProductDetailsBO> productsList) {
		this.productsList = productsList;
	}
	
	
}

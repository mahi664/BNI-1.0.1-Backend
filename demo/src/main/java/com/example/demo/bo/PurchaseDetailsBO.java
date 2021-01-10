package com.example.demo.bo;

import java.util.Date;
import java.util.List;

public class PurchaseDetailsBO {
	private 	int 							invoiceDateSkey;
	private 	String 							invoiceId;
	private 	double 							totalAmt;
	private 	double 							paidAmt;
	private 	double 							dueAmt;
	private 	Date							invoiceDate;
	private 	String 							paymentType;
	private 	double 							gstAmt;
	private 	double 							discount;
	private 	String							bankName;
	private 	String							accNo;
	private 	String							chequeNo;
	
	
	private 	List<ProductDetailsBO>		productsList = 		null;
	
	
	
	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public double getGstAmt() {
		return gstAmt;
	}

	public void setGstAmt(double gstAmt) {
		this.gstAmt = gstAmt;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public double getDueAmt() {
		return dueAmt;
	}

	public void setDueAmt(double dueAmt) {
		this.dueAmt = dueAmt;
	}

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

	

	public int getInvoiceDateSkey() {
		return invoiceDateSkey;
	}

	public void setInvoiceDateSkey(int invoiceDateSkey) {
		this.invoiceDateSkey = invoiceDateSkey;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
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

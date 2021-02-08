package com.example.demo.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceDetailsBO {
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


	public int getInvoiceDateSkey() {
		return invoiceDateSkey;
	}


	public void setInvoiceDateSkey(int invoiceDateSkey) {
		this.invoiceDateSkey = invoiceDateSkey;
	}


	public String getInvoiceId() {
		return invoiceId;
	}


	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
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


	public double getDueAmt() {
		return dueAmt;
	}


	public void setDueAmt(double dueAmt) {
		this.dueAmt = dueAmt;
	}


	public Date getInvoiceDate() {
		return invoiceDate;
	}


	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}


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


	public List<ProductDetailsBO> getProductsList() {
		return productsList;
	}


	public void setProductsList(List<ProductDetailsBO> productsList) {
		this.productsList = productsList;
	}


	@Override
	public String toString() {
		return "InvoiceDetailsBO [invoiceDateSkey=" + invoiceDateSkey + ", invoiceId=" + invoiceId + ", totalAmt="
				+ totalAmt + ", paidAmt=" + paidAmt + ", dueAmt=" + dueAmt + ", invoiceDate=" + invoiceDate
				+ ", paymentType=" + paymentType + ", gstAmt=" + gstAmt + ", discount=" + discount + ", bankName="
				+ bankName + ", accNo=" + accNo + ", chequeNo=" + chequeNo + ", productsList=" + productsList
				+ ", getInvoiceDateSkey()=" + getInvoiceDateSkey() + ", getInvoiceId()=" + getInvoiceId()
				+ ", getTotalAmt()=" + getTotalAmt() + ", getPaidAmt()=" + getPaidAmt() + ", getDueAmt()=" + getDueAmt()
				+ ", getInvoiceDate()=" + getInvoiceDate() + ", getPaymentType()=" + getPaymentType() + ", getGstAmt()="
				+ getGstAmt() + ", getDiscount()=" + getDiscount() + ", getBankName()=" + getBankName()
				+ ", getAccNo()=" + getAccNo() + ", getChequeNo()=" + getChequeNo() + ", getProductsList()="
				+ getProductsList() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
}

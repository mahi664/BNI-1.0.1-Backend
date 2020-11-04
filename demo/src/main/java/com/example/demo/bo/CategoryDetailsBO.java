package com.example.demo.bo;

public class CategoryDetailsBO {
	private 	int 		categoryId;
	private 	String 		categoryName;
	private 	String 		categoryDispName;
	private 	String 		categoryDescription;
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryDispName() {
		return categoryDispName;
	}
	public void setCategoryDispName(String categoryDispName) {
		this.categoryDispName = categoryDispName;
	}
	public String getCategoryDescription() {
		return categoryDescription;
	}
	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}
	@Override
	public String toString() {
		return "CategoryDetailsBO [categoryId=" + categoryId + ", categoryName=" + categoryName + ", categoryDispName="
				+ categoryDispName + ", categoryDescription=" + categoryDescription + "]";
	}
	
	
	
}

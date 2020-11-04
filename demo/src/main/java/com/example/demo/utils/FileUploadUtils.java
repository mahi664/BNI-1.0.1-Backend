package com.example.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.example.demo.bo.CategoryDetailsBO;
import com.example.demo.bo.ProductDetailsBO;

@Service
public class FileUploadUtils {

	public List<CategoryDetailsBO> getCategoriesFromFile(String fileName) throws FileNotFoundException {
		File file = new File(Constants.FILE_PATH + fileName);
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter(System.getProperty("line.separator"));
		List<CategoryDetailsBO> categoryList = new ArrayList<>();
		try {

			while (scanner.hasNext()) {
				String line = scanner.next();
				String[] dataSplit = line.split(",");

				CategoryDetailsBO catBO = new CategoryDetailsBO();
				catBO.setCategoryName(dataSplit[0]);
				catBO.setCategoryDispName(dataSplit[1]);
				catBO.setCategoryDescription(dataSplit[2]);

				categoryList.add(catBO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return categoryList;
	}

	public List<ProductDetailsBO> getProductListFromFile(String fileName) throws FileNotFoundException {
		File file = new File(Constants.FILE_PATH + fileName);
		Scanner scanner = new Scanner(file);
		List<ProductDetailsBO> productList = new ArrayList<>();
		try {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] dataSplit = line.split(",");

				ProductDetailsBO prodBO = new ProductDetailsBO();
				prodBO.setProductName(dataSplit[0]);
				prodBO.setDisplayName(dataSplit[1]);
				prodBO.setDescription(dataSplit[2]);
				prodBO.setCategoryName(dataSplit[3]);
				if(!dataSplit[4].equals(""))
					prodBO.setCgst(Double.parseDouble(dataSplit[4]));
				else
					prodBO.setCgst(0);
				if(!dataSplit[5].equals(""))
					prodBO.setSgst(Double.parseDouble(dataSplit[5]));
				else
					prodBO.setSgst(0);
				if(!dataSplit[6].equals(""))
					prodBO.setIgst(Double.parseDouble(dataSplit[6]));
				else
					prodBO.setIgst(0);
				if(!dataSplit[7].equals(""))
					prodBO.setDiscount(Double.parseDouble(dataSplit[7]));
				else
					prodBO.setDiscount(0);
				prodBO.setUnit(dataSplit[8]);
				productList.add(prodBO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return productList;
	}

}

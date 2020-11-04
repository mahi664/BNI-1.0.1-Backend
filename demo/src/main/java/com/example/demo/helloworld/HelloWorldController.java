package com.example.demo.helloworld;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.ProductDetailsBO;

@RestController
public class HelloWorldController {

	@Autowired
	JdbcTemplate jdbc;
	
	@GetMapping(path="/hello-world")
	public String helloWorld(){
		ProductDetailsBO product = new ProductDetailsBO();
		product.setProductId(2);
		product.setProductName("Pegassus2");
		String query="insert into product_det(product_id,prouct_name,disp_name) values(?,?,?)";
		return jdbc.execute(query, new PreparedStatementCallback<String>() {
			@Override
			public String doInPreparedStatement(PreparedStatement ps) throws SQLException {
				ps.setInt(1, product.getProductId());
				ps.setString(2, product.getProductName());
				ps.setString(3, product.getProductName());
				boolean ret = ps.execute();
				if(ret)
					return "Inserted SuccessFully";
				return "Error in insertion";
			}
		});
	}
	
	@GetMapping(path="/hello-world-bean")
	public HelloWorldBean helloWorldBean(){
		return new HelloWorldBean("Hello World");
	}
	
	@GetMapping(path="/hello-world/path-variable/{name}")
	public HelloWorldBean helloWorldPathVariable(@PathVariable String name){
		return new HelloWorldBean("Hello World "+name);
	}
	
	@GetMapping(path="/Login")
	public String welcomeString(){
		return "Welcome Enter user name and password";
	}
}

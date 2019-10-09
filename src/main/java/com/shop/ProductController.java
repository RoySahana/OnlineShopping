package com.shop;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bean.ProductBean;
import bean.PurchasedBean;

@RestController
public class ProductController {
	

	

	@RequestMapping("/addProduct")
	public String addProduct(@RequestParam String name, @RequestParam int price, @RequestParam String description, @RequestParam int stock) throws ClassNotFoundException, SQLException {
		System.out.println(name + " " + price + " " + description + " " + stock);
		ProductBean bean = new ProductBean();
		bean.setProductName(name);
		bean.setProductPrice(price);
		bean.setProductDescription(description);
		bean.setStock(stock);
		
		
		ShoppingDao.insertData(bean);
		return "added";
	}
	
	@RequestMapping("/listProduct")
	public List<ProductBean> listProduct() throws ClassNotFoundException, SQLException {
		System.out.println("Inside listProduct");

		List<ProductBean> beans = ShoppingDao.getProducts();
		return beans;
	}
	
	@RequestMapping("/buyProduct")
	public void buyProduct(@RequestBody List<PurchasedBean> purchased) throws ClassNotFoundException, SQLException {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");  
		LocalDateTime now = LocalDateTime.now();  
		String invoiceId = dtf.format(now);
		
		
		Double totalPrice = 0.0;
		for(PurchasedBean bean: purchased) {
			
			totalPrice = totalPrice + (double) (bean.getProductPrice() * bean.getQuantity());
		
		}
		
		ShoppingDao.insertPurchase(invoiceId, totalPrice);
		
	}
	
	@RequestMapping("/updateProduct")
	public void updateProduct(@RequestBody ProductBean updatedBean) throws ClassNotFoundException, SQLException {
		
		
		ShoppingDao.insertData(updatedBean);
	}
	
	@RequestMapping("/invoiceTransaction")
	public List<TransactionBean> invoiceTransaction() throws ClassNotFoundException, SQLException {
		

		List<TransactionBean> beans = ShoppingDao.transactionData();
		return beans;
		
	}
	
}



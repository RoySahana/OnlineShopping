package com.shop;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bean.ProductBean;
import bean.PurchasedBean;

public class ShoppingDao {
	
	private static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingCart", "root", "Siyona12@");
		return con;
		
	}


	public static void insertData(ProductBean bean) throws SQLException, ClassNotFoundException {
		
		
		PreparedStatement pstmt = getConnection().prepareStatement("INSERT INTO ShoppingCart.AddProduct (ID, " 
				+ "PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_DESCRIPTION, STOCK, TRANSACTION_TYPE, PRODUCT_ID) VALUES (?,?,?,?,?,?,?)" );
		
		PreparedStatement idpstmt = getConnection().prepareStatement("SELECT MAX(id)  FROM ShoppingCart.AddProduct");
		
		ResultSet rs = idpstmt.executeQuery();
		int id = 0;
		while(rs.next()) {
			id = rs.getInt(1);
			id++;
		}
		
		PreparedStatement prod_idpstmt = getConnection().prepareStatement("SELECT MAX(product_id)  FROM ShoppingCart.AddProduct");
		
		rs = prod_idpstmt.executeQuery();
		int prod_id = 0;
		while(rs.next()) {
			prod_id = rs.getInt(1);
			prod_id++;
		}
		
		
		pstmt.setInt(1, id);
		pstmt.setString(2, bean.getProductName());
		pstmt.setDouble(3, bean.getProductPrice());
		pstmt.setString(4, bean.getProductDescription());
		pstmt.setInt(5, bean.getStock());
		pstmt.setString(6, "C");
		pstmt.setInt(7, prod_id++);
		
		pstmt.execute();
	}
	


	public static List<ProductBean> getProducts() throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = getConnection().prepareStatement("SELECT * FROM ShoppingCart.AddProduct");
		
		ResultSet rs = pstmt.executeQuery();
		
		List<ProductBean> productList = new ArrayList<>();
		while(rs.next()) {
			ProductBean bean = new ProductBean();
			bean.setId(rs.getInt(1));
			bean.setProductName(rs.getString(2));
			bean.setProductPrice(rs.getInt(3));
			bean.setProductDescription(rs.getString(4));
			bean.setStock(rs.getInt(5));
			bean.setProductId(rs.getInt(7));
			productList.add(bean);
			
		}
	
		rs.close();
		pstmt.close();
		return productList;
	}


	public static void insertPurchase(String invoiceId, Double totalPrice) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = getConnection().prepareStatement("INSERT INTO ShoppingCart.PurchaseTable (ID, " 
				+ "INVOICE_ID, TOTAL_AMOUNT, CREATION_TIME) VALUES (?,?,?,?)" );
		
		PreparedStatement idpstmt = getConnection().prepareStatement("SELECT MAX(id)  FROM ShoppingCart.PurchaseTable");
		
		ResultSet rs = idpstmt.executeQuery();
		int id = 0;
		while(rs.next()) {
			id = rs.getInt(1);
		}
		
		pstmt.setInt(1, id + 1);
		pstmt.setString(2, invoiceId);
		pstmt.setDouble(3, totalPrice);
		pstmt.setDate(4, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
	
		pstmt.execute();
	}


	public static List<TransactionBean> transactionData() throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = getConnection().prepareStatement("SELECT * FROM ShoppingCart.PurchaseTable;");
		
		ResultSet rs = pstmt.executeQuery();
		
		List<TransactionBean> invoiceList = new ArrayList<>();
		while(rs.next()) {
			TransactionBean bean1 = new TransactionBean();
			bean1.setId(rs.getInt(1));
			bean1.setInvoiceId(rs.getString(2));
			bean1.setTotalAmount(rs.getDouble(3));
			bean1.setCreationTime(rs.getString(4));
		
			invoiceList.add(bean1);
			
		}
	
		rs.close();
		pstmt.close();
		return invoiceList;
		
	}


}

package com.study.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.study.dao.ItemsDao;
import com.study.domain.Items;

public class ItemsDaoImpl implements ItemsDao{

	@Override
	public List<Items> queryItems() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		List<Items> items = new ArrayList<Items>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mybaits01", "root", "123");
			String sql = "select * from items";
			ps = con.prepareStatement(sql);
			resultSet = ps.executeQuery();
			while(resultSet.next()){
				Items item = new Items();
				item.setId(resultSet.getInt("id"));
				item.setName(resultSet.getString("name"));
				item.setPrice(resultSet.getDouble("price"));
				item.setDetail(resultSet.getString("detail"));
				item.setCreateTime(resultSet.getDate("createTime"));
				items.add(item);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}
	

	public static void main(String[] args) {
		ItemsDaoImpl itemsDao = new ItemsDaoImpl();
		List<Items> items = itemsDao.queryItems();
		for (Items item : items) {
			System.out.println(item.getId()+item.getName()+item.getPrice()+item.getDetail()+item.getCreateTime());
		}
	} 
}

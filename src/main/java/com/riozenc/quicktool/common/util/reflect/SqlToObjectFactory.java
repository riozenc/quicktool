package com.riozenc.quicktool.common.util.reflect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SqlToObjectFactory {

	private String driver = null;
	private String userName = null;
	private String password = null;
	private String url = null;

	public SqlToObjectFactory(String driver, String userName, String password, String url) {
		this.driver = driver;
		this.userName = userName;
		this.password = password;
		this.url = url;
	}

	public List<?> covert(String sql, Class<?> clazz) {

		Connection connection = getConnection();

		Statement statement = null;
		ResultSet resultSet = null;

		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			return convert(resultSet, clazz);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private Connection getConnection() {
		// 创建一个数据库连接
		Connection connection = null;
		try {
			Class.forName(driver);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			connection = DriverManager.getConnection(url, userName, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connection;
	}

	private List<?> convert(final ResultSet resultSet, final Class<?> clazz) {
		return ResultSetCaseUtil.change(resultSet, clazz);
	}

}

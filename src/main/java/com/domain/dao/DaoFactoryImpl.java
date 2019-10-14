package com.domain.dao;

import com.domain.utils.PropertyUtils;
import org.apache.commons.dbutils.DbUtils;
import org.h2.tools.RunScript;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** H2 dao factory helper class. */
public final class DaoFactoryImpl extends DaoFactory {
	private static final String CONNECTION_URL = PropertyUtils.getStringProperty("connection_url");
	private static final String DRIVER = PropertyUtils.getStringProperty("driver");
	private static final String PASSWORD = PropertyUtils.getStringProperty("password");
	private static final String USER = PropertyUtils.getStringProperty("user");

	private final AccountDaoImpl accountDAO = new AccountDaoImpl();

	DaoFactoryImpl() {
		DbUtils.loadDriver(DRIVER);
	}

	static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
	}

	public AccountDao getAccountDao() {
		return accountDAO;
	}

	@Override
	public void prepareData() {
		Connection conn = null;
		try {
			conn = DaoFactoryImpl.getConnection();
			RunScript.execute(conn, new FileReader("src/test/resources/data.sql"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

}

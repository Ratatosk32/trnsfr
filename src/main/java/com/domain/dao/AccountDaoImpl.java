package com.domain.dao;

import com.domain.exception.TransferException;
import com.domain.model.Account;
import com.domain.model.Transaction;
import org.apache.commons.dbutils.DbUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.math.BigDecimal.ZERO;

/** AccountDao implementation. */
public final class AccountDaoImpl implements AccountDao {

	private final static String GET_BY_ID_QUERY = "SELECT * FROM Account WHERE AccountId = ? ";
	private final static String LOCK_BY_ID_QUERY = "SELECT * FROM Account WHERE AccountId = ? FOR UPDATE";
	private final static String UPDATE_QUERY = "UPDATE Account SET Balance = ? WHERE AccountId = ? ";

	@Override
	public Account getAccountById(long accountId) throws TransferException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Account account = null;
		try {
			connection = DaoFactoryImpl.getConnection();
			preparedStatement = connection.prepareStatement(GET_BY_ID_QUERY);
			preparedStatement.setLong(1, accountId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				account = convertToAccount(resultSet);
			}
		} catch (SQLException e) {
			processSqlException(connection);
		} finally {
			DbUtils.closeQuietly(connection, preparedStatement, resultSet);
		}
		return account;
	}

	@Override
	public int updateAccountBalance(long accountId, BigDecimal deltaAmount) throws TransferException {
		Connection connection = null;
		PreparedStatement lockStatement = null;
		PreparedStatement updateStatement = null;
		ResultSet resultSet = null;
		Account targetAccount = null;
		int updateCount = -1;
		try {
			connection = DaoFactoryImpl.getConnection();
			connection.setAutoCommit(false);
			lockStatement = connection.prepareStatement(LOCK_BY_ID_QUERY);
			lockStatement.setLong(1, accountId);
			resultSet = lockStatement.executeQuery();
			if (resultSet.next()) {
				targetAccount = convertToAccount(resultSet);
			}
			if (targetAccount == null) {
				throw new TransferException("updateAccountBalance(): fail to lock account : " + accountId);
			}
			// update account upon success locking
			BigDecimal balance = targetAccount.getBalance().add(deltaAmount);
			if (balance.compareTo(ZERO) < 0) {
				throw new TransferException("Not sufficient Fund for account: " + accountId);
			}

			updateStatement = connection.prepareStatement(UPDATE_QUERY);
			updateStatement.setBigDecimal(1, balance);
			updateStatement.setLong(2, accountId);

			updateStatement.executeUpdate();
			connection.commit();
			return updateCount;
		} catch (SQLException se) {
			processSqlException(connection);
		} finally {
			close(connection, lockStatement, updateStatement, resultSet);
		}
		return updateCount;
	}

	@Override
	public int transferAccountBalance(Transaction transaction) throws TransferException {
		Connection connection = null;
		PreparedStatement lockStatement = null;
		PreparedStatement updateStatement = null;
		ResultSet resultSet = null;
		Account fromAccount = null;
		Account toAccount = null;
		int result = -1;

		try {
			connection = DaoFactoryImpl.getConnection();
			connection.setAutoCommit(false);
			lockStatement = connection.prepareStatement(LOCK_BY_ID_QUERY);
			lockStatement.setLong(1, transaction.getFromAccountId());
			resultSet = lockStatement.executeQuery();
			if (resultSet.next()) {
				fromAccount = convertToAccount(resultSet);
			}
			lockStatement = connection.prepareStatement(LOCK_BY_ID_QUERY);
			lockStatement.setLong(1, transaction.getToAccountId());
			resultSet = lockStatement.executeQuery();
			if (resultSet.next()) {
				toAccount = convertToAccount(resultSet);
			}
			if (fromAccount == null || toAccount == null) {
				throw new TransferException("Fail to lock both accounts for write");
			}
			BigDecimal fromAccountLeftOver = fromAccount.getBalance().subtract(transaction.getAmount());
			if (fromAccountLeftOver.compareTo(ZERO) < 0) {
				throw new TransferException("Not enough Fund from source Account ");
			}

			updateStatement = connection.prepareStatement(UPDATE_QUERY);
			updateStatement.setBigDecimal(1, fromAccountLeftOver);
			updateStatement.setLong(2, transaction.getFromAccountId());
			updateStatement.addBatch();
			updateStatement.setBigDecimal(1, toAccount.getBalance().add(transaction.getAmount()));
			updateStatement.setLong(2, transaction.getToAccountId());
			updateStatement.addBatch();

			int[] rowsUpdated = updateStatement.executeBatch();
			result = rowsUpdated[0] + rowsUpdated[1];
			connection.commit();
		} catch (SQLException se) {
			processSqlException(connection);
		} finally {
			close(connection, lockStatement, updateStatement, resultSet);
		}
		return result;
	}

	private static Account convertToAccount(ResultSet rs) throws SQLException{
		return new Account(rs.getLong("AccountId"), rs.getString("UserName"), rs.getBigDecimal("Balance"));
	}

	private static void processSqlException(Connection connection) throws TransferException {
		try {
			if (connection != null)
				connection.rollback();
		} catch (SQLException re) {
			throw new TransferException("Fail to rollback transaction", re);
		}
	}

	private static void close(Connection conn, PreparedStatement lockStmt, PreparedStatement updateStmt, ResultSet rs) {
		DbUtils.closeQuietly(conn);
		DbUtils.closeQuietly(rs);
		DbUtils.closeQuietly(lockStmt);
		DbUtils.closeQuietly(updateStmt);
	}
}

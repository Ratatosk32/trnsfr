package com.domain.dao;

import com.domain.exception.TransferException;
import com.domain.model.Account;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.domain.utils.FormatterUtils.formatToBigDecimal;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class AccountDaoTest {

	private static final DaoFactory dao = DaoFactory.newInstance();

	@BeforeClass
	public static void setup() {
		dao.prepareData();
	}

	@Test
	public void getAccountById_positive() throws TransferException {
		Account account = dao.getAccountDao().getAccountById(1L);
		assertEquals("test", account.getUserName());
	}

	@Test
	public void getAccountById_negative() throws TransferException {
		Account account = dao.getAccountDao().getAccountById(100L);
		assertNull(account);
	}

	@Test
	public void testUpdateAccountBalance_positive() throws TransferException {
		dao.getAccountDao().updateAccountBalance(1L, formatToBigDecimal(50));

		assertEquals(dao.getAccountDao().getAccountById(1L).getBalance(), formatToBigDecimal(100));

		dao.getAccountDao().updateAccountBalance(1L, formatToBigDecimal(-50));

		assertEquals(dao.getAccountDao().getAccountById(1L).getBalance(), formatToBigDecimal(50));
	}

	@Test(expected = TransferException.class)
	public void testUpdateAccountBalance_negative() throws TransferException {
		int rowsUpdatedW = dao.getAccountDao().updateAccountBalance(1L, formatToBigDecimal(-50000));

		assertEquals(0, rowsUpdatedW);
	}
}
package com.domain.dao;

import com.domain.exception.TransferException;
import com.domain.model.Account;
import com.domain.model.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static com.domain.utils.FormatterUtils.formatToBigDecimal;
import static junit.framework.TestCase.assertEquals;

public class AccountBalanceTest {

    private static final int THREADS_COUNT = 100;
    private static final DaoFactory daoFactory = DaoFactory.newInstance();

	@BeforeClass
	public static void setup() {
		daoFactory.prepareData();
	}

	@Test
	public void testAccountMultiThreadedTransfer() throws InterruptedException, TransferException {
		final AccountDao accountDAO = daoFactory.getAccountDao();
		final CountDownLatch latch = new CountDownLatch(THREADS_COUNT);
		for (int i = 0; i < THREADS_COUNT; i++) {
			new Thread(() -> {
				try {
					Transaction transaction = new Transaction(formatToBigDecimal(2), 1L, 2L);
					accountDAO.transferAccountBalance(transaction);
				} catch (Exception e) {
				    throw new RuntimeException(e);
				} finally {
					latch.countDown();
				}
			}).start();
		}
		latch.await();

		Account accountFrom = accountDAO.getAccountById(1);
		Account accountTo = accountDAO.getAccountById(2);

		assertEquals(accountFrom.getBalance(), formatToBigDecimal(0));
		assertEquals(accountTo.getBalance(), formatToBigDecimal(200));
	}
}

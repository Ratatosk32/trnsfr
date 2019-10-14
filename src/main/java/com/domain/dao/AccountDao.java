package com.domain.dao;

import com.domain.exception.TransferException;
import com.domain.model.Account;
import com.domain.model.Transaction;

import java.math.BigDecimal;

/** Account specified DAO operations. */
public interface AccountDao {
    /** Returns account by specified id. */
    Account getAccountById(long accountId) throws TransferException;
    /** Updates account balance. */
    int updateAccountBalance(long accountId, BigDecimal amount) throws TransferException;
    /** Applies transaction. */
    int transferAccountBalance(Transaction transaction) throws TransferException;
}

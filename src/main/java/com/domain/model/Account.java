package com.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/** Entity represents account's information. */
public final class Account {

    @JsonIgnore
    private long accountId;

    @JsonProperty(required = true)
    private String userName;

    @JsonProperty(required = true)
    private BigDecimal balance;

    public Account() {
    }

    public Account(long accountId, String userName, BigDecimal balance) {
        this.accountId = accountId;
        this.userName = userName;
        this.balance = balance;
    }

    public long getAccountId() {
        return accountId;
    }

    public String getUserName() {
        return userName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account that = (Account) o;
        return balance.equals(that.balance) && userName.equals(that.userName) && accountId == that.accountId;

    }

    @Override
    public int hashCode() {
        int result = (int) (accountId ^ (accountId >>> 16));
        result = 31 * result + userName.hashCode();
        result = 31 * result + balance.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "account:" + accountId + userName + balance;
    }
}

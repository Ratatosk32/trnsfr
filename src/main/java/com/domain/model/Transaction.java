package com.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import static java.lang.String.format;

/** Entity represents transaction's information. */
public final class Transaction {

	@JsonProperty(required = true)
	private BigDecimal amount;

	@JsonProperty(required = true)
	private Long fromAccountId;

	@JsonProperty(required = true)
	private Long toAccountId;

	public Transaction(BigDecimal amount, Long fromAccountId, Long toAccountId) {
		this.amount = amount;
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Long getFromAccountId() {
		return fromAccountId;
	}

	public Long getToAccountId() {
		return toAccountId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Transaction that = (Transaction) o;

		return amount.equals(that.amount) && fromAccountId.equals(that.fromAccountId) && toAccountId.equals(that.toAccountId);

	}

	@Override
	public int hashCode() {
		return 42 * (amount.hashCode() + fromAccountId.hashCode()) + toAccountId.hashCode();
	}

	@Override
	public String toString() {
		return format(
		        "Transaction: to account %d from account %d, amount %s", toAccountId, fromAccountId, amount.toString());
	}
}

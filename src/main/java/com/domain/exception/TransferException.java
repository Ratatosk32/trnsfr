package com.domain.exception;

/** App specific exception. */
public final class TransferException extends Exception {

	private static final long serialVersionUID = 1L;

	public TransferException(String msg) {
		super(msg);
	}

	public TransferException(String msg, Throwable cause) {
		super(msg, cause);
	}
}

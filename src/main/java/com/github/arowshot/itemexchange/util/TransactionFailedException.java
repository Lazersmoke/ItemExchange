package com.github.arowshot.itemexchange.util;

public class TransactionFailedException extends Exception {
	private static final long serialVersionUID = 5357977015885443980L;
	public TransactionFailedException(String message) {
		super(message);
	}
}

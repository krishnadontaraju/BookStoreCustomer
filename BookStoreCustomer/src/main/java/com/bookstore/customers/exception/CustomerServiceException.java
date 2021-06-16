package com.bookstore.customers.exception;

@SuppressWarnings("serial")
public class CustomerServiceException extends RuntimeException{
	
	@SuppressWarnings("unused")
	private int exceptionCode;
	@SuppressWarnings("unused")
	private String message;
	
	public CustomerServiceException(int exceptionCode ,String message) {
		super(message);
		this.exceptionCode = exceptionCode;
	}
	
}

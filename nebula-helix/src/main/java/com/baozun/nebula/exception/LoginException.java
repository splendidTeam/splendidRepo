package com.baozun.nebula.exception;

public class LoginException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -177743644683337252L;

	public LoginException(){
		super();
	}

	public LoginException(String message, Throwable cause){
		super(message, cause);
	}

	public LoginException(String message){
		super(message);
	}

	public LoginException(Throwable cause){
		super(cause);
	}
}

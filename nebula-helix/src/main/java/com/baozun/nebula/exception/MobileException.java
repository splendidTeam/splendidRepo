package com.baozun.nebula.exception;

public class MobileException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4433044703199455550L;

	public MobileException(){
		super();
	}

	public MobileException(String message, Throwable cause){
		super(message, cause);
	}

	public MobileException(String message){
		super(message);
	}

	public MobileException(Throwable cause){
		super(cause);
	}
}

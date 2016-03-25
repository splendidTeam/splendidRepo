package com.baozun.nebula.exception;

/**
 * 验证码长度设置的异常
 * @author shouqun.li
 * @version 2016年3月25日 下午1:31:03
 */
public class CodeLengthException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6794541155896217445L;
	
	public CodeLengthException(){
		super();
	}

	public CodeLengthException(String message, Throwable cause){
		super(message, cause);
	}

	public CodeLengthException(String message){
		super(message);
	}

	public CodeLengthException(Throwable cause){
		super(cause);
	}
}

package com.baozun.nebula.utilities.integration.oauth.exception;

public class ThirdPartyException extends RuntimeException{


	private static final long serialVersionUID = 6721885228145253374L;

	public ThirdPartyException(){}

	public ThirdPartyException(String arg0){
		super(arg0);
	}

	public ThirdPartyException(Throwable arg0){
		super(arg0);
	}

	public ThirdPartyException(String arg0, Throwable arg1){
		super(arg0, arg1);
	}

}

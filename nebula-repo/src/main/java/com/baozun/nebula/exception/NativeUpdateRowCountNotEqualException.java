package com.baozun.nebula.exception;

/**
 * NativeUpdate 返回值(实际操作行数)跟期待值(期待操作行数)不符.
 * 
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 Jun 28, 2013 3:32:00 PM
 */
public class NativeUpdateRowCountNotEqualException extends RuntimeException{

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 665112595508596601L;

	/** 期待操作行数. */
	private Integer				expected;

	/** 实际操作行数. */
	private Integer				actual;

	/**
	 * Instantiates a new native update row count not equal exception.
	 * 
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 */
	public NativeUpdateRowCountNotEqualException(Integer expected, Integer actual){
		super();
		this.expected = expected;
		this.actual = actual;
	}

	/**
	 * Gets the 期待操作行数.
	 * 
	 * @return the expected
	 */
	public Integer getExpected(){
		return expected;
	}

	/**
	 * Gets the 实际操作行数.
	 * 
	 * @return the actual
	 */
	public Integer getActual(){
		return actual;
	}

	/**
	 * Sets the 期待操作行数.
	 * 
	 * @param expected
	 *            the expected to set
	 */
	public void setExpected(Integer expected){
		this.expected = expected;
	}

	/**
	 * Sets the 实际操作行数.
	 * 
	 * @param actual
	 *            the actual to set
	 */
	public void setActual(Integer actual){
		this.actual = actual;
	}

}

package com.baozun.nebula.exception;

/**
 * 意外的影响范围 <br>
 * NativeUpdate 返回值(实际操作行数)跟期待值(期待操作行数)不符.
 * 
 * <pre>
 * 使用场景必须明确:
 * 
 * 举例说明，如果说 希望更新一个实体，但是没更新到，然后认为这是一个业务异常，需要回滚之前的操作，那么应该throw new BusinessException(ErrorCode.XXX, [])；
 * 
 * 只有说当出现更新期望和实际不符，但是这个不符尚无法决定其业务逻辑，需要后续可能的异常操作时，才会引入此异常
 * 
 * 举例说明， 
 * private updateXXX() throws EffectRangeUnexpectedException {}
 * 
 * public updateX(){ 
 * 		try{ 
 * 			updateXXX()
 * 		} 
 * 		catch(EffectRangeUnexpectedException e){ 
 * 			throw new BusinessException();
 * 		}
 * }
 * 
 * 这种情况下意味着也可能会有这样的可能：
 * public updateXX(){ 
 * 		try{ 
 * 			updateXXX()
 * 		} 
 * 		catch(EffectRangeUnexpectedException e){ 
 * 			log.warn("There may have some questions here, please take a look.");
 * 		}
 * }
 * 
 * </pre>
 * 
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 Jun 28, 2013 3:32:00 PM
 */
public class EffectRangeUnexpectedException extends Exception{

	private static final long	serialVersionUID	= -5444099658261017367L;

	public EffectRangeUnexpectedException(String message){
		super(message);
		// super(ErrorCodes.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED, new Object[] { expectedCount, actualCount });
	}
}

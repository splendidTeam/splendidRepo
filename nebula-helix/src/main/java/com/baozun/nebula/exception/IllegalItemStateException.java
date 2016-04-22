package com.baozun.nebula.exception;

/**
 * 商品状态无效是抛出的异常
 *
 */
public class IllegalItemStateException extends Exception {

	private static final long serialVersionUID = 1208498912002236678L;
	
	private IllegalItemState state;
	
	public IllegalItemStateException(){
		super();
	}

	public IllegalItemStateException(IllegalItemState state){
		super();
		this.state = state;
	}

	public IllegalItemStateException(String message, Throwable cause){
		super(message, cause);
	}

	public IllegalItemStateException(String message){
		super(message);
	}
	
	public IllegalItemStateException(IllegalItemState state, String message){
		super(message);
		this.state = state;
	}

	public IllegalItemStateException(Throwable cause){
		super(cause);
	}
	
	public IllegalItemState getState() {
		return state;
	}


	public enum IllegalItemState {

	    /** 商品不存在. */
	    ITEM_NOT_EXISTS,
	    
	    /** 商品为赠品. */
	    ITEM_ILLEGAL_TYPE_GIFT,

	    /** 商品下架. */
	    ITEM_LIFECYCLE_OFFSALE,

	    /** 商品 逻辑删除. */
	    ITEM_LIFECYCLE_LOGICAL_DELETED,

	    /** 商品新建状态. */
	    ITEM_LIFECYCLE_NEW,

	    /** 商品未到提前上架时间. */
	    ITEM_BEFORE_ACTIVE_TIME;
	}
}

package com.baozun.nebula.wormhole.mq.entity.order;

import java.io.Serializable;

/**
 * 退换货申请明细行信息
 * @author yimin.qiao
 * @createtime 2016年1月13日 下午4:54:12
 */
public class ReturnApplicationLineV5 implements Serializable {

	private static final long serialVersionUID = 7374363221100685805L;

	/** 退回商品外部编码 */
	private String returnExtentionCode;
	
	/** 换货商品外部编码 */
	private String changeExtentionCode;
	
	/** 数量 */
	private Integer qty;
	
	/** 商城订单行Id */
	private Long bsSoLineId;

	public String getReturnExtentionCode() {
		return returnExtentionCode;
	}

	public void setReturnExtentionCode(String returnExtentionCode) {
		this.returnExtentionCode = returnExtentionCode;
	}

	public String getChangeExtentionCode() {
		return changeExtentionCode;
	}

	public void setChangeExtentionCode(String changeExtentionCode) {
		this.changeExtentionCode = changeExtentionCode;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Long getBsSoLineId() {
		return bsSoLineId;
	}

	public void setBsSoLineId(Long bsSoLineId) {
		this.bsSoLineId = bsSoLineId;
	}
	
}

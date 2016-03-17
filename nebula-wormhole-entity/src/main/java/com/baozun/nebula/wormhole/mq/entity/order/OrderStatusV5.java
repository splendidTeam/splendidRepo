package com.baozun.nebula.wormhole.mq.entity.order;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单状态变更
 * @author Justin Hu
 *
 */

public class OrderStatusV5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1739318072484239434L;

    /**
     * 商城订单编码
     */
    private String bsOrderCode;

    /**
     * 商城退换货编码，商城发起退换货时非空
     */
    private String bsRaCode;
    
    /**
     * 操作类型
     * 1:订单创建成功 2:订单取消 3:过单到仓库 5:销售出库 7:退货已入库 8:换货已入库 9:换货已出库 10:交易完成（收货确认）13:系统刷款成功21:退换货申请取消22:退换货申请取消成功23:退换货申请取消失败
     * 参照：OrderStatusV5Constants
     */
    private Integer opType;

    /**
     * 状态变更时间
     */
    private Date opTime;


    /**
     * 物流单号
     * 快递单号：当出库时会提供
     */
    private String transCode;

    /**
     * 物流商编码
     */
    private String logisticsProviderCode;
    
    
    /**
     * 物流商名称
     */
    private String logisticsProviderName;
    
    /**
     * 备注(相关明细信息拼接成的JSON字符串)
     * 
     */
    private String remark;
    


	public String getBsOrderCode() {
		return bsOrderCode;
	}

	public void setBsOrderCode(String bsOrderCode) {
		this.bsOrderCode = bsOrderCode;
	}
	
	public String getBsRaCode() {
		return bsRaCode;
	}

	public void setBsRaCode(String bsRaCode) {
		this.bsRaCode = bsRaCode;
	}
	
	public Integer getOpType() {
		return opType;
	}

	public void setOpType(Integer opType) {
		this.opType = opType;
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getLogisticsProviderCode() {
		return logisticsProviderCode;
	}

	public void setLogisticsProviderCode(String logisticsProviderCode) {
		this.logisticsProviderCode = logisticsProviderCode;
	}

	public String getLogisticsProviderName() {
		return logisticsProviderName;
	}

	public void setLogisticsProviderName(String logisticsProviderName) {
		this.logisticsProviderName = logisticsProviderName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	    
}

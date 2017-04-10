package com.baozun.nebula.command;
import java.math.BigDecimal;
import java.util.Date;

import com.baozun.nebula.model.BaseModel;

/**
 * 退换货<br>
 * 
 */

public class ReturnLineCommand extends BaseModel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3411890079966239231L;

	/** SoReturnApplication(退换货申请单)ID */
	private Long returnOrderId;
	private String rtnExtentionCode;// 退回商品外部编码
	private String chgExtentionCode;// 换货商品外部编码.当[退换货申请类型]为[换货申请]时，则该信息非空
	private int qty; // 退换货商品数量
	/**
	 * SoLine(平台退货订单行)ID
	 * 原始订单行ID哪怕是多次换货
	 */
	private Long soLineId;
	/** 原始套盒订单行ID packagelineid */
	private Long packageLineId;
	private String returnReason; // 退回原因
	private Date createTime; // 创建时间
	private String imgName; // 图片名,质量问题退换时上传的图片名称，图片名称自动生成
    private String memo;//备注
	/** 退货单商品行的退款价*/
	private BigDecimal returnPrice;
	/** 订单行类型 1 普通 ， 2 套盒 */
    private Integer type;
    
    private String itemCode;
    
    private String productName;

	public Long getReturnOrderId() {
		return returnOrderId;
	}

	public void setReturnOrderId(Long returnOrderId) {
		this.returnOrderId = returnOrderId;
	}

	public String getRtnExtentionCode() {
		return rtnExtentionCode;
	}

	public void setRtnExtentionCode(String rtnExtentionCode) {
		this.rtnExtentionCode = rtnExtentionCode;
	}

	public String getChgExtentionCode() {
		return chgExtentionCode;
	}

	public void setChgExtentionCode(String chgExtentionCode) {
		this.chgExtentionCode = chgExtentionCode;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public Long getSoLineId() {
		return soLineId;
	}

	public void setSoLineId(Long soLineId) {
		this.soLineId = soLineId;
	}

	public Long getPackageLineId() {
		return packageLineId;
	}

	public void setPackageLineId(Long packageLineId) {
		this.packageLineId = packageLineId;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public BigDecimal getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(BigDecimal returnPrice) {
		this.returnPrice = returnPrice;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
    


}

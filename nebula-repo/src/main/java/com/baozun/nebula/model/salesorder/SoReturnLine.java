package com.baozun.nebula.model.salesorder;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 退换货商品行表<br>
 * 
 */
@Entity
@Table(name = "T_SO_RETURN_LINE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SoReturnLine extends BaseModel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3411890079966239231L;

	private Long id; // 主键ID
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

	/**
	 * Gets the pK.
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SO_RETURN_LINE", sequenceName = "S_T_SO_RETURN_LINE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_SO_RETURN_LINE")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the new pK
	 */
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "RETURN_ORDERID", nullable = false)
	public Long getReturnOrderId() {
		return returnOrderId;
	}

	public void setReturnOrderId(Long returnOrderId) {
		this.returnOrderId = returnOrderId;
	}

	@Column(name = "RTN_EXTENTIONCODE", nullable = false)
	public String getRtnExtentionCode() {
		return rtnExtentionCode;
	}

	public void setRtnExtentionCode(String rtnExtentionCode) {
		this.rtnExtentionCode = rtnExtentionCode;
	}

//	@Column(name = "CHG_EXTENTIONCODE", nullable = false)
	@Column(name = "CHG_EXTENTIONCODE")
	public String getChgExtentionCode() {
		return chgExtentionCode;
	}

	public void setChgExtentionCode(String chgExtentionCode) {
		this.chgExtentionCode = chgExtentionCode;
	}

	@Column(name = "QTY")
	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	@Column(name = "SO_LINEID", nullable = false)
	public Long getSoLineId() {
		return soLineId;
	}

	public void setSoLineId(Long soLineId) {
		this.soLineId = soLineId;
	}
	
	@Column(name = "PACKAGE_LINE_ID")
	public Long getPackageLineId() {
		return packageLineId;
	}

	public void setPackageLineId(Long packageLineId) {
		this.packageLineId = packageLineId;
	}

	@Column(name = "RETURN_REASON", length = 1000)
	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	
	@Column(name = "CREATE_TIME", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "IMGNAME", length = 500)
	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
	@Column(name = "MEMO", length = 1000)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "RETURN_PRICE", precision = 15, scale = 5)
	public BigDecimal getReturnPrice() {
		return returnPrice;
	}
	
	public void setReturnPrice(BigDecimal returnPrice) {
		this.returnPrice = returnPrice;
	}
	
	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}

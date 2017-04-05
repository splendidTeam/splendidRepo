/**
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-8-8
 */
package com.baozun.nebula.model.delivery;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * @Description 地区配送方式表
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-8-8
 */
@javax.persistence.Entity
@Table(name = "T_SF_AREA_DELIVERY_MODE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class AreaDeliveryMode extends BaseModel {

	/**
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-8-8
	 */
	private static final long serialVersionUID = -4821787452652167769L;
	
	/**支持**/
	public static final String YES = "Y";
	
	/**不支持**/
	public static final String NO = "N";
	
	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 外键（DeliveryArea主键）
	 */
	private Long areaId;

	/**
	 * 物流类型编码
	 */
	private String logisticsCode;

	/**
	 * 物流公司名称
	 */
	private String logisticsCompany;

	/**
	 * 普通配送
	 */
	private String commonDelivery;

	/**
	 * 普通配送的起始时间（几点为单位）
	 */
	private String commonDeliveryStartTime;

	/**
	 * 普通配送的关闭时间（几点为单位）
	 */
	private String commonDeliveryEndTime;

	/**
	 * 是否支持当日达
	 */
	private String firstDayDelivery;

	/**
	 * 当日达配送的起始时间（几点为单位）
	 */
	private String firstDeliveryStartTime;

	/**
	 * 当日达送的关闭时间（几点为单位）
	 */
	private String firstDeliveryEndTime;

	/**
	 * 是否支持次日达
	 */
	private String secondDayDelivery;

	/**
	 * 次日达送的关闭时间（几点为单位）
	 */
	private String secondDeliveryStartTime;

	/**
	 * 次日达送的关闭时间（几点为单位）
	 */
	private String secondDeliveryEndTime;
	
	/**
	 * 是否支持第三种送达方式
	 */
	private String thirdDayDelivery;

	/**
	 * 第三种送达方式的关闭时间（几点为单位）
	 */
	private String thirdDeliveryStartTime;

	/**
	 * 第三种送达方式的关闭时间（几点为单位）
	 */
	private String thirdDeliveryEndTime;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date modifyTime;

	/**
	 * version
	 */
	private Date version;
	
	/**
	 * 是否支持COD
	 */
	private String support_COD;


	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SF_AREA_DELIVERY_MODE", sequenceName = "SEQ_T_SF_AREA_DELIVERY_MODE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_SF_AREA_DELIVERY_MODE")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the areaId
	 */
	@Column(name = "AREA_ID")
    @Index(name = "IDX_AREA_DELIVERY_MODE_AREA_ID")
	public Long getAreaId() {
		return areaId;
	}

	/**
	 * @param areaId
	 *            the areaId to set
	 */
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	/**
	 * @return the logisticsCode
	 */
	@Column(name = "LOGISTICS_CODE")
	public String getLogisticsCode() {
		return logisticsCode;
	}

	/**
	 * @param logisticsCode
	 *            the logisticsCode to set
	 */
	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	/**
	 * @return the logisticsCompany
	 */
	@Column(name = "LOGISTICS_COMPANY")
	public String getLogisticsCompany() {
		return logisticsCompany;
	}

	/**
	 * @param logisticsCompany
	 *            the logisticsCompany to set
	 */
	public void setLogisticsCompany(String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}

	/**
	 * @return the commonDelivery
	 */
	@Column(name = "COMMONDELIVERY")
	public String getCommonDelivery() {
		return commonDelivery;
	}

	/**
	 * @param commonDelivery
	 *            the commonDelivery to set
	 */
	public void setCommonDelivery(String commonDelivery) {
		this.commonDelivery = commonDelivery;
	}

	/**
	 * @return the firstDayDelivery
	 */
	@Column(name = "FIRSTDAYDELIVERY")
	public String getFirstDayDelivery() {
		return firstDayDelivery;
	}

	/**
	 * @param firstDayDelivery
	 *            the firstDayDelivery to set
	 */
	public void setFirstDayDelivery(String firstDayDelivery) {
		this.firstDayDelivery = firstDayDelivery;
	}

	/**
	 * @return the secondDayDelivery
	 */
	@Column(name = "SECONDDAYDELIVERY")
	public String getSecondDayDelivery() {
		return secondDayDelivery;
	}

	/**
	 * @param secondDayDelivery
	 *            the secondDayDelivery to set
	 */
	public void setSecondDayDelivery(String secondDayDelivery) {
		this.secondDayDelivery = secondDayDelivery;
	}

	/**
	 * @return the remark
	 */
	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the createTime
	 */
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the modifyTime
	 */
	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime
	 *            the modifyTime to set
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * @return the version
	 */
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Date version) {
		this.version = version;
	}

	/**
	 * @return the commonDeliveryStartTime
	 */
	@Column(name = "COMMON_DELIVERY_STARTTIME")
	public String getCommonDeliveryStartTime() {
		return commonDeliveryStartTime;
	}

	/**
	 * @param commonDeliveryStartTime
	 *            the commonDeliveryStartTime to set
	 */
	public void setCommonDeliveryStartTime(String commonDeliveryStartTime) {
		this.commonDeliveryStartTime = commonDeliveryStartTime;
	}

	/**
	 * @return the commonDeliveryEndTime
	 */
	@Column(name = "COMMON_DELIVERY_ENDTIME")
	public String getCommonDeliveryEndTime() {
		return commonDeliveryEndTime;
	}

	/**
	 * @param commonDeliveryEndTime
	 *            the commonDeliveryEndTime to set
	 */
	public void setCommonDeliveryEndTime(String commonDeliveryEndTime) {
		this.commonDeliveryEndTime = commonDeliveryEndTime;
	}

	/**
	 * @return the firstDeliveryStartTime
	 */
	@Column(name = "FIRST_DELIVERY_STARTTIME")
	public String getFirstDeliveryStartTime() {
		return firstDeliveryStartTime;
	}

	/**
	 * @param firstDeliveryStartTime
	 *            the firstDeliveryStartTime to set
	 */
	public void setFirstDeliveryStartTime(String firstDeliveryStartTime) {
		this.firstDeliveryStartTime = firstDeliveryStartTime;
	}

	/**
	 * @return the firstDeliveryEndTime
	 */
	@Column(name = "FIRST_DELIVERY_ENDTIME")
	public String getFirstDeliveryEndTime() {
		return firstDeliveryEndTime;
	}

	/**
	 * @param firstDeliveryEndTime
	 *            the firstDeliveryEndTime to set
	 */
	public void setFirstDeliveryEndTime(String firstDeliveryEndTime) {
		this.firstDeliveryEndTime = firstDeliveryEndTime;
	}

	/**
	 * @return the secondDeliveryStartTime
	 */
	@Column(name = "SECOND_DELIVERY_STARTTIME")
	public String getSecondDeliveryStartTime() {
		return secondDeliveryStartTime;
	}

	/**
	 * @param secondDeliveryStartTime
	 *            the secondDeliveryStartTime to set
	 */
	public void setSecondDeliveryStartTime(String secondDeliveryStartTime) {
		this.secondDeliveryStartTime = secondDeliveryStartTime;
	}

	/**
	 * @return the secondDeliveryEndTime
	 */
	@Column(name = "SECOND_DELIVERY_ENDTIME")
	public String getSecondDeliveryEndTime() {
		return secondDeliveryEndTime;
	}

	/**
	 * @param secondDeliveryEndTime
	 *            the secondDeliveryEndTime to set
	 */
	public void setSecondDeliveryEndTime(String secondDeliveryEndTime) {
		this.secondDeliveryEndTime = secondDeliveryEndTime;
	}
	
	/**
	 * @return the support_COD
	 */
	@Column(name = "SUPPORT_COD")
	public String getSupport_COD() {
		return support_COD;
	}

	/**
	 * @param support_COD the support_COD to set
	 */
	public void setSupport_COD(String support_COD) {
		this.support_COD = support_COD;
	}

	/**
	 * @return the thirdDayDelivery
	 */
	@Column(name = "THIRDDAYDELIVERY")
	public String getThirdDayDelivery() {
		return thirdDayDelivery;
	}

	/**
	 * @param thirdDayDelivery the thirdDayDelivery to set
	 */
	public void setThirdDayDelivery(String thirdDayDelivery) {
		this.thirdDayDelivery = thirdDayDelivery;
	}

	/**
	 * @return the thirdDeliveryStartTime
	 */
	@Column(name = "THIRD_DELIVERY_STARTTIME")
	public String getThirdDeliveryStartTime() {
		return thirdDeliveryStartTime;
	}

	/**
	 * @param thirdDeliveryStartTime the thirdDeliveryStartTime to set
	 */
	public void setThirdDeliveryStartTime(String thirdDeliveryStartTime) {
		this.thirdDeliveryStartTime = thirdDeliveryStartTime;
	}

	/**
	 * @return the thirdDeliveryEndTime
	 */
	@Column(name = "THIRD_DELIVERY_ENDTIME")
	public String getThirdDeliveryEndTime() {
		return thirdDeliveryEndTime;
	}

	/**
	 * @param thirdDeliveryEndTime the thirdDeliveryEndTime to set
	 */
	public void setThirdDeliveryEndTime(String thirdDeliveryEndTime) {
		this.thirdDeliveryEndTime = thirdDeliveryEndTime;
	}
	
}

/**
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-11-8
 */
package com.baozun.nebula.command.delivery;

import com.baozun.nebula.command.Command;

/**
 * @Description
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-11-8
 */
public class ContactDeliveryCommand implements Command {

	/**
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-8
	 */
	private static final long serialVersionUID = -1862849001021893443L;

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
	 * 是否支持COD
	 */
	private String support_COD;

	/**
	 * @return the logisticsCode
	 */
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
	 * @return the commonDeliveryStartTime
	 */
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
	 * @return the firstDayDelivery
	 */
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
	 * @return the firstDeliveryStartTime
	 */
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
	 * @return the secondDayDelivery
	 */
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
	 * @return the secondDeliveryStartTime
	 */
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
	 * @return the thirdDayDelivery
	 */
	public String getThirdDayDelivery() {
		return thirdDayDelivery;
	}

	/**
	 * @param thirdDayDelivery
	 *            the thirdDayDelivery to set
	 */
	public void setThirdDayDelivery(String thirdDayDelivery) {
		this.thirdDayDelivery = thirdDayDelivery;
	}

	/**
	 * @return the thirdDeliveryStartTime
	 */
	public String getThirdDeliveryStartTime() {
		return thirdDeliveryStartTime;
	}

	/**
	 * @param thirdDeliveryStartTime
	 *            the thirdDeliveryStartTime to set
	 */
	public void setThirdDeliveryStartTime(String thirdDeliveryStartTime) {
		this.thirdDeliveryStartTime = thirdDeliveryStartTime;
	}

	/**
	 * @return the thirdDeliveryEndTime
	 */
	public String getThirdDeliveryEndTime() {
		return thirdDeliveryEndTime;
	}

	/**
	 * @param thirdDeliveryEndTime
	 *            the thirdDeliveryEndTime to set
	 */
	public void setThirdDeliveryEndTime(String thirdDeliveryEndTime) {
		this.thirdDeliveryEndTime = thirdDeliveryEndTime;
	}

	/**
	 * @return the remark
	 */
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
	 * @return the support_COD
	 */
	public String getSupport_COD() {
		return support_COD;
	}

	/**
	 * @param support_COD
	 *            the support_COD to set
	 */
	public void setSupport_COD(String support_COD) {
		this.support_COD = support_COD;
	}

}

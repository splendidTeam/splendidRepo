/**
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-8-8
 */
package com.baozun.nebula.model.delivery;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * @Description
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-8-8
 */
@javax.persistence.Entity
@Table(name = "T_SF_RECEIVING_STATION")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ReceivingStation extends BaseModel {

	/**
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-8-8
	 */
	private static final long serialVersionUID = 1100687413460483890L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 外键（DeliveryArea主键）
	 */
	private Long areaId;

	/**
	 * 线下店编码
	 */
	private String code;

	/**
	 * 纬度
	 */
	private String longitude;

	/**
	 * 经度
	 */
	private String latitude;

	/**
	 * 是否有效
	 */
	private Integer status;

	/**
	 * 排序
	 */
	private Integer sortNo;

	/**
	 * 店名
	 */
	private String storeName;

	/**
	 * 门店类型
	 */
	private String type;

	/**
	 * 联系电话
	 */
	private String phone;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 信息
	 */
	private String message;

	/**
	 * 是否支持自提
	 */
	private Integer hasPickUp;

	/**
	 * 最大包裹上限
	 */
	private Integer maximum;

	/**
	 * 是否支持退换货
	 */
	private Integer hasReturn;

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SF_RECEIVING_STATION", sequenceName = "SEQ_T_SF_RECEIVING_STATION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_SF_RECEIVING_STATION")
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
	 * @return the code
	 */
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the longitude
	 */
	@Column(name = "LONGITUDE")
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	@Column(name = "LATITUDE")
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the sortNo
	 */
	@Column(name = "SORT_NO")
	public Integer getSortNo() {
		return sortNo;
	}

	/**
	 * @param sortNo
	 *            the sortNo to set
	 */
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	/**
	 * @return the storeName
	 */
	@Column(name = "STORE_NAME")
	public String getStoreName() {
		return storeName;
	}

	/**
	 * @param storeName
	 *            the storeName to set
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	/**
	 * @return the type
	 */
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the phone
	 */
	@Column(name = "PHONE")
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the address
	 */
	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the message
	 */
	@Column(name = "MESSAGE")
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the hasPickUp
	 */
	@Column(name = "HASPICKUP")
	public Integer getHasPickUp() {
		return hasPickUp;
	}

	/**
	 * @param hasPickUp
	 *            the hasPickUp to set
	 */
	public void setHasPickUp(Integer hasPickUp) {
		this.hasPickUp = hasPickUp;
	}

	/**
	 * @return the maximum
	 */
	@Column(name = "MAXIMUM")
	public Integer getMaximum() {
		return maximum;
	}

	/**
	 * @param maximum
	 *            the maximum to set
	 */
	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}

	/**
	 * @return the hasReturn
	 */
	@Column(name = "HASRETURN")
	public Integer getHasReturn() {
		return hasReturn;
	}

	/**
	 * @param hasReturn
	 *            the hasReturn to set
	 */
	public void setHasReturn(Integer hasReturn) {
		this.hasReturn = hasReturn;
	}

	/**
	 * @return the status
	 */
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the areaId
	 */
	@Column(name = "AREA_ID")
	public Long getAreaId() {
		return areaId;
	}

	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

}

/**
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-8-8
 */
package com.baozun.nebula.model.delivery;

import java.util.Date;

import com.baozun.nebula.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

/**
 * @Description 省市区三级下拉地址表
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-8-8
 */
@javax.persistence.Entity
@Table(name = "T_SF_DELIVERY_AREA")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class DeliveryArea extends BaseModel {

	/**
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-8-8
	 */
	private static final long serialVersionUID = -2293754635068130002L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 国家，省，市，区
	 */
	private String area;

	/**
	 * 国家，省，市，区编码
	 */
	private String code;

	/**
	 * 父级区域
	 */
	private Long parentId;

	/**
	 * 层级
	 */
	private Integer level;

	/**
	 * 排序
	 */
	private Integer sortNo;

	/**
	 * 是否有效
	 */
	private Integer status;
	
	/**
	 * 语言标识
	 */
	private String lang;

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
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SF_DELIVERY_AREA", sequenceName = "SEQ_T_SF_DELIVERY_AREA", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_SF_DELIVERY_AREA")
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
	 * @return the area
	 */
	@Column(name = "AREA")
    @Index(name = "IDX_DELIVERY_AREA_AREA")
	public String getArea() {
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the code
	 */
	@Column(name = "CODE")
    @Index(name = "IDX_DELIVERY_AREA_CODE")
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
	 * @return the parentId
	 */
	@Column(name = "PARENT_ID")
    @Index(name = "IDX_DELIVERY_AREA_PARENT_ID")
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
	 * @return the level
	 */
	@Column(name = "LEVEL")
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * @return the status
	 */
	@Column(name = "STATUS")
    @Index(name = "IDX_DELIVERY_AREA_STATUS")
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
	 * @return the lang
	 */
	@Column(name = "LANG")
    @Index(name = "IDX_DELIVERY_AREA_LANG")
	public String getLang() {
		return lang;
	}

	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

}

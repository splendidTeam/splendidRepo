package com.baozun.nebula.model.cms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * cms - 页面模板
 * 通过一个页面模板可以实例化很多页面，每个页面针对模板有自己定制化的部分
 * 
 * 
 * @author Justin Hu
 * 
 */
@Entity
@Table(name = "T_CMS_PAGE_TEMPLATE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CmsPageTemplate extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6505806169838367960L;

	/**
	 * id
	 */
	private Long				id;

	/**
	 * 模板名称
	 */
	private String				name;

	/**
	 * 模板截图
	 */
	private String				img;

	/**
	 * 模板数据文件(text)
	 */
	private String				data;

	/**
	 * 是否使用公共头尾
	 */
	private Boolean				useCommonHeader;


	/**
	 * 创建时间
	 */
	private Date				createTime;
	
	/**
	 * 修改时间
	 */
	private Date				modifyTime;
	
	/**
	 * 生命周期
	 */
	private Integer				lifecycle;


	private Date				version;
	/**
	 * 支持的类型 0:综合 ,1:pc,2:mobile
	 */
	private Integer  supportType;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_CMS_PAGE_TEMPLATE", sequenceName = "S_T_CMS_PAGE_TEMPLATE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CMS_PAGE_TEMPLATE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "IMG")
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Column(name = "DATA")
	@Lob
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Column(name = "USE_COMMON_HEADER")
	public Boolean getUseCommonHeader() {
		return useCommonHeader;
	}

	public void setUseCommonHeader(Boolean useCommonHeader) {
		this.useCommonHeader = useCommonHeader;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
	@Column(name = "SUPPORT_TYPE")
	public Integer getSupportType() {
		return supportType;
	}

	public void setSupportType(Integer supportType) {
		this.supportType = supportType;
	}

}

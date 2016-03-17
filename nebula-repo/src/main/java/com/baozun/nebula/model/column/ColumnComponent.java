package com.baozun.nebula.model.column;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 板块-组件配置
 * 
 * @author Justin Hu
 * 
 */
@Entity
@Table(name = "T_COL_COLUMN_COMPONENT")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ColumnComponent extends BaseModel {

	/**
	 * 
	 */
	private static final long	serialVersionUID			= -6250980412796601492L;

	/**
	 * id
	 */
	private Long				id;

	/**
	 * 目标对象id(商品id,分类id)
	 */
	private Long				targetId;

	/**
	 * 标题
	 */
	private String				title;

	/**
	 * 链接url
	 */
	private String				url;

	/**
	 * 描述
	 */
	private String				description;
	/**
	 * 图片链接
	 */
	private String				img;
	
	/**
	 * 图片宽度,img有值时才有效
	 */
	private Integer				imgWidth;
	
	/**
	 * 图片高度,img有值时才有效
	 */
	private Integer				imgHeight;

	private Integer				sortNo;

	/**
	 * 扩展字段，是一个map的json对象
	 */
	private String				ext;

	/**
	 * 模块id
	 */
	private Long				moduleId;

	private Date				version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_COL_COLUMN_COMPONENT", sequenceName = "S_T_COL_COLUMN_COMPONENT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_COL_COLUMN_COMPONENT")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TARGET_ID")
	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	@Column(name = "TITLE", length = 255)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "URL", length = 512)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "DESCRIPTION", length = 512)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "IMG", length = 256)
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Column(name = "SORT_NO")
	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	@Column(name = "EXT", length = 1000)
	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	@Column(name = "MODULE_ID")
	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "IMG_WIDTH")
	public Integer getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(Integer imgWidth) {
		this.imgWidth = imgWidth;
	}

	@Column(name = "IMG_HEIGHT")
	public Integer getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(Integer imgHeight) {
		this.imgHeight = imgHeight;
	}
	
	

}

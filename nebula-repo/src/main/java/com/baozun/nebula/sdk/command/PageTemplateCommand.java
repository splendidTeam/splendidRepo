package com.baozun.nebula.sdk.command;

import java.util.Date;
import java.util.List;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.baseinfo.PageItem;

public class PageTemplateCommand  implements Command{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8976067074958693817L;

	/** PK. */
	private Long				id;

	/**
	 * 页面编码
	 */
	private String				pageCode;
	
	/**
	 * 页面名称
	 */
	private String				pageName;
	
	/**
	 * 页面图片(预览图)
	 */
	private String				img;
	
	/** 创建时间. */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;
	
	/**
	 * 生命周期
	 */
	private Integer				lifecycle;
	
	
	/** 最后操作者 */
	private Long				opeartorId;
	
	/** version. */
	private Date				version;

	
	private List<PageItem>  pageItems;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPageCode() {
		return pageCode;
	}

	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Long getOpeartorId() {
		return opeartorId;
	}

	public void setOpeartorId(Long opeartorId) {
		this.opeartorId = opeartorId;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	public List<PageItem> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<PageItem> pageItems) {
		this.pageItems = pageItems;
	}

}

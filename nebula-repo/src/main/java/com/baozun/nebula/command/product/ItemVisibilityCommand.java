package com.baozun.nebula.command.product;

import java.util.Date;

import com.baozun.nebula.command.Command;

/**
 * 
 * @Description: 商品可见性
 * @author 何波
 * @date 2014年9月24日 上午11:28:45
 * 
 */
public class ItemVisibilityCommand implements Command {

	private static final long serialVersionUID = -5567607383671994824L;

	private Long id;
	/**
	 * 会员筛选器id
	 */
	private Long memFilterId;

	private String memFilterName;

	/**
	 * 商品筛选器id
	 */
	private Long itemFilterId;

	private String itemFilterName;

	/**
	 * 生命周期
	 */
	private Integer lifecycle;

	/**
	 * version.
	 */
	private Date version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemFilterId() {
		return memFilterId;
	}

	public void setMemFilterId(Long memFilterId) {
		this.memFilterId = memFilterId;
	}

	public Long getItemFilterId() {
		return itemFilterId;
	}

	public void setItemFilterId(Long itemFilterId) {
		this.itemFilterId = itemFilterId;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	public String getMemFilterName() {
		return memFilterName;
	}

	public void setMemFilterName(String memFilterName) {
		this.memFilterName = memFilterName;
	}

	public String getItemFilterName() {
		return itemFilterName;
	}

	public void setItemFilterName(String itemFilterName) {
		this.itemFilterName = itemFilterName;
	}

}

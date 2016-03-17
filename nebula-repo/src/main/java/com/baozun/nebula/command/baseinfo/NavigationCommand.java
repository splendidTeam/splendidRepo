package com.baozun.nebula.command.baseinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Version;

import org.hibernate.annotations.Index;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.model.baseinfo.Navigation;

/**
 * 菜单导航
 * 供前台使用
 *
 */
public class NavigationCommand implements Command {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1047822667863367433L;

	/** PK. */
	private Long				id;
	
	/**
	 * 导航名称
	 */
	private LangProperty		name;

	/**
	 * 导航类型 1.url类型，设置一个url 2.分类类型，表示与分类相关联
	 */
	private Integer				type;
	
	/**
	 * 当type为分类类型时，这里填写分类id
	 */
	private Long 				param;
	
	/**
	 * 点击导航菜单前往的url地址
	 */
	private String				url;
	
	/** 创建时间. */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;
	
	/**
	 * 生命周期
	 */
	private Integer				lifecycle;
	
	/**
	 * 排序
	 */
	private Integer				sort;
	
	/**
	 * 是否为新窗口
	 */
	private Boolean				isNewWin;
	
	/** 最后操作者 */
	private Long				opeartorId;

	/** 父ID */
	private Long				parentId;
	
	/** version. */
	private Date				version;

	private Navigation self;

	/** 子节点 */
	private List<NavigationCommand> subNavigations = new ArrayList<NavigationCommand>();

	/** ‘/’分割的分类路径，当此节点及其祖先的type=分类类型 时提供 */
	private String path;

	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LangProperty getName() {
		return name;
	}

	public void setName(LangProperty name) {
		this.name = name;
	}

	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "PARAM")
	public Long getParam() {
		return param;
	}

	public void setParam(Long param) {
		this.param = param;
	}

	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
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

	@Column(name = "SORT")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Column(name = "OPEARTOR_ID")
	public Long getOpeartorId() {
		return opeartorId;
	}

	public void setOpeartorId(Long opeartorId) {
		this.opeartorId = opeartorId;
	}

	@Column(name = "IS_NEW_WIN")
	public Boolean getIsNewWin() {
		return isNewWin;
	}

	public void setIsNewWin(Boolean isNewWin) {
		this.isNewWin = isNewWin;
	}

	@Index(name = "IDX_NAVIGATION_PARENT_ID")
	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Navigation getSelf() {
		return self;
	}

	public void setSelf(Navigation self) {
		this.self = self;
	}

	public void setSubNavigations(List<NavigationCommand> subNavigations) {
		this.subNavigations = subNavigations;
	}

	public List<NavigationCommand> getSubNavigations() {
		return subNavigations;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}

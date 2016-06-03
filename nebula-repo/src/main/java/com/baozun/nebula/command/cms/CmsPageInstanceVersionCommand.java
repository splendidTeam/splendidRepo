package com.baozun.nebula.command.cms;


import java.io.Serializable;
import java.util.Date;

/**
 * 需要发布cmspage版本缓存数据
 * @author nan.xie
 *
 */
public class CmsPageInstanceVersionCommand implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2863143489682375567L;

	/**
	 * id
	 */
	private Long id;
	
	/**
	 * 实例版本名称
	 */
	private String name;
	
	/**
	 * 实例id
	 */
	private Long instance_id;
	
	/**
	 * 模板id
	 */
	private Long template_id;
	
	/**
	 * 实例类型
	 */
	private Integer type;
	
	/**
	 * 是否有效
	 */
	private Integer lifecycle;
	
	/**
	 * 是否发布
	 */
	private boolean ispublished;
	
	/**
	 * 创建时间
	 */
	private Date create_time;
	
	/**
	 * 版本
	 */
	private Date version;
	
	/**
	 * 修改时间
	 */
	private Date modify_time;
	
	/**
	 * 开始发布时间
	 */
	private Date start_time;
	
	/**
	 * 结束发布时间
	 */
	private Date end_time;
	
	/**
	 * 对应页面的url
	 */
	private String url;
	
	/**
	 * 页面code
	 */
	private String code;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getInstance_id() {
		return instance_id;
	}

	public void setInstance_id(Long instance_id) {
		this.instance_id = instance_id;
	}

	public Long getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(Long template_id) {
		this.template_id = template_id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public boolean isIspublished() {
		return ispublished;
	}

	public void setIspublished(boolean ispublished) {
		this.ispublished = ispublished;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	
}

package com.baozun.nebula.sdk.command;

import java.util.Date;
import java.util.List;

import com.baozun.nebula.model.BaseModel;

/**
 * 用于封装引擎接口会员数据的信息
 * @author qiang.yang
 * @createtime 2013-12-12 PM 13:14
 *
 */
public class EngineMemberCommand extends BaseModel{
	private static final long serialVersionUID = -8841262942329818567L;
	
	/**PK*/
	private Long id;
	/**渠道*/
	private String channeNo;
	/**年纪*/
	private Integer age;
	/**生日*/
	private Date birthday;
	/**登录邮箱*/
	private String loginEmail;
	/**分组*/
	private List<String> group;
	/**登录电话*/
	private String loginMobile;
	/**状态*/
	private String state;
	/**创建时间*/
	private Date createTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getChanneNo() {
		return channeNo;
	}
	public void setChanneNo(String channeNo) {
		this.channeNo = channeNo;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getLoginEmail() {
		return loginEmail;
	}
	public void setLoginEmail(String loginEmail) {
		this.loginEmail = loginEmail;
	}
	public List<String> getGroup() {
		return group;
	}
	public void setGroup(List<String> group) {
		this.group = group;
	}
	public String getLoginMobile() {
		return loginMobile;
	}
	public void setLoginMobile(String loginMobile) {
		this.loginMobile = loginMobile;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}

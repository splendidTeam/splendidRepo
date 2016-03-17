/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.model.system;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;

/**
 * redis 缓存项
 * 
 * @author yuelou.zhang
 */
@Entity
@Table(name = "T_SYS_CACHE_CONFIG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CacheConfig extends BaseModel implements Command{

	private static final long	serialVersionUID	= -5583054777213608558L;

	/**
	 * PK
	 */
	private BigInteger			id;

	/**
	 * 缓存项名称
	 */
	private String				name;

	/**
	 * 页面截图
	 */
	private String				img;

	/**
	 * 缓存项描述信息
	 */
	private String				desc;

	/**
	 * 对应的redis键值
	 */
	private String				key;

	/**
	 * 生命周期
	 */
	private Integer				lifecycle;

	/**
	 * 创建时间
	 */
	private Timestamp			create_time;

	/**
	 * 修改时间
	 */
	private Timestamp			modify_time;

	/**
	 * 版本
	 */
	private Timestamp			version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_CACHE_CONFIG",sequenceName = "S_T_SYS_CACHE_CONFIG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_CACHE_CONFIG")
	public BigInteger getId(){
		return id;
	}

	public void setId(BigInteger id){
		this.id = id;
	}

	@Column(name = "NAME",length = 50)
	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	@Column(name = "IMG",length = 50)
	public String getImg(){
		return img;
	}

	public void setImg(String img){
		this.img = img;
	}

	@Column(name = "DESC",length = 50)
	public String getDesc(){
		return desc;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	@Column(name = "KEY",length = 50)
	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	@Column(name = "LIFECYCLE",length = 50)
	public Integer getLifecycle(){
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	@Column(name = "CREATE_TIME",length = 50)
	public Timestamp getCreate_time(){
		return create_time;
	}

	public void setCreate_time(Timestamp create_time){
		this.create_time = create_time;
	}

	@Column(name = "MODIFY_TIME",length = 50)
	public Timestamp getModify_time(){
		return modify_time;
	}

	public void setModify_time(Timestamp modify_time){
		this.modify_time = modify_time;
	}

	@Version
	@Column(name = "VERSION")
	public Timestamp getVersion(){
		return version;
	}

	public void setVersion(Timestamp version){
		this.version = version;
	}

}

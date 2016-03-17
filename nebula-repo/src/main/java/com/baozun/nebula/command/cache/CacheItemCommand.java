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
package com.baozun.nebula.command.cache;

import com.baozun.nebula.command.Command;

/**
 * redis缓存项
 * 
 * @author yuelou.zhang
 */
public class CacheItemCommand implements Command{

	private static final long	serialVersionUID	= 7767830135774842899L;

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
	 * 缓存项key
	 */
	private String				key;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getImg(){
		return img;
	}

	public void setImg(String img){
		this.img = img;
	}

	public String getDesc(){
		return desc;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

}

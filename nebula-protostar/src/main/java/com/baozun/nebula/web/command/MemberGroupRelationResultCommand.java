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
package com.baozun.nebula.web.command;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.member.Member;

/**
 * 会员分组绑定 结果 command.
 * @author dongliang.ma
 *
 */
public class MemberGroupRelationResultCommand implements Command{

	/** The Constant serialVersionUID. */
	private static final long		serialVersionUID	= -9071701751952208064L;

	/**
	 * 关联 分类 失败的会员 <br>
	 * key 为 分类名称, value 为Member.
	 */
	private Map<String, List<Member>>	failMap;

	/**
	 * 无须重复关联 会员<br>
	 * key 为 分类名称, value 为Member.
	 */
	private Map<String, List<Member>>	repeatMap;

	/**
	 * 关联成功 会员<br>
	 * key 为 分类名称, value 为Member.
	 */
	private Map<String, List<Member>>	successMap;

	/**
	 * Gets the 关联 分类 失败的会员 <br>
	 * key 为 分类名称, value 为Member.
	 * 
	 * @return the failMap
	 */
	public Map<String, List<Member>> getFailMap(){
		return failMap;
	}

	/**
	 * Gets the 无须重复关联 会员<br>
	 * key 为 分类名称, value 为Member.
	 * 
	 * @return the repeatMap
	 */
	public Map<String, List<Member>> getRepeatMap(){
		return repeatMap;
	}

	/**
	 * Gets the 关联成功 会员<br>
	 * key 为 分类名称, value 为Member.
	 * 
	 * @return the successMap
	 */
	public Map<String, List<Member>> getSuccessMap(){
		return successMap;
	}

	/**
	 * Sets the 关联 分类 失败的会员 <br>
	 * key 为 分类名称, value 为Member.
	 * 
	 * @param failMap
	 *            the failMap to set
	 */
	public void setFailMap(Map<String, List<Member>> failMap){
		this.failMap = failMap;
	}

	/**
	 * Sets the 无须重复关联 会员<br>
	 * key 为 分类名称, value 为Member.
	 * 
	 * @param repeatMap
	 *            the repeatMap to set
	 */
	public void setRepeatMap(Map<String, List<Member>> repeatMap){
		this.repeatMap = repeatMap;
	}

	/**
	 * Sets the 关联成功 会员<br>
	 * key 为 分类名称, value 为Member.
	 * 
	 * @param successMap
	 *            the successMap to set
	 */
	public void setSuccessMap(Map<String, List<Member>> successMap){
		this.successMap = successMap;
	}

}

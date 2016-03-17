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
package com.baozun.nebula.manager.auth;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.auth.OrgType;
import com.baozun.nebula.model.auth.Organization;

/**
 * 组织机构manager
 * @author Justin
 *
 */
public interface OrganizationManager extends BaseManager {

	/**
	 * 获取所有组织机构
	 * @return
	 */
	public List<Organization> findAllOrganization();
	
	
	/**
	 * 通过parent获取组织机构
	 * @param parent
	 * @return
	 */
	public List<Organization> findByParent(Long parent);
	
	
	/**
	 * 通过name获取组织机构
	 * @param name
	 * @return
	 */
	public List<Organization> findByName(String name);
	
	/**
	 * 获取所有的组织机构类型
	 * @return
	 */
	public List<OrgType> findAllOrgType();
	/**
	 * 根据组织Id查询相关组织信息
	 * @param id
	 * @return
	 * caihong.wu
	 */
	public Organization findOrgbyId(Long id);
	
	/**
	 * 验证组织与角色是否一致
	 * @param orgTypeId
	 * @param orgs
	 * @return	:Boolean
	 * @date 2014-2-26 下午08:13:01
	 */
	public Boolean findOrganizationByOrgIdAndorgs(Long orgTypeId, String orgs);
}

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
package com.baozun.nebula.dao.auth;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.auth.Organization;

/**
 * 组织机构dao
 * @author Justin
 *
 */
public interface OrganizationDao extends GenericEntityDao<Organization, Long> {

	
	/**
	 * 获取全部组织机构
	 * @return
	 * 
	 */
	@NativeQuery(model=Organization.class)
	List<Organization> findAllList();
	
	/**
	 * 通过parent获取组织机构
	 * @param parent
	 * @return
	 */
	@NativeQuery(model = Organization.class)
	List<Organization> findByParent(@QueryParam("parent")Long parent);
	
	/**
	 * 通过机构名称模糊查询组织机构
	 * @param parent
	 * @return
	 */
	@NativeQuery(model = Organization.class)
	List<Organization> findByName(@QueryParam("name")String name);
	
	
	/**
	 * 通过机构类型获取组织机构
	 * @param orgaTypeId 组织机构id 
	 * @return
	 */
	@NativeQuery(model = Organization.class)
	List<Organization> findByOrgaTypeId(@QueryParam("orgaTypeId")Long orgaTypeId);
	
	
	
	
	
	/**
	 * 通过用户id获取组织机构(机构与用户关联后再筛选查询)
	 * @param userId 用户id
	 * @return
	 */
	@NativeQuery(model = Organization.class)
	Organization findByUserId(@QueryParam("userId")Long userId);
	/**
	 * 根据组织Id查询信息
	 * @param id
	 * @return
	 */
	@NativeQuery(model=Organization.class)
	Organization findOrgbyId(@QueryParam("id")Long id);
	/**
	 * 查询是否有相同的组织名称
	 * @param name
	 * @return
	 */
	@NativeQuery(alias = "CNT",clazzes = Integer.class)
	Integer validateShopCode(@QueryParam("code")String code);
	
	/**
	 * 验证组织与角色是否一致
	 * @param orgTypeId
	 * @param orgList
	 * @return	:List<Organization>
	 * @date 2014-2-26 下午08:18:36
	 */
	@NativeQuery(model=Organization.class)
	List<Organization> findOrganizationByOrgIdAndorgs(@QueryParam("orgTypeId") Long orgTypeId, @QueryParam("orgList") List orgList);
	
}

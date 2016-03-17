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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.auth.OrgTypeDao;
import com.baozun.nebula.dao.auth.OrganizationDao;
import com.baozun.nebula.model.auth.OrgType;
import com.baozun.nebula.model.auth.Organization;

/**
 * @author Justin
 *
 */
@Transactional
@Service("organizationManager")
public class OrganizationManagerImpl implements
		OrganizationManager {

	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private OrgTypeDao orgTypeDao;
	/* (non-Javadoc)
	 * @see com.jumbo.nebula.manager.authority.OrganizationManager#findAllOrganization()
	 */
	@Override
	public List<Organization> findAllOrganization() {
		// TODO Auto-generated method stub
		try{
			return organizationDao.findAllList();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.jumbo.nebula.manager.authority.OrganizationManager#findByParent(java.lang.Long)
	 */
	@Override
	public List<Organization> findByParent(Long parent) {
		// TODO Auto-generated method stub
		try{
			return organizationDao.findByParent(parent);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.jumbo.nebula.manager.authority.OrganizationManager#findByName(java.lang.String)
	 */
	@Override
	public List<Organization> findByName(String name) {
		// TODO Auto-generated method stub
		try{
			if(StringUtils.isNotBlank(name)){
				name="%"+name+"%";
			}
			return organizationDao.findByName(name);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.jumbo.nebula.manager.authority.OrganizationManager#findAllOrgType()
	 */
	@Override
	public List<OrgType> findAllOrgType() {
		// TODO Auto-generated method stub
		return orgTypeDao.findAllList();
	}

	@Override
	public Organization findOrgbyId(Long id){
		// TODO Auto-generated method stub
		return organizationDao.findOrgbyId(id);
	}

	@Override
	public Boolean findOrganizationByOrgIdAndorgs(Long orgTypeId,String orgs){
		// TODO Auto-generated method stub
		String[] orgas = orgs.split(",");
		List<Long> orgList = new ArrayList<Long>();
		for(String org : orgas){
			orgList.add(Long.valueOf(org));
		}
		List<Organization> organizationList = organizationDao.findOrganizationByOrgIdAndorgs(orgTypeId, orgList);
		if(organizationList != null && organizationList.size() >0){
			return false;
		}else{
			return true;
		}
	}
}

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
 */
package com.baozun.nebula.manager.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.product.IndustryDao;
import com.baozun.nebula.model.product.Industry;

/**
 * @author kefan.chen
 */
@Transactional
@Service("industryManager")
public class IndustryManagerImpl implements IndustryManager{

	@SuppressWarnings("unused")
	private static final Logger	logger	= LoggerFactory.getLogger(IndustryManagerImpl.class);

	@Autowired
	private IndustryDao industryDao;

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.IndustryManager#findIndustryList()
	 */
	@Override
	public List<Industry> findAllIndustryList(){
		return industryDao.findAllIndustryList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.IndustryManager#addIndustry(java.lang .Long, java.lang.Integer, java.lang.String)
	 */
	@Override
	public boolean createOrUpdateIndustry(Industry industry,String strIds){
		industry = industryDao.save(industry);
		if(!"-1".equals(strIds)){
			String[] list = strIds.split(",");
			List<Long> ids = new ArrayList<Long>();
			if (list.length > 0) {
				for (int i = 0; i < list.length; i++) {
					Long propertyId = Long.valueOf(list[i].toString());
					ids.add(propertyId);
				}
			}
			industryDao.enableOrDisableIndustryByIds(ids,0);
		}
		return industry.getId()>0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.IndustryManager#removeIndustry(java.lang .Long)
	 */
	@Override
	public Integer removeIndustryByIds(List<Long> ids){
		Integer res = industryDao.removeIndustryByIds(ids);
		return res;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.IndustryManager#findIndustryById(java.lang.Long)
	 */
	@Override
	public Industry findIndustryById(Long id) {
		return industryDao.findIndustryById(id);
	}
	
	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.IndustryManager#findIndustryById(java.lang.Long)
	 */
	@Override
	public  boolean validateIndustryName(Long pId,String name){
		Integer result = industryDao.findIndustryCount(name, pId);
		return result == 0;
	}

    @Override
    public List<Industry> findIndustryByCategoryId(Long categoryId) {
        
        return industryDao.findIndustryByCategoryId(categoryId);
    }		
}

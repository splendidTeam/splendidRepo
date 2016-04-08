/**
 * 
 */
package com.baozun.nebula.sdk.manager.product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.product.PropertyValueGroupDao;
import com.baozun.nebula.model.product.PropertyValueGroup;

/**
 * @author Viktor Huang
 * @date Apr 7, 2016 5:17:30 PM
 */
@Service("sdkPropertyManager")
@Transactional
public class SdkPropertyManagerImpl implements SdkPropertyManager{

	@SuppressWarnings("unused")
	private static final Logger		LOGGER	= LoggerFactory.getLogger(SdkPropertyManagerImpl.class);

	@Autowired
	private PropertyValueGroupDao	propertyValueGroupDao;
	
	
	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.product.SdkPropertyManager#findProValueGroupByPropertyId(java.lang.Long)
	 */
	@Override
	public List<PropertyValueGroup> findProValueGroupByPropertyId(Long propertyId){

		return propertyValueGroupDao.findPropertyValueGroupByPropertyId(propertyId);
	}
}

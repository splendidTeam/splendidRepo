/**
 * 
 */
package com.baozun.nebula.sdk.manager.product;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.product.PropertyValueGroupDao;
import com.baozun.nebula.dao.product.PropertyValueGroupRelationDao;
import com.baozun.nebula.model.product.PropertyValueGroup;
import com.baozun.nebula.model.product.PropertyValueGroupRelation;
import com.feilong.core.Validator;

/**
 * @author Viktor Huang
 * @date Apr 7, 2016 5:17:30 PM
 */
@Service("sdkPropertyManager")
@Transactional
public class SdkPropertyManagerImpl implements SdkPropertyManager{

	@SuppressWarnings("unused")
	private static final Logger				LOGGER	= LoggerFactory.getLogger(SdkPropertyManagerImpl.class);

	@Autowired
	private PropertyValueGroupDao			propertyValueGroupDao;

	@Autowired
	private PropertyValueGroupRelationDao	propertyValueGroupRelationDao;

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.product.SdkPropertyManager#findProValueGroupByPropertyId(java.lang.Long)
	 */
	@Override
	public List<PropertyValueGroup> findProValueGroupByPropertyId(Long propertyId){

		return propertyValueGroupDao.findPropertyValueGroupByPropertyId(propertyId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.product.SdkPropertyManager#savePropertyValueGroup(java.lang.Long,java.lang.Long, java.lang.String)
	 */
	@Override
	public PropertyValueGroup savePropertyValueGroup(Long propertyId,Long groupId,String groupName){
		PropertyValueGroup group = new PropertyValueGroup();

		if (Validator.isNullOrEmpty(groupId)){
			group.setId(groupId);
			group.setVersion(new Date());
		}else{
			group.setCreateTime(new Date());
			group.setVersion(new Date());
		}

		group.setName(groupName);
		group.setPropertyId(propertyId);
		group = propertyValueGroupDao.save(group);

		return group;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.product.SdkPropertyManager#bindPropertyValueAndProValueGroup(java.util.List, java.lang.Long)
	 */
	@Override
	public boolean bindPropertyValueAndProValueGroup(List<Long> proValueIds,Long propertyValueGroupId){

		propertyValueGroupRelationDao.deleteRelationByProValueGroupId(propertyValueGroupId);

		for (Long proValueId : proValueIds){
			
			PropertyValueGroupRelation relation = new PropertyValueGroupRelation();
			relation.setProValGroupId(propertyValueGroupId);
			relation.setProValueId(proValueId);
			
			propertyValueGroupRelationDao.save(relation);
		}
		
		return true;
	}
}

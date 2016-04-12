/**
 * 
 */
package com.baozun.nebula.sdk.manager.product;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.product.PropertyValueCommand;
import com.baozun.nebula.dao.product.PropertyDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.dao.product.PropertyValueGroupDao;
import com.baozun.nebula.dao.product.PropertyValueGroupRelationDao;
import com.baozun.nebula.dao.product.PropertyValueLangDao;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.PropertyValueGroup;
import com.baozun.nebula.model.product.PropertyValueGroupRelation;
import com.baozun.nebula.model.product.PropertyValueLang;
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
	private PropertyDao						propertyDao;

	@Autowired
	private PropertyValueDao				propertyValueDao;

	@Autowired
	private PropertyValueGroupDao			propertyValueGroupDao;

	@Autowired
	private PropertyValueGroupRelationDao	propertyValueGroupRelationDao;

	@Autowired
	private PropertyValueLangDao			propertyValueLangDao;

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
		PropertyValueGroup group = null;

		if (Validator.isNotNullOrEmpty(groupId)){
			group = this.findProValueGroupById(groupId);
			group.setVersion(new Date());
		}else{
			group = new PropertyValueGroup();
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

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.product.SdkPropertyManager#findBoundGroupPropertyValue(java.lang.Long)
	 */
	@Override
	public List<PropertyValue> findBoundGroupPropertyValue(Long proValGroupId){

		return propertyValueDao.findBoundGroupPropertyValue(proValGroupId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.product.SdkPropertyManager#findFreeGroupPropertyValue(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<PropertyValue> findFreeGroupPropertyValue(Long propertyId,Long proValGroupId){
		return propertyValueDao.findFreeGroupPropertyValue(propertyId, proValGroupId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.product.SdkPropertyManager#findProValueGroupById(java.lang.Long)
	 */
	@Override
	public PropertyValueGroup findProValueGroupById(Long groupId){
		return propertyValueGroupDao.getByPrimaryKey(groupId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.product.SdkPropertyManager#findPropertyValueByPage(loxia.dao.Page, loxia.dao.Sort[],
	 * java.lang.Long, java.lang.Long)
	 */
	@Override
	public Pagination<PropertyValueCommand> findPropertyValueByPage(Page page,Sort[] sorts,Long propertyId,Long proValueGroupId){

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.product.SdkPropertyManager#addOrUpdatePropertyValue(java.lang.Long,
	 * com.baozun.nebula.command.product.PropertyValueCommand)
	 */
	@Override
	public void addOrUpdatePropertyValue(Long propertyId,PropertyValueCommand propertyValues){

		Property property = propertyDao.getByPrimaryKey(propertyId);
		if (Validator.isNullOrEmpty(property)){
			return;
		}

		// 取到要更新的属性值id
		Long propertyValueId = propertyValues.getId();
		// 属性值名称的国际化
		MutlLang valueLang = (MutlLang) propertyValues.getValue();

		Integer sortNo = propertyValues.getSortNo();
		String thumb = propertyValues.getThumb();

		String[] values = valueLang.getValues();
		String[] langs = valueLang.getLangs();

		if (Validator.isNotNullOrEmpty(propertyValueId)){
			/**
			 * 如果传过来的PropertyValueCommand中id不为空，则作更新操作
			 */
			// 更新PropertyValue主体
			propertyValueDao.updateById(propertyValueId, valueLang.getDefaultValue(), thumb, sortNo);

			// 更新PropertyValue name的国际化
			for (int j = 0; j < values.length; j++){
				String val = values[j];
				String lang = langs[j];
				// 修改
				PropertyValueLang pvl1 = propertyValueDao.findPropertyValueLang(propertyValueId, lang);
				if (pvl1 != null){
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("value", val);
					params.put("propertyValueId", propertyValueId);
					params.put("lang", lang);
					propertyValueDao.updatePropertyValueLang(params);
				}else{
					PropertyValueLang pvl = new PropertyValueLang();
					pvl.setPropertyValueId(propertyValueId);
					pvl.setLang(lang);
					pvl.setValue(val);
					propertyValueLangDao.save(pvl);
				}
			}
		}else{
			/**
			 * 新建属性值
			 */
			Calendar calendar = Calendar.getInstance();
			Date date = calendar.getTime();
			
			PropertyValue pv = new PropertyValue();
			pv.setValue(valueLang.getDefaultValue());
			pv.setModifyTime(date);
			pv.setCreateTime(date);
			pv.setPropertyId(propertyId);
			pv = propertyValueDao.save(pv);

			Long pvid = pv.getId();
			for (int j = 0; j < values.length; j++){
				String val = values[j];
				String lang = langs[j];
				PropertyValueLang pvl = new PropertyValueLang();
				pvl.setPropertyValueId(pvid);
				pvl.setLang(lang);
				pvl.setValue(val);
				propertyValueLangDao.save(pvl);
			}
		}
	}
}

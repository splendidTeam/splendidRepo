/**
 * 
 */
package com.baozun.nebula.sdk.manager.product;

import java.util.ArrayList;
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

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;
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

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.product.SdkPropertyManager#findPropertyById(java.lang.Long)
	 */
	@Override
	public Property findPropertyById(Long id){
		return propertyDao.getByPrimaryKey(id);
	}
	
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
	public Pagination<PropertyValueCommand> findPropertyValueByPage(Page page,Sort[] sorts,Long propertyId,Long proValGroupId){

		Pagination<PropertyValue> propertyValues = propertyValueDao.findPropertyValueWithPage(page, sorts, propertyId, proValGroupId);

		// List<PropertyValue> pvs = propertyValueDao.findPropertyValueListById(propertyId);

		if (Validator.isNullOrEmpty(propertyValues)){
			return null;
		}
		List<PropertyValue> proValues = propertyValues.getItems();

		List<PropertyValueCommand> proValCommands = new ArrayList<PropertyValueCommand>();
		List<Long> pvIds = new ArrayList<Long>();

		// 将PropertyValue转换为PropertyValueCommand
		for (PropertyValue propertyValue : proValues){
			PropertyValueCommand pvc = new PropertyValueCommand();
			LangProperty.I18nPropertyCopyToSource(propertyValue, pvc);

			Long pvId = propertyValue.getId();
			pvIds.add(pvId);

			proValCommands.add(pvc);
		}

		/**
		 * 查询属性值对应的国际化
		 */
		List<PropertyValueLang> propertyLangs = propertyValueDao.findPropertyValueLangByPvids(pvIds, MutlLang.i18nLangs());
		
		if (Validator.isNullOrEmpty(propertyLangs)){

//			return proValCommands;

		}
		//Map[key：属性值id]
		Map<Long, List<PropertyValueLang>> map = new HashMap<Long, List<PropertyValueLang>>();
		for (PropertyValueLang propertyLang : propertyLangs){
			Long pid = propertyLang.getPropertyValueId();
			if (map.containsKey(pid)){
				map.get(pid).add(propertyLang);
			}else{
				List<PropertyValueLang> pls = new ArrayList<PropertyValueLang>();
				pls.add(propertyLang);
				map.put(pid, pls);
			}
		}
		
		for (int i = 0; i < proValCommands.size(); i++){
			PropertyValueCommand pvc = proValCommands.get(i);
			
			Long pvId = pvc.getId();
			List<PropertyValueLang> pls = map.get(pvId);
			if (Validator.isNullOrEmpty(pls)){
				continue;
			}
			// 名称
			String[] values = new String[MutlLang.i18nSize()];
			String[] langs = new String[MutlLang.i18nSize()];
			for (int j = 0; j < pls.size(); j++){
				PropertyValueLang propertyLang = pls.get(j);
				values[j] = propertyLang.getValue();
				langs[j] = propertyLang.getLang();
			}
			MutlLang mutlLang = new MutlLang();
			mutlLang.setValues(values);
			mutlLang.setLangs(langs);
			pvc.setValue(mutlLang);
		}
		
		Pagination<PropertyValueCommand>  result = new Pagination<PropertyValueCommand>(proValCommands, propertyValues.getCount());
		result.setCurrentPage(propertyValues.getCurrentPage());
		result.setSize(propertyValues.getSize());
		result.setSortStr(propertyValues.getSortStr());
		result.setTotalPages(propertyValues.getTotalPages());
		result.setStart(propertyValues.getStart());
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.product.SdkPropertyManager#addOrUpdatePropertyValue(java.lang.Long,
	 * com.baozun.nebula.command.product.PropertyValueCommand)
	 */
	@Override
	public void addOrUpdatePropertyValue(Long groupId,PropertyValueCommand propertyValues){

		Long propertyId = propertyValues.getPropertyId();
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
			Integer no = propertyValueDao.findMaxSortNoByPropertyId(propertyId);

			sortNo = Validator.isNotNullOrEmpty(no) ? no + 1 : 1;

			PropertyValue pv = new PropertyValue();
			pv.setValue(valueLang.getDefaultValue());
			pv.setSortNo(sortNo);
			pv.setModifyTime(date);
			pv.setCreateTime(date);
			pv.setPropertyId(propertyId);
			pv = propertyValueDao.save(pv);

			propertyValueId = pv.getId();
			for (int j = 0; j < values.length; j++){
				String val = values[j];
				String lang = langs[j];

				PropertyValueLang pvl = new PropertyValueLang();
				pvl.setPropertyValueId(propertyValueId);
				pvl.setLang(lang);
				pvl.setValue(val);
				propertyValueLangDao.save(pvl);
			}
		}

		if (Validator.isNotNullOrEmpty(groupId)){
			PropertyValueGroupRelation relation = new PropertyValueGroupRelation();
			relation.setProValueId(propertyValueId);
			relation.setProValGroupId(groupId);
			propertyValueGroupRelationDao.save(relation);
		}

	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.product.SdkPropertyManager#updatePropertyValueSortById(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public Integer updatePropertyValueSortById(Long id,Integer sortNo){
		
		return propertyValueDao.updatePropertyValueSortById(id, sortNo);
	}


}

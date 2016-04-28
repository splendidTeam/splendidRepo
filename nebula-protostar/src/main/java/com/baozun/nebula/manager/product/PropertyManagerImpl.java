package com.baozun.nebula.manager.product;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.PropertyCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;
import com.baozun.nebula.command.product.CommonPropertyCommand;
import com.baozun.nebula.command.product.PropertyValueCommand;
import com.baozun.nebula.dao.product.CommonPropertyDao;
import com.baozun.nebula.dao.product.IndustryDao;
import com.baozun.nebula.dao.product.PropertyDao;
import com.baozun.nebula.dao.product.PropertyLangDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.dao.product.PropertyValueLangDao;
import com.baozun.nebula.model.product.CommonProperty;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyLang;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.PropertyValueLang;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.web.command.DynamicPropertyCommand;
import com.baozun.nebula.web.command.PropertyValueUploadCommand;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * 属性管理
 * 
 * @author lin.liu
 * @date 2013-7-2
 */
@Transactional
@Service("propertyManager")
public class PropertyManagerImpl implements PropertyManager{

	@SuppressWarnings("unused")
	private static final Logger		log	= LoggerFactory.getLogger(PropertyManagerImpl.class);

	@Autowired
	private PropertyDao				propertyDao;

	@Autowired
	private PropertyValueDao		propertyValueDao;

	@Autowired
	private PropertyLangDao			propertyLangDao;

	@Autowired
	private PropertyValueLangDao	propertyValueLangDao;

	@Autowired
	private CommonPropertyDao		commonPropertyDao;

	@Autowired
	private IndustryDao				industryDao;

	@Override
	public Pagination<PropertyCommand> findPropertyListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
		Pagination<PropertyCommand> result = propertyDao.findPropertyListByQueryMapWithPage(page, sorts, paraMap);
		return result;
	}
	@Override
	public Pagination<PropertyCommand> findPropertyPaginationByQueryMap(Page page,Sort[] sorts,Map<String, Object> paraMap){
		Pagination<PropertyCommand> result = propertyDao.findPropertyPaginationByQueryMap(page, sorts, paraMap);
		return result;
	}

	@Override
	public Integer enableOrDisablePropertyByIds(List<Long> ids,Integer state){
		Integer result = propertyDao.enableOrDisablePropertyByIds(ids, state);
		return result;
	}

	@Override
	public Integer removePropertyByIds(List<Long> ids){
		Integer result = propertyDao.removePropertyByIds(ids);
		return result;
	}

	@Override
	public Property findPropertyById(Long id){
		return propertyDao.findPropertyById(id);
	}
	
	@Override
	public Property findPropertiesById(Long id){
		return propertyDao.findPropertyById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.PropertyManager# findPropertyListByIndustryId(java.lang.Long)
	 */
	@Override
	public List<Property> findPropertyListByIndustryId(Long industryId){

//		return propertyDao.findPropertyListByIndustryId(industryId);
		
		return propertyDao.findPropertysByIndustryId(industryId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.PropertyManager# findPropertyListByIndustryId(java.lang.Long)
	 */
	@Override
	public boolean createOrUpdateProperty(Property property){
		property = propertyDao.save(property);
		return property.getId() > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.PropertyManager#updateSortById(java .lang.Long,java.lang.Integer)
	 */
	@Override
	public boolean updatePropertyByParamList(String ids){
		String[] list = ids.split(",");
		String value;
		int end;
		Long propertyId;
		Integer sortNo;
		boolean flag = true;
		for (int i = 0; i < list.length; i++){
			value = list[i];
			System.out.println(value);
			end = value.indexOf("sortNo");
			propertyId = Long.parseLong((value.substring(10, end)));
			sortNo = Integer.parseInt(value.substring(end + 6));
			Integer result = propertyDao.updateSortById(propertyId, sortNo);
			if (result < 1){
				flag = false;
			}
		}
		return flag;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.PropertyManager#findPropertyValueList (java.lang.Long)
	 */
	@Override
	public List<PropertyValue> findPropertyValueList(Long propertyId){
		return propertyValueDao.findPropertyValueListById(propertyId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.PropertyManager# createOrUpdatePropertyValueByList
	 * (com.baozun.nebula.model.product.PropertyValue[])
	 */
	@Override
	public void createOrUpdatePropertyValueByList(PropertyValue[] propertyValues,Long propertyId){

		List<PropertyValue> propertyValue = propertyValueDao.findPropertyValueListById(propertyId);
		List<Long> ids = new ArrayList<Long>();// 数据库中的ID
		for (PropertyValue pv : propertyValue){
			Long id = pv.getId();
			ids.add(id);
		}

		if (propertyValues != null){
			List<Long> idnew = new ArrayList<Long>();// 存放页面修改后的ID
			for (PropertyValue pv : propertyValues){
				if (StringUtils.isNotBlank(pv.getValue())){
					Long id = pv.getId();
					String value = pv.getValue();
					// String showValue = pv.getShowValue();
					String thumb = pv.getThumb();
					Integer sortNo = pv.getSortNo();

					if (null != id){
						idnew.add(id);
						propertyValueDao.updateById(id, value, thumb, sortNo);

					}else if (null != value){
						Calendar calendar = Calendar.getInstance();
						Date date = calendar.getTime();
						pv.setModifyTime(date);
						pv.setCreateTime(date);
						pv.setPropertyId(propertyId);
						propertyValueDao.save(pv);
					}
				}
			}
			ids.removeAll(idnew);
		}

		propertyValueDao.deleteById(ids);

	}

	@Override
	public List<Property> selectPropertyByCategoryIdAndIndustryId(Long categoryId,Long industryId){

		return propertyDao.findPropertyByCategoryIdAndIndustryId(categoryId, industryId);
	}

	@Override
	public List<Property> findAllPropertys(){
		return propertyDao.findAllPropertyList();
	}

	@Override
	public com.baozun.nebula.command.product.PropertyCommand createOrUpdateProperty(
			com.baozun.nebula.command.product.PropertyCommand propertyCommand) {
		Property property = null;
		if (propertyCommand.getId() != null && propertyCommand.getId() != -1) {
			property = findPropertyById(propertyCommand.getId());
			property.setModifyTime(new Date());
		}else{
			property = new Property();
			property.setCreateTime(new Date());
			// TODO 这是干吗的？
			property.setIsCommonIndustry(true);
		}
		property.setHasThumb(propertyCommand.getHasThumb());
		property.setIndustryId(propertyCommand.getIndustryId());
		property.setRequired(propertyCommand.getRequired());
		property.setSearchable(propertyCommand.getSearchable());
		property.setSortNo(propertyCommand.getSortNo());
		property.setLifecycle(propertyCommand.getLifecycle());

		boolean i18n = LangProperty.getI18nOnOff();
		// 1.保存CommentProperty
		CommonProperty commonproperty = createOrUpdateCommentProperty(propertyCommand, property);

		if (property.getCommonPropertyId() == null) {
			property.setCommonPropertyId(commonproperty.getId());
		}

		if (i18n){
			MutlLang pname = (MutlLang) propertyCommand.getName();
			String[] values = pname.getValues();
			String[] langs = pname.getLangs();
			MutlLang gname = (MutlLang) propertyCommand.getGroupName();
			String[] gnames = gname.getValues();
			String name = pname.getDefaultValue();
			String gpName = gname.getDefaultValue();
			property.setName(name);
			property.setGroupName(gpName);
			property = propertyDao.save(property);
			Long propertyId = property.getId();
			for (int i = 0; i < values.length; i++){
				String val = values[i];
				String lang = langs[i];
				String groupName = gnames[i];
				// 新增
				PropertyLang pl = propertyDao.findPropertyLang(propertyId, lang);
				if (pl == null){
					PropertyLang propertyLang = new PropertyLang();
					propertyLang.setPropertyId(propertyId);
					propertyLang.setLang(lang);
					propertyLang.setName(val);
					propertyLang.setGroupName(groupName);
					propertyLangDao.save(propertyLang);
				}else{
					// 修改
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("name", val);
					params.put("propertyId", propertyId);
					params.put("lang", lang);
					params.put("groupName", groupName);
					propertyDao.updatePropertyLang(params);
				}
			}
		}else{
			SingleLang singleLang = (SingleLang) propertyCommand.getName();
			property.setName(singleLang.getValue());

			SingleLang gname = (SingleLang) propertyCommand.getGroupName();
			property.setGroupName(gname.getValue());

			property = propertyDao.save(property);
		}

		LangProperty.I18nPropertyCopyToSource(property, propertyCommand);
		return propertyCommand;

	}

	/**
	 * 新增或更新commentProperty信息
	 * @param propertyCommand
	 * @param i18n
	 * @param newProperty
	 * @param newProperty
	 */
	private CommonProperty createOrUpdateCommentProperty(
			com.baozun.nebula.command.product.PropertyCommand propertyCommand, Property property) {

		Long commonpropertyId = null;
		if (property.getId() != null) {
			commonpropertyId = property.getCommonPropertyId();
		} else if (propertyCommand.getCommonPropertyId() != null) {
			commonpropertyId = propertyCommand.getCommonPropertyId();
		}

		CommonProperty commonproperty = null;
		if (commonpropertyId != null) {
			commonproperty = commonPropertyDao.getByPrimaryKey(commonpropertyId);
			commonproperty.setModifyTime(new Date());
		} else {
			commonproperty = new CommonProperty();
			commonproperty.setCreateTime(new Date());
		}

		commonproperty.setName(propertyCommand.getCommonName());
		commonproperty.setEditingType(propertyCommand.getEditingType());
		commonproperty.setIsColorProp(propertyCommand.getIsColorProp());
		commonproperty.setIsSaleProp(propertyCommand.getIsSaleProp());
		commonproperty.setValueType(propertyCommand.getValueType());
		return commonPropertyDao.save(commonproperty);
	}

	@Override
	public void createOrUpdatePropertyValueByList(PropertyValueCommand[] propertyValueCommands,Long propertyId){
		List<PropertyValue> propertyValue = propertyValueDao.findPropertyValueListById(propertyId);
		Property property = propertyDao.getByPrimaryKey(propertyId);
		List<Long> ids = new ArrayList<Long>();// 数据库中的ID
		for (PropertyValue pv : propertyValue){
			Long id = pv.getId();
			ids.add(id);
		}
		List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
		for (PropertyValueCommand command : propertyValueCommands){
			PropertyValue pv = new PropertyValue();
			pv.setId(command.getId());
			pv.setCreateTime(command.getCreateTime());
			pv.setModifyTime(command.getModifyTime());
			pv.setPropertyId(command.getPropertyId());
			pv.setSortNo(command.getSortNo());
			pv.setThumb(command.getThumb());
			pv.setVersion(command.getVersion());
			pv.setCommonPropertyId(property.getCommonPropertyId());
			propertyValues.add(pv);
		}
		if (propertyValues != null){
			// 存放页面修改后的ID
			List<Long> idnew = new ArrayList<Long>();
			boolean i18n = LangProperty.getI18nOnOff();
			if (i18n){
				for (int i = 0; i < propertyValues.size(); i++){
					PropertyValue pv = propertyValues.get(i);
					PropertyValueCommand pvc = propertyValueCommands[i];
					MutlLang valueLang = (MutlLang) pvc.getValue();
					String[] values = valueLang.getValues();
					String value = valueLang.getDefaultValue();
					String[] langs = valueLang.getLangs();
					Long id = pv.getId();
					String thumb = pv.getThumb();
					Integer sortNo = pv.getSortNo();
					if (null != id){
						idnew.add(id);
						propertyValueDao.updateById(id, value, thumb, sortNo);
						for (int j = 0; j < values.length; j++){
							String val = values[j];
							String lang = langs[j];
							// 修改
							PropertyValueLang pvl1 = propertyValueDao.findPropertyValueLang(id, lang);
							if (pvl1 != null){
								Map<String, Object> params = new HashMap<String, Object>();
								params.put("value", val);
								params.put("propertyValueId", id);
								params.put("lang", lang);
								propertyValueDao.updatePropertyValueLang(params);
							}else{
								PropertyValueLang pvl = new PropertyValueLang();
								pvl.setPropertyValueId(id);
								pvl.setLang(lang);
								pvl.setValue(val);
								propertyValueLangDao.save(pvl);
							}
						}
					}else if (null != value){
						Calendar calendar = Calendar.getInstance();
						Date date = calendar.getTime();
						pv.setValue(value);
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
			}else{
				for (int i = 0; i < propertyValues.size(); i++){
					PropertyValue pv = propertyValues.get(i);
					PropertyValueCommand pvc = propertyValueCommands[i];
					// 取出国际化value
					SingleLang valueLang = (SingleLang) pvc.getValue();
					String value = valueLang.getValue();
					if (StringUtils.isNotBlank(value)){
						Long id = pv.getId();
						String thumb = pv.getThumb();
						Integer sortNo = pv.getSortNo();
						if (null != id){
							idnew.add(id);
							propertyValueDao.updateById(id, value, thumb, sortNo);
						}else if (null != value){
							Calendar calendar = Calendar.getInstance();
							Date date = calendar.getTime();
							pv.setValue(value);
							pv.setModifyTime(date);
							pv.setCreateTime(date);
							pv.setPropertyId(propertyId);
							pv = propertyValueDao.save(pv);
						}
					}
				}
			}
			ids.removeAll(idnew);
			propertyValueDao.deleteById(ids);
		}
	}

	@Override
	public List<com.baozun.nebula.command.product.PropertyCommand> findPropertyCommandListByIndustryId(Long industryId){
		List<Property> properties = propertyDao.findPropertyListByIndustryId(industryId);
		if (properties != null){
			List<com.baozun.nebula.command.product.PropertyCommand> propertyCommands = new ArrayList<com.baozun.nebula.command.product.PropertyCommand>();
			List<Long> pIds = new ArrayList<Long>();
			for (Property property : properties){
				com.baozun.nebula.command.product.PropertyCommand propertyCommand = new com.baozun.nebula.command.product.PropertyCommand();
				LangProperty.I18nPropertyCopyToSource(property, propertyCommand);
				findindustrylistbypropertyId(propertyCommand);
				findCommonPropertyName(propertyCommand);
				propertyCommands.add(propertyCommand);
				pIds.add(property.getId());
			}
			boolean i18n = LangProperty.getI18nOnOff();
			if (i18n){
				List<PropertyLang> propertyLangs = propertyDao.findPropertyLangByPids(pIds, MutlLang.i18nLangs());
				if (Validator.isNullOrEmpty(propertyLangs)){
					return propertyCommands;
				}
				Map<Long, List<PropertyLang>> map = new HashMap<Long, List<PropertyLang>>();
				for (PropertyLang propertyLang : propertyLangs){
					Long pid = propertyLang.getPropertyId();
					if (map.containsKey(pid)){
						map.get(pid).add(propertyLang);
					}else{
						List<PropertyLang> pls = new ArrayList<PropertyLang>();
						pls.add(propertyLang);
						map.put(pid, pls);
					}
				}
				for (int i = 0; i < propertyCommands.size(); i++){
					com.baozun.nebula.command.product.PropertyCommand propertyCommand = propertyCommands.get(i);
					Long pid = propertyCommand.getId();
					List<PropertyLang> pls = map.get(pid);
					if (Validator.isNullOrEmpty(pls)){
						continue;
					}
					// 名称
					String[] values = new String[MutlLang.i18nSize()];
					String[] langs = new String[MutlLang.i18nSize()];
					for (int j = 0; j < pls.size(); j++){
						PropertyLang propertyLang = pls.get(j);
						values[j] = propertyLang.getName();
						langs[j] = propertyLang.getLang();
					}
					MutlLang mutlLang = new MutlLang();
					mutlLang.setValues(values);
					mutlLang.setLangs(langs);
					// 分组名称
					values = new String[pls.size()];
					langs = new String[pls.size()];
					for (int j = 0; j < pls.size(); j++){
						PropertyLang propertyLang = pls.get(j);
						values[j] = propertyLang.getGroupName();
						langs[j] = propertyLang.getLang();
					}
					MutlLang mutlLang1 = new MutlLang();
					mutlLang1.setValues(values);
					mutlLang1.setLangs(langs);
					propertyCommand.setName(mutlLang);
					propertyCommand.setGroupName(mutlLang1);
				}
			}else{
				for (int i = 0; i < propertyCommands.size(); i++){
					Property property = properties.get(i);
					com.baozun.nebula.command.product.PropertyCommand propertyCommand = propertyCommands.get(i);
					// 名称
					SingleLang singleLang = new SingleLang();
					singleLang.setValue(property.getName());
					propertyCommand.setName(singleLang);
					// 分组名称
					SingleLang singleLang1 = new SingleLang();
					singleLang1.setValue(property.getGroupName());
					propertyCommand.setGroupName(singleLang1);
				}
			}
			return propertyCommands;
		}else{
			return null;
		}

	}

	/**
	 * 获取通用属性名称
	 * 
	 * @param propertyCommand
	 * @param commonPropertyId
	 */
	private void findCommonPropertyName(com.baozun.nebula.command.product.PropertyCommand propertyCommand){
		CommonProperty commonProperty = commonPropertyDao.getByPrimaryKey(propertyCommand.getCommonPropertyId());
//		boolean i18n = LangProperty.getI18nOnOff();
//		if (i18n){
//
//		}else{
//			SingleLang singleLang = new SingleLang();
//			singleLang.setValue(commonProperty.getName());
//			propertyCommand.setCommonName(singleLang);
//		}
		propertyCommand.setCommonName(commonProperty.getName());
	}

	/**
	 * 查询使用了该属性的行业集合
	 * 
	 * @param propertyCommand
	 * @return
	 */
	private void findindustrylistbypropertyId(com.baozun.nebula.command.product.PropertyCommand propertyCommand){
		Long commonPropertyId = propertyCommand.getCommonPropertyId();
		List<Industry> industryList = industryDao.findIndustryListByCommonPropertyId(commonPropertyId);
		propertyCommand.setIndustrylist(industryList);
	}

	@Override
	public List<PropertyValueCommand> findPropertyValuecCommandList(Long propertyId){
		List<PropertyValue> pvs = propertyValueDao.findPropertyValueListById(propertyId);
		if (Validator.isNullOrEmpty(pvs)){
			return null;
		}
		List<PropertyValueCommand> pvcs = new ArrayList<PropertyValueCommand>();
		List<Long> pvIds = new ArrayList<Long>();
		for (PropertyValue propertyValue : pvs){
			PropertyValueCommand pvc = new PropertyValueCommand();
			LangProperty.I18nPropertyCopyToSource(propertyValue, pvc);
			Long pvId = propertyValue.getId();
			pvIds.add(pvId);
			pvcs.add(pvc);
		}
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){

			List<PropertyValueLang> propertyLangs = propertyValueDao.findPropertyValueLangByPvids(pvIds, MutlLang.i18nLangs());
			if (Validator.isNullOrEmpty(propertyLangs)){
				return pvcs;
			}
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
			for (int i = 0; i < pvcs.size(); i++){
				PropertyValueCommand pvc = pvcs.get(i);
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
		}else{
			for (int i = 0; i < pvcs.size(); i++){
				PropertyValue property = pvs.get(i);
				PropertyValueCommand pvc = pvcs.get(i);
				// 名称
				SingleLang singleLang = new SingleLang();
				singleLang.setValue(property.getValue());
				pvc.setValue(singleLang);
			}
		}
		return pvcs;
	}

	@Override
	public List<CommonPropertyCommand> findAllCommonPropertyByindustryId(Long industryId){
		List<CommonProperty> commonPropertyList = commonPropertyDao.findAllCommonPropertyByindustryId(industryId);
		List<CommonPropertyCommand> commonPropertyCommandList = new ArrayList<CommonPropertyCommand>();
		for (CommonProperty commonProperty : commonPropertyList){
			CommonPropertyCommand commonPropertyCommand = new CommonPropertyCommand();
			LangProperty.I18nPropertyCopyToSource(commonProperty, commonPropertyCommand);
			List<Industry> industryList = industryDao.findIndustryListByCommonPropertyId(commonPropertyCommand.getId());
			commonPropertyCommand.setIndustrylist(getindustrynames(industryList));
			commonPropertyCommandList.add(commonPropertyCommand);
		}
		return commonPropertyCommandList;
	}

	private String getindustrynames(List<Industry> industryList){
		StringBuffer sbf=new StringBuffer();
		String industrynames = "";
		for (Industry industry : industryList){
			sbf.append(industry.getName()+",");
		}
		if(sbf.length()>0){
			industrynames=sbf.subSequence(0, sbf.length()-1).toString();
		}
		return industrynames;
	}

	@Override
	public List<PropertyValue> findPropertyValueListBycommonPropertyId(Long propertyId){
		Property property = propertyDao.getByPrimaryKey(propertyId);
		List<PropertyValue> PropertyValueList = propertyValueDao.findPropertyValueListByCommonPropertyId(propertyId,property.getCommonPropertyId());
		
		Map<String, PropertyValue> map = new HashMap<String, PropertyValue>();
		for (PropertyValue propertyValue : PropertyValueList){
			if (map.containsKey(propertyValue.getValue())){
				continue;
			}
			map.put(propertyValue.getValue(), propertyValue);
			
		}
		
		List<PropertyValue> newPropertyValueList = new ArrayList<PropertyValue>();
		List<Long> propertyValueIds = new ArrayList<Long>();
		for (PropertyValue propertyValue : map.values()){
			newPropertyValueList.add(propertyValue);
			Long pvId = propertyValue.getId();
			propertyValueIds.add(pvId);
		}
		boolean i18n = LangProperty.getI18nOnOff();
		
		if (i18n){
			List<PropertyValue> propertyValues = propertyValueDao.findPropertyValueListByIdsI18n(propertyValueIds, LangUtil.getCurrentLang());
			if (Validator.isNotNullOrEmpty(propertyValues)){
				return propertyValues;
			}
		}
		
		return newPropertyValueList;
	}

	@Override
	public Map<String, Object> validatePropertyData(Long industryId,Long commonPropertyId,Long propertyId,String proname){
		Map<String, Object> map = new HashMap<String, Object>();
		if(propertyId==null){
			propertyId=0L;
		}
		Long number_name = propertyDao.validatePropertyname(industryId, propertyId, proname);
		map.put("repeatStatus", false);// 默认不重复
		if (number_name == 0){// 未重复
			Long number_com = propertyDao.validatecommonProperty(industryId, propertyId, commonPropertyId);
			if (number_com == 0){// 未重复
				
			}else{
				map.put("repeatStatus", true);// 重复
				map.put("repeatType", 0);// 同一个行业下的属性重复关联了同一个公用属性ID
				
			}
		}else{
			map.put("repeatStatus", true);// 重复
			map.put("repeatType", 1);// 属性名称重复
		}
		return map;
	}

	@Override
	public boolean validatecommonPropertyName(String commonPropertyName){
		Long num=commonPropertyDao.validatecommonPropertyName(commonPropertyName);
		if(num==0){//不存在
			return true;//验证通过
		}else{
			return false;
		}
	}
	

	@Override
	public DynamicPropertyCommand findByProGroupIdAndPropertyId(Long proGroupId,Long propertyId){
		DynamicPropertyCommand dynamicPropertyCommand = new DynamicPropertyCommand();
		List<PropertyValue> propertyValueList = null;
		//如果分组ID为空，则说明查询属性完全分类
		if(proGroupId==null){
			propertyValueList = propertyValueDao.findPropertyValueListById(propertyId);
		}else{
			propertyValueList = propertyValueDao.findByProGroupId(proGroupId);
		}
		
		Property property = propertyDao.findByIdWithoutCommonProperty(propertyId);
		dynamicPropertyCommand.setProperty(property);
		dynamicPropertyCommand.setPropertyValueList(propertyValueList);
		return dynamicPropertyCommand;
	}

	
	@Override
	public List<PropertyValueLang> findPropertyValueCommandById(Long id){
		List<Long> pvIds = new ArrayList<Long>();
		pvIds.add(id);
		List<PropertyValueLang> propertyLangs = propertyValueDao.findPropertyValueLangByPvids(pvIds, MutlLang.i18nLangs());
		
		return propertyLangs;
	}
	
	
	@Override
	public com.baozun.nebula.command.product.PropertyCommand nebulaCreateOrUpdateProperty(
			com.baozun.nebula.command.product.PropertyCommand propertyCommand) {
		Property property = null;
		if (propertyCommand.getId() != null && propertyCommand.getId() != -1) {
			property = propertyDao.findPropertyByPropertyId(propertyCommand.getId());
		}else{
			property = new Property();
			property.setCreateTime(new Date());
		}
		
		property.setModifyTime(new Date());
		property.setEditingType(propertyCommand.getEditingType());
		property.setIsColorProp(propertyCommand.getIsColorProp());
		property.setIsSaleProp(propertyCommand.getIsSaleProp());
		property.setHasThumb(propertyCommand.getHasThumb());
		property.setRequired(propertyCommand.getRequired());
		property.setSearchable(propertyCommand.getSearchable());		
		property.setValueType(propertyCommand.getValueType());
		if(Validator.isNotNullOrEmpty(propertyCommand.getSortNo())){
			property.setSortNo(propertyCommand.getSortNo());
		}		
		property.setLifecycle(propertyCommand.getLifecycle());
		
		//国际化的判断
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			MutlLang pname = (MutlLang) propertyCommand.getName();
			String[] values = pname.getValues();
			String[] langs = pname.getLangs();
			MutlLang gname = (MutlLang) propertyCommand.getGroupName();
			String[] gnames = gname.getValues();
			String name = pname.getDefaultValue();
			String gpName = gname.getDefaultValue();
			
			property.setName(name);
			property.setGroupName(gpName);
			property = propertyDao.save(property);
			
			Long propertyId = property.getId();
			for (int i = 0; i < values.length; i++){
				String val = values[i];
				String lang = langs[i];
				String groupName = gnames[i];
				// 新增
				PropertyLang pl = propertyDao.findPropertyLang(propertyId, lang);
				if (pl == null){
					PropertyLang propertyLang = new PropertyLang();
					propertyLang.setPropertyId(propertyId);
					propertyLang.setLang(lang);
					propertyLang.setName(val);
					propertyLang.setGroupName(groupName);
					propertyLangDao.save(propertyLang);
				}else{
					// 修改
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("name", val);
					params.put("propertyId", propertyId);
					params.put("lang", lang);
					params.put("groupName", groupName);
					propertyDao.updatePropertyLang(params);
				}
			}
		}else{
			SingleLang singleLang = (SingleLang) propertyCommand.getName();
			property.setName(singleLang.getValue());

			SingleLang gname = (SingleLang) propertyCommand.getGroupName();
			property.setGroupName(gname.getValue());

			property = propertyDao.save(property);
		}

		LangProperty.I18nPropertyCopyToSource(property, propertyCommand);
		return propertyCommand;

	}
	
	@Override
	@Transactional(readOnly=true)
	public Property findPropertyByPropertyId(Long propertyId){
		return propertyDao.findPropertyByPropertyId(propertyId);
	}

	@Override
	@Transactional(readOnly=true)
	public List<PropertyLang> findPropertyLangByPropertyId(Long propertyId){
		return propertyDao.findPropertyLangByPropertyId(propertyId);
	}
	@Override
	public List<PropertyValueLang> findPropertyValueLangByPropertyId(Long propertyId){
		return propertyValueDao.findPropertyValueLangByPropertyId(propertyId,MutlLang.i18nLangs());
	}
	@Override
	public void createOrUpdatePropertyValueByUpload(List<PropertyValueUploadCommand> propertyValueUploadCommandList,Long propertyId){		
		for (PropertyValueUploadCommand propertyValueUploadCommand : propertyValueUploadCommandList){
			PropertyValue propertyValue=null;
			
			Long id = propertyValueUploadCommand.getId();
			if(com.feilong.core.Validator.isNotNullOrEmpty(id)){
				propertyValue=propertyValueDao.findPropertyValueById(id);
			}else{
				propertyValue=new PropertyValue();
				propertyValue.setPropertyId(propertyId);
				propertyValue.setCreateTime(new Date());
			}			
			propertyValue.setValue(propertyValueUploadCommand.getValue());
			propertyValue.setSortNo(propertyValueUploadCommand.getSortNo());
			propertyValue.setModifyTime(new Date());
			
			propertyValue=propertyValueDao.save(propertyValue);
			Map<String, String> valueLangMap = propertyValueUploadCommand.getValueLangMap();
			if(propertyValue!=null&&com.feilong.core.Validator.isNotNullOrEmpty(valueLangMap)){
				PropertyValueLang propertyValueLang=null;
				
				for (Entry<String, String> entry : valueLangMap.entrySet()){
					propertyValueLang = propertyValueDao.findPropertyValueLang(propertyValue.getId(), entry.getKey());
					if(propertyValueLang==null){
						propertyValueLang=new PropertyValueLang();
					}
					propertyValueLang.setLang(entry.getKey());
					propertyValueLang.setValue(entry.getValue());
					propertyValueLang.setPropertyValueId(propertyValue.getId());
					propertyValueLangDao.save(propertyValueLang);
				}
				
			}
		}		
	}
	
	@Override
	public List<DynamicPropertyCommand> findAllDynamicPropertyCommand(){
		List<DynamicPropertyCommand> dynamicPropertyCommandList = new ArrayList<DynamicPropertyCommand>();
		List<Property> properties = propertyDao.findWidthoutCommonPropertyId();
		if(properties!=null && !properties.isEmpty()){
			for(Property property:properties){
				List<PropertyValue>  values = propertyValueDao.findPropertyValueListById(property.getId());
				if(values==null || values.isEmpty()){
					continue;
				}
				DynamicPropertyCommand dynamicPropertyCommand = new DynamicPropertyCommand();
				dynamicPropertyCommand.setProperty(property);
				dynamicPropertyCommand.setPropertyValueList(propertyValueDao.findPropertyValueListById(property.getId()));
				dynamicPropertyCommandList.add(dynamicPropertyCommand);
			}
		}
		return dynamicPropertyCommandList;
		
	}
}

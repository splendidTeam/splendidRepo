package com.baozun.nebula.manager.industry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;
import com.baozun.nebula.command.product.PropertyCommand;
import com.baozun.nebula.dao.industry.IndustryItemDao;
import com.baozun.nebula.dao.industry.IndustryPropertyDao;
import com.baozun.nebula.dao.industry.IndustryPropertyRelationDao;
import com.baozun.nebula.dao.product.PropertyDao;
import com.baozun.nebula.model.product.IndustryPropertyRelation;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyLang;
import com.feilong.core.Validator;

/**
 * @author shouqun.li
 * @version 2016年4月12日 上午11:04:23
 */
@Transactional
@Service("industryPropertyManager")
public class IndustryPropertyManagerImpl implements IndustryPropertyManager{

	@Autowired
	private IndustryPropertyDao industryPropertyDao;
	@Autowired
	private PropertyDao propertyDao;
	@Autowired
	private IndustryPropertyRelationDao industryPropertyRelationDao;
	@Autowired
	private IndustryItemDao industryItemDao;
	
	@Override
	public List<PropertyCommand> findIndustryPropertyListByIndustryId(
			long industryId) {
		List<Property> industryPropertyList = industryPropertyDao.findIndustryPropertyListByIndustryId(industryId);
		if(industryPropertyList != null){
			return getPropertyCommandList(industryPropertyList);
		}else{
			return null;
		}
	}

	@Override
	public List<PropertyCommand> findEnableSelectPropertyListByIndustryId(
			long industryId) {
		List<Property> enableSelectPropertyList = industryPropertyDao.findEnableSelectPropertyListByIndustryId(industryId);
		if(Validator.isNullOrEmpty(enableSelectPropertyList)){
			return null;
		}else{
			return getPropertyCommandList(enableSelectPropertyList);
		}
		
	}
	
	public List<PropertyCommand> getPropertyCommandList(List<Property> PropertyList){
		List<Long> pIds = new ArrayList<Long>();
		List<PropertyCommand> propertyCommandList = new ArrayList<PropertyCommand>();
		for (Property property : PropertyList) {
			PropertyCommand propertyCommand = new PropertyCommand();
			LangProperty.I18nPropertyCopyToSource(property, propertyCommand);
			propertyCommandList.add(propertyCommand);
			pIds.add(property.getId());
		}
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n){
			List<PropertyLang> propertyLangs = propertyDao.findPropertyLangByPids(pIds, MutlLang.i18nLangs());
			if (Validator.isNullOrEmpty(propertyLangs)){
				return propertyCommandList;
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
			for (int i = 0; i < propertyCommandList.size(); i++){
				PropertyCommand propertyCommand = propertyCommandList.get(i);
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
			for (int i = 0; i < propertyCommandList.size(); i++){
				Property property = PropertyList.get(i);
				PropertyCommand propertyCommand = propertyCommandList.get(i);
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
		return propertyCommandList;
	}

	@Override
	public void relationIndustryProperty(IndustryPropertyRelation industryPropertyRelation) {
		industryPropertyRelationDao.save(industryPropertyRelation);
	}

	@Override
	public void deleteIndustryPropertyRelation(long industryId, long propertyId) {
		industryPropertyRelationDao.deleteIndustryPropertyRelation(industryId, propertyId);
	}

	@Override
	public List<Item> findItemsByIndustryIdAndPropertyId(long industryId,
			long propertyId) {
		return industryItemDao.findItemsByIndustryIdAndPropertyId(industryId, propertyId);
	}

	@Override
	public boolean updateIndustryPropertySort(long industryId, String str) {
		String[] list = str.split(",");
		String value;
		int end;
		Long propertyId;
		Integer sortNo;
		boolean flag = true;
		for (int i = 0; i < list.length; i++){
			value = list[i];
			end = value.indexOf("sortNo");
			propertyId = Long.parseLong((value.substring(10, end)));
			sortNo = Integer.parseInt(value.substring(end + 6));
			Integer result = industryPropertyRelationDao.updateIndustryPropertySort(industryId, propertyId, sortNo);
			if (result < 1){
				flag = false;
				break;
			}
		}
		return flag;
	}

	@Override
	public Integer findMaxIndustryPropertySortId(long industryId) {
		return industryPropertyRelationDao.findMaxIndustryPropertySortId(industryId);
	}
}

package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;
import com.baozun.nebula.dao.product.SearchConditionItemDao;
import com.baozun.nebula.dao.product.SearchConditionItemLangDao;
import com.baozun.nebula.model.product.SearchConditionItem;
import com.baozun.nebula.model.product.SearchConditionItemlang;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;
import com.baozun.nebula.sdk.manager.SdkSearchConditionItemManager;
import com.baozun.nebula.utils.Validator;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * 
 * @author 阳羽
 * @createtime 2014-2-10 下午12:59:15
 */
@Transactional
@Service("sdkSearchConditionItemManager")
public class SdkSearchConditionItemManagerImpl implements
		SdkSearchConditionItemManager {

	@Autowired
	private SearchConditionItemDao searchConditionItemDao;
	
	@Autowired
	private SearchConditionItemLangDao searchConditionItemLangDao;
	
	@Override
	@Transactional(readOnly=true)
	public Pagination<SearchConditionItemCommand> findSearchConditionItemByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
        if(paraMap.containsKey("type")){
            if(!paraMap.get("type").equals(1)){
                paraMap.remove("type");
            }
        }
	    
		return searchConditionItemDao.findSearchConditionItemByQueryMapWithPage(page, sorts, paraMap);
	}

	@Override
	public SearchConditionItem createOrUpdateConditionItem(
			SearchConditionItem searchConditionItem) {
	    if(searchConditionItem.getId()!=null){
	        searchConditionItem.setModifyTime(new Date());
	        searchConditionItemDao.updateSearchConditionItemById(searchConditionItem,searchConditionItem.getId());
        }else{
            searchConditionItem.setCreateTime(new Date());
            searchConditionItem.setVersion(new Date());
            searchConditionItem.setLifecycle(1);
            return searchConditionItemDao.save(searchConditionItem);
        }
		
		return null;
	}

	@Override
	public Integer removeSearchConditionItemByIds(List<Long> ids) {
		return searchConditionItemDao.deleteSearchConditionItemByIds(ids);
	}

	@Override
	public void removeSearchConditionItemById(Long id) {
	    List<Long> ids=new ArrayList<Long>();
	    ids.add(id);
		searchConditionItemDao.deleteSearchConditionItemByIds(ids);
	}

	@Override
	public void enableSearchConditionItem(Long id) {
	    searchConditionItemDao.enableSearchConditionItemById(id);
	}

	@Override
	public void disableSearchConditionItem(Long id) {
        searchConditionItemDao.disableSearchConditionItemById(id);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkSearchConditionItemManager#findItemByPropertyValueId(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SearchConditionItemCommand> findItemByPropertyValueId(
			Long pValueId) {
		List<SearchConditionItem> cmdList = searchConditionItemDao.findItemByPropertyValueId(pValueId);
		List<SearchConditionItemCommand> resultList = new ArrayList<SearchConditionItemCommand>();
		
		if(null!= cmdList){
			for(SearchConditionItem s : cmdList){
				SearchConditionItemCommand cmd = new SearchConditionItemCommand();
				cmd =(SearchConditionItemCommand) ConvertUtils.convertFromTarget(cmd, s);
				resultList.add(cmd);
			}
		}
		return resultList;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkSearchConditionItemManager#findItemBySId(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SearchConditionItemCommand> findItemBySId(Long sId,String lang) {
		return searchConditionItemDao.findItemMetaBySIdAndLang(sId, lang);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkSearchConditionItemManager#findItemById(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public SearchConditionItemCommand findItemById(Long id) {
		SearchConditionItem item = searchConditionItemDao.getByPrimaryKey(id);
		SearchConditionItemCommand cmd =(SearchConditionItemCommand) ConvertUtils.convertFromTarget(new SearchConditionItemCommand(), item);
		return cmd;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkSearchConditionItemManager#findItemByPropertyId(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SearchConditionItemCommand> findItemByPropertyId(Long propertyId) {
		List<SearchConditionItem> cmdList = searchConditionItemDao.findItemByPropertyId(propertyId);
		List<SearchConditionItemCommand> resultList = new ArrayList<SearchConditionItemCommand>();
		
		if(null!= cmdList){
			for(SearchConditionItem s : cmdList){
				SearchConditionItemCommand cmd = new SearchConditionItemCommand();
				cmd =(SearchConditionItemCommand) ConvertUtils.convertFromTarget(cmd, s);
				resultList.add(cmd);
			}
		}
		return resultList;
	}

    @Override
    public Integer removeSearchConditionItemByPids(List<Long> pids) {
        return searchConditionItemDao.deleteSearchConditionItemByParentIds(pids);
    }

    @Override
    @Transactional(readOnly=true)
    public SearchConditionItemCommand findSearchConditionItemCommandById(Long id) {
        
        return searchConditionItemDao.findSearchConditionItemById(id);
    }

	@Override
	public SearchConditionItem createOrUpdateConditionItem(
			com.baozun.nebula.command.product.SearchConditionItemCommand searchConditionItemCommand) {
		Long id = searchConditionItemCommand.getId();
		SearchConditionItem searchConditionItem = new SearchConditionItem();
		LangProperty.I18nPropertyCopy(searchConditionItemCommand, searchConditionItem);
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			MutlLang nameLangs = (MutlLang) searchConditionItemCommand.getName();
			String[] values = nameLangs.getValues();
			String[] langs = nameLangs.getLangs();
			String name = nameLangs.getDefaultValue();
			searchConditionItem.setName(name);
			if(id != null){
				searchConditionItem.setModifyTime(new Date());
				searchConditionItemDao.updateSearchConditionItemById(searchConditionItem,id);
			    for (int i = 0; i < values.length; i++) {
			    	String val  =  values[i];
					String lang  =  langs[i];
					SearchConditionItemlang scil = searchConditionItemDao.findSearchConditionItemlang(id, lang);
					if(scil != null){
						Map<String, Object>  params= new HashMap<String, Object>();
						params.put("name", val);
						params.put("searchConditionItemId", id);
						params.put("lang", lang);
						searchConditionItemDao.updateSearchConditionItemLang(params);
					}else{
						SearchConditionItemlang searchConditionLang = new SearchConditionItemlang();
						searchConditionLang.setLang(lang);
						searchConditionLang.setName(val);
						searchConditionLang.setSearchConditionItemId(id);
						searchConditionItemLangDao.save(searchConditionLang);
					}
			    }
			}else{
				searchConditionItem.setCreateTime(new Date());
				searchConditionItem.setVersion(new Date());
				searchConditionItem.setLifecycle(1);
				searchConditionItem = searchConditionItemDao.save(searchConditionItem);;
			    id = searchConditionItem.getId();
			    for (int i = 0; i < values.length; i++) {
			    	String val  =  values[i];
					String lang  =  langs[i];
					SearchConditionItemlang searchConditionLang = new SearchConditionItemlang();
					searchConditionLang.setLang(lang);
					searchConditionLang.setName(val);
					searchConditionLang.setSearchConditionItemId(id);
					searchConditionItemLangDao.save(searchConditionLang);
				}
		        return searchConditionItem;
			}
		}else{
			SingleLang nameLang = (SingleLang) searchConditionItemCommand.getName();
			String name = nameLang.getValue();
			searchConditionItem.setName(name);
			if(id != null){
		        searchConditionItem.setModifyTime(new Date());
		        searchConditionItemDao.updateSearchConditionItemById(searchConditionItem,id);
		    }else{
	            searchConditionItem.setCreateTime(new Date());
	            searchConditionItem.setVersion(new Date());
	            searchConditionItem.setLifecycle(1);
	            searchConditionItem = searchConditionItemDao.save(searchConditionItem);
	            return searchConditionItem;
		    }
		}
	
		return null;
	}

	@Override
	public com.baozun.nebula.command.product.SearchConditionItemCommand findSearchConditionItemCommandI18nById(
			Long id) {
		SearchConditionItemCommand searchConditionItemVo = findSearchConditionItemCommandById(id);
		com.baozun.nebula.command.product.SearchConditionItemCommand searchConditionItemCommand 
		= new com.baozun.nebula.command.product.SearchConditionItemCommand();
		 LangProperty.I18nPropertyCopyToSource(searchConditionItemVo, searchConditionItemCommand);
		 boolean i18n = LangProperty.getI18nOnOff();
		 if (i18n){
			 List<Long> scids = new ArrayList<Long>();
			 scids.add(id);
			 List<SearchConditionItemlang> searchConditionLangs = searchConditionItemDao.findSearchConditionItemLangByScids(scids, MutlLang.i18nLangs());
			 String[] values = new String[MutlLang.i18nSize()];
			 String[] langs = new String[MutlLang.i18nSize()];
			if(Validator.isNotNullOrEmpty(searchConditionLangs)){
				for (int i = 0; i < searchConditionLangs.size(); i++) {
					SearchConditionItemlang searchConditionLang = searchConditionLangs.get(i);
					values[i] = searchConditionLang.getName();
					langs[i] = searchConditionLang.getLang();
				}
			}
			MutlLang mutlLang = new MutlLang();
			mutlLang.setValues(values);
			mutlLang.setLangs(langs);
			searchConditionItemCommand.setName(mutlLang);
		 }else{
			 SingleLang singleLang = new SingleLang();
			 singleLang.setValue(searchConditionItemVo.getName());
			 searchConditionItemCommand.setName(singleLang);
		 }
		return searchConditionItemCommand;
	}

}

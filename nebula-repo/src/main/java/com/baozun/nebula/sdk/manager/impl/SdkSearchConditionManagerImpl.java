package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;
import com.baozun.nebula.dao.product.SearchConditionDao;
import com.baozun.nebula.dao.product.SearchConditionItemDao;
import com.baozun.nebula.dao.product.SearchConditionLangDao;
import com.baozun.nebula.model.product.SearchCondition;
import com.baozun.nebula.model.product.SearchConditionLang;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.manager.SdkSearchConditionManager;
import com.baozun.nebula.utils.Validator;

/**
 * 
 * @author 阳羽
 * @createtime 2014-2-10 下午12:52:25
 */
@Transactional 
@Service("sdkSearchConditionManager")
public class SdkSearchConditionManagerImpl implements SdkSearchConditionManager {
	
	@Autowired
	private SearchConditionDao searchConditionDao;
	
    @Autowired
    private SearchConditionItemDao searchConditionItemDao;
    
    @Autowired
    private SearchConditionLangDao searchConditionLangDao;
	@Override
	@Transactional(readOnly=true)
	public Pagination<SearchConditionCommand> findSearchConditionByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap) {
		return searchConditionDao.findSearchConditionByQueryMapWithPage(page, sorts, paraMap);
	}

	@Override
	public SearchCondition createOrUpdateSearchCondition(
			SearchCondition searchCondition) {
		if(searchCondition.getId()!=null){
		    searchCondition.setModifyTime(new Date());
		    searchConditionDao.updateSearchConditionById(searchCondition, searchCondition.getId());
		}else{
		    searchCondition.setCreateTime(new Date());
		    searchCondition.setVersion(new Date());
		    searchCondition.setLifecycle(1);
	        return searchConditionDao.save(searchCondition);
		}
		
		return null;
	}

	@Override
	public Integer removeSearchConditionByIds(List<Long> ids) {
	    
	    //searchConditionItemDao.deleteSearchConditionItemByParentIds(ids);
	    
		return searchConditionDao.deleteSearchConditionByIds(ids);
	}

	@Override
	public void removeSearchCondition(Long id) {
//	    List<Long> ids=new ArrayList<Long>();
//	    ids.add(id);
//	    searchConditionItemDao.deleteSearchConditionItemByParentIds(ids);
	    List<Long> ids=new ArrayList<Long>();
        ids.add(id);
		searchConditionDao.deleteSearchConditionByIds(ids);
	}

	@Override
	public void enableSearchCondition(Long id) {
	    searchConditionDao.enableSearchConditionById(id);
	}

	@Override
	public void disableSearchCondition(Long id) {
		searchConditionDao.disableSearchConditionById(id);
	}
	

	@Override
	@Transactional(readOnly=true)
	public List<SearchConditionCommand> findConditionByCategoryId(Long cid) {
		List<SearchCondition> cmdList =  searchConditionDao.findConditionByCategoryId(cid);
		List<SearchConditionCommand> resultList = new ArrayList<SearchConditionCommand>();
		if(null!=cmdList){
			for(SearchCondition s:cmdList){
				SearchConditionCommand cmd = new SearchConditionCommand();
				cmd = (SearchConditionCommand) ConvertUtils.convertFromTarget(cmd, s);
				cmd.setPropertyId(s.getPropertyId());
				resultList.add(cmd);
			}
		}
		return resultList;
	}

    @Override
    @Transactional(readOnly=true)
    public SearchConditionCommand findSearchConditionCommandById(Long id) {
        
        return searchConditionDao.findSearchConditionById(id);
    }

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkSearchConditionManager#findConditionByCategoryIdList(java.util.List)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SearchConditionCommand> findConditionByCategoryIdList(
			List<Long> cids) {
		List<SearchCondition> cmdList =  searchConditionDao.findConditionByCategoryIds(cids);
		List<SearchConditionCommand> resultList = new ArrayList<SearchConditionCommand>();
		if(null!=cmdList){
			for(SearchCondition s:cmdList){
				SearchConditionCommand cmd = new SearchConditionCommand();
				cmd = (SearchConditionCommand) ConvertUtils.convertFromTarget(cmd, s);
				cmd.setPropertyId(s.getPropertyId());
				resultList.add(cmd);
			}
		}
		return resultList;
	}

	@Override
	public SearchCondition createOrUpdateSearchCondition(
			com.baozun.nebula.command.product.SearchConditionCommand searchConditionCommand) {
		Long id = searchConditionCommand.getId();
		SearchCondition searchCondition = new SearchCondition();
		LangProperty.I18nPropertyCopy(searchConditionCommand, searchCondition);
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			MutlLang nameLangs = (MutlLang) searchConditionCommand.getName();
			String[] values = nameLangs.getValues();
			String[] langs = nameLangs.getLangs();
			String name = nameLangs.getDefaultValue();
			searchCondition.setName(name);
			if(id != null){
			    searchCondition.setModifyTime(new Date());
			    searchConditionDao.updateSearchConditionById(searchCondition, searchCondition.getId());
			    for (int i = 0; i < values.length; i++) {
			    	String val  =  values[i];
					String lang  =  langs[i];
					SearchConditionLang scl = searchConditionDao.findSearchConditionLang(id, lang);
					if(scl != null){
						Map<String, Object>  params= new HashMap<String, Object>();
						params.put("name", val);
						params.put("searchconditionid", id);
						params.put("lang", lang);
						searchConditionDao.updateSearchConditionLang(params);
					}else{
						SearchConditionLang searchConditionLang = new SearchConditionLang();
						searchConditionLang.setLang(lang);
						searchConditionLang.setName(val);
						searchConditionLang.setSearchConditionId(id);
						searchConditionLangDao.save(searchConditionLang);
					}
			    }
			}else{
			    searchCondition.setCreateTime(new Date());
			    searchCondition.setVersion(new Date());
			    searchCondition.setLifecycle(1);
			    searchCondition = searchConditionDao.save(searchCondition);
			    id = searchCondition.getId();
			    for (int i = 0; i < values.length; i++) {
			    	String val  =  values[i];
					String lang  =  langs[i];
					SearchConditionLang searchConditionLang = new SearchConditionLang();
					searchConditionLang.setLang(lang);
					searchConditionLang.setName(val);
					searchConditionLang.setSearchConditionId(id);
					searchConditionLangDao.save(searchConditionLang);
				}
		        return searchCondition;
			}
		}else{
			SingleLang nameLang = (SingleLang) searchConditionCommand.getName();
			String name = nameLang.getValue();
			searchCondition.setName(name);
			if(id != null){
			    searchCondition.setModifyTime(new Date());
			    searchConditionDao.updateSearchConditionById(searchCondition, searchCondition.getId());
			}else{
			    searchCondition.setCreateTime(new Date());
			    searchCondition.setVersion(new Date());
			    searchCondition.setLifecycle(1);
		        return searchConditionDao.save(searchCondition);
			}
		}
		
		return null;
	}

	@Override
	public com.baozun.nebula.command.product.SearchConditionCommand findSearchConditionCommandI18nById(
			Long id) {
		 SearchConditionCommand searchConditionVo = findSearchConditionCommandById(id);
		 com.baozun.nebula.command.product.SearchConditionCommand searchConditionCommand = 
				 new com.baozun.nebula.command.product.SearchConditionCommand();
		 LangProperty.I18nPropertyCopyToSource(searchConditionVo, searchConditionCommand);
		 boolean i18n = LangProperty.getI18nOnOff();
		 if (i18n){
			 List<Long> scids = new ArrayList<Long>();
			 scids.add(id);
			 List<SearchConditionLang> searchConditionLangs = searchConditionDao.findSearchConditionLangByScids(scids, MutlLang.i18nLangs());
				if(Validator.isNotNullOrEmpty(searchConditionLangs)){
					String[] values = new String[MutlLang.i18nSize()];
					String[] langs = new String[MutlLang.i18nSize()];
					for (int i = 0; i < searchConditionLangs.size(); i++) {
						SearchConditionLang searchConditionLang = searchConditionLangs.get(i);
						values[i] = searchConditionLang.getName();
						langs[i] = searchConditionLang.getLang();
					}
					MutlLang mutlLang = new MutlLang();
					mutlLang.setValues(values);
					mutlLang.setLangs(langs);
					searchConditionCommand.setName(mutlLang);
				}
		 }else{
			 SingleLang singleLang = new SingleLang();
			 singleLang.setValue(searchConditionVo.getName());
			 searchConditionCommand.setName(singleLang);
		 }
		return searchConditionCommand;
	}

}

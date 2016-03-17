package com.baozun.nebula.manager.baseinfo;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.model.baseinfo.PageItem;
import com.baozun.nebula.model.baseinfo.PageTemplate;
import com.baozun.nebula.sdk.command.PageTemplateCommand;
import com.baozun.nebula.sdk.manager.SdkPageTemplateManager;


@Service("pageTemplateManager")
public class PageTemplateManagerImpl implements PageTemplateManager{

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(PageTemplateManagerImpl.class);

	/** 程序返回结果 **/
	private static final Integer SUCCESS = 1;
	/** 程序返回结果 **/
	private static final Integer FAIL = 0;
	
	@Autowired
	private SdkPageTemplateManager sdkPageTemplateManager;
	
	@Override
	@Transactional(readOnly = true)
	public Pagination<PageTemplate> findPageTemplateListByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap) {
		Pagination<PageTemplate> result =sdkPageTemplateManager.findPageTemplateByQueryMapWithPage(page, sorts, paraMap);
		
		return result;
	}

	@Override
	public Integer enableOrDisablePageTemplateById(Long id, Integer state) {
		//
		if(state.equals(PageTemplate.LIFECYCLE_ENABLE)){
			sdkPageTemplateManager.enablePageTemplate(id);
		}else{
			sdkPageTemplateManager.disablePageTemplate(id);
		}
		return SUCCESS;
	}


	@Override
	public PageTemplateCommand createOrUpdatePageTemplate(
			PageTemplateCommand itemCommand) throws Exception {
		return sdkPageTemplateManager.createOrUpdatePageTemplate(itemCommand);
	}

	@Override
	public Integer removePageTemplateByIds(List<Long> ids) {
		return sdkPageTemplateManager.removePageTemplateByIds(ids);
	}

	@Override
	public List<PageItem> findPageItemByPtid(Long pageTemplateId) {
		return sdkPageTemplateManager.findPageItemByPtid(pageTemplateId);
	}

}

package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import com.baozun.nebula.dao.baseinfo.PageItemDao;
import com.baozun.nebula.dao.baseinfo.PageTemplateDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.baseinfo.PageItem;
import com.baozun.nebula.model.baseinfo.PageTemplate;
import com.baozun.nebula.sdk.command.PageTemplateCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkPageTemplateManager;

/**
 * 
 * @author 阳羽
 * @createtime 2014-2-10 下午12:44:57
 */
@Transactional
@Service("sdkPageTemplateManager")
public class SdkPageTemplateManagerImpl implements SdkPageTemplateManager {


	@Autowired
	private PageTemplateDao pageTemplateDao;

	@Autowired
	private PageItemDao pageItemDao;

	@Override
	@Transactional(readOnly=true)
	public Pagination<PageTemplate> findPageTemplateByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap) {
		return pageTemplateDao.findPageTemplateListByQueryMapWithPage(page,
				sorts, paraMap);
	}

	@Override
	public PageTemplateCommand createOrUpdatePageTemplate(
			PageTemplateCommand pageTemplateCommnad) {
		PageTemplate pageTemplate = null;
		if (pageTemplateCommnad.getId() == null
				|| pageTemplateCommnad.getId() == 0) {
			// 保存
			pageTemplate = new PageTemplate();
			pageTemplate = convertPageTemplateCommandToPageTemplate(
					pageTemplateCommnad, pageTemplate);
			pageTemplate = pageTemplateDao.save(pageTemplate);
			
			if(pageTemplateCommnad.getPageItems() != null && pageTemplateCommnad.getPageItems().size()>0){
				for(PageItem item : pageTemplateCommnad.getPageItems()){
					pageItemDao.save(item);
				}
			}
		} else {
			// 更新
			pageTemplateDao.updatePageTemplate(pageTemplateCommnad.getId(), pageTemplateCommnad.getPageCode(),
					pageTemplateCommnad.getPageName(), pageTemplateCommnad.getImg(), pageTemplateCommnad.getOpeartorId());
			
			if(pageTemplateCommnad.getPageItems() != null && pageTemplateCommnad.getPageItems().size()>0){
				for(PageItem item : pageTemplateCommnad.getPageItems()){
					pageItemDao.save(item);
				}
			}
			pageTemplate = pageTemplateDao.findPageTemplateById(pageTemplateCommnad.getId());
		}
		if (null != pageTemplate) {
			return convertPageTemplateToPageTemplateCommand(pageTemplate);
		}
		return null;
	}

	private PageTemplateCommand convertPageTemplateToPageTemplateCommand(
			PageTemplate pageTemplate) {
		PageTemplateCommand pageCommand = new PageTemplateCommand();

		pageCommand.setCreateTime(pageTemplate.getCreateTime());
		pageCommand.setImg(pageTemplate.getImg());
		pageCommand.setLifecycle(pageTemplate.getLifecycle());
		pageCommand.setModifyTime(pageTemplate.getModifyTime());
		pageCommand.setOpeartorId(pageTemplate.getOpeartorId());
		pageCommand.setPageCode(pageTemplate.getPageCode());
		pageCommand.setPageName(pageTemplate.getPageName());
		pageCommand.setVersion(pageTemplate.getVersion());

		return pageCommand;
	}

	private PageTemplate convertPageTemplateCommandToPageTemplate(
			PageTemplateCommand pageCommand, PageTemplate page) {
		page.setCreateTime(pageCommand.getCreateTime());
		page.setImg(pageCommand.getImg());
		page.setLifecycle(pageCommand.getLifecycle());
		page.setModifyTime(pageCommand.getModifyTime());
		page.setOpeartorId(pageCommand.getOpeartorId());
		page.setPageCode(pageCommand.getPageCode());
		page.setPageName(pageCommand.getPageName());
		page.setVersion(pageCommand.getVersion());

		return page;
	}

	@Override
	public Integer removePageTemplateByIds(List<Long> ids) {
		Integer count = pageTemplateDao.removePageTemplateByIds(ids);
		if(count!= ids.size()){
			throw new BusinessException(Constants.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED);
		}
		return count;
	}

	@Override
	public void removePageTemplateById(Long id) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		Integer count = pageTemplateDao.removePageTemplateByIds(ids);
		if(count!= 1){
			throw new BusinessException(Constants.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED);
		}
	}

	@Override
	public void enablePageTemplate(Long id) {
		Integer count = pageTemplateDao.enableOrDisablePageTemplateById(id,
				PageTemplate.LIFECYCLE_ENABLE);
		if(count!= 1){
			throw new BusinessException(Constants.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED);
		}

	}

	/**
	 * 禁用页面模板
	 * 
	 * @param 页面模版id
	 */
	@Override
	public void disablePageTemplate(Long id) {
		Integer count = pageTemplateDao.enableOrDisablePageTemplateById(id,
				PageTemplate.LIFECYCLE_DISABLE);
		if(count!= 1){
			throw new BusinessException(Constants.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED);
		}

	}

	@Override
	@Transactional(readOnly=true)
	public boolean validatePageTemplateCode(String pageCode) {
		List<String> pageCodes = new ArrayList<String>();
		pageCodes.add(pageCode);
		List<PageTemplate> pages = pageTemplateDao
				.findPageTemplateListByPageCodes(pageCodes);
		return (pages !=null && pages.size() > 0) ? true : false;
	}

	@Override
	@Transactional(readOnly=true)
	public boolean validatePageItemCode(String pageCode) {
		List<String> codes = new ArrayList<String>();
		codes.add(pageCode);
		List<PageItem> pages = pageItemDao.findPageItemListByCodes(codes);
		return (pages !=null && pages.size() > 0) ? true : false;
	}

	@Override
	@Transactional(readOnly=true)
	public List<PageTemplate> findPageTemplateListByIds(List<Long> ids) {
		List<PageTemplate> templateList = pageTemplateDao
				.findPageTemplateListByIds(ids);
		return templateList;
	}

	@Override
	@Transactional(readOnly=true)
	public List<PageItem> findPageItemByPtid(Long pageTemplateId) {		
		return pageItemDao.findPageItemListByPageTemplateId(pageTemplateId);
	}

}

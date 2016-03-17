package com.baozun.nebula.manager.baseinfo;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.baseinfo.PageItem;
import com.baozun.nebula.model.baseinfo.PageTemplate;
import com.baozun.nebula.sdk.command.PageTemplateCommand;

public interface PageTemplateManager extends BaseManager{

	/**
	 * 分页获取PageTemplate列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	Pagination<PageTemplate> findPageTemplateListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	/**
	 * 保存修改模板
	 * @param itemCommand
	 * @return
	 */
		 
	PageTemplateCommand createOrUpdatePageTemplate(PageTemplateCommand itemCommand) throws Exception;

	
	/**
	 * 通过id启用或禁用PageTemplate 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	public Integer enableOrDisablePageTemplateById(Long id,Integer state);
	
	
	/**
	 * 找出模板的全部页面元素
	 * @param pageTemplateId
	 * @return
	 */
	public List<PageItem> findPageItemByPtid(Long pageTemplateId);

	
	
	/**
	 * 批量删除页面模版
	 * @param ids
	 * @return
	 */
	public Integer removePageTemplateByIds(List<Long> ids);
	
}

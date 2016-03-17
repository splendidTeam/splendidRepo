package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;
import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.baseinfo.PageItem;
import com.baozun.nebula.model.baseinfo.PageTemplate;
import com.baozun.nebula.sdk.command.PageTemplateCommand;

/**
 * 页面模板管理
 * @author 阳羽
 * @createtime 2014-2-10 下午12:28:46
 */
public interface SdkPageTemplateManager extends BaseManager{

	/**
	 * 获取页面模板列表
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	public Pagination<PageTemplate> findPageTemplateByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	/**
	 * 保存或修改页面模板(带页面元素切入点)
	 * @param pageTemplateCommnad
	 * @return
	 */
	public PageTemplateCommand createOrUpdatePageTemplate(PageTemplateCommand pageTemplateCommnad);
	
	/**
	 * 批量删除页面模版
	 * @param ids
	 * @return
	 */
	public Integer removePageTemplateByIds(List<Long> ids);
	
	
	/**
	 * 删除页面模版
	 * @param id 页面模版id
	 */
	public void removePageTemplateById(Long id);
	
	/**
	 * 启用页面模板
	 * @param id 页面模版id
	 */
	public void enablePageTemplate(Long id);
	
	/**
	 * 禁用页面模板
	 * @param id 页面模版id
	 */
	public void disablePageTemplate(Long id);
	
	/**
	 * 验证页面编码重复
	 * @param pageCode
	 * @return 重复返回true,不重复返回false
	 */
	public boolean validatePageTemplateCode(String pageCode);
	
	/**
	 * 验证页面切入点元素编码重复
	 * @param pageCode
	 * @return 重复返回true,不重复返回false
	 */
	public boolean validatePageItemCode(String pageCode);
	
	/**
	 * 通过模版页面ids查找页面模版
	 * @param ids
	 * @return
	 */
	public List<PageTemplate> findPageTemplateListByIds(List<Long> ids);
	
	/**
	 * 找出模板的全部页面元素
	 * @param pageTemplateId
	 * @return
	*/
	public List<PageItem> findPageItemByPtid(Long pageTemplateId);

}

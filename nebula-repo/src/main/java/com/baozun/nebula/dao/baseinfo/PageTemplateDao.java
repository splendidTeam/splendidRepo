package com.baozun.nebula.dao.baseinfo;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.model.baseinfo.PageTemplate;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;


/**页面模板
 * @author baobao.sun
 * */
public interface PageTemplateDao extends GenericEntityDao<PageTemplate, Long>{

	/**
	 * 根据id查询PageTemplate
	 * @param id
	 * @return
	 */
	@NativeQuery(model = PageTemplate.class)
	PageTemplate findPageTemplateById(@QueryParam("id") Long id);
	
	/**
	 * 分页获取PageTemplate列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = PageTemplate.class)
	Pagination<PageTemplate> findPageTemplateListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 通过多个pageCode查询会员列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PageTemplate.class)
	List<PageTemplate> findPageTemplateListByPageCodes(@QueryParam("pageCodes") List<String> pageCodes);
	
	/**
	 * 启用禁用模板
	 * @param state
	 * @param ids
	 */
	@NativeUpdate
	Integer enableOrDisablePageTemplateById(@QueryParam("id") Long id,@QueryParam("state") Integer state);
	
	/**
	 * 保存或修改页面模板
	 * @return
	 */
	@NativeUpdate
	Integer updatePageTemplate(@QueryParam("id")Long id,
							@QueryParam("pageCode")String pageCode,
							@QueryParam("pageName")String pageName,
							@QueryParam("img")String img, 
							@QueryParam("opeartorId")Long opeartorId);
	
	/**
	 * (逻辑删除)
	 * @param ids
	 */
	@NativeUpdate
	Integer removePageTemplateByIds(@QueryParam("ids") List<Long> ids);
	
	/**
	 * 根据Ids集合查询PageTemplate
	 * 
	 * @param id集合
	 * @return
	 */
	@NativeQuery(model = PageTemplate.class)
	List<PageTemplate> findPageTemplateListByIds(@QueryParam("Ids") List<Long> ids);
	
	
	
}

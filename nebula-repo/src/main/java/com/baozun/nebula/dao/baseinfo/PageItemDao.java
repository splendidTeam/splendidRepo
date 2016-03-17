package com.baozun.nebula.dao.baseinfo;


import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.baseinfo.PageItem;


/**页面元素
 * @author baobao.sun
 * */
public interface PageItemDao extends GenericEntityDao<PageItem, Long>{

	/**
	 * 根据id查询PageItem
	 * 
	 * @param code
	 * @return
	 */
	@NativeQuery(model = PageItem.class)
	PageItem findPageItemById(@QueryParam("id") Long id);
	
	
	/**
	 * 通过pageTemplateId获取PageItem列表
	 * @param pageTemplateId
	 * @return
	 */
	@NativeQuery(model = PageItem.class)
	List<PageItem> findPageItemListByPageTemplateId(@QueryParam("ptId")Long pageTemplateId);
	
	
	
	/**
	 * 通过多个Code查询列表
	 * @param 
	 * @return
	 */
	@NativeQuery(model = PageItem.class)
	List<PageItem> findPageItemListByCodes(@QueryParam("codes") List<String> codes);
	
	/**
	 * (逻辑删除)
	 * @param ids
	 */
	@NativeUpdate
	Integer removePageItemByIds(@QueryParam("ids") List<Long> ids);
	
	
}

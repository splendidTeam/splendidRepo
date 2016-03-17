/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.dao.i18n;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.model.i18n.I18nLang;

/**
 * I18nLangDao
 * 
 * @author 何波
 * 
 */
public interface I18nLangDao extends GenericEntityDao<I18nLang, Long> {

	/**
	 * 获取所有I18nLang列表
	 * 
	 * @return
	 */
	@NativeQuery(model = I18nLang.class)
	List<I18nLang> findAllI18nLangList();

	/**
	 * 通过ids获取I18nLang列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = I18nLang.class)
	List<I18nLang> findI18nLangListByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 通过参数map获取I18nLang列表
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = I18nLang.class)
	List<I18nLang> findI18nLangListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 分页获取I18nLang列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = I18nLang.class)
	Pagination<I18nLang> findI18nLangListByQueryMapWithPage(Page page,
			Sort[] sorts, @QueryParam Map<String, Object> paraMap);

	@NativeUpdate
	int updateDefaultLang(@QueryParam("id") Long id);

	@NativeUpdate
	int updateLifecycle(@QueryParam("ids")List<Long> ids, @QueryParam("lifecycle")int lifecycle);

	@NativeQuery(clazzes = Integer.class, alias = "sort")
	int selectSort();

}

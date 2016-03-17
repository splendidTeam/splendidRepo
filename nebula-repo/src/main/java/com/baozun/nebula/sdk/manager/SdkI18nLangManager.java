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
package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.i18n.I18nLang;

/**
 * I18nLangManager
 * @author  何波
 *
 */
public interface SdkI18nLangManager extends BaseManager {

	/**
	 * 保存I18nLang
	 * 
	 */
	I18nLang saveI18nLang(I18nLang model);
	
	/**
	 * 通过id获取I18nLang
	 * 
	 */
	I18nLang findI18nLangById(Long id);

	/**
	 * 获取所有I18nLang列表
	 * @return
	 */
	List<I18nLang> findAllI18nLangList();
	
	/**
	 * 通过ids获取I18nLang列表
	 * @param ids
	 * @return
	 */
	List<I18nLang> findI18nLangListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取I18nLang列表
	 * @param paraMap
	 * @return
	 */
	List<I18nLang> findI18nLangListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取I18nLang列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<I18nLang> findI18nLangListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	/**
	 * 缓存中的国际化信息集合
	 * @return
	 */
	List<I18nLang> geti18nLangCache();
	
	int updateLifecycle(List<Long> ids, int lifecycle);
	
	void loadI18nLangs();
	
	/**
	 * 获取默认语言
	 * @return
	 */
	String getDefaultlang();
	
	/**
	 * 根据语言编码获取语言名称
	 * @param 语言编码
	 * @return
	 */
	String getCurrentLangValue(String lang);
	
	/**
	 * 判断配置中是否存在该语言
	 * @param lang
	 * @return
	 */
	boolean isExistLang(String lang);
	
	/**
	 * 获取所有可用的语言标识
	 * @return
	 */
	List<String> getAllEnabledI18nKeyList();
}

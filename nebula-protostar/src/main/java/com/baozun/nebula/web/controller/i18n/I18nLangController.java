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
package com.baozun.nebula.web.controller.i18n;


import java.util.Arrays;
import java.util.List;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;
/**
 * I18nLangController
 * @author  何波
 *
 */
 @Controller
public class I18nLangController extends BaseController{


	@Autowired
	private SdkI18nLangManager i18nLangManager;
	
	@RequestMapping("/i18nLang/list.htm")
	public String list(){
		return "/i18n/i18nLang";
	}
	/**
	 * 保存I18nLang
	 * 
	 */
	@RequestMapping("/i18nLang/save.json")
	@ResponseBody
	public BackWarnEntity saveI18nLang(I18nLang model){
			 i18nLangManager.saveI18nLang(model);
		return SUCCESS;
	}
	
	/**
	 * 通过id获取I18nLang
	 * 
	 */
	@RequestMapping("/i18nLang/findByid.json")
	@ResponseBody
	public I18nLang findI18nLangById(Long id){
	
		return i18nLangManager.findI18nLangById(id);
	}

	/**
	 * 获取所有I18nLang列表
	 * @return
	 */
	@RequestMapping("/i18nLang/findAll.json")
	@ResponseBody
	public List<I18nLang> findAllI18nLangList(){
	
		return i18nLangManager.findAllI18nLangList();
	};
	
	/**
	 * 通过ids获取I18nLang列表
	 * @param ids
	 * @return
	 */
	@RequestMapping("/i18nLang/findByIds.json")
	@ResponseBody
	public List<I18nLang> findI18nLangListByIds(Long[] ids){
	
		return i18nLangManager.findI18nLangListByIds(Arrays.asList(ids));
	};
	
	/**
	* @Description: 分页获取I18nLang列表
	* @param queryBean
	* @return   
	* Pagination<I18nLang>
	* @throws
	 */
	@RequestMapping("/i18nLang/page.json")
	@ResponseBody
	public Pagination<I18nLang> findI18nLangListByQueryMapWithPage(@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		if(sorts==null){
			sorts = Sort.parse("id desc");
		}
		return  i18nLangManager.findI18nLangListByQueryMapWithPage(queryBean.getPage(),
				sorts, queryBean.getParaMap());
	}
	/**
	 * 通过ids批量启用或禁用I18nLang
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@RequestMapping("/i18nLang/enableOrDisableByIds.json")
	@ResponseBody
	public BackWarnEntity  enableOrDisableI18nLangByIds(Long[] ids,Integer state) {
		i18nLangManager.updateLifecycle(Arrays.asList(ids), state);
		return SUCCESS;
	}
	
	/**
	 * 通过ids批量删除I18nLang
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@RequestMapping("/i18nLang/removeByIds.json")
	@ResponseBody
	public BackWarnEntity removeI18nLangByIds(Long[] ids){
		i18nLangManager.updateLifecycle(Arrays.asList(ids),2);
		return SUCCESS;
	}
}

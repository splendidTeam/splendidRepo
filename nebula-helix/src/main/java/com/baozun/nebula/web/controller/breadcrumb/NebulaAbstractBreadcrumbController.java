/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.web.controller.breadcrumb;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.exception.IllegalItemStateException;
import com.baozun.nebula.manager.breadcrumb.BreadcrumbManager;
import com.baozun.nebula.manager.system.AbstractCacheBuilder;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.web.controller.breadcrumb.converter.BreadcrumbsViewCommandConverter;
import com.baozun.nebula.web.controller.breadcrumb.viewcommand.BreadcrumbsViewCommand;
import com.feilong.core.Validator;

/**  
 * 面包屑 
 * @Description 
 * @author dongliang ma
 * @date 2016年5月23日 上午10:56:34 
 * @version   
 */
public abstract class NebulaAbstractBreadcrumbController {
	
	
	//缓存 key的定义
	/** 面包屑的缓存key*/
	private static final String	CACHE_KEY_BREADCRUMB				="cache_key_breadcrumb";
	
	@Autowired
	private BreadcrumbManager										breadcrumbManager;
	
	@Autowired
	@Qualifier("breadcrumbsViewCommandConverter")
	private BreadcrumbsViewCommandConverter 						breadcrumbsViewCommandConverter;
	
	protected List<BreadcrumbsViewCommand> bulidCurmbViewCommandsWithCache(final Long navId,
			final Long itemId,
			final HttpServletRequest request)throws IllegalItemStateException{
		//构造缓存key [cache_key_breadcrumb_navId_5_zh_CN]
		String concat ="";
		if(Validator.isNotNullOrEmpty(navId)){
			concat +="_navId_"+navId;
		}
		if(Validator.isNotNullOrEmpty(itemId)){
			concat +="_itemId_"+itemId;
		}
		String key =CACHE_KEY_BREADCRUMB +concat;
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n){
			String lang = LangUtil.getCurrentLang();
			key =key.concat("_").concat(lang);
		}
		Integer expireSeconds =getBreadcrumbCacheExpireSeconds();
		List<CurmbCommand> curmbCommands =new AbstractCacheBuilder<List<CurmbCommand>, 
				IllegalItemStateException>(key, expireSeconds){
			@Override
			protected List<CurmbCommand> buildCachedObject()
					throws IllegalItemStateException {
				return breadcrumbManager.findCurmbCommands(navId, itemId, request);
			}
		}.getCachedObject();
		List<BreadcrumbsViewCommand> breadcrumbsViewCommands = breadcrumbsViewCommandConverter.convert(curmbCommands);
		return breadcrumbsViewCommands;
	}
	
	/**
	 * 获取面包屑缓存有效期
	 * @return
	 */
	protected abstract Integer getBreadcrumbCacheExpireSeconds();
	
}

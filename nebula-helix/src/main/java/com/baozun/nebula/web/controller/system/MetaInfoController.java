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
 */
package com.baozun.nebula.web.controller.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.manager.system.MataInfoManager;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 系统相关设置controller
 * 
 * @author caihong.wu
 * @date 2013-6-26下午03:52:58
 */
@Controller
public class MetaInfoController extends BaseController{

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(MetaInfoController.class);

	@Autowired
	private MataInfoManager mataInfoManager;
	/**
	 * 显示系统的相关界面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/metainfo/list.htm")
	public String showRelatedCategory(){
		return "/system/metainfo/list";
	}
	
	@RequestMapping(value = "/system/monitorUrl.htm")
	@ResponseBody
	public String monitorUrl(){
		return mataInfoManager.monitorUrl();
	}
}

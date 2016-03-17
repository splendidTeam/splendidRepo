/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.web.controller.column;

import java.util.List;

import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.column.ColumnComponentCommand;
import com.baozun.nebula.command.column.ColumnModuleCommand;
import com.baozun.nebula.manager.column.ColumnManager;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.model.column.ColumnComponent;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.web.bind.ArrayCommand;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 版块管理
 * 
 * @author chenguang.zhou
 * @date 2014年4月3日 下午6:02:46
 */
@Controller
public class ColumnController extends BaseController {

	@Autowired
	private ColumnManager		columnManager;

	@Autowired
	private CategoryManager		categoryManager;

	/** 上传图片的域名 */
	@Value("#{meta['upload.img.domain.base']}")
	private String				UPLOAD_IMG_DOMAIN		= "";

	@Value("#{meta['defaultNonItemImgUrl']}")
	private String				defaultNonItemImgUrl	= "";

	@Value("#{meta['frontend.url']}")
	private String				columnBrowseUrl			= "";

	private final static String	HOME_PAGE_CODE			= "HOME_PAGE";

	private final static String	BRAND_PAGE_CODE			= "BRAND_PAGE";

	private final static String	PDP_PAGE_CODE			= "PDP_PAGE";

	private final static String	LIST_PAGE_CODE			= "LIST_PAGE";

	/**
	 * 跳转到首页模块管理
	 * 
	 * @return
	 */
	@RequestMapping("/system/homeBoardManager.htm")
	public String homeModulePage(Model model) {
		List<ColumnComponentCommand> columnCompCommandList = columnManager.findColumnComponentByPageCode(HOME_PAGE_CODE);
		// 分类列表
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("columnCompCommandList", columnCompCommandList);
		model.addAttribute("defaultNonItemImgUrl", defaultNonItemImgUrl);
		model.addAttribute("customBaseUrl", UPLOAD_IMG_DOMAIN);
		model.addAttribute("columnBrowseUrl", columnBrowseUrl);
		return "/column/column-home";
	}

	/**
	 * 保存模块-组件信息
	 * 
	 * @param columnComponents
	 * @param moduleCode
	 * @param pageCode
	 * @param publishTime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/column/saveColumnComponent.htm", method = RequestMethod.POST)
	@ResponseBody
	public Object saveColumnComponent(@ArrayCommand ColumnComponent[] columnComponents,
			@RequestParam() String moduleCode, @RequestParam() String pageCode, 
			@RequestParam(value="publishTime", required=false) String publishTime)
			throws Exception {
		columnManager.saveColumnComponent(columnComponents, UPLOAD_IMG_DOMAIN, moduleCode, pageCode, publishTime);
		return SUCCESS;
	}

	/**
	 * 页面发布
	 * 
	 * @param pageCode
	 * @return
	 */
	@RequestMapping("/column/nowPublish.json")
	@ResponseBody
	public Object nowPublish(@RequestParam("pageCode") String pageCode) {
		columnManager.publishColumnPage(pageCode);
		return SUCCESS;
	}

	// **************************PDP页面版块*********************************************
	/**
	 * 到pdp页面版块管理
	 * 
	 * @return
	 */
	@RequestMapping("/column/toPDPColumnPage.htm")
	public String toPDPColumnPage(Model model) {
		List<ColumnComponentCommand> columnCompCommandList = columnManager.findColumnComponentByPageCode(PDP_PAGE_CODE);
		model.addAttribute("columnCompCommandList", columnCompCommandList);
		return "/column/column-pdp";
	}

	// **************************列表页面版块*********************************************
	@RequestMapping("/column/toListColumnPage.htm")
	public String toListColumnPage(Model model) {
		List<ColumnComponentCommand> columnCompCommandList = columnManager
				.findColumnComponentByPageCode(LIST_PAGE_CODE);
		model.addAttribute("columnCompCommandList", columnCompCommandList);
		model.addAttribute("defaultNonItemImgUrl", defaultNonItemImgUrl);
		model.addAttribute("customBaseUrl", UPLOAD_IMG_DOMAIN);
		return "/column/column-list";
	}

	// *****************************品牌页版块*********************************************
	
	/**
	 * 品牌页版块管理 
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/system/brandColumnManager.htm")
	public String brandColumnManager(Model model) {
		List<ColumnComponentCommand> columnCompCommandList = columnManager
				.findColumnComponentByPageCode(BRAND_PAGE_CODE);
		// 分类列表
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("columnCompCommandList", columnCompCommandList);
		model.addAttribute("defaultNonItemImgUrl", defaultNonItemImgUrl);
		model.addAttribute("customBaseUrl", UPLOAD_IMG_DOMAIN);
		model.addAttribute("columnBrowseUrl", columnBrowseUrl);
		return "/column/column-brand";
	}
}

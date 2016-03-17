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

package com.baozun.nebula.web.controller.baseinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.baseinfo.NavigationCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.baseinfo.NavigationManager;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.model.baseinfo.Navigation;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.solr.utils.JsonFormatUtil;
import com.baozun.nebula.web.bind.I18nCommand;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 菜单导航管理
 * 
 * @author - 项硕
 */
@Controller
public class NavigationController extends BaseController {

	private static final Logger	log	= LoggerFactory.getLogger(NavigationController.class);
	
	@Autowired
	private NavigationManager navigationManager;
	@Autowired
	private CategoryManager categoryManager;
	
	/**
	 * 前往页面
	 * 将 分类列表 与 导航列表 传给页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/base/navigation.htm", method = RequestMethod.GET)
	public String navigationList(Model model) {
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> cateList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", cateList);
		log.debug(JsonFormatUtil.format(cateList));
		
		List<Navigation> naviList = navigationManager.findAllNavigationList(Navigation.COMMOM_SORTS);
		model.addAttribute("navigationList", naviList);
		log.debug(JsonFormatUtil.format(naviList));
		return "system/navigation/navigation";
	}
	
	/**
	 * 加载导航树
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/base/navigationTree.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> navigationTree() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Navigation> list = navigationManager.findAllNavigationList(Navigation.COMMOM_SORTS);
		List<Map<String, Object>> nodeList = new ArrayList<Map<String,Object>>();
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("id", 0);
		root.put("name", "ROOT");
		root.put("state", 1);
		root.put("open", true);
		root.put("root", true);
		nodeList.add(root);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> node = new HashMap<String, Object>();
			Navigation navi = list.get(i);
			node.put("id", navi.getId());
			node.put("pId", navi.getParentId());
			node.put("name", navi.getName());
			node.put("state", navi.getLifecycle());
			node.put("diy_type", navi.getType());
			node.put("diy_param", navi.getParam());
			node.put("diy_sort", navi.getSort());
			node.put("diy_url", navi.getUrl());
			node.put("diy_isNewWin", navi.getIsNewWin());
			node.put("open", navi.getParentId().equals(0L));
			nodeList.add(node);
		}
		map.put("tree", JsonFormatUtil.format(nodeList));
		log.debug(JsonFormatUtil.format(nodeList));
		return map;
	}

	/**
	 * 新增或更新导航
	 * @param navigation
	 * @return
	 */
	@RequestMapping(value = "/base/saveOrUpdateNavigation.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createOrUpdateNavigation(Navigation navigation) {
		Map<String, Object> rs = new HashMap<String, Object>();
		navigation.setOpeartorId(getUserDetails().getUserId());
		try {
			rs.put("model", navigationManager.createOrUpdateNavigation(navigation)); 
			rs.put("isSuccess", true);
		} catch (Exception e) {
			e.printStackTrace();
			rs.put("isSuccess", false);
		}
		log.debug(JsonFormatUtil.format(rs));
		return rs;
	}
	
	
	@RequestMapping(value = "/i18n/base/saveOrUpdateNavigation.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createOrUpdateNavigationI18n(@I18nCommand NavigationCommand navigationCommand) {
		Map<String, Object> rs = new HashMap<String, Object>();
		navigationCommand.setOpeartorId(getUserDetails().getUserId());
		try {
			rs.put("model", navigationManager.i18nCreateOrUpdateNavigation(navigationCommand)); 
			rs.put("isSuccess", true);
		} catch (BusinessException e) {
			//e.printStackTrace();
			String errMsg =null;
			if(e.getArgs()==null){
				errMsg =getMessage(e.getErrorCode());
			}else{
				errMsg =getMessage(e.getErrorCode(), e.getArgs());
			}
			
			rs.put("isSuccess", false);
			rs.put("errMsg", errMsg);
			return rs;
		}
		log.debug(JsonFormatUtil.format(rs));
		return rs;
	}
	
	@RequestMapping("/i18n/navigation/findNavigationLangByNavigationId.json")
	@ResponseBody
	public NavigationCommand findNavigationLangByNavigationId(@RequestParam("navigationId")Long navigationId){
		return navigationManager.i18nFindNavigationLangByNavigationId(navigationId);
	}
	

	/**
	 * 删除导航
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/base/removeNavigation.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity removeNavigation(@RequestParam Long id) {
		try {
			navigationManager.removeNavigationById(id);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return FAILTRUE;
		}
	}

	/**
	 * 排序导航
	 * @param ids	已排序的导航id集（升序）
	 * @return
	 */
	@RequestMapping(value = "/base/sortNavigation.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity sortNavigation(@RequestParam String ids) {
		log.debug("ids:  " + ids);
		try {
			navigationManager.sortNavigationsByIds(ids, getUserDetails().getUserId());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return FAILTRUE;
		}
	}
}

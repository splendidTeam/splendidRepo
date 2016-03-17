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
package com.baozun.nebula.web.controller.product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.ItemCategoryManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 商品关联分类controller
 * 
 * @author yi.huang
 * @date 2013-6-26 上午09:54:03
 */
@Controller
public class ItemCategoryController extends BaseController{

	
	@Autowired
	private CategoryManager		categoryManager;
	
	@Autowired
	private ItemCategoryManager itemCategoryManager;

	
	@Autowired
	private IndustryManager industryManager;
	
	@Autowired
    private ShopManager shopManager;

	/**
	 * 显示商品关联分类页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/item/itemCategory.htm")
	public String itemCategory(Model model){
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", categoryList);
		
		List<Industry> result=industryManager.findAllIndustryList(); 
		model.addAttribute("industrylist",result);
		
		return "/product/item/item-category";
	}
	
	/**
	 * 已分类商品列表
	 * 
	 * @param model
	 * @param queryBean
	 * @param request
	 * @param response
	 * @return
	 */
	
	@RequestMapping(value = "/item/itemCtList.json")
	@ResponseBody
	public Pagination<ItemCommand> findItemCtListJson(Model model,
			@QueryBeanParam QueryBean queryBean, HttpServletRequest request,
			HttpServletResponse response){
		
		//查询orgId
		UserDetails userDetails = this.getUserDetails();
		
		ShopCommand shopCommand =null;
		Long shopId=0L;
		
		Long currentOrgId =	userDetails.getCurrentOrganizationId();
		//根据orgId查询shopId
		if(currentOrgId!=null){
			shopCommand=shopManager.findShopByOrgId(currentOrgId);
			if(shopCommand!=null){
				shopId = shopCommand.getShopid();
			}
		}
				
		Sort[] sorts=queryBean.getSorts();
		
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("tpit.create_time","desc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
		Pagination<ItemCommand> args = itemCategoryManager.
				findItemCtListByQueryMapWithPage(queryBean.getPage(),
						sorts, queryBean.getParaMap(),shopId);
			
		return args;
	}
	
	/**
	 * 未分类商品列表
	 * @param model
	 * @param queryBean
	 * @param request
	 * @param response
	 * @return
	 */
	
	@RequestMapping(value = "/item/itemNoctList.json")
	@ResponseBody
	public Pagination<ItemCommand> findNoctItemListJson(Model model,
			@QueryBeanParam QueryBean queryBean, HttpServletRequest request,
			HttpServletResponse response){
		
		//查询orgId
		UserDetails userDetails = this.getUserDetails();
		
		ShopCommand shopCommand =null;
		Long shopId=0L;
		
		Long currentOrgId =	userDetails.getCurrentOrganizationId();
		//根据orgId查询shopId
		if(currentOrgId!=null){
			shopCommand=shopManager.findShopByOrgId(currentOrgId);
			if(shopCommand!=null){
				shopId = shopCommand.getShopid();
			}
		}
		
		Sort[] sorts=queryBean.getSorts();
		
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("tpit.create_time","desc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
		
		Pagination<ItemCommand> args = itemCategoryManager.
				findItemNoctListByQueryMapWithPage(queryBean.getPage(),
						sorts, queryBean.getParaMap(),shopId);
			
		return args;
	}
	
	@RequestMapping("/item/bindItemCategory.json")
	@ResponseBody
	public Object bindItemCategory(
			@RequestParam("itemIds") Long[] itemIds,
			@RequestParam("categoryIds") Long[] categoryIds,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		itemCategoryManager.bindItemCategory(itemIds, categoryIds);
		

		return SUCCESS;
	}
	@RequestMapping("/item/unBindItemCategory.json")
	@ResponseBody
	public Object unBindItemCategory(
			@RequestParam("itemIds") Long[] itemIds,
			@RequestParam("categoryId") Long categoryId,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		boolean removeFlag=
				itemCategoryManager.unBindItemCategory(itemIds, categoryId);

		if(!removeFlag){
			throw new Exception();
		}
		return SUCCESS;
	}
	
	@RequestMapping("/item/setDefaultCategoryUrl.json")
	@ResponseBody
	public Object setDefaultCategory(
			@RequestParam("itemIds") Long[] itemIds,
			@RequestParam("categoryId") Long categoryId,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		itemCategoryManager.setDefaultCategory(itemIds, categoryId);

		return SUCCESS;
	}
	
	
	@RequestMapping("/item/validateUnBindByItemIdsAndCategoryId.json")
	@ResponseBody
	public Object validateUnBindSel(
			@RequestParam("itemIds") Long[] itemIds,
			@RequestParam("categoryId") Long categoryId,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		boolean flag=
				itemCategoryManager.validateUnBindByItemIdsAndCategoryId(itemIds, categoryId);

		if(!flag){
			//throw new Exception();
			return FAILTRUE;
		}
		return SUCCESS;
	}

	/**
	 * 查询默认分类Id为categoryId的商品
	 * @param categoryId
	 * @return	:Object
	 * @date 2014-2-19 下午04:52:08
	 */
	@RequestMapping("/item/findItemByDefaultCategoryId.json")
	@ResponseBody
	public Object findItemByDefaultCategoryId(@RequestParam("categoryId") Long categoryId){
		List<ItemCategory> itemCategoryList = itemCategoryManager.findItemByDefaultCategoryId(categoryId);
		return itemCategoryList;
	}
}

/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Baozun. You
 * shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Baozun.
 * 
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 */
package com.baozun.nebula.web.controller.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.sdk.command.ItemSortCommand;
import com.baozun.nebula.sdk.manager.SdkItemSortManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 分类商品排序ItemCategorySortController
 * 
 * 2014-2-10 下午2:11:40
 * 
 * @author <a href="xinyuan.guo@baozun.cn">郭馨元</a>
 * 
 */
@Controller
public class ItemCategorySortController extends BaseController {

	@Autowired
	private CategoryManager categoryManager;
	@Autowired
	private SdkItemSortManager sdkItemSortManager;
	@Autowired
	private ShopManager shopManager;

	/**
	 * 显示商品关联分类页面
	 */
	@RequestMapping(value = "/item/itemCategorySort.htm")
	public String itemCategory(Model model) {
		Sort[] sorts = Sort.parse("sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", categoryList);
		//model.addAttribute("shopId", shopManager.getShopId(getUserDetails()));

		return "/product/item/item-category-sort";
	}

	/**
	 * 未排序商品列表
	 */
	@RequestMapping(value = "/item/itemUnsortedList.json")
	@ResponseBody
	public Pagination<ItemSortCommand> findItemCtListJson(@QueryBeanParam QueryBean queryBean) {

        Map<String, Object> paraMap = queryBean.getParaMap();
        List<Long> idList=getChildIdList((Long)paraMap.get("categoryId"));
        paraMap.put("idList", idList);
		Pagination<ItemSortCommand> result = sdkItemSortManager.findItemSortByQueryMapWithPage(queryBean.getPage(),
				queryBean.getSorts(), queryBean.getParaMap());

		return result;
	}

	/**
	 * 已排序商品列表
	 */
	@RequestMapping(value = "/item/itemSortedList.json")
	@ResponseBody
	public Pagination<ItemSortCommand> findSortedItem(@QueryBeanParam QueryBean queryBean) {

		Map<String, Object> paraMap = queryBean.getParaMap();
		paraMap.put("sorted", true);
		List<Long> idList=getChildIdList((Long)paraMap.get("categoryId"));
		paraMap.put("idList", idList);
		Pagination<ItemSortCommand> result = sdkItemSortManager.findItemSortByQueryMapWithPage(queryBean.getPage(),
				queryBean.getSorts(), paraMap);

		return result;
	}
	
	/**
	 * 获取一个节点下所有节点ID
	 * @param categoryId
	 * @return
	 */
	private List<Long> getChildIdList(Long cateId){
	    if(cateId==null){
	        return null;
	    }
        List<Long> idList=new ArrayList<Long>();
	    Long categoryId=Long.valueOf(cateId);
	    List<Category> categories=categoryManager.findEnableCategoryList(null);
	    putChildIdList(categoryId, idList ,categories);
	    return idList;
	}
	
	/**
	 * 查询一个节点下所有节点ID并且放入idList
	 * @param categoryId
	 * @param idList
	 */
	private void putChildIdList(Long categoryId,List<Long> idList,List<Category> categories){
	    idList.add(categoryId);
	    List<Category> categoryList=getCategoryListByParentId(categories,categoryId);
	    if(null==categoryList||categoryList.isEmpty()){
	        return;
	    }
	    for(Category cate:categoryList){
	        putChildIdList(cate.getId(), idList,categories);
	    }
	}

	private List<Category> getCategoryListByParentId(List<Category> categories,Long categoryId){
	    if(categories==null||categories.isEmpty()){
	        return null;
	    }
	    List<Category> result=new ArrayList<Category>();
	    for(Category category:categories){
	        if(categoryId.equals(category.getParentId())){
	            result.add(category);
	        }
	    }
	    categories.removeAll(result);
	    return result;
	}
	
	@RequestMapping(value = "/item/sortUp.json")
	@ResponseBody
	public Object sortUp(Long id,Long categoryId) {
		sdkItemSortManager.upItemSort(id,categoryId);
		return SUCCESS;
	}

	@RequestMapping(value = "/item/sortDown.json")
	@ResponseBody
	public Object sortDown(Long id,Long categoryId) {
		sdkItemSortManager.downItemSort(id,categoryId);
		return SUCCESS;
	}

	@RequestMapping(value = "/item/sortDel.json")
	@ResponseBody
	public Object sortDel(Long id) {
		sdkItemSortManager.removeItemSortById(id);
		return SUCCESS;
	}

	@RequestMapping(value = "/item/sortMutiDel.json")
	@ResponseBody
	public Object sortMutiDel(Long[] ids) {
		sdkItemSortManager.removeItemSortByIds(Arrays.asList(ids));
		return SUCCESS;
	}

	@RequestMapping(value = "/item/sortAdd.json")
	@ResponseBody
	public Object sortAdd(Long[] ids, Long categoryId) {
		sdkItemSortManager.createItemSortByIds(ids, categoryId);
		return SUCCESS;
	}
}

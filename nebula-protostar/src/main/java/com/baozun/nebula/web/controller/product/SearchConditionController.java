/**
 * Copyright (c) 2013 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.product;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.baseinfo.NavigationManager;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.PropertyManager;
import com.baozun.nebula.model.baseinfo.Navigation;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.SearchCondition;
import com.baozun.nebula.model.product.SearchConditionItem;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;
import com.baozun.nebula.sdk.manager.SdkSearchConditionItemManager;
import com.baozun.nebula.sdk.manager.SdkSearchConditionManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.I18nCommand;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

/**
 * SearchConditionController
 * 
 * @author: shiyang.lv
 * @date: 2014年4月16日
 **/
@Controller
public class SearchConditionController extends BaseController {

    @Autowired
    private SdkSearchConditionManager searchConditionManager;
    
    @Autowired
    private SdkSearchConditionItemManager searchConditionItemManager;
    
    @Autowired
    private CategoryManager categoryManager;
    
    @Autowired
    private IndustryManager industryManager;
    
    @Autowired
    private PropertyManager propertyManager;
    
	@Autowired
	private NavigationManager			navigationManager;
    
    
    @RequestMapping("/item/itemSearchCondition/manager.htm")
    public String itemSearchConditionManager(Model model,QueryBean queryBean) {
        
//        Sort[] sorts = Sort.parse("PARENT_ID asc,sort_no asc");
//        List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
//        model.addAttribute("categoryList", categoryList);
        
        List<Navigation> naviList = navigationManager.findAllNavigationList(Navigation.COMMOM_SORTS);
        
        model.addAttribute("naviList", naviList);
        
        model.addAttribute("industryList", industryManager.findAllIndustryList());
        
        return "product/item/searchCondition";
    }
    
    /**
     * 页面跳转 列表页面通过Json获取信息
     * 
     * @param model
     * @param memberId
     * @return
     */
    @RequestMapping("/item/itemSearchCondition/searchConditionList.json")
    @ResponseBody
    public Pagination<SearchConditionCommand> getSearchConditions(Model model,
            @QueryBeanParam QueryBean queryBean) {

        Sort[] sorts = queryBean.getSorts();

        if (sorts == null || sorts.length == 0) {
            Sort sort = new Sort("t1.create_time", "desc");
            sorts = new Sort[1];
            sorts[0] = sort;
        }; 
        
        Pagination<SearchConditionCommand> searchConditions = searchConditionManager
                .findSearchConditionByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
        return searchConditions;
    }
    
    /**
     * 查询行业根据分类ID
     * @param categoryId
     * @return
     */
    @RequestMapping("/item/itemSearchCondition/findIndustryByCategoryId.json")
    @ResponseBody
    public Map<String,Object> findIndustryByCategoryId(Long categoryId){
        Map<String,Object> result=new HashMap<String, Object>();
        result.put("industryList", industryManager.findIndustryByCategoryId(categoryId));
        return result;
    }
    
    /**
     * 查询属性根据分类ID与行业ID
     * @param categoryId
     * @return
     */
    @RequestMapping("/item/itemSearchCondition/findPropertyByCategoryIdAndIndustryId.json")
    @ResponseBody
    public Map<String,Object> findPropertyByCategoryIdAndIndustryId(Long categoryId,Long industryId){
        Map<String,Object> result=new HashMap<String, Object>();
        result.put("propertyList", propertyManager.selectPropertyByCategoryIdAndIndustryId(categoryId, industryId));
        return result;
    }
    
    /**
     * 查询属性根据分类ID与行业ID
     * @param categoryId
     * @return
     */
    @RequestMapping("/item/itemSearchCondition/findPropertyByIndustryId.json")
    @ResponseBody
    public Map<String,Object> findPropertyByIndustryId(Long industryId){
        Map<String,Object> result=new HashMap<String, Object>();
        result.put("propertyList", propertyManager.findPropertyListByIndustryId(industryId));
        return result;
    }
    
    @RequestMapping("/item/itemSearchCondition/endisableSearchCondition.json")
    @ResponseBody
    public void endisableSearchCondition(@RequestParam Long id,@RequestParam Integer activeMark){
        if(activeMark==0){
            searchConditionManager.disableSearchCondition(id);
        }else if(activeMark==1){
            searchConditionManager.enableSearchCondition(id);
        }
    }
    
    @RequestMapping("/item/itemSearchCondition/removeSearchCondition.json")
    @ResponseBody
    public void removeSearchCondition(@RequestParam Long id){
        searchConditionManager.removeSearchCondition(id);
    }
    
    @RequestMapping("/item/itemSearchCondition/removeSearchConditionByIds.json")
    @ResponseBody
    public Map<String,Object> removeSearchConditionByIds(String ids){
        Map<String,Object> map=new HashMap<String, Object>();
        
        String[] array=ids.split(",");
        List<Long> idList=new ArrayList<Long>();
        for(String id:array){
            idList.add(Long.valueOf(id));
        }
        Integer count=searchConditionManager.removeSearchConditionByIds(idList);
        if(count==idList.size()){
            map.put("isSuccess",true);
        }else{
            map.put("isSuccess",false);
        }
        
        return map;
    }
    
    
    @RequestMapping("/item/itemSearchCondition/managerSetting.htm")
    public String itemSearchConditionManagerSetting(Model model,Long pid) {
        model.addAttribute("searchConditionVo", searchConditionManager.findSearchConditionCommandById(pid));
        
        return "product/item/searchConditionItem";
    }
    
    /**
     * 页面跳转 列表页面通过Json获取信息
     * 
     * @param model
     * @param memberId
     * @return
     */
    @RequestMapping("/item/itemSearchCondition/searchConditionItemList.json")
    @ResponseBody
    public Pagination<SearchConditionItemCommand> getSearchConditionItems(Model model,
            @QueryBeanParam QueryBean queryBean) {

        Sort[] sorts = queryBean.getSorts();

        if (sorts == null || sorts.length == 0) {
            Sort sort = new Sort("t1.create_time", "desc");
            sorts = new Sort[1];
            sorts[0] = sort;
        }; 
        
        Pagination<SearchConditionItemCommand> searchConditions = searchConditionItemManager
                .findSearchConditionItemByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
        return searchConditions;
    }
    
    @RequestMapping("/item/itemSearchCondition/endisableSearchConditionItem.json")
    @ResponseBody
    public void endisableSearchConditionItem(@RequestParam Long id,@RequestParam Integer activeMark){
        if(activeMark==0){
            searchConditionItemManager.disableSearchConditionItem(id);
        }else if(activeMark==1){
            searchConditionItemManager.enableSearchConditionItem(id);
        }
    }
    
    @RequestMapping("/item/itemSearchCondition/removeSearchConditionItem.json")
    @ResponseBody
    public void removeSearchConditionItem(@RequestParam Long id){
        searchConditionItemManager.removeSearchConditionItemById(id);
    }
    
    @RequestMapping("/item/itemSearchCondition/removeSearchConditionItemByIds.json")
    @ResponseBody
    public Map<String,Object> removeSearchConditionItemByIds(String ids){
        Map<String,Object> map=new HashMap<String, Object>();
        
        String[] array=ids.split(",");
        List<Long> idList=new ArrayList<Long>();
        for(String id:array){
            idList.add(Long.valueOf(id));
        }
        Integer count=searchConditionItemManager.removeSearchConditionItemByIds(idList);
        
        if(count==idList.size()){
            map.put("isSuccess",true);
        }else{
            throw new BusinessException(ErrorCodes.SEARCH_CODITION_DELETE_FAIL);
        }
        
        return map;
    }
    
    
    @RequestMapping("/item/itemSearchCondition/managerAddCondition.htm")
    public String itemSearchConditionManagerAdd(Model model,Long id) {
        
//        Sort[] sorts = Sort.parse("PARENT_ID asc,sort_no asc");
//        List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
//        model.addAttribute("categoryList", categoryList);
        List<Navigation> naviList = navigationManager.findAllNavigationList(Navigation.COMMOM_SORTS);
        model.addAttribute("naviList", naviList);
        model.addAttribute("industryList", industryManager.findAllIndustryList());
        
        if(id!=null){
            SearchConditionCommand searchConditionVo= searchConditionManager.findSearchConditionCommandById(id);
            model.addAttribute("searchConditionVo",searchConditionVo);
            if(null==searchConditionVo){
                throw new BusinessException(ErrorCodes.SEARCH_CODITION_NOT_EXIST);
            }
            return "product/item/editSearchCondition";
        }
        
        return "product/item/addSearchCondition";
    }
    
    @RequestMapping("/i18n/item/itemSearchCondition/managerAddCondition.htm")
    public String itemSearchConditionManagerAddI18n(Model model,Long id) {
        
//        Sort[] sorts = Sort.parse("PARENT_ID asc,sort_no asc");
//        List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
//        model.addAttribute("categoryList", categoryList);
    	
    	 List<Navigation> naviList = navigationManager.findAllNavigationList(Navigation.COMMOM_SORTS);
         model.addAttribute("naviList", naviList);
    	
        model.addAttribute("industryList", industryManager.findAllIndustryList());
        
        if(id != null){
            com.baozun.nebula.command.product.SearchConditionCommand searchConditionVo 
            = searchConditionManager.findSearchConditionCommandI18nById(id);
            model.addAttribute("searchConditionVo",searchConditionVo);
            if(null==searchConditionVo){
                throw new BusinessException(ErrorCodes.SEARCH_CODITION_NOT_EXIST);
            }
            return "product/item/editSearchCondition";
        }
        
        return "product/item/addSearchCondition";
    }
    
    @RequestMapping("/item/itemSearchCondition/managerSaveCondition.htm")
    public String itemSearchConditionManagerSave(Model model,SearchCondition condition) {
        searchConditionManager.createOrUpdateSearchCondition(condition);
        
        return "redirect:/item/itemSearchCondition/manager.htm?keepfilter=true";
    }
    
    @RequestMapping("/i18n/item/itemSearchCondition/managerSaveCondition.htm")
    public String itemSearchConditionManagerSaveI18n(Model model,@I18nCommand com.baozun.nebula.command.product.SearchConditionCommand condition) {
        searchConditionManager.createOrUpdateSearchCondition(condition);
        
        return "redirect:/item/itemSearchCondition/manager.htm?keepfilter=true";
    }

    @RequestMapping("/item/itemSearchCondition/managerAddConditionItem.htm")
    public String itemSearchConditionManagerAddItem(Model model,@RequestParam Long pid,Long id) {
        
        SearchConditionCommand searchConditionVo = searchConditionManager.findSearchConditionCommandById(pid);
        if(null==searchConditionVo){
            throw new BusinessException(ErrorCodes.SEARCH_CODITION_NOT_EXIST);
        }
        model.addAttribute("searchConditionVo",searchConditionVo);
        
        if(searchConditionVo.getPropertyId()!=null){
            List<PropertyValue> propertyValueList=propertyManager.findPropertyValueList(searchConditionVo.getPropertyId());
            model.addAttribute("propertyValueList",propertyValueList);
        }
        
        if(id!=null){
            SearchConditionItemCommand searchConditionItemVo=searchConditionItemManager.findSearchConditionItemCommandById(id);
            if(null==searchConditionItemVo){
                throw new BusinessException(ErrorCodes.SEARCH_CODITION_ITEM_NOT_EXIST);
            }
            model.addAttribute("searchConditionItemVo",searchConditionItemVo);
            
            return "product/item/editSearchConditionItem";
        }
        
        return "product/item/addSearchConditionItem";
    }
    
    @RequestMapping(value="/item/itemSearchCondition/managerSaveConditionItem.htm", method=RequestMethod.POST)
    public String managerSaveConditionItem(Model model,SearchConditionItem searchConditionItem,@RequestParam Long pid) {
        
        searchConditionItemManager.createOrUpdateConditionItem(searchConditionItem);
        
        return "redirect:/item/itemSearchCondition/managerSetting.htm?pid="+pid+"&keepfilter=true";
    }
    
    
    @RequestMapping(value="/i18n/item/itemSearchCondition/managerSaveConditionItem.htm", method=RequestMethod.POST)
    public String managerSaveConditionItemI18n(Model model,@I18nCommand com.baozun.nebula.command.product.SearchConditionItemCommand searchConditionItem,@RequestParam Long pid) {
        
        searchConditionItemManager.createOrUpdateConditionItem(searchConditionItem);
        
        return "redirect:/item/itemSearchCondition/managerSetting.htm?pid="+pid+"&keepfilter=true";
    }
    
    @RequestMapping("/i18n/item/itemSearchCondition/managerAddConditionItem.htm")
    public String itemSearchConditionManagerAddItemI18n(Model model,@RequestParam Long pid,Long id) {
        
        SearchConditionCommand searchConditionVo = searchConditionManager.findSearchConditionCommandById(pid);
        if(null==searchConditionVo){
            throw new BusinessException(ErrorCodes.SEARCH_CODITION_NOT_EXIST);
        }
        model.addAttribute("searchConditionVo",searchConditionVo);
        
        if(searchConditionVo.getPropertyId()!=null){
            List<PropertyValue> propertyValueList=propertyManager.findPropertyValueList(searchConditionVo.getPropertyId());
            model.addAttribute("propertyValueList",propertyValueList);
        }
        
        if(id != null){
            com.baozun.nebula.command.product.SearchConditionItemCommand searchConditionItemVo 
            = searchConditionItemManager.findSearchConditionItemCommandI18nById(id);
            if(null==searchConditionItemVo){
                throw new BusinessException(ErrorCodes.SEARCH_CODITION_ITEM_NOT_EXIST);
            }
            model.addAttribute("searchConditionItemVo",searchConditionItemVo);
            
            return "product/item/editSearchConditionItem";
        }
        
        return "product/item/addSearchConditionItem";
    }
}

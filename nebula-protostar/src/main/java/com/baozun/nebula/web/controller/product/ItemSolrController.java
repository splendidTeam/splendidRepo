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
import java.util.Arrays;
import java.util.List;

import loxia.dao.Pagination;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.ItemManager;
import com.baozun.nebula.manager.product.ItemSolrSettingManager;
import com.baozun.nebula.solr.manager.ItemSolrManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * SolrController
 * 
 * @author: shiyang.lv
 * @date: 2014年4月29日
 **/
@Controller
public class ItemSolrController extends BaseController{
    
    	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ItemSolrController.class);
    
    @Autowired
    private ItemSolrSettingManager 	itemSolrSettingManager;
    
    @Autowired
    private ItemSolrManager 		itemSolrManager;
    
    @Autowired
    private ShopManager 			shopManager;
    
    @Autowired
	private ItemManager				itemManager;
    
    /**
     * 进入页面
     * @param model
     * @return
     */
    @RequestMapping("/item/itemSolrSetting/manager.htm")
    public String itemSearchConditionManager(Model model) {
        //itemSolrManager.reRefreshAllItem();
        
        return "product/item/itemSolrSetting";
    }
    
    /**
     * 分页查询列表
     * @param model
     * @param queryBean
     * @return
     */
    @RequestMapping("/item/itemSolrSetting/searchItemList.json")
    @ResponseBody
    public Pagination<ItemCommand> getItems(Model model,
            @QueryBeanParam QueryBean queryBean) {
        // 查询orgId
        UserDetails userDetails = this.getUserDetails();
        ShopCommand shopCommand = null;
        Long shopId = 0L;

        Long currentOrgId = userDetails.getCurrentOrganizationId();
        // 根据orgId查询shopId
        if (currentOrgId != null){
            shopCommand = shopManager.findShopByOrgId(currentOrgId);
            if (shopCommand != null){
                shopId = shopCommand.getShopid();
            }
        }
        
        Pagination<ItemCommand> result=itemSolrSettingManager.findItemListByQueryMap(queryBean.getPage(),null, queryBean.getParaMap(), shopId);
        return result;
    }
    
    /**
     * 批量重建与删除
     * @param model
     * @param ids
     * @param type
     * @return
     */
    @RequestMapping("/item/itemSolrSetting/changeByIds.json")
    @ResponseBody
    public Object changeByIds(Model model,String ids,String type) {
        
        String[] array=ids.split(",");
        List<Long> idList=new ArrayList<Long>();
        for(String id:array){
            idList.add(Long.valueOf(id));
        }
        
        if("update".equals(type)){
        	boolean i18n = LangProperty.getI18nOnOff();
        	if(i18n){
        		 if(!itemSolrManager.saveOrUpdateItemI18n(idList)){
                     throw new BusinessException(ErrorCodes.SOLR_SETTING_DELETE_FAIL);
                 }
        	}else{
        		 if(!itemSolrManager.saveOrUpdateItem(idList)){
                     throw new BusinessException(ErrorCodes.SOLR_SETTING_DELETE_FAIL);
                 }
        	}
        }else if("delete".equals(type)){
            if(!itemSolrManager.deleteItem(idList)){
                throw new BusinessException(ErrorCodes.SOLR_SETTING_UPDATE_FAIL);
            }
        }
        
        return BaseController.SUCCESS;
    }
    
    /**
     * 当前店铺下删除与重建所有
     * @param model
     * @param type
     * @return
     */
    @RequestMapping("/item/itemSolrSetting/changeAll.json")
    @ResponseBody
    public Object changeAll(Model model,String type) {
     // 查询orgId
        UserDetails userDetails = this.getUserDetails();
        ShopCommand shopCommand = null;
        Long shopId = 0L;

        Long currentOrgId = userDetails.getCurrentOrganizationId();
        // 根据orgId查询shopId
        if (currentOrgId != null){
            shopCommand = shopManager.findShopByOrgId(currentOrgId);
            if (shopCommand != null){
                shopId = shopCommand.getShopid();
            }
        }
        
        if("update".equals(type)){
            if(!itemSolrSettingManager.reRefreshAll(shopId)){
                throw new BusinessException(ErrorCodes.SOLR_SETTING_UPDATE_FAIL);
            }
        }else if("delete".equals(type)){
            if(!itemSolrSettingManager.deleteAll(shopId)){
                throw new BusinessException(ErrorCodes.SOLR_SETTING_DELETE_FAIL);
            }
        }
        
        return BaseController.SUCCESS;
    }
    
    	/**
	 * 批量操作中的批量更新
	 * @param itemCodes
	 * @return
	 */
	@RequestMapping(value = "/item/solr/index/batchUpdateItemSolrIndex.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity batchUpdateItemSolrIndex(@RequestParam("itemCodes") String itemCodes) {
		BackWarnEntity result = new BackWarnEntity(Boolean.TRUE, "");
		
		if(StringUtils.isBlank(itemCodes)){
			throw new BusinessException(ErrorCodes.ITEM_CODE_LIST_IS_NULL);
		}
		
		Long shopId = shopManager.getShopId(getUserDetails());
		
		List<String> itemCodeList = Arrays.asList(StringUtils.split(itemCodes));
		Boolean isSuccess =itemSolrSettingManager.batchOperationItemSolrIndex(itemCodeList, shopId, 1);
		
		if(isSuccess != null && isSuccess){
			return result;
		}else{
			throw new BusinessException(ErrorCodes.SOLR_SETTING_UPDATE_FAIL);
		}
	}

	/**
	 * 批量操作中的批量删除
	 * @param itemCodes
	 * @return
	 */
	@RequestMapping(value = "/item/solr/index/batchDeteleItemSolrIndex.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity batchDeteleItemSolrIndex(@RequestParam("itemCodes") String itemCodes) {
		BackWarnEntity result = new BackWarnEntity(Boolean.TRUE, "");
		if(StringUtils.isBlank(itemCodes)){
			throw new BusinessException(ErrorCodes.ITEM_CODE_LIST_IS_NULL);
		}
		
		Long shopId = shopManager.getShopId(getUserDetails());
		
		List<String> itemCodeList = Arrays.asList(StringUtils.split(itemCodes));
		Boolean isSuccess =itemSolrSettingManager.batchOperationItemSolrIndex(itemCodeList, shopId, 0);
		
		if(isSuccess != null && isSuccess){
			return result;
		}else{
			throw new BusinessException(ErrorCodes.SOLR_SETTING_DELETE_FAIL);
		}
	}
    
}


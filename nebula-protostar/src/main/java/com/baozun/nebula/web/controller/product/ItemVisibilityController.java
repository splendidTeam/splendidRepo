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
package com.baozun.nebula.web.controller.product;


import java.util.Arrays;
import java.util.List;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.product.ItemVisibilityCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.product.ItemVisibility;
import com.baozun.nebula.sdk.manager.SdkItemVisibilityManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;
/**
 * ItemVisibilityController
 * @author  何波
 *
 */
 @Controller
public class ItemVisibilityController extends BaseController{


	@Autowired
	private SdkItemVisibilityManager itemVisibilityManager;
	
	@RequestMapping("/itemVisibility/list.htm")
	public String list(){
		return "/product/item/itemVisibility";
	}
	/**
	 * 保存ItemVisibility
	 * 
	 */
	@RequestMapping("/itemVisibility/save.json")
	@ResponseBody
	public BackWarnEntity saveItemVisibility(ItemVisibility model){
		BackWarnEntity  back = new BackWarnEntity();
		try {
			 itemVisibilityManager.saveItemVisibility(model);
			 back.setIsSuccess(true);
			 return back;
		} catch (BusinessException e) {
			back.setIsSuccess(false);
			back.setDescription(e.getMessage());
			return back;
		}
			
	}
	
	/**
	 * 通过id获取ItemVisibility
	 * 
	 */
	@RequestMapping("/itemVisibility/findByid.json")
	@ResponseBody
	public ItemVisibilityCommand findItemVisibilityById(Long id){
	
		return itemVisibilityManager.findItemVisibilityCommandbyId(id);
	}
	

	/**
	 * 获取所有ItemVisibility列表
	 * @return
	 */
	@RequestMapping("/itemVisibility/findAll.json")
	@ResponseBody
	public List<ItemVisibility> findAllItemVisibilityList(){
	
		return itemVisibilityManager.findAllItemVisibilityList();
	};
	
	/**
	 * 通过ids获取ItemVisibility列表
	 * @param ids
	 * @return
	 */
	@RequestMapping("/itemVisibility/findByIds.json")
	@ResponseBody
	public List<ItemVisibility> findItemVisibilityListByIds(Long[] ids){
	
		return itemVisibilityManager.findItemVisibilityListByIds(Arrays.asList(ids));
	};
	
	/**
	* @Description: 分页获取ItemVisibility列表
	* @param queryBean
	* @return   
	* Pagination<ItemVisibility>
	* @throws
	 */
	@RequestMapping("/itemVisibility/page.json")
	@ResponseBody
	public Pagination<ItemVisibilityCommand> findItemVisibilityListByQueryMapWithPage(@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		return  itemVisibilityManager.findItemVisibilityListByQueryMapWithPage(queryBean.getPage(),
				sorts, queryBean.getParaMap());
	}
	/**
	 * 通过ids批量启用或禁用ItemVisibility
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@RequestMapping("/itemVisibility/enableOrDisableByIds.json")
	@ResponseBody
	public BackWarnEntity  enableOrDisableItemVisibilityByIds(Long[] ids,Integer state) {
		itemVisibilityManager.enableOrDisableItemVisibilityByIds(Arrays.asList(ids), state);
		return SUCCESS;
	}
	
	/**
	 * 通过ids批量删除ItemVisibility
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@RequestMapping("/itemVisibility/removeByIds.json")
	@ResponseBody
	public BackWarnEntity removeItemVisibilityByIds(Long[] ids){
		itemVisibilityManager.removeItemVisibilityByIds(Arrays.asList(ids));
		return SUCCESS;
	}
}

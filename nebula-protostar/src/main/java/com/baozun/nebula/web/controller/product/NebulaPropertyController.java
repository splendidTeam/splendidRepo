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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.PropertyCommand;
import com.baozun.nebula.command.product.CommonPropertyCommand;
import com.baozun.nebula.command.product.PropertyValueCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.ItemManager;
import com.baozun.nebula.manager.product.PropertyManager;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.PropertyValueLang;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.ArrayCommand;
import com.baozun.nebula.web.bind.I18nCommand;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 商品属性controller
 * 
 * @author lin.liu
 */
@Controller
public class NebulaPropertyController extends BaseController{

	private static final Logger	log	= LoggerFactory.getLogger(NebulaPropertyController.class);

	@Autowired
	private PropertyManager		propertyManager;

	@Autowired
	private IndustryManager		industryManager;	
	
	@Autowired
	private ItemManager			itemManager;
	
	
	/**
	 * 新的商品属性管理
	 * @return String
	 * @param model
	 * @author 冯明雷
	 * @time 2016年4月6日下午4:33:54
	 */
	@RequestMapping("/property/nebulaPropertyList.htm")
	public String propertyManager(Model model){
		return "product/property/nebula-property-list";
	}
	
	
	/**
	 * 新增商品属性
	 * @return String
	 * @param model
	 * @author 冯明雷
	 * @time 2016年4月6日下午4:33:54
	 */
	@RequestMapping("/property/nebulaCreateProperty.htm")
	public String createProperty(Model model){
		return "product/property/nebula-add-property";
	}
	
	
	/**
	 * 访问属性列表数据
	 * 
	 * @param model
	 * @param queryBean
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/property/nebulaPropertyList.json")
	@ResponseBody
	public Pagination<PropertyCommand> findProductPropertylist(
			Model model,
			@QueryBeanParam QueryBean queryBean,
			HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> paraMap = queryBean.getParaMap();
		if (null == paraMap){
			paraMap = new HashMap<String, Object>();
			queryBean.setParaMap(paraMap);
		}
		paraMap.put("isSystem", true);
		Sort[] paraSorts = queryBean.getSorts();
		if (null == paraSorts || paraSorts.length == 0){
			Sort sort = new Sort("tpp.sort_no", "asc");
			paraSorts = new Sort[1];
			paraSorts[0] = sort;
			queryBean.setSorts(paraSorts);
		}

		Pagination<PropertyCommand> args = propertyManager.findPropertyListByQueryMapWithPage(queryBean.getPage(), paraSorts, paraMap);
		return args;
	}
	
	
		
	/**
	 * 批量删除属性
	 * @return Object
	 * @param propertyIds
	 * @author 冯明雷
	 * @time 2016年4月7日下午4:32:04
	 */
	@ResponseBody
	@RequestMapping("/property/nebulaRemoveProperty.json")
	public Object removePropertyByIds(@RequestParam String propertyIds){
		String[] strIds = propertyIds.split(",");
		List<Long> ids = new ArrayList<Long>();
		if (strIds.length > 0){
			for (int i = 0; i < strIds.length; i++){
				Long propertyId = Long.valueOf(strIds[i].toString());
				ids.add(propertyId);
			}
		}else{
			ids.add(Long.valueOf(propertyIds.toString()));
		}
		
		
		//查询属性是否被商品引用，如果引用就不能删除
		boolean flag=false;
		for (Long propertyId : ids){
			Integer count = itemManager.findItemCountByPropertyId(propertyId);
			if (count > 0){
				flag=true;
				break;
			}
		}
		
		if(flag){
			throw new BusinessException(ErrorCodes.PRODUCT_PROPERTY_DELETION_FAIL);
		}
		
		Integer result = propertyManager.removePropertyByIds(ids);
		if (result < 1){
			throw new BusinessException(ErrorCodes.PRODUCT_PROPERTY_DELETION_FAIL);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 更改属性状态
	 * @return Object
	 * @param propertyId
	 * @param state
	 * @author 冯明雷
	 * @time 2016年4月7日下午4:32:26
	 */
	@ResponseBody
	@RequestMapping("/property/nebulaEnableOrDisableproperty.json")
	public Object enableOrDisablePropertyByIds(@RequestParam Long propertyId,@RequestParam Integer state){
		List<Long> ids = new ArrayList<Long>();
		ids.add(propertyId);
		
		//如果state是禁用状态，代表是禁用操作，需要判断是否被商品引用
		if(Property.LIFECYCLE_DISABLE.equals(state)){
			Integer count = itemManager.findItemCountByPropertyId(propertyId);
			if (count > 0){
				throw new BusinessException(ErrorCodes.PRODUCT_PROPERTY_DISABLED_QUOTE);
			}
		}
		
		Integer result = propertyManager.enableOrDisablePropertyByIds(ids, state);
		if (result < 1){
			if (state != 1){
				throw new BusinessException(ErrorCodes.PRODUCT_PROPERTY_DISABLED_FAIL);
			}else{
				throw new BusinessException(ErrorCodes.PRODUCT_PROPERTY__ENABLE_FAIL);
			}
		}
		return SUCCESS;
	}
	
	
	
	
	
}

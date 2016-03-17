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
public class PropertyController extends BaseController{

	private static final Logger	log	= LoggerFactory.getLogger(PropertyController.class);

	@Autowired
	private PropertyManager		propertyManager;

	@Autowired
	private IndustryManager		industryManager;

	/**
	 * 页面跳转 属性管理列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/property/propertyList.htm")
	public String propertyManager(Model model){
		List<Industry> result = industryManager.findAllIndustryList();
		model.addAttribute("industrylist", result);
		return "product/property/property-list";
	}

	/**
	 * 页面跳转 新增属性
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/property/createProperty.htm")
	public String createProperty(Model model){
		List<Industry> list = industryManager.findAllIndustryList();
		model.addAttribute("industryList", list);
		return "product/property/add-property";
	}

	/**
	 * 页面跳转 修改属性
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/property/updateProperty.htm")
	public String updatePropery(
			Model model,
			@RequestParam("industryId") Long industryId,
			@RequestParam(required = false,value = "properyId") Long propertyId){
		Industry industry = industryManager.findIndustryById(industryId);
		if (industry != null){
			model.addAttribute("industryName", industry.getName());
		}
		List<CommonPropertyCommand> commonPropertyList = propertyManager.findAllCommonPropertyByindustryId(industryId);
		model.addAttribute("commonPropertyList", commonPropertyList);
		model.addAttribute("industryId", industryId);
		model.addAttribute("propertyId", propertyId);
		return "product/property/update-property";
	}

	/**
	 * 下一步按钮
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/property/propertyListByIndustryid.json")
	@ResponseBody
	public Object findPropertyListByIndustryId(Model model,@RequestParam("industryId") Long industryId){
		List<Property> list = propertyManager.findPropertyListByIndustryId(industryId);
		SUCCESS.setDescription(list);
		return SUCCESS;
	}

	/**
	 * 下一步按钮
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/i18n/property/propertyListByIndustryid.json")
	@ResponseBody
	public Object findPropertyListByIndustryIdI18n(Model model,@RequestParam("industryId") Long industryId){
		List<com.baozun.nebula.command.product.PropertyCommand> list = propertyManager.findPropertyCommandListByIndustryId(industryId);
		SUCCESS.setDescription(list);
		return SUCCESS;
	}

	/**
	 * 获取通用属性表信息
	 * 
	 * @param model
	 * @param industryId
	 * @return
	 */
	@RequestMapping("/i18n/property/commonpropertyListByIndustryid.json")
	@ResponseBody
	public List<CommonPropertyCommand> findcommonPropertyListByIndustryIdI18n(Model model,@RequestParam("industryId") Long industryId){
		List<CommonPropertyCommand> commonPropertyList = propertyManager.findAllCommonPropertyByindustryId(industryId);
		return commonPropertyList;
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
	@RequestMapping("/property/propertyList.json")
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
	 * 
	 * @param propertyIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/property/removeProperty.json")
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
		Integer result = propertyManager.removePropertyByIds(ids);
		if (result < 1){
			throw new BusinessException(ErrorCodes.PRODUCT_PROPERTY_DELETION_FAIL);
		}
		return SUCCESS;
	}

	/**
	 * 更改属性状态
	 * 
	 * @param propertyId
	 * @param state
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/property/enableOrDisableproperty.json")
	public Object enableOrDisablePropertyByIds(@RequestParam Long propertyId,@RequestParam Integer state){
		List<Long> ids = new ArrayList<Long>();
		ids.add(propertyId);
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

	/**
	 * 显示商品属性管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/property/propertyValueList.json")
	public String findPropertyValueList(@RequestParam("propertyId") Long propertyId,Model model,HttpServletRequest request){
		request.getSession().setAttribute("propertyId", propertyId);
		Property property = propertyManager.findPropertyById(propertyId);
		model.addAttribute("propertyName", property.getName());
		Industry industry = industryManager.findIndustryById(property.getIndustryId());
		model.addAttribute("industryId", industry.getId());
		model.addAttribute("industryName", industry.getName());
		List<PropertyValue> propertyValue = new ArrayList<PropertyValue>();
		propertyValue = propertyManager.findPropertyValueList(propertyId);
		model.addAttribute("propertyValue", propertyValue);
		return "/product/industry/industry-property-value";

	}

	/**
	 * 显示商品属性管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/i18n/property/propertyValueList.json")
	public String findPropertyValueListI18n(@RequestParam("propertyId") Long propertyId,Model model,HttpServletRequest request){
		request.getSession().setAttribute("propertyId", propertyId);
		Property property = propertyManager.findPropertyById(propertyId);
		model.addAttribute("propertyName", property.getName());
		Industry industry = industryManager.findIndustryById(property.getIndustryId());
		model.addAttribute("industryId", industry.getId());
		model.addAttribute("industryName", industry.getName());
		List<PropertyValueCommand> propertyValue = propertyManager.findPropertyValuecCommandList(propertyId);
		model.addAttribute("propertyValue", propertyValue);
		List<PropertyValue> propertyValueList = propertyManager.findPropertyValueListBycommonPropertyId(propertyId);
		model.addAttribute("propertyValueList", propertyValueList);
		return "/product/industry/industry-property-value";

	}

	/**
	 * 新增或修改商品属性列表
	 * 
	 * @param propertyValues
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/property/savePropertyValue.json",method = RequestMethod.POST)
	@ResponseBody
	public Object savePropertyValueByList(
			@ArrayCommand() PropertyValue[] propertyValues,
			@RequestParam("propertyId") Long propertyId,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response){

		// Long propertyId = (Long) request.getSession().getAttribute("propertyId");
		model.addAttribute("propertyId", propertyId);

		/*
		 * if(propertyValues==null||propertyValues.length==0){ propertyValues=new PropertyValue[1]; Calendar calendar =
		 * Calendar.getInstance(); Date date = calendar.getTime(); PropertyValue propertyValue=new PropertyValue();
		 * propertyValue.setModifyTime(date); propertyValue.setCreateTime(date); propertyValue.setPropertyId(propertyId);
		 * propertyValues[0]=propertyValue; }
		 */

		propertyManager.createOrUpdatePropertyValueByList(propertyValues, propertyId);
		return SUCCESS;
	}

	@RequestMapping(value = "/i18n/property/savePropertyValue.json",method = RequestMethod.POST)
	@ResponseBody
	public Object savePropertyValueByListI18n(
			@I18nCommand PropertyValueCommand[] propertyValues,
			@RequestParam("propertyId") Long propertyId,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response){
		model.addAttribute("propertyId", propertyId);
		propertyManager.createOrUpdatePropertyValueByList(propertyValues, propertyId);
		return SUCCESS;
	}

	/**
	 * 新增属性或者修改属性
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/property/saveProperty.Json")
	@ResponseBody
	public BackWarnEntity saveProperty(Model model,@ModelAttribute Property property){
		boolean isSuccess = true;
		Map<String, Object> result = new HashMap<String, Object>();
		Property newProperty = new Property();
		if (property.getId() != null && property.getId() != -1){
			newProperty = propertyManager.findPropertyById(property.getId());
			newProperty.setModifyTime(new Date());
		}else{
			newProperty.setCreateTime(new Date());
			newProperty.setIsCommonIndustry(true);
		}
		newProperty.setEditingType(property.getEditingType());
		newProperty.setHasThumb(property.getHasThumb());
		newProperty.setIndustryId(property.getIndustryId());
		newProperty.setIsColorProp(property.getIsColorProp());
		newProperty.setIsSaleProp(property.getIsSaleProp());
		newProperty.setName(property.getName());
		newProperty.setRequired(property.getRequired());
		newProperty.setSearchable(property.getSearchable());
		newProperty.setSortNo(property.getSortNo());
		newProperty.setValueType(property.getValueType());
		newProperty.setLifecycle(property.getLifecycle());
		newProperty.setGroupName(property.getGroupName());
		isSuccess = propertyManager.createOrUpdateProperty(newProperty);
		if (!isSuccess){
			throw new BusinessException(ErrorCodes.PROPERTY_SAVE_FAIL);
		}
		SUCCESS.setDescription(newProperty);
		return SUCCESS;
	}

	@RequestMapping("/i18n/property/saveProperty.Json")
	@ResponseBody
	public BackWarnEntity savePropertyI18n(Model model,@I18nCommand com.baozun.nebula.command.product.PropertyCommand property){
		com.baozun.nebula.command.product.PropertyCommand newProperty = propertyManager.createOrUpdateProperty(property);
		if (newProperty == null){
			throw new BusinessException(ErrorCodes.PROPERTY_SAVE_FAIL);
		}
		BackWarnEntity back = new BackWarnEntity();
		back.setIsSuccess(true);
		back.setDescription(newProperty);
		return back;
	}

	/**
	 * 属性排序
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/property/updatePropertyBylist.json")
	@ResponseBody
	public BackWarnEntity updatePropertyByParamList(Model model,@RequestParam("ids") String ids){
		boolean isSuccess = true;
		isSuccess = propertyManager.updatePropertyByParamList(ids);
		if (!isSuccess){
			throw new BusinessException(ErrorCodes.PROPERTY_SORT_FAIL);
		}
		return SUCCESS;
	}

	@RequestMapping("/property/getPropertyByid.json")
	@ResponseBody
	public Property getPropertyByid(Long id){
		try{
			return propertyManager.findPropertyById(id);
		}catch (Exception e){
			log.error("获取属性失败", e);
			return null;
		}
	}

	/**
	 * 验证属性是否重复(属性名称不能重复,其关联的公共属性也不能重复)
	 * 
	 * @param industryId
	 *            行业Id
	 * @param commonPropertyId
	 *            公共属性Id
	 * @param propertyId
	 *            属性Id
	 * @param proname
	 *            属性名
	 * @return
	 */
	@RequestMapping("/property/validatePropertyData.json")
	@ResponseBody
	public Map<String, Object> validatePropertyData(Long industryId,Long commonPropertyId,Long propertyId,String proname){
		try{
			return propertyManager.validatePropertyData(industryId, commonPropertyId, propertyId, proname);
		}catch (Exception e){
			log.error("获取属性失败", e);
			return null;
		}
	}

	/**
	 * 验证通用属性名是否存在(true:不存在且验证通过)
	 * 
	 * @param commonPropertyName
	 * @return
	 */
	@RequestMapping("/property/validatecommonPropertyName.json")
	@ResponseBody
	public boolean validatecommonPropertyName(String commonPropertyName){
		return propertyManager.validatecommonPropertyName(commonPropertyName);
	}
	
	/**
	 * 选择属性值设值内容
	 * 
	 * @param commonPropertyId
	 * @return
	 */
	@RequestMapping("/i18n/property/selectPropertyValue.json")
	@ResponseBody
	public BackWarnEntity selectPropertyValue(String propertyValueId){
		List<PropertyValueLang> propertyValueCommandList = propertyManager.findPropertyValueCommandById(Long.valueOf(propertyValueId));
		SUCCESS.setDescription(propertyValueCommandList);
		return SUCCESS;
	}
}

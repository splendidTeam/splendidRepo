/**
 * 
 */
package com.baozun.nebula.web.controller.product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.manager.product.PropertyManager;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValueGroup;
import com.baozun.nebula.sdk.manager.product.SdkPropertyManager;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 商品属性值控制器 <br/>
 * 实现以下功能点
 * <ol>
 * <li>根据属性创建属性值（i18n）</li>
 * <li>“属性值分组”创建，修改</li>
 * <li>“属性值”和“属性值分组”的关联</li>
 * <li>“属性值分组”下面属性值的排序</li>
 * <li>属性值 导入导出功能</li>
 * </ol>
 * 
 * @author viktor huang
 * @date Apr 6, 2016 5:39:53 PM
 */
@Controller
public class NebulaPropertyValueController extends BaseController{

	@SuppressWarnings("unused")
	private static final Logger	LOGGER	= LoggerFactory.getLogger(NebulaPropertyValueController.class);

	@Autowired
	private PropertyManager		propertyManager;

	@Autowired
	private SdkPropertyManager	sdkPropertyManager;

	/**
	 * 进入属性值编辑页面 ， 渲染选择属性下的属性值分组和属性值
	 * 
	 * @param request
	 * @param model
	 * @param propertyId
	 *            属性id
	 * @return
	 */
	@RequestMapping(value = "/i18n/property/editPropertyValue.htm")
	public String showEditPropertyValue(
			HttpServletRequest request,
			Model model,
			@RequestParam(value = "propertyId",required = true) Long propertyId){
		// request.getSession().setAttribute("propertyId", propertyId);

		Property property = propertyManager.findPropertyById(propertyId);
		// 设置属性组分组只针对于属性类型为‘多选’类型
		if (property.getEditingType().equals(Property.EDITING_TYPE_MULTI_SELECT)){
			List<PropertyValueGroup> propertyValueGroupList = sdkPropertyManager.findProValueGroupByPropertyId(propertyId);
		}

		// model.addAttribute("propertyName", property.getName());
		// Industry industry = industryManager.findIndustryById(property.getIndustryId());
		// model.addAttribute("industryId", industry.getId());
		// model.addAttribute("industryName", industry.getName());
		// List<PropertyValueCommand> propertyValue = propertyManager.findPropertyValuecCommandList(propertyId);
		// model.addAttribute("propertyValue", propertyValue);
		// List<PropertyValue> propertyValueList = propertyManager.findPropertyValueListBycommonPropertyId(propertyId);
		// model.addAttribute("propertyValueList", propertyValueList);
		return "/product/property/edit-property-value";

	}

	@RequestMapping(value = "/i18n/property/showAddGroup.htm")
	public String showAddGroup(HttpServletRequest request,Model model,@RequestParam(value = "propertyId",required = true) Long propertyId){
		
		return "/product/property/add-property-value-group";
	}
}

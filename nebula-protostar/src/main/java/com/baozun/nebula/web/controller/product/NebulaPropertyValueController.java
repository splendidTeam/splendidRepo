/**
 * 
 */
package com.baozun.nebula.web.controller.product;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.manager.product.PropertyManager;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.PropertyValueGroup;
import com.baozun.nebula.sdk.manager.product.SdkPropertyManager;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;

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
		model.addAttribute("property", property);
		// 设置属性组分组只针对于属性类型为‘多选’类型
		if (property.getEditingType().equals(Property.EDITING_TYPE_MULTI_SELECT)){
			List<PropertyValueGroup> propertyValueGroupList = sdkPropertyManager.findProValueGroupByPropertyId(propertyId);
			model.addAttribute("propertyValueGroupList", propertyValueGroupList);
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

	/**
	 * 渲染进入修改或者新增属性值组页面
	 * 
	 * @param request
	 * @param model
	 * @param type
	 *            操作类型（0=new || 1=update）
	 * @param groupId
	 *            属性值组id
	 * @param propertyId
	 *            对应的属性
	 * @return
	 */
	@RequestMapping(value = "/i18n/property/showAddOrUpdateGroup.htm")
	public String showAddOrUpdateGroup(
			HttpServletRequest request,
			Model model,
			@RequestParam(value = "type",required = true) int type,
			@RequestParam(value = "groupId",required = false) Long groupId,
			@RequestParam(value = "propertyId",required = true) Long propertyId){

		if (type == 0){
			// 新建
			List<PropertyValue> propertyValueList = propertyManager.findPropertyValueList(propertyId);
			// 没有与属性值组绑定的属性值
			model.addAttribute("freeProValueList", propertyValueList);
		}else{

			// 已经加入到属性值组的PropertyValue
			List<PropertyValue> boundProValueList = sdkPropertyManager.findBoundGroupPropertyValue(groupId);
			model.addAttribute("boundProValueList", boundProValueList);
			// 还没有加入到属性值组的PropertyValue
			List<PropertyValue> freeProValueList = sdkPropertyManager.findFreeGroupPropertyValue(propertyId, groupId);
			model.addAttribute("freeProValueList", freeProValueList);
			PropertyValueGroup proValueGroup = sdkPropertyManager.findProValueGroupById(groupId);
			model.addAttribute("proValueGroup", proValueGroup);
		}
		model.addAttribute("propertyId", propertyId);

		return "/product/property/property-value-group";
	}

	@ResponseBody
	@RequestMapping(value = "/i18n/property/saveOrUpdatePropertyValueGroup.json")
	public BackWarnEntity saveOrUpdatePropertyValueGroup(
			HttpServletRequest request,
			Model model,
			@RequestParam("propertyId") Long propertyId,
			@RequestParam("groupId") Long groupId,
			@RequestParam("groupName") String groupName,
			@RequestParam("propertyValueIds") String propertyValueIds){
		try{
			// 保存PropertyValueGroup
			PropertyValueGroup propertyValueGroup = sdkPropertyManager.savePropertyValueGroup(propertyId, groupId, groupName);
			groupId = propertyValueGroup.getId();
			List<Long> valueIds = new ArrayList<Long>();
			// 处理【属性值】和【属性值组】的关联
			if (Validator.isNotNullOrEmpty(propertyValueIds)){
				String[] strIds = propertyValueIds.split(",");
				if (strIds.length > 0){
					for (int i = 0; i < strIds.length; i++){
						Long valueId = Long.valueOf(strIds[i].toString());
						valueIds.add(valueId);
					}
				}else{
					valueIds.add(Long.valueOf(propertyValueIds.toString()));
				}
			}

			sdkPropertyManager.bindPropertyValueAndProValueGroup(valueIds, groupId);
			return SUCCESS;

		}catch (Exception e){
			LOGGER.error("", e);
			return FAILTRUE;
		}

	}

}

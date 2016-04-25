package com.baozun.nebula.web.controller.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.product.PropertyCommand;
import com.baozun.nebula.manager.industry.IndustryPropertyManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.IndustryPropertyRelation;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;

/**
 * 行业属性的管理，将属性与行业通过中间表关联
 * @author shouqun.li
 * @version 2016年4月7日 上午11:10:22
 */
@Controller
public class NebulaIndustryPropertyController extends BaseController{
	
	private static final Logger LOG = LoggerFactory
			.getLogger(NebulaIndustryPropertyController.class);
	
	@Autowired
	private IndustryManager industryManager;
	@Autowired
	private IndustryPropertyManager industryPropertyManager;
	
	/**
	 * 读取行业信息，并返回到行业属性关联页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/industry/industry-property.htm")
	public String showIndustryProperty(Model model){
		List<Industry> list = industryManager.findAllIndustryList();
		model.addAttribute("industryList", list);
		return "product/industry/industry-property";
	}

	/**
	 * 根据行业Id找出关联该行业的属性与可选的属性
	 * @param model
	 * @param industryId
	 * @return
	 */
	@RequestMapping("/i18n/industry/propertyListByIndustryid.json")
	@ResponseBody
	public Object industryPropertyList(Model model,@RequestParam(value="industryId") Long industryId){
		List<PropertyCommand> industryPropertyList = industryPropertyManager.findIndustryPropertyListByIndustryId(industryId);
		List<PropertyCommand> enableSelectPropertyList = industryPropertyManager.findEnableSelectPropertyListByIndustryId(industryId);
		Map<String, List<PropertyCommand>> propertryMap = new HashMap<String, List<PropertyCommand>>();
		propertryMap.put("selected",industryPropertyList);
		propertryMap.put("enableSelect", enableSelectPropertyList);
		BackWarnEntity SUCCESS = new BackWarnEntity();
		if(Validator.isNullOrEmpty(propertryMap)){
			SUCCESS.setIsSuccess(false);
		}else{
			SUCCESS.setIsSuccess(true);
			SUCCESS.setDescription(propertryMap);
		}
		return SUCCESS;
	}
	
	/**
	 * 将某个属性与某个行业的关联
	 * @param model
	 * @param propertyId
	 * @param industryId
	 * @return
	 */
	@RequestMapping("/industry/saveIndustryProperty.json")
	@ResponseBody
	public Object saveIndustryProperty(Model model, @RequestParam(value="propertyId") Long propertyId,
			@RequestParam(value="industryId") Long industryId){
		BackWarnEntity result = new BackWarnEntity();
		if(propertyId != null && industryId != null){
			IndustryPropertyRelation industryPropertyRelation = new IndustryPropertyRelation();
			industryPropertyRelation.setIndustryId(industryId);
			industryPropertyRelation.setPropertyId(propertyId);
			//industryPropertyRelation.setSortNo(1);
			//查找出某行业中属性排序的最大值
			Integer maxSortNo = industryPropertyManager.findMaxIndustryPropertySortId(industryId);
			LOG.info("the max sortNo is " + maxSortNo);
			if(maxSortNo == null){
				industryPropertyRelation.setSortNo(1);
			}else{
				industryPropertyRelation.setSortNo(maxSortNo + 1);
			}
			try {
				industryPropertyManager.relationIndustryProperty(industryPropertyRelation);
				result.setIsSuccess(true);
				return result;
			} catch (Exception e) {
				LOG.error("insert into t_pd_industry_property_relation is fail");
				result.setIsSuccess(false);
				return result;
			}
		}else{
			result.setIsSuccess(false);
			return result;
		}
	}
	
	/**
	 * 删除某个行业与某个属性的关联关系
	 * @param model
	 * @param propertyId
	 * @param industryId
	 * @return
	 */
	@RequestMapping("/industry/deleteIndustryProperty.json")
	@ResponseBody
	public Object deleteIndustryProperty(Model model, @RequestParam(value="propertyId") Long propertyId,
			@RequestParam(value="industryId") Long industryId){
		BackWarnEntity result = new BackWarnEntity();
		if(propertyId != null && industryId != null){
			try {
				List<Item> itemList = industryPropertyManager.findItemsByIndustryIdAndPropertyId(industryId, propertyId);
				if(Validator.isNullOrEmpty(itemList)){
					industryPropertyManager.deleteIndustryPropertyRelation(industryId, propertyId);
					result.setIsSuccess(true);
					return result;
				}else{
					result.setIsSuccess(false);
					result.setErrorCode(1);
					return result;
				}
			} catch (Exception e) {
				LOG.error("delete from t_pd_industry_property_relation is fail");
				result.setIsSuccess(false);
				return result;
			}
		}else{
			result.setIsSuccess(false);
			return result;
		}
	}
	
	/**
	 * 更新某个行业中属性的排序
	 * @param model
	 * @param str
	 * @param industryId
	 * @return
	 */
	@RequestMapping("/industry/updateIndustryPropertySortBylist.json")
	@ResponseBody
	public Object updateIndustryPropertySortBylist(Model model, @RequestParam(value="ids") String str,
			@RequestParam(value="industryId") Long industryId){
		BackWarnEntity result = new BackWarnEntity();
		if(industryId != null && str != null){
			try {
				boolean flag = industryPropertyManager.updateIndustryPropertySort(industryId, str);
				result.setIsSuccess(flag);
				if(flag == false){
					result.setErrorCode(2);
				}
				return result;
			} catch (Exception e) {
				LOG.error("dberror:update industryPropertySort error");
				result.setIsSuccess(false);
				return result;
			}
		}else{
			result.setIsSuccess(false);
			return result;
		}
	}
}

package com.baozun.nebula.web.controller.delivery;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.manager.product.ItemCategoryManager;
import com.baozun.nebula.model.delivery.AreaDeliveryMode;
import com.baozun.nebula.model.delivery.DeliveryArea;
import com.baozun.nebula.sdk.manager.SdkAreaDeliveryModeManager;
import com.baozun.nebula.sdk.manager.SdkDeliveryAreaManager;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.web.controller.BaseController;

@Controller
public class DeliveryAreaModeController extends BaseController{
	
	private static final Logger	log	= LoggerFactory.getLogger(DeliveryAreaModeController.class);
	
	@Autowired
	private CategoryManager  categoryManager;
	
	@Autowired
	private ItemCategoryManager	itemCategoryManager;
	
	@Autowired
	private SdkDeliveryAreaManager deliveryAreaManager;
	
	@Autowired
	private SdkAreaDeliveryModeManager areaDeliveryModeManager;
	
	/**
	 * 物流管理
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/logistics/areaDeliverMode/manager.htm")
	public String areaDeliveryManager(Model model) {
		// 顺序 ,一般先有父 再有 子
		Sort[] sorts = Sort.parse("PARENT_ID asc,sort_no asc");
		//查询所有中国内的地区信息
		List<DeliveryArea> areaList = deliveryAreaManager.findEnableDeliveryAreaList(LangUtil.getCurrentLang(),sorts);
		model.addAttribute("areaList", areaList);
		return "delivery/delivery-mode";
	}
	
	
	/**
	 * 通过deliveryAreaId查询DeliveryAreaMode信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/logistics/areaDeliverMode/findDeliveryAreaModeByAreaId.json")
	@ResponseBody
	public AreaDeliveryMode findAreaDeliveryModeByAreaId(Long areaId) {
		AreaDeliveryMode adm = areaDeliveryModeManager.findAreaDeliveryModeByAreaId(areaId);
		return  adm;
	}
	
	
	/**
	 * 修改或保存areaDeliveryMode
	 * @param areaDeliveryMode
	 */
	@ResponseBody
	@RequestMapping(value = { "/logistics/areaDeliverMode/updateDeliveryAreaModeByAreaId.json" }, method = RequestMethod.POST, headers = HEADER_WITH_AJAX_SPRINGMVC)
	public void updateDeliveryAreaModeByAreaId(@ModelAttribute("AreaDeliveryMode") AreaDeliveryMode areaDeliveryMode) {
		//id不为空，表示是更新操作,否则为新增
		
		if(null!=areaDeliveryMode.getId()){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", areaDeliveryMode.getId());
			map.put("commonDelivery", areaDeliveryMode.getCommonDelivery());
			map.put("commonEndTime", areaDeliveryMode.getCommonDeliveryEndTime());
			map.put("commonStartTime", areaDeliveryMode.getCommonDeliveryStartTime());
			map.put("firstDelivery", areaDeliveryMode.getFirstDayDelivery());
			map.put("firstEndTime", areaDeliveryMode.getFirstDeliveryEndTime());
			map.put("firstStartTime", areaDeliveryMode.getFirstDeliveryStartTime());
			map.put("logisticsCode", areaDeliveryMode.getLogisticsCode());
			map.put("logisticsCompany", areaDeliveryMode.getLogisticsCompany());
			map.put("remark", areaDeliveryMode.getRemark());
			map.put("secondDelivery", areaDeliveryMode.getSecondDayDelivery());
			map.put("secondEndTime", areaDeliveryMode.getSecondDeliveryEndTime());
			map.put("secondStartTime", areaDeliveryMode.getSecondDeliveryStartTime());
			map.put("supportCod", areaDeliveryMode.getSupport_COD());
			map.put("lang", LangUtil.getCurrentLang());
			areaDeliveryModeManager.updateDeliveryMode(map);
		}else{
			areaDeliveryMode.setCreateTime(new Date());
			areaDeliveryMode.setModifyTime(new Date());
			areaDeliveryMode.setVersion(new Date());
			areaDeliveryModeManager.saveDeliveryMode(areaDeliveryMode);
		}
		
	}

	/**
	 * 根据id删除areaDeliveryMode
	 * @param id
	 */
	@ResponseBody
	@RequestMapping(value = { "/logistics/areaDeliverMode/deleteDeliveryAreaModeById.json" }, method = RequestMethod.POST, headers = HEADER_WITH_AJAX_SPRINGMVC)
	public void deleteDeliveryAreaModeById(@RequestParam(value = "id", required = true) Long id) {
		areaDeliveryModeManager.deleteDeliveryModeById(id);
	}
	
}

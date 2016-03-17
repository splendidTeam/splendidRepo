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
package com.baozun.nebula.web.controller.promotion;
  
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionPriorityAdjustCommand;
import com.baozun.nebula.command.promotion.PromotionPriorityAdjustDetailCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.promotion.PromotionPriorityAdjustGroupManager;
import com.baozun.nebula.model.promotion.PromotionPriorityAdjust;
import com.baozun.nebula.model.promotion.PromotionPriorityAdjustDetail;
import com.baozun.nebula.model.promotion.PromotionPriorityAdjustGroup;
import com.baozun.nebula.sdk.manager.SdkPriorityAdjustManager;
import com.baozun.nebula.sdk.manager.SdkPromotionManager;
import com.baozun.nebula.sdk.manager.SdkPromotionPriorityAdjustGroupManager;
import com.baozun.nebula.solr.utils.Validator;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 
 * @author 项硕
 */
@Controller
public class PriorityAdjustController extends BaseController {
 
	@Autowired
	private SdkPriorityAdjustManager sdkPriorityAdjustManager;
	@Autowired
	private PromotionPriorityAdjustGroupManager promotionPriorityAdjustGroupManager;
	@Autowired
    private ShopManager shopManager;
	@Autowired
	private SdkPromotionManager sdkPromotionManager;
	@Autowired
	private SdkPromotionPriorityAdjustGroupManager sdkPromotionPriorityAdjustGroupManager;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	  
	/**
	 * 前往促销优先级列表页
	 */
	@RequestMapping(value = "/promotion/promotionpriorityadjust.htm", method = RequestMethod.GET)
	public String goPriorityList() {  
		return "/promotion/priority-list";
	}
	
	/**
	 * 获取优先级列表数据
	 * @param model
	 * @param queryBean
	 * @return
	 */
	@RequestMapping(value = "/promotion/priorityAdjustList.json", method = RequestMethod.GET)
	@ResponseBody
	public Pagination<PromotionPriorityAdjustCommand> getPriorityList(
			Model model,
			@QueryBeanParam QueryBean queryBean) { 
		Sort[] sorts = queryBean.getSorts(); 
		if (ArrayUtils.isEmpty(sorts)) sorts = Sort.parse("pa.lastupdatetime desc");
		Map<String, Object> paraMap = queryBean.getParaMap();
		paraMap.put("shopId", shopManager.getShopId(super.getUserDetails()));	//支持多店铺
		
		Pagination<PromotionPriorityAdjustCommand> pagination = sdkPriorityAdjustManager
				.findPriorityAdjustList(queryBean.getPage(), sorts, paraMap);
		return pagination;
	}  
	
	/**
	 * 解组
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/promotion/ungroup.json", method = RequestMethod.POST)
	@ResponseBody 
	public BackWarnEntity ungroup(@RequestParam Long adjustId,@RequestParam String groupName) {
		BackWarnEntity backWarnEntity = new BackWarnEntity(false, null);
		Integer res= sdkPromotionPriorityAdjustGroupManager.removePromotionPriorityAdjustGroupByAdjustIdsAndGroupName(adjustId, groupName);
		if(res>0){
			backWarnEntity.setIsSuccess(true);
		}else{
			backWarnEntity.setIsSuccess(false);
		}
		return backWarnEntity;
	}
	
	/**
	 * 启用优先级
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/promotion/enablePriority.json", method = RequestMethod.POST)
	@ResponseBody 
	public BackWarnEntity enablePriority(@RequestParam Long id) {
		BackWarnEntity backWarnEntity = new BackWarnEntity(false, null);
		Long userId = getUserDetails().getUserId();
		
		List<PromotionPriorityAdjustCommand> list =  sdkPriorityAdjustManager.findEffectivePriorityList(shopManager.getShopId(super.getUserDetails()));
		
		if(list!=null && list.size()>0){
			backWarnEntity.setIsSuccess(false);
		}else{
			Integer res= sdkPriorityAdjustManager.enableOrUnablePriorityById(id, userId, PromotionPriorityAdjust.ACTIVEMARK_ENABLE);
			
			if(res>0){
				backWarnEntity.setIsSuccess(true);
			}else{
				backWarnEntity.setIsSuccess(false);
			}
		}
		
		return backWarnEntity;
	}

	/**
	 * 禁用优先级
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/promotion/disablePriority.json", method = RequestMethod.POST)
	@ResponseBody 
	public BackWarnEntity disablePriority(@RequestParam Long id) {
		BackWarnEntity backWarnEntity = new BackWarnEntity(false, null);
		Long userId = getUserDetails().getUserId();

		Integer res= sdkPriorityAdjustManager.enableOrUnablePriorityById(id, userId, PromotionPriorityAdjust.ACTIVEMARK_FORBIDDEN);
	
		if(res>0){
			backWarnEntity.setIsSuccess(true);
		}else{
			backWarnEntity.setIsSuccess(false);
		}
		return backWarnEntity;
	}
	
	/**
	 * 前往优先级查看页
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/promotion/viewPriority.htm")
	public String goPriorityView(@RequestParam Long id, Model model) { 
		PromotionPriorityAdjust priority = sdkPriorityAdjustManager.findPriorityAdjustById(id);
		List<PromotionPriorityAdjustDetailCommand> detailList = sdkPriorityAdjustManager.findPriorityDetailListByPriorityId(id);
		
		List<PromotionPriorityAdjustDetailCommand> newList = refactoringDetailList(detailList);
		model.addAttribute("priority", priority);
		model.addAttribute("detailList", newList);
		model.addAttribute("isView", true);
		return "/promotion/priority-edit";
	}  
	
	/**
	 * 前往优先级编辑页
	 */ 
	@RequestMapping(value = "/promotion/editPriority.htm")
	public String goPriorityEdit(@RequestParam(required = false) Long id, Model model) { 
		if (null != id) {
			PromotionPriorityAdjust priority = sdkPriorityAdjustManager.findPriorityAdjustById(id);
			List<PromotionPriorityAdjustDetailCommand> detailList = sdkPriorityAdjustManager.findPriorityDetailListByPriorityId(id);
			List<PromotionPriorityAdjustDetailCommand> newList = refactoringDetailList(detailList);
			
			//Map<String, Object> groupCountMap = getGroupCountMap(id);
			//model.addAttribute("groupCountMap", groupCountMap);
			model.addAttribute("priority", priority);
			model.addAttribute("detailList", newList);
			model.addAttribute("isUpdate", true);
		}else{
			List<PromotionCommand>  promotionCommandList  = sdkPromotionManager.findAllPromotionEnableList();
			List<PromotionPriorityAdjustDetailCommand> detailList = getDetailList(promotionCommandList);
			model.addAttribute("detailList", detailList);
			model.addAttribute("isAdd", true);
		}
		return "/promotion/priority-edit";
	}

	private List<PromotionPriorityAdjustDetailCommand> getDetailList(
			List<PromotionCommand> promotionCommandList) {
		List<PromotionPriorityAdjustDetailCommand> detailList = null;
		if(Validator.isNotNullOrEmpty(promotionCommandList)){
			Integer priority = 1;
			detailList = new ArrayList<PromotionPriorityAdjustDetailCommand>();
			for(PromotionCommand promotionCommand : promotionCommandList){
				PromotionPriorityAdjustDetailCommand promotionPriorityAdjustDetailCommand = new PromotionPriorityAdjustDetailCommand();
				promotionPriorityAdjustDetailCommand.setPromotionName(promotionCommand.getPromotionName());
				promotionPriorityAdjustDetailCommand.setPromotionId(promotionCommand.getPromotionId());
				promotionPriorityAdjustDetailCommand.setPromotionStartTime(promotionCommand.getStartTime());
				promotionPriorityAdjustDetailCommand.setPromotionEndTime(promotionCommand.getEndTime());
				promotionPriorityAdjustDetailCommand.setAudienceName(promotionCommand.getMemComboName());
				promotionPriorityAdjustDetailCommand.setScopeName(promotionCommand.getScopeName());
				promotionPriorityAdjustDetailCommand.setGroupName(promotionCommand.getGroupName());
				//默认顺序
				promotionPriorityAdjustDetailCommand.setPriority(priority++);
				promotionPriorityAdjustDetailCommand.setExclusiveMark(PromotionPriorityAdjustDetail.EXCLUSIVE_MARK_DISABLE);
				
				detailList.add(promotionPriorityAdjustDetailCommand);
			}
		}
		return detailList;
	}

	private List<PromotionPriorityAdjustDetailCommand> refactoringDetailList(
			List<PromotionPriorityAdjustDetailCommand> detailList) {
		List<PromotionPriorityAdjustDetailCommand> newList = new ArrayList<PromotionPriorityAdjustDetailCommand>();
		Map<Long,Object> map = new HashMap<Long,Object>();
		if(Validator.isNotNullOrEmpty(detailList)){
			for(PromotionPriorityAdjustDetailCommand promotionPriorityAdjustDetailCommand:detailList){
				if(Validator.isNotNullOrEmpty(promotionPriorityAdjustDetailCommand.getGroupName())){
					if(map.get(promotionPriorityAdjustDetailCommand.getPromotionId())==null){
						//新的groupList
						PromotionPriorityAdjustDetailCommand newCommand = new PromotionPriorityAdjustDetailCommand();
						newCommand.setGroupName(promotionPriorityAdjustDetailCommand.getGroupName());
						newCommand.setGroupType(promotionPriorityAdjustDetailCommand.getGroupType());
						List<PromotionPriorityAdjustDetailCommand> groupList = new ArrayList<PromotionPriorityAdjustDetailCommand>();
						
						for(PromotionPriorityAdjustDetailCommand comm:detailList){
							if(Validator.isNotNullOrEmpty(comm.getGroupName())
									&& comm.getGroupName().equals(promotionPriorityAdjustDetailCommand.getGroupName())){
								groupList.add(comm);
								map.put(comm.getPromotionId(), comm.getPromotionId());
							}
						}
						newCommand.setPromotionPriorityAdjustDetailCommandList(groupList);
						newList.add(newCommand);
					}
				}else{
					newList.add(promotionPriorityAdjustDetailCommand);
				}
			}
		}
		return newList;
	}

	private Map<String, Object> getGroupCountMap(Long id) {
		Map<String,Object> groupCountMap = new HashMap<String,Object>(); 
		List<PromotionPriorityAdjustGroup> promotionPriorityAdjustGroupList = sdkPromotionPriorityAdjustGroupManager.findPromotionPriorityAdjustGroupListByAdjustId(id);
		if(Validator.isNotNullOrEmpty(promotionPriorityAdjustGroupList)){
			int i = 0;
			for(PromotionPriorityAdjustGroup promotionPriorityAdjustGroup:promotionPriorityAdjustGroupList){
				if(groupCountMap.get(promotionPriorityAdjustGroup.getGroupName())!=null){
					groupCountMap.put(promotionPriorityAdjustGroup.getGroupName(), ++i);
				}else{
					i=0;
					groupCountMap.put(promotionPriorityAdjustGroup.getGroupName(), ++i);
				}
			}
		}
		return groupCountMap;
	}  
	
	/**
	 * 
	 * @param model
	 * @param priorityJson
	 * @param adjustname
	 * @param type
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping("/promotion/savePriority.json")
	@ResponseBody
	public BackWarnEntity savePriorityAdjust(PromotionPriorityAdjustCommand cmd) { 
		BackWarnEntity rs = new BackWarnEntity(false, null);
		
		UserDetails user = getUserDetails();
		cmd.setLastUpdateId(user.getUserId());
		cmd.setShopId(shopManager.getShopId(user));
		
		try {
			Long id = sdkPriorityAdjustManager.saveOrUpdatePriority(cmd);
			rs.setIsSuccess(true);
			rs.setDescription(id);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
		}
		return rs;
	}
	
	/**
	 * 获取促销列表数据
	 * @param model
	 * @param queryBean
	 * @return
	 */
	@RequestMapping(value = "/promotion/promotionListForPriority.json", method = RequestMethod.GET)
	@ResponseBody
	public List<PromotionCommand> getPromotionListForPriority(@RequestParam Date timePoint) { 
		Long shopId = shopManager.getShopId(super.getUserDetails());	//支持多店铺
		List<PromotionCommand> promotionList = sdkPriorityAdjustManager
				.findConflictingPromotionListByTimePoint(timePoint, shopId);
		return promotionList;
	}  
	
	
	/**
	 * 检查分组名称是否已存在
	 * @param groupName
	 * @return
	 */
	@RequestMapping(value = "/promotion/checkAdjustName.json", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkAdjustName(@RequestParam("name") String name){
		PromotionPriorityAdjust  promotionPriorityAdjust  = sdkPriorityAdjustManager.findPriorityAdjustByName(name);
		
		return promotionPriorityAdjust==null;
	}
	
	
	/**
	 * 查询可以分组的促销活动 
	 * @return
	 */
	@RequestMapping(value = "/promotion/findCanGroupPromotionList.json")
	@ResponseBody
	public Object findCanGroupPromotionList(@RequestParam("adjustId") Long adjustId){
		
		List<PromotionPriorityAdjustDetailCommand> detailList = sdkPriorityAdjustManager.findPriorityDetailListByPriorityId(adjustId);
		List<PromotionPriorityAdjustGroup> groupList = promotionPriorityAdjustGroupManager.findPromotionPriorityAdjustGroupListByAdjustId(adjustId);
		/** map key: promotionId*/
		Map<Long, PromotionPriorityAdjustGroup> groupMap = new HashMap<Long, PromotionPriorityAdjustGroup>();
		
		for(PromotionPriorityAdjustGroup group : groupList){
			groupMap.put(group.getPromotionId(), group);
		}
		
		
		List<PromotionPriorityAdjustDetailCommand> adjustDetailList = new ArrayList<PromotionPriorityAdjustDetailCommand>();
		for(PromotionPriorityAdjustDetailCommand priorityAdjustDetail : detailList){
			/**
			 * 不能可以在添加分组列表里活动:
			 * 1: 排他活动
			 * 2: 已经分组的活动
			 */
			// 排他活动
			if(PromotionPriorityAdjustDetail.EXCLUSIVE_MARK_ENABLE.equals(priorityAdjustDetail.getExclusiveMark())){
				continue;
			}
			// 已经分组的活动
			if(null != groupMap.get(priorityAdjustDetail.getPromotionId())){
				continue;
			}
			
			adjustDetailList.add(priorityAdjustDetail);
		}
		return adjustDetailList;
	}
	
	
	/**
	 * 检查分组名称是否已存在
	 * @param groupName
	 * @return
	 */
	@RequestMapping(value = "/promotion/checkGroupName.json", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkGroupName(@RequestParam("groupName") String groupName){
		List<PromotionPriorityAdjustGroup> groupList = promotionPriorityAdjustGroupManager.findPromotionPriorityAdjustGroupListByGroupName(groupName);
		if(null == groupList || groupList.size() <= 0){
			return false;
		}
		return true;
	}
	
	/**
	 * 保存促销活动分组数据
	 * @param groupName
	 * @param promotionIds
	 * @return
	 */
	@RequestMapping(value = "/promotion/savePriorityAdjustGroup.json", method = RequestMethod.POST)
	@ResponseBody
	public Object savePriorityAdjustGroup(@RequestParam("groupName") String groupName, 
			@RequestParam("adjustId") Long adjustId,
			@RequestParam("promotionIds[]") Long[] promotionIds,
			@RequestParam("excGroupType") Integer excGroupType){
		promotionPriorityAdjustGroupManager.savePromotionPriorityAdjustGroup(adjustId, groupName, promotionIds,excGroupType);
		return SUCCESS;
	}
	
}

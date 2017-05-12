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

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.command.promotion.PromotionCouponCommand;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.manager.product.ItemRateManager;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.system.InstationMessageTemplate;
import com.baozun.nebula.sdk.manager.EmailTemplateManager;
import com.baozun.nebula.sdk.manager.ItemColorValueRefManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCouponManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.ArrayCommand;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;

/**
 * 商品评价管理
 * @author xingyu 
 * @date  
 * @version
 */
@Controller
public class ItemRateController extends BaseController{

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(ItemRateController.class);
    @Autowired
	private ItemRateManager	itemRateManager;
    @Autowired
	private MemberManager memberManager;
    @Autowired
	private SdkPromotionCouponManager sdkCouponManager; 
    @Autowired
   	private ItemColorValueRefManager  itemColorValueRefManager;
    @Autowired
   	private  EmailTemplateManager emailTemplateManager;

	/**
	 * 跳转到商品评价列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/product/itemEvaluateListe.htm")
	public String itemRateList(Model model) {
		return "/product/item/item-evaluate";
	}	

	/**
	 * 评价列表(json)
	 * @param model
	 * @param queryBean
	 * @return
	 */
	@RequestMapping("/product/itemEvaluateList.json")
	@ResponseBody
	public Pagination<RateCommand> itemRateListJson(Model model,
			@QueryBeanParam QueryBean queryBean){
		
		Pagination<RateCommand>  rateCommandList =
				itemRateManager.findRateListByQueryMapWithPage(queryBean.getPage(), queryBean.getSorts(), queryBean.getParaMap());
		
		List<RateCommand> items = rateCommandList.getItems();
		for (RateCommand rateList : items) {
			String imge="";
			String imgnames = rateList.getImg_names();
			Long itemId = rateList.getItemId();
			if(Validator.isNotNullOrEmpty(imgnames) && Validator.isNotNullOrEmpty(itemId)){				
				String[] splitimgname = imgnames.split(":");
				for (String imgname : splitimgname) {		
					String userDefinedPath = "item-rate"+"/"+itemId + "" + "/";
					imge += userDefinedPath+imgname + ":";
				}
				rateList.setImg_names(imge);			
			}
		}	
		return rateCommandList;
	}
	

	/**
	 * 批量审核通过
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/product/enableRateByIds.json")
	public Object enableRateByIds(@RequestParam("ids") List<Long> ids) { 
		UserDetails userDetails = this.getUserDetails();
		itemRateManager.enableRateByIds(ids, userDetails.getUserId());
		return SUCCESS;
	}
	

	/**
	 * 批量删除评价
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/product/disableRateByIds.json")
	public Object disableRateByIds(@RequestParam("ids") List<Long> ids) { 
		UserDetails userDetails = this.getUserDetails();
		itemRateManager.disableRateByIds(ids, userDetails.getUserId());
		return SUCCESS;
	}
	
	/**
	 * 评价审核通过
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/product/enableRateById.json")
	public Object enableRateById(@RequestParam("id") Long id) { 
		UserDetails userDetails = this.getUserDetails();
		itemRateManager.enableRateById(id, userDetails.getUserId());
		return SUCCESS;
	}
	/**
	 * 删除评价
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/product/disableRateById.json")
	public Object disableRateById(@RequestParam("id") Long id) { 
		UserDetails userDetails = this.getUserDetails();
		itemRateManager.disableRateById(id, userDetails.getUserId());

		return SUCCESS;
	}
	/**
	 * 回复评价
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/product/replyItemEvaluate.Json")
	public Object replyItemEvaluate(@RequestParam("reply") String reply,
			@RequestParam("id") Long id,
			@RequestParam("isReply") Integer isReply,
			@RequestParam("isPass") Integer isPass) { 
		UserDetails userDetails = this.getUserDetails();
		itemRateManager.replyRate(id,reply, userDetails.getUserId(),isReply,isPass );
		SUCCESS.setDescription("");
		return SUCCESS;
	}

	
	/** 
	* @Title: sendInstationMessage 
	* @Description:(查询发送站内信信息) 
	* @param @param reply
	* @param @param id
	* @param @param isReply
	* @param @param isPass
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	* @date 2016年1月14日 下午8:00:36 
	* @author GEWEI.LU   
	*/
	@RequestMapping("/send/instation/message")
	public String sendInstationMessage(Model model) { 
		List<InstationMessageTemplate> templatelist=itemColorValueRefManager.finInstationMessageTemplatelist();
		List<MemberGroup> list=memberManager.findAllMemberGroup();
		model.addAttribute("memberList", list);
		model.addAttribute("templatelist", templatelist);
		return "/product/item/send-instation-message";
	}
	/** 
	* @Title: enterPromotionInfoListPage 
	* @Description:(跳转站内信模板管理页面) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	* @date 2016年1月15日 下午3:34:41 
	* @author GEWEI.LU   
	*/
	@RequestMapping(value = "/instation/message/template",method = RequestMethod.GET)
	public String enterPromotionInfoListPage(){
		return "/product/item/instation-templet-list";
	}
	/** 
	* @Title: getPromotionInfoListData 
	* @Description:(站内信模板管理,获取所有站内信模板) 
	* @param @param queryBean
	* @param @return    设定文件 
	* @return Pagination<InstationMessageTemplate>    返回类型 
	* @throws 
	* @date 2016年1月15日 下午3:34:30 
	* @author GEWEI.LU   
	*/
	@RequestMapping(value = "/station/getStationTempletListData.json",method = RequestMethod.GET)
	@ResponseBody
	public Pagination<InstationMessageTemplate> getPromotionInfoListData(@QueryBeanParam QueryBean queryBean){
		Sort[] sorts = queryBean.getSorts();
		if (ArrayUtils.isEmpty(sorts)){
			sorts = Sort.parse("id desc"); // 默认排序
		}
		Map<String, Object> paraMap = queryBean.getParaMap();
		paraMap.put("sorted", true);
		Pagination<InstationMessageTemplate> pagination = itemColorValueRefManager.findTempletByQueryMapWithPage(queryBean.getPage(), queryBean.getSorts(), paraMap);
		return pagination;
	}
	
	/** 
	* @Title: sendCoupon 
	* @Description:(进入优惠券发送页面) 
	* @param @param model
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	* @date 2016年1月15日 下午4:54:18 
	* @author GEWEI.LU   
	*/
	@RequestMapping("/send/coupon.htm")
	public String sendCoupon(Model model) { 
		List<PromotionCouponCommand> couponTypeList = sdkCouponManager.findAllcouponList();
		List<MemberGroup> list=memberManager.findAllMemberGroup();
		List<InstationMessageTemplate> templatelist=itemColorValueRefManager.finInstationMessageTemplatelist();
		//List<EmailTemplate> emailTemplatelistp=emailTemplateManager.findEmailTemplateList();
		//model.addAttribute("emailTemplatelist", emailTemplatelistp);
		model.addAttribute("templatelist", templatelist);
		model.addAttribute("memberList", list);
		model.addAttribute("couponTypeList", couponTypeList);
		return "/product/item/sendCoupon";
	}
	 
	/** 
	* @Title: editStationTemplet 
	* @Description:(跳转站内信模板管理页面) 
	* @param @param id
	* @param @param model
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	* @date 2016年1月15日 下午4:54:53 
	* @author GEWEI.LU   
	*/
	@RequestMapping(value = "/station/editStationTemplet.htm",method = RequestMethod.GET)
	public String editStationTemplet(@RequestParam(required = false) Long id, Model model) {
		return "/product/item/instation-templet-edit";
	}
	
	
	/** 
	* @Title: updateStationTemplet 
	* @Description:(编辑站内信息模板) 
	* @param @param id
	* @param @param model
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	* @date 2016年1月19日 下午7:50:44 
	* @author GEWEI.LU   
	*/
	@RequestMapping(value = "/update/getStationTemplet.json",method = RequestMethod.GET)
	public String updateStationTemplet(@RequestParam(required = false) Long id, Model model) {
		InstationMessageTemplate  instationMessageTemplate =itemColorValueRefManager.findTempletByid(id);
		model.addAttribute("info", instationMessageTemplate);
		return "/product/item/instation-templet-edit";
	}
	
	
	
	 
	/** 
	* @Title: saveOrUpdateStationTemplet 
	* @Description:(保存模板) 
	* @param @param info
	* @param @return    设定文件 
	* @return BackWarnEntity    返回类型 
	* @throws 
	* @date 2016年1月19日 下午7:53:45 
	* @author GEWEI.LU   
	*/
	@RequestMapping(value = "/station/saveOrUpdateStationTemplet.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity saveOrUpdateStationTemplet(InstationMessageTemplate info) {
		if(info.getId()==null){
			info.setModifyTime(new Date());
			info.setType(1);;
			info.setCreateTime(new Date());
		}else{
			info.setModifyTime(new Date());
			info.setCreateTime(new Date());
		}
		try {
			itemColorValueRefManager.saveinstationMessageTemplate(info);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return FAILTRUE;
		}
	}
	

	/**
	 * 批量启用或禁用
	 * 
	 * @param ids
	 * @param isEnable
	 * @return
	 */
	@RequestMapping(value = "/promotion/enableOrDisablePromotionInfo.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity enableOrDisablePromotionInfo(@ArrayCommand(dataBind = true) Long id,@ArrayCommand(dataBind = true) Long type, @RequestParam Boolean isEnable) {
		try {
			itemColorValueRefManager.updateinstationMessageTemplate(type,id);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return FAILTRUE;
		}
	}
	
}

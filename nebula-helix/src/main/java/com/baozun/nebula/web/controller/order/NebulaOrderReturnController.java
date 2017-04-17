package com.baozun.nebula.web.controller.order;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.constant.SoReturnConstants;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.manager.SoReturnApplicationManager;
import com.baozun.nebula.manager.SoReturnLineManager;
import com.baozun.nebula.model.salesorder.SoReturnApplication;
import com.baozun.nebula.model.salesorder.SoReturnApplicationDeliveryInfo;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.order.form.ReturnOderForm;
import com.baozun.nebula.web.controller.order.resolver.ReturnApplicationResolver;
import com.baozun.nebula.web.controller.order.validator.ReturnApplicationValidator;
import com.baozun.nebula.web.controller.order.viewcommand.ReturnApplicationViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.ReturnLineViewCommand;

/**
* 退货基类控制器
* <ol>
* <li>{@link #returnCommit( MemberDetails memberDetails,
* 							BindingResult bindingResult,
*							HttpServletRequest request,
*						    ReturnOderForm returnOrderForm,
*							Model model)} 退货提交</li>
*
* </ol>
* 
*<ol>
* <li>{@link #turnToReturn( String isTrack, Long orderId, Model model)} 去处理退货</li>
*</ol>
* 
* 
* <ol>
* <li>{@link #showReturnDetail( String isTrack, String orderId, Model model)} 退货详情</li>
* </ol>
* 
* 
* 
*/
public class NebulaOrderReturnController  extends BaseController{
	
	 /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaOrderReturnController.class);
    
	/**
	 *  <br/>
	 * 退货Form的校验器
	 */
	@Autowired
	@Qualifier("returnApplicationNormalValidator")
	private ReturnApplicationValidator			returnApplicationNormalValidator;
	
	@Autowired
	private ReturnApplicationResolver returnApplicationResolver;
	
	@Autowired
	private OrderManager orderManager;
	
	@Autowired
	private SoReturnApplicationManager soReturnApplicationManager;
	
	@Autowired
	private SoReturnLineManager soReturnLineManager;
	
	@Autowired
	private SdkOrderLineDao sdkOrderLineDao;
	
	
/**	
  * 
  * @param memberDetails
  * @param bindingResult
  * @param request
  * @param returnOrderForm
  * @param model
  * @ResponseBody
	@RequestMapping(value = "/myAccountOrder/returnCommit", method = RequestMethod.GET)
  * @return
  */
	public NebulaReturnResult returnCommit(
			@LoginMember MemberDetails memberDetails,
			BindingResult bindingResult,
			HttpServletRequest request,
			@ModelAttribute(value = "returnOrderForm") ReturnOderForm returnOrderForm,
			Model model)  {
		DefaultReturnResult defaultReturnResult = new DefaultReturnResult();
		returnApplicationNormalValidator.validate(returnOrderForm, bindingResult);
		
		   // 如果校验失败，返回错误
        if (bindingResult.hasErrors()){
            LOGGER.error("[returnOrder] {} [{}] returnOrderForm validation error. \"\"");
            return getResultFromBindingResult(bindingResult);
        }
		
		defaultReturnResult = (DefaultReturnResult)getResultFromBindingResult(bindingResult);
		
		if (!defaultReturnResult.isResult()){
			defaultReturnResult.setStatusCode("return.validator.errors");
			return defaultReturnResult;
		}
		SalesOrderCommand salesOrder = orderManager.findOrderById(Long.parseLong(returnOrderForm.getOrderId()), null);
		//如果是换货，需要处理换货物流对象
		SoReturnApplicationDeliveryInfo	deliveryInfo=null;
		if(SoReturnConstants.TYPE_EXCHANGE==returnOrderForm.getReturnType()){
				deliveryInfo=returnApplicationResolver.toReturnApplicationDelivery(returnOrderForm);
		}
		ReturnApplicationCommand appCommand=returnApplicationResolver.toReturnApplicationCommand(memberDetails, returnOrderForm, salesOrder);
		
		SoReturnApplication sra=soReturnApplicationManager.createReturnApplication(appCommand, salesOrder,deliveryInfo);
		if(null==sra){
			 LOGGER.error("[returnOrder] {} [{}] returnOrder save error. \"\"");
				DefaultResultMessage defaultResultMessage = new DefaultResultMessage();
				defaultResultMessage.setMessage("系统异常！请联系管理员！");
				defaultReturnResult.setResultMessage(defaultResultMessage);
				defaultReturnResult.setResult(false);
		}
				return defaultReturnResult;
		
	}
	
	/**
	 * 
	 * @param List<Long> orderLineIds  订单行id
	 * @param model
	 * @RequestMapping(value = "/myAccountOrder/turnToReturn")
	 *
	 * @return
	 * 
	 * 
	 */
	
	public String showReturnApplication(@LoginMember MemberDetails memberDetails,@RequestParam(value = "orderLineIds",required=true) List<Long> orderLineIds, Model model) {
		//如果没有订单行，通过订单找订单行
		if(null==orderLineIds||orderLineIds.size()<=0){
				return "/errors/500";
		}
		//如果订单行
		else {
			 List<ReturnLineViewCommand> soReturnLineViews=returnApplicationResolver.toReturnLineViewCommand(orderLineIds);
				model.addAttribute("soReturnLineVo", soReturnLineViews);
				return "return.return-line";
		}
		
	}
    
	
	/**
	 * 
	 * @param orderId
	 * @param returnIds
	 * @param model
	 * @param request
	 * @RequestMapping(value = "/myAccountOrder/returnDetail")
	 * @return
	 */

		public String showReturnDetail(@LoginMember MemberDetails memberDetails,@RequestParam(value = "returnIds") List<Long> returnIds,@RequestParam(value = "orderId") Long orderId, Model model,
				HttpServletRequest request) {

			List<ReturnApplicationCommand>  returnApplications=soReturnApplicationManager.findReturnApplicationCommandsByIds(returnIds);
			List<ReturnApplicationViewCommand> returnApplicationViewCommands=returnApplicationResolver.toTurnReturnApplicationViewCommand(returnApplications);
			model.addAttribute("returnApplications", returnApplicationViewCommands);
			return "return.returnDetail";

		}

}

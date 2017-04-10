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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.command.ReturnLineCommand;
import com.baozun.nebula.constant.SoReturnConstants;
import com.baozun.nebula.manager.SoReturnApplicationManager;
import com.baozun.nebula.manager.SoReturnLineManager;
import com.baozun.nebula.model.salesorder.SoReturnApplication;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
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
	
	private SdkSkuManager sdkSkuManager;
	
	@Autowired
	private SoReturnLineManager soReturnLineManager;
	
	
	@ResponseBody
	@RequestMapping(value = "/myAccountOrder/returnCommit", method = RequestMethod.GET)
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
		ReturnApplicationCommand appCommand=returnApplicationResolver.toReturnApplicationCommand(memberDetails, returnOrderForm, salesOrder);
		SoReturnApplication sra=soReturnApplicationManager.createReturnApplication(appCommand, salesOrder);
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
	 * @param isTrack  是否是订单追踪入口进入
	 * @param orderId  订单id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/myAccountOrder/turnToReturn")
	public String turnToReturn(@RequestParam(value = "isTrack") String isTrack,
			@RequestParam(value = "orderId") Long orderId, Model model) {
		if(null==orderId){
			return "/errors/500";
		}
		// 查询订单
		SalesOrderCommand saleOrder = orderManager.findOrderById(orderId, null);
		if (saleOrder == null) {
			return "/errors/500";
		}
		for (OrderLineCommand line : saleOrder.getOrderLines()) {
			String properties = line.getSaleProperty();
			List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
			line.setSkuPropertys(propList);
			
		}
		List<OrderLineCommand> saleOrderLine = saleOrder.getOrderLines();
		List<ReturnLineViewCommand> soReturnLineViewList = new ArrayList<ReturnLineViewCommand>();
		for (OrderLineCommand line : saleOrderLine) {
			ReturnLineViewCommand lineView = new ReturnLineViewCommand();
			if (null != line.getType() && line.getType() != 0) {
				// 查询 当前订单行 已经退过货的商品个数（退换货状态为已完成)
				Integer count = soReturnApplicationManager
						.countCompletedAppsByPrimaryLineId(line.getId());
				lineView.setCount(count);
				lineView.setOrderLineCommand(line);
				soReturnLineViewList.add(lineView);
			}
		}

		model.addAttribute("soReturnLineVo", soReturnLineViewList);

		model.addAttribute("salesOrder", saleOrder);
		
		if (!isTrack.equals("isTrack")) {
			return "return.return-line";
		} else if(isTrack.equals("isTrack")){
			return "return.return-line-track";
		}else {
			return "/errors/500";
		}
	}
    
	
	// 退货详情
		@RequestMapping(value = "/myAccountOrder/returnDetail")
		public String showReturnDetail(
				@RequestParam(value = "isTrack") String isTrack,
				@RequestParam(value = "orderId") String orderId, Model model,
				HttpServletRequest request) {

			// 查询订单的退货详情查询的应该是最近一笔退货单
			SoReturnApplication app = soReturnApplicationManager
					.findLastApplicationByOrderId(Long.valueOf(orderId));
			List<ReturnLineCommand> lineList = soReturnLineManager
					.findSoReturnLinesByReturnOrderId(app.getId());
			Integer returnCount = 0;
			for (ReturnLineCommand line : lineList) {
				returnCount += line.getQty();
				String reason = line.getReturnReason();
				if (reason.equals("我改变主意了")) {
					line.setReturnReason(SoReturnConstants.CHEANGE_MIND);
				} else if (reason.equals("商品质量有问题")) {
					line.setReturnReason(SoReturnConstants.DAMAGED_GOOD);
				} else if (reason.equals("商品包装破损")) {
					line.setReturnReason(SoReturnConstants.DAMAGED_PACKAGE);
				} else if (reason.equals("尺码不合适")) {
					line.setReturnReason(SoReturnConstants.SIZE_UNMATCH);
				} else if (reason.equals("颜色/款式与商品不符")) {
					line.setReturnReason(SoReturnConstants.PRODUCT_UNMATCH);
				}else if(reason.equals("其他原因")){
					line.setReturnReason(SoReturnConstants.OTHER_REASON);
				}
			}

			SalesOrderCommand salesOrder = orderManager.findOrderById(Long
					.valueOf(orderId), null);
			//如果是银联、cod
			Boolean isCod=false;
			if(salesOrder.getPayment()==1||salesOrder.getPayment()==320){
				isCod=true;
			}
			for (OrderLineCommand line : salesOrder.getOrderLines()) {
				String properties = line.getSaleProperty();
				List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
				line.setSkuPropertys(propList);
			}
			ReturnApplicationViewCommand appViewCommand=new ReturnApplicationViewCommand();
			appViewCommand.setApp(app);
			appViewCommand.setLineList(lineList);
			appViewCommand.setReturnCount(returnCount);
			appViewCommand.setIsCod(isCod);
			appViewCommand.setOrderLines(salesOrder.getOrderLines());
			model.addAttribute("returnApplicationViewCommand", salesOrder.getOrderLines());
			if (isTrack.equals("isTrack")) {
				return "return.returnTrackDetail";
			} else {
				return "return.returnDetail";
			}

		}

}

/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.order.validator;

import java.util.Date;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.command.delivery.ContactDeliveryCommand;
import com.baozun.nebula.model.delivery.AreaDeliveryMode;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.model.salesorder.Consignee;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.manager.SdkDeliveryAreaManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.web.controller.order.form.InvoiceInfoSubForm;
import com.baozun.nebula.web.controller.order.form.OrderForm;
import com.baozun.nebula.web.controller.order.form.ShippingInfoSubForm;
import com.feilong.core.RegexPattern;
import com.feilong.core.date.DateUtil;
import com.feilong.core.util.RegexUtil;

/**
 * 对用户提交的order 信息进行校验.
 *
 * @author weihui.tang
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年4月28日 下午9:31:45
 * @since 5.3.1
 */
public class OrderFormValidator implements Validator{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderFormValidator.class);
	
	@Autowired
	private OrderManager orderManager;
	@Autowired
	private SdkDeliveryAreaManager	deliveryAreaManager;
	
    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    @Override
    public boolean supports(Class<?> clazz){
        return OrderForm.class.isAssignableFrom(clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    @Override
    public void validate(Object target,Errors errors){
        OrderForm orderForm = (OrderForm) target;
        
        //验证订单form流程，流程模板模式，如果不需要某一项验证，就继承空方法
        validateShippingInfoSubForm(orderForm,errors);
        validateEmail(orderForm,errors);
        validateMobile(orderForm,errors);
        validatePaymentType(orderForm,errors);
 		validateCouponInfoSubForm(orderForm,errors);
 		validateInvoiceInfoSubForm(orderForm, errors);

    }
    
    protected void validatePaymentType(OrderForm orderForm,Errors errors){
    	// 支付信息验证
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "paymentInfoSubForm.paymentType", "paymentType.field.required");// 支付方式
    }
    
    protected void validateEmail(OrderForm orderForm,Errors errors){
    	// 邮箱有效性检查
 		if (!errors.hasFieldErrors("shippingInfoSubForm.email")){
			if (!EmailValidator.getInstance().isValid(orderForm.getShippingInfoSubForm().getEmail())){
				errors.rejectValue("shippingInfoSubForm.email", "member.email.error");
			}
		}
    }
    
    protected void validateMobile(OrderForm orderForm,Errors errors){
    	// 手机有效性检查
 		if (!errors.hasFieldErrors("shippingInfoSubForm.mobile")){
			if (!RegexUtil.matches(RegexPattern.MOBILEPHONE, orderForm.getShippingInfoSubForm().getMobile())){
				errors.rejectValue("shippingInfoSubForm.mobile", "member.mobile.error");
			}
		}
    }
    
    protected void validateShippingInfoSubForm(OrderForm orderForm,Errors errors){
    	// 地址信息数据
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.name", "name.field.required");// 收货人姓名
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.buyerName", "buyerName.field.required");// 收货人购买人姓名
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.countryId", "countryId.field.required");// 国家
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.provinceId", "provinceId.field.required");// 省
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.cityId", "cityId.field.required");// 城市
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.areaId", "areaId.field.required");// 区
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.townId", "townId.field.required");// 县
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.address", "address.field.required");// 地址
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.postcode", "postcode.field.required");// 邮编
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.mobile", "mobile.field.required");// 收货人手机
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.email", "email.field.required");// 收货人邮箱
		
		ContactDeliveryCommand deliveryCommand = deliveryAreaManager.findContactDeliveryByDeliveryAreaCode(getDeliveryAreaCode(orderForm.getShippingInfoSubForm()));
		String appointType = orderForm.getShippingInfoSubForm().getAppointType();
		String dateFormat = "HH:mm:ss";
		Date now = DateUtil.toDate(DateUtil.toString(new Date(), dateFormat), dateFormat);
		if(com.feilong.core.Validator.isNotNullOrEmpty(deliveryCommand)){
			if(Consignee.CONSIGNEE_APPOINT_COMMON_TYPE.equals(appointType)){
				if (AreaDeliveryMode.YES.equals(deliveryCommand.getCommonDelivery()) 
						&& com.feilong.core.Validator.isNotNullOrEmpty(deliveryCommand.getCommonDeliveryStartTime())
						&& com.feilong.core.Validator.isNotNullOrEmpty(deliveryCommand.getCommonDeliveryEndTime())) {
					// 时间是否允许
					Date beginTime = DateUtil.toDate(deliveryCommand.getCommonDeliveryStartTime(),dateFormat);
					Date endTime = DateUtil.toDate(deliveryCommand.getCommonDeliveryEndTime(),dateFormat);
					if (!DateUtil.isInTime(now, beginTime, endTime)){
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.appointType", "common.delivery.not.in.time");
					}
				} else {
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.appointType", "appointType.not.support.common");
				}
			}
			
			if(Consignee.CONSIGNEE_APPOINT_FIRSTDAY_TYPE.equals(appointType)){
				if (AreaDeliveryMode.YES.equals(deliveryCommand.getFirstDayDelivery()) 
						&& com.feilong.core.Validator.isNotNullOrEmpty(deliveryCommand.getFirstDeliveryStartTime())
						&& com.feilong.core.Validator.isNotNullOrEmpty(deliveryCommand.getFirstDeliveryEndTime())) {
					// 时间是否允许
					Date beginTime = DateUtil.toDate(deliveryCommand.getFirstDeliveryStartTime(),dateFormat);
					Date endTime = DateUtil.toDate(deliveryCommand.getFirstDeliveryEndTime(),dateFormat);
					if (!DateUtil.isInTime(now, beginTime, endTime)){
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.appointType", "firstday.delivery.not.in.time");
					}
				} else {
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.appointType", "appointType.not.support.firstday");
				}
			}
			

			if(Consignee.CONSIGNEE_APPOINT_SECONDDAY_TYPE.equals(appointType)){
				// 是否支持次日达
				if (AreaDeliveryMode.YES.equals(deliveryCommand.getSecondDayDelivery()) 
						&& com.feilong.core.Validator.isNotNullOrEmpty(deliveryCommand.getSecondDeliveryStartTime())
						&& com.feilong.core.Validator.isNotNullOrEmpty(deliveryCommand.getSecondDeliveryEndTime())) {
					// 时间是否允许
					Date beginTime = DateUtil.toDate(deliveryCommand.getSecondDeliveryStartTime(),dateFormat);
					Date endTime = DateUtil.toDate(deliveryCommand.getSecondDeliveryEndTime(),dateFormat);
					if (!DateUtil.isInTime(now, beginTime, endTime)){
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.appointType", "second.delivery.not.in.time");
					}
				} else {
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.appointType", "appointType.not.support.second");
				}
			}

			if(Consignee.CONSIGNEE_APPOINT_THIRDDAY_TYPE.equals(appointType)){
				if (AreaDeliveryMode.YES.equals(deliveryCommand.getThirdDayDelivery()) 
						&& com.feilong.core.Validator.isNotNullOrEmpty(deliveryCommand.getThirdDeliveryStartTime())
						&& com.feilong.core.Validator.isNotNullOrEmpty(deliveryCommand.getThirdDeliveryEndTime())) {
					// 时间是否允许
					Date beginTime = DateUtil.toDate(deliveryCommand.getThirdDeliveryStartTime(),dateFormat);
					Date endTime = DateUtil.toDate(deliveryCommand.getThirdDeliveryEndTime(),dateFormat);
					if (!DateUtil.isInTime(now, beginTime, endTime)){
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.appointType", "third.delivery.not.in.time");
					}
				} else {
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.appointType", "appointType.not.support.third");
				}
			}
			
			if(SalesOrder.SO_PAYMENT_TYPE_COD.equals(orderForm.getPaymentInfoSubForm().getPaymentType())
					&& !AreaDeliveryMode.YES.equals(deliveryCommand.getSupport_COD())){
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.appointType", "cod.not.support");
			}
		}
		
    }
    
    protected void validateCouponInfoSubForm(OrderForm orderForm,Errors errors){
    	// 优惠券信息验证
 		// 优惠券不为空时，需要验证优惠券是否可用
 		if (com.feilong.core.Validator.isNotNullOrEmpty(orderForm.getCouponInfoSubForm())) {
 			if (com.feilong.core.Validator.isNotNullOrEmpty(orderForm.getCouponInfoSubForm().getCouponCode())) {
 	 			// FIXME 优惠券有效性检查
 				// nebula暂无用户绑定优惠券，具体优惠券绑定相关验证在商场端ajax校验
 				PromotionCouponCode couponCode=orderManager.validCoupon(orderForm.getCouponInfoSubForm().getCouponCode());//數據庫校驗
 				if(com.feilong.core.Validator.isNullOrEmpty(couponCode)){//無效
 					errors.rejectValue("couponInfoSubForm.couponCode", "couponCode.unavalible.error");
 				}
 				
 	 		}
 		}	
    }
    
    protected void validateInvoiceInfoSubForm(OrderForm orderForm,Errors errors){
    	// 发票信息验证
 		// 需要发票时候，验证发票相关信息
 		if (com.feilong.core.Validator.isNotNullOrEmpty(orderForm.getInvoiceInfoSubForm())) {
 		    //是否需要发票信息不能为空
 			if (com.feilong.core.Validator.isNotNullOrEmpty(orderForm.getInvoiceInfoSubForm().getIsNeedInvoice())) {
 				if (orderForm.getInvoiceInfoSubForm().getIsNeedInvoice()) {// 是否需要发票
 				    //判断发票类型
 					if (InvoiceInfoSubForm.INVOICE_TYPE_COMPANY.equals(orderForm.getInvoiceInfoSubForm().getInvoiceType())) {// 发票类型  个人还是公司  公司要判断抬头
 						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "invoiceInfoSubForm.invoiceTitle", "invoiceTitle.field.required");// 发票抬头
 					}
 	 			}
 			}
		}
    }  
    
    protected String getDeliveryAreaCode(ShippingInfoSubForm shippingInfoSubForm){
    	return shippingInfoSubForm.getAreaId() + "";
    }

}

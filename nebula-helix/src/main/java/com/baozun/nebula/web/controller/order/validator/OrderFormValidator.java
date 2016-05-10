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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.manager.OrderManager;
import com.baozun.nebula.web.controller.order.form.OrderForm;
import com.baozun.nebula.web.controller.order.resolver.SalesorderResult;
import com.feilong.core.RegexPattern;
import com.feilong.core.util.RegexUtil;

/**
 * 对用户提交的order 信息进行校验.
 *
 * @author weihui.tang
 * @author feilong
 * @version 5.3.1 2016年4月28日 下午9:31:45
 * @since 5.3.1
 */
public class OrderFormValidator implements Validator{

	
	@Autowired
	private OrderManager orderManager;
	
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
        
        // 地址信息数据
 		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.name", "name.field.required");// 收货人姓名
 		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.buyerName", "buyerName.field.required");// 收货人购买人姓名
 		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.countryId", "countryId.field.required");// 国家
 		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.provinceId", "provinceId.field.required");// 省
 		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.cityId", "cityId.field.required");// 城市
 		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.areaId", "areaId.field.required");// 区
 		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.townId", "townId.field.required");// 县
 		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.address", "address.field.required");// 地址
 		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.postcode", "postcode.field.required");// 邮编
 		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.mobile", "mobile.field.required");// 收货人手机
 		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingInfoSubForm.email", "email.field.required");// 收货人邮箱
 		// 邮箱有效性检查
 		if (!errors.hasFieldErrors("shippingInfoSubForm.email")){
			if (!RegexUtil.matches(RegexPattern.EMAIL,orderForm.getShippingInfoSubForm().getEmail())){
				errors.rejectValue("shippingInfoSubForm.email", "member.email.error");
			}
		}
 		// 手机有效性检查
 		if (!errors.hasFieldErrors("shippingInfoSubForm.mobile")){
			if (!RegexUtil.matches(RegexPattern.MOBILEPHONE, orderForm.getShippingInfoSubForm().getMobile())){
				errors.rejectValue("shippingInfoSubForm.mobile", "member.mobile.error");
			}
		}
 		// 支付信息验证
 		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "paymentInfoSubForm.paymentType", "paymentType.field.required");// 支付方式
 		
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
 		 
 		// 发票信息验证
 		// 需要发票时候，验证发票相关信息
 		if (com.feilong.core.Validator.isNotNullOrEmpty(orderForm.getInvoiceInfoSubForm())) {
 			if (com.feilong.core.Validator.isNotNullOrEmpty(orderForm.getInvoiceInfoSubForm().getIsNeedInvoice())) {
 				if (orderForm.getInvoiceInfoSubForm().getIsNeedInvoice()) {
 					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "invoiceInfoSubForm.invoiceTitle", "invoiceTitle.field.required");// 发票抬头
 					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "invoiceInfoSubForm.invoiceContent", "invoiceContent.field.required");// 发票内容
 	 			}
 			}
		}

    }
    
    
    /**
     * 购物车和优惠券促销验证
     * 
     * @param cartCommand
     * @return
     */
    public SalesorderResult validateWithShoppingCart(ShoppingCartCommand cartCommand,String coupon){
    	/** 校驗購物車*/
    	SalesorderResult salesorderResult = checkShoppingCart(cartCommand);
    	if (null != salesorderResult) {
    		return salesorderResult;
    	}
    	/** 校驗优惠券促销*/
    	salesorderResult = checkCoupon(coupon, cartCommand);
    	if (null != salesorderResult) {
    		return salesorderResult;
    	}
    	return SalesorderResult.SUCCESS;
    }
    
    
    
	/**
	 *  校驗購物車
	 *  失败返回错误，正确返回null
	 **/
	private SalesorderResult checkShoppingCart(ShoppingCartCommand cartCommand){
		if(com.feilong.core.Validator.isNullOrEmpty(cartCommand) || com.feilong.core.Validator.isNullOrEmpty(cartCommand.getShoppingCartLineCommands())){//購物車不能為空
			return SalesorderResult.SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
		}
		return null;
	}
    
	/** 
	 * 校驗優惠券是否有效  success 有效，其他 無效
	 * 失败返回错误，正确返回null
	 **/
	private SalesorderResult checkCoupon(String coupon,ShoppingCartCommand cartCommand){
		if(com.feilong.core.Validator.isNullOrEmpty(cartCommand.getCartPromotionBriefList())){//無效
			return SalesorderResult.COUPON_NOT_AVALIBLE;
		}
		for(PromotionBrief pro : cartCommand.getCartPromotionBriefList()){//從活動中取記錄校驗
			if(com.feilong.core.Validator.isNotNullOrEmpty(pro.getDetails())){
				for(PromotionSettingDetail settingDetail : pro.getDetails()){//遍曆活動詳情
					if(com.feilong.core.Validator.isNullOrEmpty(settingDetail.getCouponCodes())){//先校驗整單優惠券有沒有
						if(com.feilong.core.Validator.isNotNullOrEmpty(settingDetail.getAffectSKUDiscountAMTList())){
							for(PromotionSKUDiscAMTBySetting skuSetting : settingDetail.getAffectSKUDiscountAMTList()){//遍曆商品行優惠記錄
								if(com.feilong.core.Validator.isNullOrEmpty(skuSetting.getCouponCodes())){//如果整單優惠券沒有，校驗商品行優惠券
									continue;
								}
								if(skuSetting.getCouponCodes().contains(coupon)){
									return null;
								}
							}
						}
						continue;
					}
					if(settingDetail.getCouponCodes().contains(coupon)){
						return null;
					}
				}
			}
		}
		return SalesorderResult.COUPON_NOT_AVALIBLE;
		
	}
	

    

}

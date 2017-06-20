package com.baozun.nebula.sdk.utils;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.payment.manager.ReservedPaymentType;

public class BankCodeConvertUtil {

	public static final String UNDERLINE = "_";
	
	public static String getPayTypeDetail(String bankCode,String payType) {
	    	StringBuffer pay_bank = new StringBuffer();
            
		if(SalesOrder.SO_PAYMENT_TYPE_COD.equals(payType)){
		    pay_bank.append("货到付款");
		}
		
		if(SalesOrder.SO_PAYMENT_TYPE_ALIPAY.equals(payType)){
		    pay_bank.append("支付宝支付");
		}
		if(SalesOrder.SO_PAYMENT_TYPE_NETPAY.equals(payType)){
		    pay_bank.append("支付宝支付");
		}
		if(SalesOrder.SO_PAYMENT_TYPE_WECHAT.equals(payType)){
		    pay_bank.append("微信支付");
		}
		
		if(SalesOrder.SO_PAYMENT_TYPE_UNIONPAY.equals(payType)){
		    pay_bank.append("银联在线支付");
		}
	        if(StringUtils.isBlank(bankCode)){
	            return pay_bank.toString();
	        }else{
	            pay_bank.append(UNDERLINE);
		        switch (BankDesc.valueOf(bankCode)) {
		        case BOCB2C:
		            pay_bank.append("中国银行");
		            break;
		        case ICBCB2C:
		            pay_bank.append("中国工商银行");
		            break;
		        case CMB:
		            pay_bank.append("招商银行");
		            break;
		        case CCB:
		            pay_bank.append("中国建设银行");
		            break;
		        case ABC:
		            pay_bank.append("中国农业银行");
		            break;
		        case SPDB:
		            pay_bank.append("上海浦东发展银行");
		            break;
		        case CIB:
		            pay_bank.append("兴业银行");
		            break;
		        case GDB:
		            pay_bank.append("广东发展银行");
		            break;
		        case CMBC:
		            pay_bank.append("中国民生银行");
		            break;
		        case COMM:
		            pay_bank.append("交通银行");
		            break;
		        case CITIC:
		            pay_bank.append("中信银行");
		            break;
		        case HZCBB2C:
		            pay_bank.append("杭州银行");
		            break;
		        case SHBANK:
		            pay_bank.append("上海银行");
		            break;
		        case NBBANK:
		            pay_bank.append("宁波银行");
		            break;
		        case SPABANK:
		            pay_bank.append("平安银行");
		            break;
		        case POSTGC:
		            pay_bank.append("中国邮政储蓄银行");
		            break;
		        }
		        return pay_bank.toString();
	        }
	}
	
	
//	public static String getPayTypeDetail(String bankCode,int payType) {
//		String payTypeDetail = "支付宝支付";
//	
//		if(payType == ReservedPaymentType.UNIONPAY){
//				payTypeDetail = "银联在线支付";
//				return payTypeDetail;
//		}
//		
//		if(payType == ReservedPaymentType.CHINAPNR){
//			payTypeDetail = "汇付天下支付";
//			return payTypeDetail;
//		}
//		
//		if(payType == 1){
//			return payTypeDetail;	
//		}
//		
//		if(payType == ReservedPaymentType.ALIPAY_CREDIT_INT_V || payType == ReservedPaymentType.ALIPAY_CREDIT_INT_M
//				|| payType == ReservedPaymentType.ALIPAY_CREDIT){
//				switch (payType) {
//				case ReservedPaymentType.ALIPAY_CREDIT:
//					payTypeDetail = "支付宝国内信用卡支付";
//					break;
//				case ReservedPaymentType.ALIPAY_CREDIT_INT_V:
//					payTypeDetail = "支付宝VISA信用卡支付";
//					break;
//				case ReservedPaymentType.ALIPAY_CREDIT_INT_M:
//					payTypeDetail = "支付宝国际信用卡支付";
//					break;
//				default:
//					break;
//				}
//			return payTypeDetail;
//		}
//		
//		StringBuffer bankDesc = new StringBuffer(payTypeDetail).append(UNDERLINE);
//		if(StringUtils.isBlank(bankCode)){
//			return payTypeDetail;
//		}
//		switch (BankDesc.valueOf(bankCode)) {
//		case BOCB2C:
//			bankDesc.append("中国银行");
//			break;
//		case ICBCB2C:
//			bankDesc.append("中国工商银行");
//			break;
//		case CMB:
//			bankDesc.append("招商银行");
//			break;
//		case CCB:
//			bankDesc.append("中国建设银行");
//			break;
//		case ABC:
//			bankDesc.append("中国农业银行");
//			break;
//
//		case SPDB:
//			bankDesc.append("上海浦东发展银行");
//			break;
//		case CIB:
//			bankDesc.append("兴业银行");
//			break;
//		case GDB:
//			bankDesc.append("广东发展银行");
//			break;
//		case CMBC:
//			bankDesc.append("中国民生银行");
//			break;
//		case COMM:
//			bankDesc.append("交通银行");
//			break;
//		case CITIC:
//			bankDesc.append("中信银行");
//			break;
//		case HZCBB2C:
//			bankDesc.append("杭州银行");
//			break;
//		case CEB:
//			bankDesc.append("中国光大银行");
//			break;
//		case SHBANK:
//			bankDesc.append("上海银行");
//			break;
//		case NBBANK:
//			bankDesc.append("宁波银行");
//			break;
//		case SPABANK:
//			bankDesc.append("平安银行");
//			break;
//		case BJRCB:
//			bankDesc.append("北京农商银行");
//			break;
//		case FDB:
//			bankDesc.append("富滇银行");
//			break;
//		}
//		return bankDesc.toString();
//	}
	
	public static String getPayTypeDetail(String bankCode,int payType,boolean exist) {
		String payTypeDetail = "支付宝支付";
	
		if(payType == ReservedPaymentType.UNIONPAY){
			if(!exist){
				payTypeDetail = "银联在线支付";
				return payTypeDetail;
			}
			return StringUtils.EMPTY;
		}
		
		if(payType == ReservedPaymentType.CHINAPNR){
			if(!exist){
				payTypeDetail = "汇付天下支付";
				return payTypeDetail;
			}
			return StringUtils.EMPTY;
		}
		
		if(payType == 1){
			if(!exist){
				return payTypeDetail;	
			}
			return StringUtils.EMPTY;
		}
		
		if(payType == ReservedPaymentType.ALIPAY_CREDIT_INT_V || payType == ReservedPaymentType.ALIPAY_CREDIT_INT_M
				|| payType == ReservedPaymentType.ALIPAY_CREDIT){
			if(!exist){
				switch (payType) {
				case ReservedPaymentType.ALIPAY_CREDIT:
					payTypeDetail = "支付宝国内信用卡支付";
					break;
				case ReservedPaymentType.ALIPAY_CREDIT_INT_V:
					payTypeDetail = "支付宝VISA信用卡支付";
					break;
				case ReservedPaymentType.ALIPAY_CREDIT_INT_M:
					payTypeDetail = "支付宝国际信用卡支付";
					break;
				default:
					break;
				}
			}else{ 
				payTypeDetail = StringUtils.EMPTY;
			}
			return payTypeDetail;
		}
		
		StringBuffer bankDesc = null;
		if(!exist){
			bankDesc = new StringBuffer(payTypeDetail);
		}else{
			bankDesc = new StringBuffer();
		}
		bankDesc.append(UNDERLINE);
		if(StringUtils.isBlank(bankCode)){
			return payTypeDetail;
		}
		switch (BankDesc.valueOf(bankCode)) {
		case BOCB2C:
			bankDesc.append("中国银行");
			break;
		case ICBCB2C:
			bankDesc.append("中国工商银行");
			break;
		case CMB:
			bankDesc.append("招商银行");
			break;
		case CCB:
			bankDesc.append("中国建设银行");
			break;
		case ABC:
			bankDesc.append("中国农业银行");
			break;

		case SPDB:
			bankDesc.append("上海浦东发展银行");
			break;
		case CIB:
			bankDesc.append("兴业银行");
			break;
		case GDB:
			bankDesc.append("广东发展银行");
			break;
		case CMBC:
			bankDesc.append("中国民生银行");
			break;
		case COMM:
			bankDesc.append("交通银行");
			break;
		case CITIC:
			bankDesc.append("中信银行");
			break;
		case HZCBB2C:
			bankDesc.append("杭州银行");
			break;
		case CEB:
			bankDesc.append("中国光大银行");
			break;
		case SHBANK:
			bankDesc.append("上海银行");
			break;
		case NBBANK:
			bankDesc.append("宁波银行");
			break;
		case SPABANK:
			bankDesc.append("平安银行");
			break;
		case BJRCB:
			bankDesc.append("北京农商银行");
			break;
		case FDB:
			bankDesc.append("富滇银行");
			break;
		}
		return bankDesc.toString();
	}

	enum BankDesc {
		BOCB2C, // 中国银行
		ICBCB2C, // 中国工商银行
		CMB, // 招商银行
		CCB, // 中国建设银行
		ABC, // 中国农业银行
		SPDB, // 上海浦东发展银行
		CIB, // 兴业银行
		GDB, // 广东发展银行
		CMBC, // 中国民生银行
		COMM, // 交通银行
		CITIC, // 中信银行
		HZCBB2C, // 杭州银行
		CEB, // 中国光大银行
		SHBANK, // 上海银行
		NBBANK, // 宁波银行
		SPABANK,//平安银行
		BJRCB, // 北京农商银行
		FDB,// 富滇银行
		POSTGC // 中国邮政储蓄银行
	}

	enum PayType {
		Alipay,chinaPnr;
	}

	public static void main(String[] args) {
		System.out.println(PayType.valueOf("Alipay") == PayType.Alipay);
	}
}

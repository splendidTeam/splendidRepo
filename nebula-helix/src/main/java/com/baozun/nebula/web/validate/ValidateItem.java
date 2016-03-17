package com.baozun.nebula.web.validate;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.sdk.utils.RegulareExpUtils;

public class ValidateItem {

	public enum ValidType{
		notnull,	//非空验证
		range,		//区间
		reg,		//正则
		email,		//邮箱
		mobile,		//手机
		date		//日期
		} ;
	
	public enum DataType{
		string,	//字符串
		num, //整数
		date	//日期
		} ;
		
	/**
	 * 验证类型
	 */
	private ValidType validType;
	
	/**
	 * 数据类型
	 */
	private DataType dataType;
	
	/**
	 * 验证参数
	 * notnull时，此值为空
	 * range时，此值为最大最小范围如  10-20
	 * reg时，此值为正则表达式
	 */
	private String parameter;
	
	public ValidateItem(ValidType vt){
		validType=vt;	
	}
	
	public ValidateItem(ValidType vt,String parameter){
		validType=vt;
		this.parameter=parameter;
	}
	
	
	
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * 查找数据类型
	 * @param value
	 */
	private DataType findDataType(Object value){
		
		if(value instanceof Number){
			dataType=DataType.num;
		}
		else if(value instanceof Date){
			dataType=DataType.date;
		}
		else if(value instanceof String){
			dataType=DataType.string;
		}
		
		return dataType;
	}
	
		

	
	private boolean validDate(Object value){
		
		String date=(String)value;
		
		try{
			SimpleDateFormat sdf=new SimpleDateFormat(parameter);
			
			sdf.parse(date);
			
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	

	
	
	private boolean validNull(Object value){
		
		if(value==null){
			return false;
		}
		else if(dataType==DataType.string){
			String str=(String)value;
			
				
			if(StringUtils.isBlank(str))
					return false;
		
		}
		
		
		return true;
	}
	
	private boolean validRange(Object value){
		
		
		String[] params=parameter.split("-");
		
		if(dataType==DataType.num){
			Number num=(Number)value;
			if(num.longValue()<Long.parseLong(params[0])){
				return false;
			}
			else if(num.longValue()>Long.parseLong(params[1])){
				return false;
			}
			
		}
		else if(dataType==DataType.string){
			
			String str=(String)value;
			if(StringUtils.isNotBlank(str)&&str.length()<Long.parseLong(params[0])){
				return false;
			}
			else if(StringUtils.isNotBlank(str)&&str.length()>Long.parseLong(params[1])){
				return false;
			}
			
		}
		
		return true;
		
		
	}
	
	private boolean validReg(Object value){
		
		if(dataType==DataType.string){
			String str=(String)value;
			if(StringUtils.isNotBlank(str)&&!str.matches(parameter))
				return false;
		}
		
		return true;
	}
	
	private boolean validEmail(Object value){
		
		if(dataType==DataType.string){
			String str=(String)value;
			if(StringUtils.isNotBlank(str)&&!str.matches(RegulareExpUtils.EMAIL_REG))
				return false;
		}
		
		return true;
	}
	
	private boolean validMobile(Object value){
		
		if(dataType==DataType.string){
			String str=(String)value;
			if(StringUtils.isNotBlank(str)&&!str.matches(RegulareExpUtils.MOBILE_REG))
				return false;
		}
		
		return true;
	}
	
	/**
	 * 验证数据
	 * @param value
	 * @return
	 */
	public ValidType valid(Object value){
		
		
		findDataType(value);
		
		if(validType==ValidType.notnull&&!validNull(value)){
			return ValidType.notnull;
		}
		else if(validType==ValidType.range&&!validRange(value)){
			return ValidType.range;
		}
		else if(validType==ValidType.reg&&!validReg(value)){
			return ValidType.reg;
		}
		else if(validType==ValidType.email&&!validEmail(value)){
			return ValidType.reg;
		}
		else if(validType==ValidType.mobile&&!validMobile(value)){
			return ValidType.reg;
		}
		else if(validType==ValidType.date&&!validDate(value)){
			return ValidType.date;
		}		
		
		return null;
	}
	
}

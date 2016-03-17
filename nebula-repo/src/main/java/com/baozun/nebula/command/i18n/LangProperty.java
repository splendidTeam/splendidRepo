package com.baozun.nebula.command.i18n;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;

import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utils.spring.SpringUtil;

public abstract class LangProperty implements Serializable {

	private static final long serialVersionUID = 8091644538996183070L;
	
	public static void I18nPropertyCopy(Object source,Object target) {
		if(source == null || target == null){
			return ;
		}
		Class<?> scls= source.getClass();
		Field[] sFields = scls.getDeclaredFields();
		Class<?> tcls= target.getClass();
		Field[] tFields = tcls.getDeclaredFields();
		for (Field sfield : sFields) {
			String sname  = sfield.getName();
			if (Modifier.isFinal(sfield.getModifiers())
					|| Modifier.isStatic(sfield.getModifiers())){
				continue;
			}
			Object sValue = null;
			try {
				sValue = PropertyUtils.getProperty(source, sname);
			} catch (Exception e) {
				throw new RuntimeException("获取源对象属性的值出错:",e);
			}
			if(sValue == null){
				continue;
			}
			//不复制
			if(sValue instanceof LangProperty){
				continue;
			}
			for (Field tfield : tFields) {
				String tname  = tfield.getName();
				if(sname.equals(tname)){
					try {
						 PropertyUtils.setProperty(target, tname, sValue);
					} catch (Exception e) {
						throw new RuntimeException("设置目标对象属性的值出错:",e);
					}
				}
			}
		}
		
	}
	
	public static void I18nPropertyCopyToSource(Object source,Object target) {
		if(source == null || target == null){
			return ;
		}
		Class<?> scls= source.getClass();
		Field[] sFields = scls.getDeclaredFields();
		Class<?> tcls= target.getClass();
		Field[] tFields = tcls.getDeclaredFields();
		for (Field sfield : sFields) {
			String sname  = sfield.getName();
			if (Modifier.isFinal(sfield.getModifiers())
					|| Modifier.isStatic(sfield.getModifiers())){
				continue;
			}
			Object sValue = null;
			try {
				sValue = PropertyUtils.getProperty(source, sname);
			} catch (Exception e) {
				throw new RuntimeException("获取源对象属性的值出错:",e);
			}
			if(sValue == null){
				continue;
			}
			for (Field tfield : tFields) {
				String tname  = tfield.getName();
				try {
					Class<?> cls = tfield.getType();
					if(cls.equals(LangProperty.class)){
						continue;
					}
					if(sname.equals(tname)){
						PropertyUtils.setProperty(target, tname, sValue);
					}
				} catch (Exception e) {
					throw new RuntimeException("设置目标对象属性的值出错:",e);
				}
			}
		}
		
	}
	
	public static void I18nPropertyCopyExcelToLang(Object source,Object target) {
		if(source == null || target == null){
			return ;
		}
		Class<?> scls= source.getClass();
		Field[] sFields = scls.getDeclaredFields();
		Class<?> tcls= target.getClass();
		Field[] tFields = tcls.getDeclaredFields();
		for (Field sfield : sFields) {
			String sname  = sfield.getName();
			if (Modifier.isFinal(sfield.getModifiers())
					|| Modifier.isStatic(sfield.getModifiers())){
				continue;
			}
			if(sname.equals("code")){
				continue;
			}
			Object sValue = null;
			try {
				sValue = PropertyUtils.getProperty(source, sname);
			} catch (Exception e) {
				throw new RuntimeException("获取源对象属性的值出错:",e);
			}
			if(sValue == null){
				continue;
			}
			for (Field tfield : tFields) {
				String tname  = tfield.getName();
				try {
					if(sname.equals(tname)){
						PropertyUtils.setProperty(target, tname, sValue);
					}
				} catch (Exception e) {
					throw new RuntimeException("设置目标对象属性的值出错:",e);
				}
			}
		}
		
	}
	
	public static boolean getI18nOnOff() {
		SdkMataInfoManager sdkMataInfoManager = (SdkMataInfoManager) SpringUtil.getBean("sdkMataInfoManager");
		String i18n_on_off =sdkMataInfoManager.findValue("i18n.on.off");
		if(i18n_on_off == null){
			Properties pro =  ProfileConfigUtil.findPro("config/metainfo.properties");
			i18n_on_off = pro.getProperty("i18n.on.off");
		}
		if(i18n_on_off == null || i18n_on_off.equals("false") || !i18n_on_off.equals("true")){
			return false;
		}
		return true;
		
	}
}


package com.baozun.nebula.sdk.manager.cms;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;


@Transactional
@Service("sdkCmsCommonManager") 
public class SdkCmsCommonManagerImpl implements SdkCmsCommonManager{
	
	private final static Logger log = LoggerFactory.getLogger(SdkCmsCommonManagerImpl.class);

	/**
	 * 静态base标识
	 */
	public final static String STATIC_BASE_CHAR="#{staticbase}";
	
	/**
	 * 页面base标识
	 */
	public final static String PAGE_BASE_CHAR="#{pagebase}";
	
	/**
	 * 图片base标识
	 */
	public final static String IMG_BASE_CHAR="#{imgbase}";
	
	
	@Override
	public String processTemplateBase(String html) {
		if(StringUtils.isBlank(html)){
			return "";
		}
		
		Properties properties=ProfileConfigUtil.findPro("config/metainfo.properties");
		
		String pagebase=StringUtils.trim(properties.getProperty("page.base"));
		
		String staticbase=StringUtils.trim(properties.getProperty("static.domain.base"));
		
		String imgbase=StringUtils.trim(properties.getProperty("upload.img.domain.base"));
		log.info("pagebase is {}, staticbase is {} and imgbase is {}", pagebase, staticbase, imgbase);
		if(StringUtils.isBlank(pagebase)){
			pagebase="";
		}else if("/".equals(pagebase)){
			pagebase="";
		}else if(pagebase.endsWith("/")){
			pagebase=pagebase.substring(0,pagebase.length()-1);
		}
		
		if(StringUtils.isBlank(staticbase)){
			staticbase="";
		}else if("/".equals(staticbase)){
			staticbase = "";
		}else if(staticbase.endsWith("/")){
			staticbase = staticbase.substring(0, staticbase.length()-1);
		}
		
		if(StringUtils.isBlank(imgbase)){
			imgbase="";
		}else if("/".equals(imgbase)){
			imgbase = "";
		}else if(imgbase.endsWith("/")){
			imgbase = imgbase.substring(0, imgbase.length()-1);
		}
		
		html = html.replace(PAGE_BASE_CHAR, pagebase);
		
		html = html.replace(STATIC_BASE_CHAR, staticbase);
		
		html = html.replace(IMG_BASE_CHAR, imgbase);
		
		return html;
	}

	
}

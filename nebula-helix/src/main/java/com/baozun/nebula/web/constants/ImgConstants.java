package com.baozun.nebula.web.constants;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;

public class ImgConstants {

	/**
	 * 默认图片
	 */
	public static final String DEFAULT_IMG_URL;
	
	/**
	 * 图片根路径，如http://img.xxx.com/
	 */
	public static final String IMG_BASE;
	
	/**
	 * 图片存储根路径，如/home/abc/file/
	 */
	public static final String IMG_STORE_BASE;
	
	/**
	 * 静态资源地址，如/home/abc/file/
	 */
	public static final String STATEIC_BASE;
	
	static{
		Properties pro=ProfileConfigUtil.findPro("config/metainfo.properties");
		
		DEFAULT_IMG_URL=StringUtils.trim(pro.getProperty("defaultNonItemImgUrl"));
		
		IMG_STORE_BASE=StringUtils.trim(pro.getProperty("upload.img.base"));
			
		IMG_BASE=StringUtils.trim(pro.getProperty("upload.img.domain.base"));
		
		STATEIC_BASE=StringUtils.trim(pro.getProperty("static.domain.base"));
		 
	}
}

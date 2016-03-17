package com.baozun.nebula.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

public class ImgUrlTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8777438090844578804L;

	private String imgUrl;
	
	private String size;

	/**
	 * 原图尺寸
	 */
	private static final String SOURCE="source";

	
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		
		StringBuffer sb=new StringBuffer();
	
		try{
			//为null使用默认图片
			if(StringUtils.isBlank(imgUrl)){
				// TODO 
				//sb.append(ImgConstants.DEFAULT_IMG_URL);
			}else{
				//sb.append(ImgConstants.IMG_BASE);
				int index=imgUrl.lastIndexOf(".");
				int index2=imgUrl.lastIndexOf("_");
				
				//如果找到了下划线 "_",截取下划线及之前的部分
				if(index2!=-1){
						
					sb.append(imgUrl.substring(0,index2));
					
				}
				//如果找不到下划线 "_",截取.之前的部分
				else{
					sb.append(imgUrl.substring(0,index));
					
				}
				
				if(!SOURCE.equals(size)){
					sb.append("_");
					sb.append(size);
				}
				sb.append(imgUrl.substring(index));
			}
			out.println(sb.toString());
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		
		return EVAL_PAGE;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	
}

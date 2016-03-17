package com.baozun.nebula.web.taglib;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * @author xianze.zhang
 *@creattime 2013-6-19
 */
public abstract class BaseTag extends BodyTagSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5391637760307916701L;

	/**
	 * 将文字输出到页面
	 * 
	 * @param object
	 */
	public void print(Object object){
		try{
			pageContext.getOut().print(object);
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * 将文字输出到页面
	 * 
	 * @param object
	 */
	public void println(Object object){
		try{
			pageContext.getOut().println(object);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 获得HttpServletRequest
	 * 
	 * @return
	 */
	protected final HttpServletRequest getHttpServletRequest(){
		return (HttpServletRequest) getServletRequest();
	}

	/**
	 * 获得ServletRequest
	 * 
	 * @return
	 */
	protected final ServletRequest getServletRequest(){
		return this.pageContext.getRequest();
	}

	/**
	 * 获得 HttpSession
	 * @return
	 */
	protected final HttpSession getHttpSession(){
		return this.pageContext.getSession();
	}

	/**
	 * 获得HttpServletResponse
	 * @return
	 */
	protected final HttpServletResponse getHttpServletResponse(){
		return (HttpServletResponse) pageContext.getResponse();
	}
	/**
	 * 获得Spring bean对象
	 * @return
	 */
	protected final Object getBean(String name){
		return WebApplicationContextUtils
				.getWebApplicationContext(pageContext.getServletContext()).getBean(name);
	}
	/**
	 * 获得message
	 * @return
	 */
	protected final Object getMessage(String code,Object[] args){
		try {
			return WebApplicationContextUtils
					.getWebApplicationContext(pageContext.getServletContext()).getMessage(code, args, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			// TODO: handle exception
			
		}
		return code;
	}
}
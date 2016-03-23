package com.baozun.nebula.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.baozun.nebula.sdk.manager.impl.SdkCmsModuleInstanceManagerImpl;

public class ModuleTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	private String code;

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			String data = SdkCmsModuleInstanceManagerImpl.moduleMap.get(code);
			out.println(data);
		} catch (Exception e) {
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}

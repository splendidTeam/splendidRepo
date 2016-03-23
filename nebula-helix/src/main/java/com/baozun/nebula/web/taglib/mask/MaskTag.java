/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.taglib.mask;

import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
/**
 * 
 * @author D.C
 * @version 2016/03/03 4:12:31
 */
public class MaskTag extends TagSupport {
	private static final long serialVersionUID = -2259895320368932248L;
	/**
	 * 待mask字符串
	 */
	private String value;
	/**
	 * mask的字符，比如*,#等，默认是*
	 */
	private char maskStr = '*';
	/**
	 * 左侧不马赛克长度
	 */
	private int leftUnmaskLength = 0;
	/**
	 * 右侧不马赛克长度
	 */
	private int rightUnmaskLength = 0;

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			ArrayList<UnmaskRange> ranges = new ArrayList<UnmaskRange>();
			ranges.add(new UnmaskRange(UnmaskRange.START_MODE, this.getLeftUnmaskLength()));
			ranges.add(new UnmaskRange(UnmaskRange.END_MODE, this.getRightUnmaskLength()));
			StringMask mask = new StringMask(ranges, this.getMaskStr());
			out.println(mask.mask(this.getValue()));
		} catch (Exception e) {
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public char getMaskStr() {
		return maskStr;
	}

	public void setMaskStr(char maskStr) {
		this.maskStr = maskStr;
	}

	public int getLeftUnmaskLength() {
		return leftUnmaskLength;
	}

	public void setLeftUnmaskLength(int leftUnmaskLength) {
		this.leftUnmaskLength = leftUnmaskLength;
	}

	public int getRightUnmaskLength() {
		return rightUnmaskLength;
	}

	public void setRightUnmaskLength(int rightUnmaskLength) {
		this.rightUnmaskLength = rightUnmaskLength;
	}

}

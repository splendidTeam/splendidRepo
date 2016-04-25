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

/**
 * 明码段定义
 * @author D.C
 * @date 2015年9月22日 下午3:57:25
 */
public class UnmaskRange {
    /**
     * 从开头开始
     */
	public static final int START_MODE = 0;
	/**
	 * 从结尾开始
	 */
	public static final int END_MODE = 1;

	/**
	 * 起始模式
	 */
	private int mode;
	/**
	 * 明码段长度
	 */
	private int length;

	public UnmaskRange(int mode, int length) {
		this.setMode(mode);
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

}
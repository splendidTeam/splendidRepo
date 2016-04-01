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

package com.baozun.nebula.workflow;
/**
 * 
 * @author D.C
 *
 */
public class WorkflowException extends Exception implements RootCauseAccessor {

	private static final long serialVersionUID = 1L;

	private Throwable rootCause;

	public WorkflowException() {
		super();
	}

	public WorkflowException(String message) {
		super(message);
		this.rootCause = this;
	}

	public WorkflowException(Throwable cause) {
		super(cause);
		if (cause != null) {
			rootCause = findRootCause(cause);
		}
	}

	public WorkflowException(String message, Throwable cause) {
		super(message, cause);
		if (cause != null) {
			rootCause = findRootCause(cause);
		} else {
			rootCause = this;
		}
	}

	private Throwable findRootCause(Throwable cause) {
		Throwable rootCause = cause;
		while (rootCause != null && rootCause.getCause() != null) {
			rootCause = rootCause.getCause();
		}
		return rootCause;
	}

	public Throwable getRootCause() {
		return rootCause;
	}

	public String getRootCauseMessage() {
		if (rootCause != null) {
			return rootCause.getMessage();
		} else {
			return getMessage();
		}
	}
}

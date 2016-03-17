/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
package com.baozun.nebula.command;

import java.util.List;


/**
 * @author dianchao.song
 */
public class EmailCommand implements Command {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 271737573657705935L;

	/** 发件人 **/
	private String from;
	
	/** 收件人 **/
	private String address;

	/** 邮件主题 **/
	private String subject;

	/** 邮件内容 **/
	private String content;
	
	/**
	 * 发件人别名
	 */
	private String senderAlias;
	
	/**
	 * 附件列表
	 */
	private List<EmailAttachmentCommand> attachmentList=null;
	
	/** 同一种类型邮件的标识，比如同一个邮件模板发出的邮件，可使用模板的编码作为标识 */
	private String identifier;
 
		
	
	public String getSenderAlias() {
		return senderAlias;
	}

	public void setSenderAlias(String senderAlias) {
		this.senderAlias = senderAlias;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	public List<EmailAttachmentCommand> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<EmailAttachmentCommand> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	
}

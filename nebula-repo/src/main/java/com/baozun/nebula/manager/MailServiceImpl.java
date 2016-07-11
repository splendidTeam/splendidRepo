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
package com.baozun.nebula.manager;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.EmailAttachmentCommand;
import com.baozun.nebula.command.EmailCommand;

/**
 * @author songdianchao 邮件服务
 */
@Service
public class MailServiceImpl implements MailService {

	@Resource
	JavaMailSender javaMailSender;// 注入Spring封装的javamail，Spring的xml中已让框架装配
	@Resource
	TaskExecutor taskExecutor;// 注入Spring封装的异步执行器

	@Value("#{meta['mail.username']}")
	private String mailFrom;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void sendMail(EmailCommand email) {
		//email.setFrom(mailFrom);
		this.sendMailTask(email);
	}

	public void sendMailTask(final EmailCommand email) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					MimeMessage mime = javaMailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(mime,
							true, "utf-8");
					if(StringUtils.isBlank(email.getSenderAlias()))
						helper.setFrom(email.getFrom());// 发件人
					else
						helper.setFrom(email.getFrom(), email.getSenderAlias());// 发件人 加上别名
					
					helper.setTo(email.getAddress());// 收件人
					helper.setSubject(email.getSubject());// 邮件主题
					helper.setText(email.getContent(), true);// true表示设定html格式
					if(email.getAttachmentList()!=null){
						for(EmailAttachmentCommand eac:email.getAttachmentList()){
							helper.addAttachment(eac.getName(), new FileSystemResource(eac.getFilePath()));
						}
					}
					
					javaMailSender.send(mime);
				} catch (Exception e) {
				    logger.error(e.getMessage(), e);
				}
			}
		});
	}
}
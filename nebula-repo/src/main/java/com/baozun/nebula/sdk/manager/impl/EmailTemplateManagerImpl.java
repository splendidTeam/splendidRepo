package com.baozun.nebula.sdk.manager.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.EmailAttachmentCommand;
import com.baozun.nebula.command.EmailCommand;
import com.baozun.nebula.dao.system.EmailIntercepterDao;
import com.baozun.nebula.dao.system.EmailSendLogDao;
import com.baozun.nebula.dao.system.EmailTemplateDao;
import com.baozun.nebula.event.EmailEvent;
import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.MailService;
import com.baozun.nebula.manager.VelocityManager;
import com.baozun.nebula.model.system.EmailIntercepter;
import com.baozun.nebula.model.system.EmailSendLog;
import com.baozun.nebula.model.system.EmailTemplate;
import com.baozun.nebula.model.system.WarningConfig;
import com.baozun.nebula.sdk.handler.MailServiceHandler;
import com.baozun.nebula.sdk.manager.EmailTemplateManager;
import com.baozun.nebula.sdk.manager.SdkWarningConfigManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.Validator;

@Transactional
@Service("emailTemplateManager")
public class EmailTemplateManagerImpl implements EmailTemplateManager {

	@Autowired
	private EmailSendLogDao emailSendLogDao;
	
	@Autowired
	private EmailTemplateDao emailTemplateDao;
	
	@Autowired
	private MailService mailService;
	
	/**
	 * 自定义的MailService, 如果实现了该接口，会覆盖系统默认的mailService。
	 * 用于店铺使用第三发的邮件发送服务。
	 */
	@Autowired(required = false)
	private MailServiceHandler customMailService;
	
	@Autowired
	private VelocityManager velocityManager;
	
	@Autowired
	private EventPublisher                              eventPublisher;
	
	@Autowired
	private EmailIntercepterDao                         emailIntercepterDao;
	/**
	 * 告警配置
	 */
	@Autowired
	private SdkWarningConfigManager sdkWarningConfigManager;
	
	@Value("#{meta['static.path']}")
	private  String STATIC_PATH="";
	
	@Override
	public void sendEmail(String receiverEmail, String code, Map<String, Object> dataMap)throws Exception {

		sendEmail(receiverEmail,code,dataMap,null);
	}
	
	@Override
	public void sendEmail(String receiverEmail,String code,Map<String,Object> dataMap,List<EmailAttachmentCommand> attachmentList)throws Exception{
		
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("code", code);
		List<EmailTemplate> emailTemplateList=emailTemplateDao.findEmailTemplateListByQueryMap(paramMap);
		if(emailTemplateList!=null&&emailTemplateList.size()>0){
			
			EmailTemplate et=emailTemplateList.get(0);
			
			//如果当前模板的lifecycle不为1,则不发送邮件
			if(!et.getLifecycle().equals(EmailTemplate.LIFECYCLE_ENABLE)){
				return ;
			}
			
			EmailCommand ec=null;
			//发送邮件
			dataMap.put("staticUrlBase", STATIC_PATH);
			ec = constructMail(et,receiverEmail,dataMap,attachmentList);
			if(customMailService != null) {
				customMailService.sendMail(ec);
			} else {
				mailService.sendMail(ec);
			}
			//记录日志	
			EmailSendLog sendLog =generateMailLog(et.getId(),receiverEmail);
			emailSendLogDao.save(sendLog);
			
			
		}
	}
	

	@Override
	public void saveEmailTemplate(EmailTemplate emailTemplate) {
		Long id = emailTemplate.getId();
		if (id != null) {
			List<Long> ids = new ArrayList<Long>();
			ids.add(id);
			EmailTemplate model = emailTemplateDao.getByPrimaryKey(id);
			if (model == null) {
				throw new BusinessException("根据id未查询到邮件模板数据");
			}
			model.setBody(emailTemplate.getBody());
			model.setDescription(emailTemplate.getDescription());
			model.setModifyTime(new Date());
			model.setName(emailTemplate.getName());
			model.setSubject(emailTemplate.getSubject());
			model.setType(emailTemplate.getType());
			emailTemplateDao.save(model);
		} else {
			//验证编码是否重复
			String code = emailTemplate.getCode();
			if(Validator.isNullOrEmpty(code)){
				throw new BusinessException("编码为空");
			}
			if(emailTemplateDao.findEffectEmailTemplateListByCode(code) != null){;
				throw new BusinessException("编码已经存在");
			}
			emailTemplate.setLifecycle(1);
			emailTemplate.setCreateTime(new Date());
			emailTemplateDao.save(emailTemplate);
		}
	
	}
	
	private EmailSendLog generateMailLog(Long templateId,String receiverEmail){
		EmailSendLog sendLog=new EmailSendLog();
		sendLog.setReceiverEmail(receiverEmail);
		sendLog.setSendTime(new Date());
		sendLog.setTemplateId(templateId);
		return sendLog;
	}

	private EmailCommand constructMail(EmailTemplate et,String receiverEmail,Map<String, Object> params,List<EmailAttachmentCommand> attachmentList) throws MessagingException,UnsupportedEncodingException{

		/*// add attachments
		for (Entry<String, InputStreamSource> entry : getAttachments(mi).entrySet()){
			helper.addAttachment(entry.getKey(), entry.getValue());
		}*/

		String content=et.getBody() == null ? "" : velocityManager.parseVMContent(et.getBody(), params);;
		String subject=et.getSubject() == null ? "" : velocityManager.parseVMContent(et.getSubject(), params);
		
		EmailCommand ec=new EmailCommand();
		ec.setAddress(receiverEmail);
		ec.setFrom(et.getSenderEmail());
		ec.setSubject(subject);
		ec.setContent(content);
		ec.setAttachmentList(attachmentList);
		ec.setSenderAlias(et.getSenderAlias());
		ec.setIdentifier(et.getCode());
		return ec;
		
		
	}

	@Override
	public void sendWarningEmail(String warningConfigCode, String uniqueContent, Map<String,Object> dataMap) {
		WarningConfig wc = sdkWarningConfigManager.findWarningConfigByCode(warningConfigCode);
		String[] strs = new String[]{};
		//同一收件人可以收到N封相同邮件
		String repeatNumber = null;
		String emailTemplateCode = null;
		if(wc != null){
			//邮件模板
			 emailTemplateCode = wc.getTemplateCode();
			//同一收件人可以收到N封相同邮件
			 repeatNumber = String.valueOf(wc.getCount());
			//邮件接收者 可为多个
			 String receivers = wc.getReceivers();
			 strs  = receivers.split(",");
		}else{
			Properties properties=ProfileConfigUtil.findPro("config/email_receiver.properties");
			//邮件接收者 可为多个
			String receivers = properties.getProperty("warning.email.receiver");
			repeatNumber = properties.getProperty("warning.email.repeat.number");
			strs  = receivers.split(",");
			emailTemplateCode= warningConfigCode;
		}
		
		if(Validator.isNotNullOrEmpty(strs)){
			 for(String receiver :strs){
				 if(receiver==null || receiver==""){
					 continue;
				 }
				 Map<String, Object> paraMap = new HashMap<String, Object>();
				 paraMap.put("code", emailTemplateCode);
				 paraMap.put("uniqueContent", uniqueContent);
				 paraMap.put("receiverEmail", receiver);
				 
				 List<EmailIntercepter>  emailIntercepterList = emailIntercepterDao.findEmailIntercepterListByQueryMap(paraMap);
				 
				 if(Validator.isNotNullOrEmpty(emailIntercepterList)){
					 EmailIntercepter emailIntercepter = emailIntercepterList.get(0);
					 if(Validator.isNotNullOrEmpty(repeatNumber)){
						 int repNum = Integer.valueOf(repeatNumber);
						 if(emailIntercepter.getCount()<repNum){
							 //update count++
							 emailIntercepter.setSendTime(new Date());
							 emailIntercepter.setCount(emailIntercepter.getCount()+1);
							 emailIntercepterDao.save(emailIntercepter);
							 
							 EmailEvent emailEvent=new EmailEvent(this, receiver, emailTemplateCode, dataMap);
							 eventPublisher.publish(emailEvent);
						 }
					 }
					 
				 }else{
					 //add
					 EmailIntercepter emailIntercepter = new EmailIntercepter();
					 emailIntercepter.setCode(emailTemplateCode);
					 emailIntercepter.setCount(1);
					 emailIntercepter.setReceiverEmail(receiver);
					 emailIntercepter.setUniqueContent(uniqueContent);
					 emailIntercepter.setSendTime(new Date());
					 
					 emailIntercepterDao.save(emailIntercepter);
					 
					 EmailEvent emailEvent=new EmailEvent(this, receiver, emailTemplateCode, dataMap);
					 eventPublisher.publish(emailEvent);
				 }
			 }
		}
		
		
		
	}

	/**
	 * 分页查询邮件模板  何波
	 */
	@Override
	@Transactional(readOnly=true)
	public Pagination<EmailTemplate> findEmailTemplateListByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap) {
		return emailTemplateDao.findEmailTemplateListByQueryMapWithPage(page, sorts, paraMap);
	}

	@Override
	public void remove(Long[] ids) {
		emailTemplateDao.removeEmailTemplateByIds(Arrays.asList(ids));
	}

	@Override
	public void enOrDisable(Long[] ids, Integer state) {
		emailTemplateDao.enableOrDisableEmailTemplateByIds(Arrays.asList(ids), state);
	}

	@Override
	@Transactional(readOnly=true)
	public EmailTemplate getEmailTemplateByid(Long id) {
		return emailTemplateDao.getByPrimaryKey(id);
	}

	/*<p>Title: findEmailTemplateList</p> 
	* <p>Description: </p> 
	* @return 
	* @see com.baozun.nebula.sdk.manager.EmailTemplateManager#findEmailTemplateList() 
	* @date 2016年1月15日 下午8:25:32 
	* @author GEWEI.LU   
	*/
	@Override
	public List<EmailTemplate> findEmailTemplateList() {
		return emailTemplateDao.findAllEffectEmailTemplateList();
	}

}

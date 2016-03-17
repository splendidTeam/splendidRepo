package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.EmailAttachmentCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.system.EmailTemplate;

public interface EmailTemplateManager extends BaseManager{

	/**
	 * 发送邮件
	 * @param receiverEmail 接收邮件人
	 * @param code 邮件模板编码
	 * @param dataMap 替换模板中数据
	 */
	public void sendEmail(String receiverEmail,String code,Map<String,Object> dataMap)throws Exception;
	
	/**
	 * 发送邮件
	 * @param receiverEmail 接收邮件人
	 * @param code 邮件模板编码
	 * @param dataMap 替换模板中数据
	 * @param attachmentList 表示为附件列表
	 */
	public void sendEmail(String receiverEmail,String code,Map<String,Object> dataMap,List<EmailAttachmentCommand> attachmentList)throws Exception;
	
	
	/**
	 * 保存邮件模板
	 * @param emailTemplate
	 */
	public void saveEmailTemplate(EmailTemplate emailTemplate);
	
	EmailTemplate getEmailTemplateByid(Long id);
	
	/**
	 * 发送告警邮件
	 * @param emailTemplateCode 模板编码
	 * @param uniqueContent  标志内容,如订单状态同步,type-订单号，内容为order_status_sync-BN1000002
	 * @param dataMap 邮件数据
	 * 需要在email_receiver.properties文件里面配置两个参数
	 *  a:warning.email.receiver 收件人 可以为多个 用英文","隔开
	 *  b:warning.email.repeat.number 相同邮件收到的次数 
	 */
	public void sendWarningEmail(String emailTemplateCode,String uniqueContent,Map<String,Object> dataMap);
	/**
	 * 
	* @author 何波
	* @Description: 分页查询邮件模板 
	* @param page
	* @param sorts
	* @param paraMap
	* @return   
	* Pagination<EmailTemplate>   
	* @throws
	 */
	Pagination<EmailTemplate> findEmailTemplateListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/** 
	* @Title: findEmailTemplateList 
	* @Description:(查询邮件模板) 
	* @param @return    设定文件 
	* @return List<EmailTemplate>    返回类型 
	* @throws 
	* @date 2016年1月15日 下午8:25:12 
	* @author GEWEI.LU   
	*/
	List<EmailTemplate> findEmailTemplateList();
	
	
	/**
	 * 
	* @author 何波
	* @Description: 批量删除邮件模板
	* @param ids
	* @throws
	 */
	void remove(Long[] ids);
	/**
	 * 
	* @author 何波
	* @Description: 启用或禁用邮件模板
	* @param id   
	* void   
	* @throws
	 */
	void enOrDisable(Long[] ids, Integer state);
	
}

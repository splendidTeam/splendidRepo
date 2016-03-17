package com.baozun.nebula.web.controller.baseinfo;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.system.EmailTemplate;
import com.baozun.nebula.sdk.manager.EmailTemplateManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

@RequestMapping("/email")
@Controller
public class EmailTemplateController extends BaseController {

	private static final Logger log = LoggerFactory
			.getLogger(NavigationController.class);
	@Autowired
	private EmailTemplateManager emailTemplateManager;

	/**
	 * 
	* @author 何波
	* @Description: 邮件模板列表页面
	* @return   
	* String   
	* @throws
	 */
	@RequestMapping("/list.htm")
	public String list() {
		return "system/email/list";
	}
	/**
	 * 
	* @author 何波
	* @Description: 编辑界面
	* @return   
	* String   
	* @throws
	 */
	@RequestMapping("/edit.htm")
	public String edit(Long id, Model model) {
		if(id == null){
			model.addAttribute("optype", "new");
		}else{
			model.addAttribute("optype", "edit");
			model.addAttribute("email", emailTemplateManager.getEmailTemplateByid(id));
		}
		return "system/email/edit";
	}

	/**
	 * 
	* @author 何波
	* @Description: 获取分页邮件模板数据
	* @param queryBean
	* @return   
	* Pagination<EmailTemplate>   
	* @throws
	 */
	@RequestMapping("/pagination.json")
	@ResponseBody
	public Pagination<EmailTemplate> pagination(@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		if (ArrayUtils.isEmpty(sorts)) {
			sorts = Sort.parse("create_time desc");
		}
		return emailTemplateManager.findEmailTemplateListByQueryMapWithPage(queryBean.getPage(),
				sorts, queryBean.getParaMap());
	}
	/**
	 * 
	* @author 何波
	* @Description: 新增或修改邮件模板
	* @param emailTemplate
	* @return   
	* BackWarnEntity   
	* @throws
	 */
	@RequestMapping("/addOrEdit.json")
	@ResponseBody
	public BackWarnEntity  add(EmailTemplate emailTemplate) {
		BackWarnEntity result = new BackWarnEntity();
		try {
			emailTemplate.setOptUserId(getUserDetails().getUserId());
			emailTemplateManager.saveEmailTemplate(emailTemplate);
			result.setIsSuccess(true);
			return result;
		} catch (BusinessException e) {
			log.error("删除或修改邮件模板出错", e);
			result.setIsSuccess(false);
			result.setDescription(e.getMessage());
			return result;
		}
	}
	
	@RequestMapping("/remove.json")
	@ResponseBody
	public BackWarnEntity  remove(Long[] ids) {
		emailTemplateManager.remove(ids);
		return SUCCESS;
		
	}
	@RequestMapping("/enOrDisable.json")
	@ResponseBody
	public BackWarnEntity  enOrDisable(Long[] ids,Integer state) {
		emailTemplateManager.enOrDisable(ids, state);
		return SUCCESS;
	}
	
}

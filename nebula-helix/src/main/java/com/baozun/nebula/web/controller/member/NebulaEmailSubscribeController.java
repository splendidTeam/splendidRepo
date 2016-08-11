package com.baozun.nebula.web.controller.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.baozun.nebula.model.system.EmailSubscribe;
import com.baozun.nebula.sdk.manager.SdkEmailSubscribeManager;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.feilong.core.RegexPattern;
import com.feilong.core.util.RegexUtil;

/**
 * 邮件订阅功能
 * 取消邮件订阅
 * @author zhaojun.fang
 *
 */

@Controller
public class NebulaEmailSubscribeController extends BaseController{
	
	@Autowired
	private SdkEmailSubscribeManager sdkEmailSubscribeManager;
	
	/**
	 * 
	 * 提交邮箱订阅信息
	 * @RequestMapping("email/subscribe.json")
	 * @ResponseBody
	 * @param emailSubscribe
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public DefaultReturnResult submitEmailSubscribe(EmailSubscribe emailSubscribe){
		DefaultReturnResult defaultReturnResult = new DefaultReturnResult();
		
		String email=emailSubscribe.getReceiver();
		if (!RegexUtil.matches(RegexPattern.EMAIL, email)){
			// 电子邮箱 格式不正确
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("member.email.error");
			return defaultReturnResult;
		}
		Map<String,Object> paraMap=new HashMap<String,Object>();
		paraMap.put("receiver", email);
		List<EmailSubscribe> emailSubscribes = this.sdkEmailSubscribeManager.findEmailSubscribeListByQueryMap(paraMap);
		
		if(emailSubscribes!=null && emailSubscribes.size()>0){
			//该邮箱已经订阅
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("email.subscribe.exist");
			return defaultReturnResult;
		}
		emailSubscribe.setId(null);
		this.sdkEmailSubscribeManager.saveEmailSubscribe(emailSubscribe);
		defaultReturnResult.setResult(true);
		return defaultReturnResult;
	}
	
	
	/**
	 * 取消邮箱订阅
	 * @RequestMapping("email/subscribe.json")
	 * @ResponseBody
	 * @param emailSubscribe
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public DefaultReturnResult cancelEmailSubscribe(String email){
		DefaultReturnResult defaultReturnResult = new DefaultReturnResult();
		
		if (!RegexUtil.matches(RegexPattern.EMAIL, email)){
			// 电子邮箱 格式不正确
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("member.email.error");
			return defaultReturnResult;
		}
		Map<String,Object> paraMap=new HashMap<String,Object>();
		paraMap.put("receiver", email);
		List<EmailSubscribe> emailSubscribes = this.sdkEmailSubscribeManager.findEmailSubscribeListByQueryMap(paraMap);
		
		if(emailSubscribes==null || emailSubscribes.size()==0){
			//该邮箱未被订阅
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("email.subscribe.notexist");
			return defaultReturnResult;
		}
		//删除该邮箱的订阅信息
		this.sdkEmailSubscribeManager.deleteEmailSubscribeByPrimaryKey(emailSubscribes.get(0).getId());
		defaultReturnResult.setResult(true);
		return defaultReturnResult;
	}
}

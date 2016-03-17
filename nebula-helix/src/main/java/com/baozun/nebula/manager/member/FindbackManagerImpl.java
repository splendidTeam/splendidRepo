package com.baozun.nebula.manager.member;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.constant.EmailConstants;
import com.baozun.nebula.dao.email.EmailCheckDao;
import com.baozun.nebula.event.EmailEvent;
import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.email.EmailCheckManager;
import com.baozun.nebula.model.email.EmailCheck;
import com.baozun.nebula.model.member.MemberCryptoguard;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.EmailParamEnciphermentUtil;
import com.baozun.nebula.web.constants.Constants;
import com.baozun.nebula.web.constants.SessionKeyConstants;

/**
 * FindbackManagerImpl
 * 
 * 2014-2-13 下午4:08:31
 * 
 * @author <a href="xinyuan.guo@baozun.cn">郭馨元</a>
 * 
 */
@Transactional
@Service("FindbackManager")
public class FindbackManagerImpl implements FindbackManager {

	static final Logger log = LoggerFactory.getLogger(FindbackManagerImpl.class);
	
	@Value("#{meta['page.base']}")
	private String pageUrlBase = "";
	@Autowired
	private SdkMemberManager sdkMemberManager;
	
	@Autowired
	private MemberManager	membManager;
	@Autowired
	private EventPublisher eventPublisher;
	@Autowired
	private EmailCheckManager emailCheckManager;
	@Autowired
	private EmailCheckDao emailCheckDao;

	@Override
	public void confirmAccount(String loginName, String vcode, HttpSession session) {
		Object vcodeObj = session.getAttribute(SessionKeyConstants.MEMBER_REGISTER_RANDOMCODE);

		if (vcodeObj != null && vcodeObj instanceof String) {
			if (vcodeObj.toString().equals(vcode)) {
				MemberCommand memberCommand = sdkMemberManager.findMemberByLoginName(loginName);
				if (memberCommand != null) {
					session.setAttribute(SessionKeyConstants.APP_SESSIONKEY_ACCCMD, memberCommand);
					return;
				} else
					throw new BusinessException(ErrorCodes.FINDBACK_USERNOTEXIST);
			} else {
				throw new BusinessException(ErrorCodes.INVALID_SESSION);
			}
		}
		throw new BusinessException(ErrorCodes.INVALID_SESSION);
	}

	@Override
	public void setPassWord(String pwd, MemberCommand mc, Long ecId) {
		String encodePassword = EncryptUtil.getInstance().hash(pwd, mc.getLoginName());
		sdkMemberManager.resetPasswd(mc.getId(), encodePassword);
		if(ecId !=null){
			emailCheckManager.updateEmailCheckStatusById(ecId, EmailCheck.STATUS_INVALID_USED);
		}
	}

	@Override
	public void checkQa(String[] q, String[] a) {
		if (q != null && a != null && q.length > 0 && a.length > 0 && q.length == a.length) {
			List<MemberCryptoguard> cryptList = new ArrayList<MemberCryptoguard>();
			for (int i = 0; i < q.length; i++) {
				MemberCryptoguard mc = new MemberCryptoguard();
				mc.setQuestion(q[i]);
				mc.setAnswer(a[i]);
				cryptList.add(mc);
			}
			if (sdkMemberManager.checkCryptoguard(cryptList))
				return;
		}
		throw new BusinessException(ErrorCodes.FINDBACK_QAERROR);
	}

	@Override
	public void sendEmailCode(HttpSession session) {
		if (valueInSession(session, SessionKeyConstants.APP_SESSIONKEY_FINDBACKSTEP, Constants.FINDBACKSTEP_2)) {
			Object acc = session.getAttribute(SessionKeyConstants.APP_SESSIONKEY_ACCCMD);
			if (acc != null && acc instanceof MemberCommand) {
				MemberCommand mc = (MemberCommand) acc;
				String code = sdkMemberManager.sendPassResetCodeByEmail(mc.getId());
				session.setAttribute(SessionKeyConstants.APP_SESSIONKEY_EMAILCODE, code);
				
				MemberPersonalData memberPersonalData = membManager.findMemberPersonData(mc.getId());
				//key值
				Properties properties=ProfileConfigUtil.findPro("config/email.properties");
				String url = properties.getProperty("param.retrieval.url");
				 Map<String,Object> dataMap=new HashMap<String,Object>();
				 String nickname = memberPersonalData.getNickname();
				 if(Validator.isNullOrEmpty(nickname)){
					 nickname = mc.getLoginName();
				 }
				 dataMap.put("name", nickname);
				 dataMap.put("code", code);

				EmailEvent emailEvent=new EmailEvent(this, mc.getLoginEmail(), EmailConstants.FORGET_PASSWORD, dataMap);	
				eventPublisher.publish(emailEvent);
			} else
				throw new BusinessException(ErrorCodes.INVALID_SESSION);
		} else
			throw new BusinessException(ErrorCodes.ACCESS_DENIED);
	}

	@Override
	public void sendMobileCode(HttpSession session) {
		if (valueInSession(session, SessionKeyConstants.APP_SESSIONKEY_FINDBACKSTEP, Constants.FINDBACKSTEP_2)) {
			Object acc = session.getAttribute(SessionKeyConstants.APP_SESSIONKEY_ACCCMD);
			if (acc != null && acc instanceof MemberCommand) {
				MemberCommand mc = (MemberCommand) acc;
				String code = sdkMemberManager.sendPassResetCodeByEmail(mc.getId());
				session.setAttribute(SessionKeyConstants.APP_SESSIONKEY_EMAILCODE, code);
			} else
				throw new BusinessException(ErrorCodes.INVALID_SESSION);
		} else
			throw new BusinessException(ErrorCodes.ACCESS_DENIED);
	}

	/**
	 * 判断session中某key的值是否与参数值相同
	 */
	boolean valueInSession(HttpSession session, String key, Object value) {
		Object obj = session.getAttribute(key);
		if (obj != null)
			return obj.equals(value);
		return false;
	}

	@Override
	public void sendEmailUrl(String email) {
		MemberCommand member = sdkMemberManager.findMemberByLoginEmail(email);
		MemberPersonalData memberPersonalData = membManager.findMemberPersonData(member.getId());
		//key值
		Properties properties=ProfileConfigUtil.findPro("config/email.properties");
		String key = properties.getProperty("param.retrieval");
		String url = properties.getProperty("param.retrieval.url");
		String action = properties.getProperty("param.retrieval.action");
		String t_q_s ="";
		//3.检测用户在emailchekc表中是否有记录(有效记录)
		EmailCheck oldec = emailCheckManager.findEmailCheckByLoginEmail(email,EmailCheck.STATUS_VALID_NOTCLICK);
		//　1.以createtime为基准检测用户当天的请求是否超过5次。
		//　2.以ModifyTime为基准检测用户的请求是否超过2小时。
		//　3.将当前数据库中的Encrypted_S,Token,createTime取出拼接成发送给用户的url地址。
		if(checkEmail(oldec)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String q = sdf.format(oldec.getCreateTime());//时间参数
			t_q_s = "t="+oldec.getToken()+"&q="+q+"&s="+oldec.getEncrypted_S();
			EmailCheck newec=emailCheckDao.getByPrimaryKey(oldec.getId());
			newec.setCount(oldec.getCount()+1);
			newec.setModifyTime(new Date());	
			log.info("foget password stats:"+newec.getStatus());
		}else{
			//date
			Date date = new Date();
			//sequence
			Long sequence= emailCheckManager.findNextval();
			t_q_s = EmailParamEnciphermentUtil.enciphermentParam(null, date, action, key, sequence.toString());
			String token = t_q_s.substring(t_q_s.indexOf("t=")+2, t_q_s.indexOf("&q="));
			String s = t_q_s.substring(t_q_s.indexOf("s=")+2, t_q_s.length());
			EmailCheck ec = new EmailCheck();
			ec.setMemberId(member.getId());
			ec.setAction(action);
			ec.setCreateTime(date);
			ec.setEmailAddress(member.getLoginEmail());
			ec.setEncrypted_S(s);
			ec.setStatus(EmailCheck.STATUS_VALID_NOTCLICK);
			ec.setCount(1);
			ec.setModifyTime(date);
			ec.setToken(token);
			emailCheckManager.createEmailCheck(ec);
			log.info("foget password stats:"+ec.getStatus()+" status static:"+EmailCheck.STATUS_VALID_NOTCLICK);
		}
		
		 Map<String,Object> dataMap=new HashMap<String,Object>();
		 String nickname = memberPersonalData.getNickname();
		 if(Validator.isNullOrEmpty(nickname)){
			 nickname = member.getLoginName();
		 }
		 dataMap.put("name", nickname);
		 dataMap.put("pageUrlBase", pageUrlBase);
		 dataMap.put("link", "<a href='"+url+"?"+t_q_s+"'>"+url+"?"+t_q_s+"</a>");

		EmailEvent emailEvent=new EmailEvent(this, member.getLoginEmail(), EmailConstants.FORGET_PASSWORD, dataMap);
		
		eventPublisher.publish(emailEvent);
	}

	@Override
	public boolean chechTQS(String t, String q, String s) {
		//key值
		Properties properties=ProfileConfigUtil.findPro("config/email.properties");
		String key = properties.getProperty("param.retrieval");
		String action = properties.getProperty("param.retrieval.action");
	
		boolean flag = EmailParamEnciphermentUtil.checkParam(action, key, t, s, q);
		return flag;
		
	}
	
	/** 检查记录是否有效*/
	public boolean checkEmail(EmailCheck oldec) {
		boolean temp = false;
		//　2.以ModifyTime为基准检测用户的请求是否超过2小时。
		if(oldec != null){
			long cuttime = System.currentTimeMillis();
			long inteval = cuttime - oldec.getModifyTime().getTime();
			if(inteval >EmailCheck.EFFECT_TIME){//过期设置无效
				emailCheckManager.updateEmailCheckStatusById(oldec.getId(), EmailCheck.STATUS_INVALID_USED);
			}else{
				//　3.将当前数据库中的Encrypted_S,Token,createTime取出拼接成发送给用户的url地址。
				temp = true;
			}
		}

		return temp;
		
	}

	@Override
	public Integer getEmailCountByDay(String emailAddress) {
		return emailCheckManager.findCountsByLoginEmail(emailAddress);
	}
	
	
}

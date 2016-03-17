package com.baozun.nebula.manager.email;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.email.EmailCheckDao;
import com.baozun.nebula.model.email.EmailCheck;

@Transactional
@Service("emailCheckManager")
public class EmailCheckManagerImpl implements EmailCheckManager{

	@Autowired
	private EmailCheckDao emailCheckDao;
	
	@Override
	public Long findNextval() {
		// TODO Auto-generated method stub
		return emailCheckDao.findNextval();
	}

	@Override
	public EmailCheck createEmailCheck(EmailCheck email) {
		EmailCheck con = emailCheckDao.save(email);
		return con;
	}

	@Override
	public EmailCheck findEmailCheckByEncryptedS(String Encrypted_S) {
		return emailCheckDao.findEmailCheckByEncryptedS(Encrypted_S);
	}

	@Override
	public List<EmailCheck> findEmailCheckListByDay(Long memberId) {
		return emailCheckDao.findEmailCheckListByDay(memberId);
	}

	@Override
	public Integer updateExpireEmailCheckInvalid(Long memberId) {
		long curtime = System.currentTimeMillis();
		long expireTime = curtime-EmailCheck.EFFECT_TIME;
		return emailCheckDao.updateEmailCheckStatusByMemberid(memberId, EmailCheck.STATUS_INVALID_USED, new Date(expireTime));
	}

	@Override
	public EmailCheck findEmailCheckByLoginEmail(String emailAddress,Integer status) {
		return emailCheckDao.findEmailCheckByLoginEmail(emailAddress, status);
	}

	@Override
	public Integer findCountsByLoginEmail(String emailAddress) {
		// TODO Auto-generated method stub
		return emailCheckDao.findCountsByLoginEmail(emailAddress);
	}

	@Override
	public Integer updateEmailCheckStatusById(Long id, Integer status) {
		return emailCheckDao.updateEmailCheckStatusById(id, status);
	}
}

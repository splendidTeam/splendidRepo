package com.baozun.nebula.manager.email;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.baozun.nebula.utils.EmailParamEnciphermentUtil;

@Service("EmailEnciphermentManager")
public class EmailEnciphermentManagerImpl implements EmailEnciphermentManager {

	@Override
	public String getEncryptString(String token, Date nowDate, String action,
			String key, String sequence) {
		return EmailParamEnciphermentUtil.enciphermentParam(token, nowDate, action, key, sequence);
	}

	@Override
	public Boolean checkParam(String action, String key, String token,
			String s, String date) {
		return EmailParamEnciphermentUtil.checkParam(action, key, token, s, date);
	}

}

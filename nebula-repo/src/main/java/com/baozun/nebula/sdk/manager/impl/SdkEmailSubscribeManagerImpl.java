package com.baozun.nebula.sdk.manager.impl;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.system.EmailSubscribeDao;
import com.baozun.nebula.model.system.EmailSubscribe;
import com.baozun.nebula.sdk.manager.SdkEmailSubscribeManager;

@Service("sdkEmailSubscribeManager")
@Transactional
public class SdkEmailSubscribeManagerImpl implements SdkEmailSubscribeManager {

	@Autowired
	private EmailSubscribeDao emailSubscribeDao;
	
	@Override
	public EmailSubscribe saveEmailSubscribe(EmailSubscribe emailSubscribe) {
		return emailSubscribeDao.save(emailSubscribe);
	}

	@Override
	@Transactional(readOnly=true)
	public List<EmailSubscribe> findAllEmailSubscribeList() {
		return emailSubscribeDao.findAllEmailSubscribeList();
	}

	@Override
	@Transactional(readOnly=true)
	public List<EmailSubscribe> findEmailSubscribeListByIds(List<Long> ids) {
		return emailSubscribeDao.findEmailSubscribeListByIds(ids);
	}

	@Override
	@Transactional(readOnly=true)
	public List<EmailSubscribe> findEmailSubscribeListByQueryMap(Map<String, Object> paraMap) {
		return emailSubscribeDao.findEmailSubscribeListByQueryMap(paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<EmailSubscribe> findEmailSubscribeListByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap) {
		return emailSubscribeDao.findEmailSubscribeListByQueryMapWithPage(page, sorts, paraMap);
	}

}

package com.baozun.nebula.sdk.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.sdk.manager.SdkCustomMemberManager;

@Transactional
@Service("SdkCustomMemberManager")
public class SdkCustomMemberManagerImpl implements SdkCustomMemberManager {

	final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MemberDao memberDao;

	@Override
	@Transactional(readOnly=true)
	public List<Long> findMemberIdsByComsumeeAmtPromotionId(Long shopId,
			BigDecimal amt, String prmId) {
		List<Long> list = memberDao.findMemberIdsByComsumeeAmtPromotionId(shopId,amt, prmId);
		return list;
	}

	@Override
	public List<Long> execute() {
		List<Long> listIds = new ArrayList<Long>();
		listIds.add(633L);
		listIds.add(81L);
		return listIds;
	}
}

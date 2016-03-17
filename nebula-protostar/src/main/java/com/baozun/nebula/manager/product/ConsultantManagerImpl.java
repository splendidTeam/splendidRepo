/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.product.ConsultantCommand;
import com.baozun.nebula.command.product.ConsultantOperationlogCommand;
import com.baozun.nebula.command.product.ConsultantStatusCommand;
import com.baozun.nebula.dao.sns.ConsultantsDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.system.ChooseOption;

/**
 * @author Tianlong.Zhang
 *
 */
@Transactional
@Service("consultantManager")
public class ConsultantManagerImpl implements ConsultantManager{

	@SuppressWarnings("unused")
	private static final Logger	logger	= LoggerFactory.getLogger(ConsultantManagerImpl.class);
	
	private static final Integer STATUS_RESOLVED = 3;
	
	private static final String CONSULTANT_STATUS = "CONSULTANT_STATUS";
	
	@Autowired
	private ConsultantsDao consultantsDao;
	
	@Autowired
	private ChooseOptionManager chooseOptionManager;
	
	@Override
	public Pagination<ConsultantCommand> findConsultantListByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap) {
		return consultantsDao.findConsultants(page, sorts, paraMap);
	}

	@Override
	public List<ConsultantStatusCommand> findConsultantStatus() {
		List<ConsultantStatusCommand> list = null;
		List<ChooseOption> options = chooseOptionManager.findEffectChooseOptionListByGroupCode(CONSULTANT_STATUS);
		if(options!=null){
			list = new ArrayList<ConsultantStatusCommand>(options.size());
			for(ChooseOption opt :options){
				ConsultantStatusCommand cmd= new ConsultantStatusCommand();
				cmd.setLabel(opt.getOptionLabel());
				cmd.setValue(opt.getOptionValue());
				list.add(cmd);
			}
		}
		return list;
	}

	@Override
	public boolean responseConsultant(ConsultantCommand cmd) {
		// TODO Auto-generated method stub  响应 暂时不做
		return false;
	}

	@Override
	public boolean resolveConsultant(ConsultantCommand cmd) {
		int result = consultantsDao.resolveConsultant(cmd.getId(), STATUS_RESOLVED, cmd.getResolveNote(), cmd.getResolveId(), cmd.getPublishMark());
		checkUpdateResult(result,1);
		return true;
	}

	@Override
	public boolean publishConsultant(ConsultantCommand cmd) {
		int result = consultantsDao.publishConsultant(cmd.getId(), cmd.getPublishMark(), cmd.getPublishId());
		checkUpdateResult(result,1);
		
		return true;
	}

	@Override
	public boolean unpublishConsultant(ConsultantCommand cmd) {
		int result = consultantsDao.unpublishConsultant(cmd.getId(), cmd.getPublishMark(), cmd.getUnPublishId());
		checkUpdateResult(result,1);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ConsultantManager#findOperationlogListByQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
	 */
	@Override
	public Pagination<ConsultantOperationlogCommand> findOperationlogListByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap) {
		// TODO Auto-generated method stub 操作历史 暂时不做
		return null;
	}
	
	private void checkUpdateResult(Integer resultSize,Integer exceptSize){
		if(resultSize!=exceptSize){
			throw new BusinessException(ErrorCodes.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED);
		}
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ConsultantManager#updateConsultant(com.baozun.nebula.command.product.ConsultantCommand)
	 */
	@Override
	public boolean updateConsultant(ConsultantCommand cmd) {
		Integer result = consultantsDao.updateConsultant(cmd.getId(), cmd.getResolveNote(), cmd.getLastUpdateId(), cmd.getPublishMark());
		checkUpdateResult(result,1);
		return true;
	}

	@Override
	public ConsultantCommand findConsultantById(Long id) {
		return consultantsDao.findById(id);
	}

	@Override
	public List<ConsultantCommand> findConsultantListByQueryMap(Sort[] sorts, Map<String, Object> paraMap) {
		return consultantsDao.findConsultantsNoPage(sorts, paraMap);
	}

}

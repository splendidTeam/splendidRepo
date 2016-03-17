package com.baozun.nebula.sdk.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.system.MsgReceiveContentDao;
import com.baozun.nebula.dao.system.MsgSendContentDao;
import com.baozun.nebula.dao.system.MsgSendRecordDao;
import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;
import com.baozun.nebula.sdk.manager.SdkMsgManager;

@Service("sdkMsgManager")
@Transactional
public class SdkMsgManagerImpl implements SdkMsgManager {

	@Autowired
	private MsgReceiveContentDao msgReceiveContentDao;

	@Autowired
	private MsgSendContentDao msgSendContentDao;

	@Autowired
	private MsgSendRecordDao msgSendRecordDao;

	@Override
	public MsgReceiveContent saveMsgReceiveContent(MsgReceiveContent mrc) {

		return msgReceiveContentDao.save(mrc);
	}

	@Override
	@Transactional(readOnly=true)
	public List<MsgReceiveContent> findAllMsgReceiveContentList() {

		return msgReceiveContentDao.findAllMsgReceiveContentList();
	}

	@Override
	@Transactional(readOnly=true)
	public MsgReceiveContent findMsgReceiveContentById(Long id) {

		return msgReceiveContentDao.getByPrimaryKey(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<MsgReceiveContent> findMsgReceiveContentListByIds(List<Long> ids) {

		return msgReceiveContentDao.findMsgReceiveContentListByIds(ids);
	}

	@Override
	@Transactional(readOnly=true)
	public List<MsgReceiveContent> findMsgReceiveContentListByQueryMap(Map<String, Object> paraMap) {

		return msgReceiveContentDao.findMsgReceiveContentListByQueryMap(paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<MsgReceiveContent> findMsgReceiveContentListByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap) {

		return msgReceiveContentDao.findMsgReceiveContentListByQueryMapWithPage(page, sorts, paraMap);
	}

	@Override
	public MsgSendContent saveMsgSendContent(MsgSendContent msc) {

		return msgSendContentDao.save(msc);
	}

	@Override
	@Transactional(readOnly=true)
	public List<MsgSendContent> findAllMsgSendContentList() {

		return msgSendContentDao.findAllMsgSendContentList();
	}

	@Override
	@Transactional(readOnly=true)
	public MsgSendContent findMsgSendContentById(Long id) {

		return msgSendContentDao.getByPrimaryKey(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<MsgSendContent> findMsgSendContentListByIds(List<Long> ids) {

		return msgSendContentDao.findMsgSendContentListByIds(ids);
	}

	@Override
	@Transactional(readOnly=true)
	public List<MsgSendContent> findMsgSendContentListByQueryMap(Map<String, Object> paraMap) {

		return msgSendContentDao.findMsgSendContentListByQueryMap(paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<MsgSendContent> findMsgSendContentListByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap) {

		return msgSendContentDao.findMsgSendContentListByQueryMapWithPage(page, sorts, paraMap);
	}

	@Override
	public MsgSendRecord saveMsgSendRecord(MsgSendRecord msr) {

		return msgSendRecordDao.save(msr);
	}

	@Override
	@Transactional(readOnly=true)
	public MsgSendRecord findMsgSendRecordById(Long id) {

		return msgSendRecordDao.getByPrimaryKey(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<MsgSendRecord> findAllMsgSendRecordList() {

		return msgSendRecordDao.findAllMsgSendRecordList();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<MsgSendRecord> findAllNoFeedbackMsgSendRecordList(){
		return msgSendRecordDao.findAllNoFeedbackMsgSendRecordList();
	}

	@Override
	@Transactional(readOnly=true)
	public List<MsgSendRecord> findMsgSendRecordListByIds(List<Long> ids) {

		return msgSendRecordDao.findMsgSendRecordListByIds(ids);
	}

	@Override
	@Transactional(readOnly=true)
	public List<MsgSendRecord> findMsgSendRecordListByQueryMap(Map<String, Object> paraMap) {

		return msgSendRecordDao.findMsgSendRecordListByQueryMap(paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<MsgSendRecord> findMsgSendRecordListByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap) {

		return msgSendRecordDao.findMsgSendRecordListByQueryMapWithPage(page, sorts, paraMap);
	}

	@Override
	public void updateMsgRecContIsProByIds(List<Long> ids) {

		msgReceiveContentDao.updateMsgRecContIsProByIds(ids);
	}

	@Override
	public MsgSendRecord saveMsgSendRecord(String ifIdentify, Long targetId, String ext) {
		MsgSendRecord msr = new MsgSendRecord();
		msr.setCreateTime(new Date());
		msr.setIfIdentify(ifIdentify);
		msr.setSendCount(0);
		msr.setVersion(new Date());
		msr.setTargetId(targetId);
		msr.setExt(ext);
		return msgSendRecordDao.save(msr);
	}

}

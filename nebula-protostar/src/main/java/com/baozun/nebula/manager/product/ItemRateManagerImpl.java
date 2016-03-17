package com.baozun.nebula.manager.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.dao.product.ItemRateDao;
import com.baozun.nebula.dao.product.ItemRateOperatorLogDao;
import com.baozun.nebula.model.product.ItemRate;
import com.baozun.nebula.model.product.ItemRateOperatorLog;


@Transactional
@Service("ItemRateManager")
public class ItemRateManagerImpl implements ItemRateManager{
	
	private static final Logger	log	= LoggerFactory.getLogger(ItemRateManager.class);
	
	@Autowired
	private ItemRateDao itemRateDao;
	@Autowired
	private ItemRateOperatorLogDao itemRateOperatorLogDao;
    /** 1 通过  2 通过并回复 3 回复 4 删除评论*/
	private static final Integer ENABLE_RATE_TYPE = 1;
	private static final Integer ENABLE_AND_REPLEY_RATE_TYPE = 2;
	private static final Integer REPLAY_RATE_TYPE = 3;
	private static final Integer DISABLE_RATE_TYPE = 4;

	@Override
	public Pagination<RateCommand> findRateListByQueryMapWithPage(Page page,
			Sort[] sorts, Map<String, Object> paraMap) {
		Pagination<RateCommand> rateCommandList= itemRateDao.findEffectItemRateListByQueryMapWithPage(page, sorts, paraMap);
		return rateCommandList;
	}

	@Override
	public void replyRate(Long id,String reply, Long operatorId,Integer isReply,Integer isPass) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		Integer type =REPLAY_RATE_TYPE;
		if(isPass==1){
			type = ENABLE_AND_REPLEY_RATE_TYPE;
		    itemRateDao.enableOrDisableItemRateByIds(ids, 1, operatorId);
		}
		itemRateDao.replyRate(id, reply, operatorId,isReply);
		
		ItemRateOperatorLog  itemRateOperatorLog = new ItemRateOperatorLog();
		/**操作类型  记录回复内容  操作员Id 操作时间  评价id**/
		itemRateOperatorLog.setType(type);
		itemRateOperatorLog.setNote(reply);
		itemRateOperatorLog.setOperatorId(operatorId);
		itemRateOperatorLog.setOperatorTime(new Date());
		itemRateOperatorLog.setRateId(id);
		itemRateOperatorLogDao.save(itemRateOperatorLog);
		
	}

	@Override
	public Integer enableRateByIds(List<Long> itemRateIds, Long operatorId) {
		for(Long id:itemRateIds){
			ItemRateOperatorLog  itemRateOperatorLog = new ItemRateOperatorLog();
			/**操作类型  记录回复内容  操作员Id 操作时间  评价id**/
			itemRateOperatorLog.setType(ENABLE_RATE_TYPE);
			itemRateOperatorLog.setNote(null);
			itemRateOperatorLog.setOperatorId(operatorId);
			itemRateOperatorLog.setOperatorTime(new Date());
			itemRateOperatorLog.setRateId(id);
			itemRateOperatorLogDao.save(itemRateOperatorLog);
		}
		return itemRateDao.enableOrDisableItemRateByIds(itemRateIds, 1, operatorId);
		 
	}

	@Override
	public Integer disableRateByIds(List<Long> itemRateIds, Long operatorId) {
		for(Long id:itemRateIds){
			ItemRateOperatorLog  itemRateOperatorLog = new ItemRateOperatorLog();
			/**操作类型  记录回复内容  操作员Id 操作时间  评价id**/
			itemRateOperatorLog.setType(DISABLE_RATE_TYPE);
			itemRateOperatorLog.setNote(null);
			itemRateOperatorLog.setOperatorId(operatorId);
			itemRateOperatorLog.setOperatorTime(new Date());
			itemRateOperatorLog.setRateId(id);
			itemRateOperatorLogDao.save(itemRateOperatorLog);
		}
		return itemRateDao.enableOrDisableItemRateByIds(itemRateIds, 0, operatorId);
	}

	@Override
	public void enableRateById(Long itemRateId, Long operatorId) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(itemRateId);
		itemRateDao.enableOrDisableItemRateByIds(ids, 1, operatorId);
		
		ItemRateOperatorLog  itemRateOperatorLog = new ItemRateOperatorLog();
		/**操作类型  记录回复内容  操作员Id 操作时间  评价id**/
		itemRateOperatorLog.setType(ENABLE_RATE_TYPE);
		itemRateOperatorLog.setNote(null);
		itemRateOperatorLog.setOperatorId(operatorId);
		itemRateOperatorLog.setOperatorTime(new Date());
		itemRateOperatorLog.setRateId(itemRateId);
		itemRateOperatorLogDao.save(itemRateOperatorLog);
	}

	@Override
	public void disableRateById(Long itemRateId, Long operatorId) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(itemRateId);
		itemRateDao.enableOrDisableItemRateByIds(ids, 0, operatorId);
		
		ItemRateOperatorLog  itemRateOperatorLog = new ItemRateOperatorLog();
		/**操作类型  记录回复内容  操作员Id 操作时间  评价id**/
		itemRateOperatorLog.setType(DISABLE_RATE_TYPE);
		itemRateOperatorLog.setNote(null);
		itemRateOperatorLog.setOperatorId(operatorId);
		itemRateOperatorLog.setOperatorTime(new Date());
		itemRateOperatorLog.setRateId(itemRateId);
		itemRateOperatorLogDao.save(itemRateOperatorLog);
	}

	@Override
	public ItemRate findRateById(Long itemRateId) {
		ItemRate itemRate = itemRateDao.findItemRateListById(itemRateId);
		return itemRate;
	}

	@Override
	public Streams exportRate(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}

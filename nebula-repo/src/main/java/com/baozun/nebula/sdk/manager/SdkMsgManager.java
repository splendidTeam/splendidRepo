/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.product.MsgSendRecordCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;

/**
 * 消息记录manager
 * 
 * @author Justin
 * 
 */
public interface SdkMsgManager extends BaseManager{

	/**
	 * 保存接收消息内容
	 * 
	 * @param mrc
	 * @return
	 */
	MsgReceiveContent saveMsgReceiveContent(MsgReceiveContent mrc);

	/**
	 * 获取所有接收消息内容列表
	 * 
	 * @return
	 */
	List<MsgReceiveContent> findAllMsgReceiveContentList();

	/**
	 * 通过id获取接收消息内容
	 * 
	 * @param id
	 * @return
	 */
	MsgReceiveContent findMsgReceiveContentById(Long id);

	/**
	 * 通过ids获取接收消息内容列表
	 * 
	 * @param ids
	 * @return
	 */
	List<MsgReceiveContent> findMsgReceiveContentListByIds(List<Long> ids);

	/**
	 * 通过参数map获取接收消息内容列表
	 * 
	 * @param paraMap
	 * @return
	 */
	List<MsgReceiveContent> findMsgReceiveContentListByQueryMap(Map<String, Object> paraMap);

	/**
	 * 分页获取接收消息内容列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	Pagination<MsgReceiveContent> findMsgReceiveContentListByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap);

	/**
	 * 保存消息发送内容
	 * 
	 * @param msc
	 * @return
	 */
	MsgSendContent saveMsgSendContent(MsgSendContent msc);

	/**
	 * 获取所有消息发送内容列表
	 * 
	 * @return
	 */
	List<MsgSendContent> findAllMsgSendContentList();

	/**
	 * 通过id获取消息发送内容
	 * 
	 * @param ids
	 * @return
	 */
	MsgSendContent findMsgSendContentById(Long id);

	/**
	 * 通过ids获取消息发送内列表
	 * 
	 * @param ids
	 * @return
	 */
	List<MsgSendContent> findMsgSendContentListByIds(List<Long> ids);

	/**
	 * 通过参数map获取消息发送内列表
	 * 
	 * @param paraMap
	 * @return
	 */
	List<MsgSendContent> findMsgSendContentListByQueryMap(Map<String, Object> paraMap);

	/**
	 * 分页获取消息发送内列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	Pagination<MsgSendContent> findMsgSendContentListByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap);

	/**
	 * 保存消息发送记录
	 * 
	 * @param msr
	 * @return
	 */
	MsgSendRecord saveMsgSendRecord(MsgSendRecord msr);

	/**
	 * 保存消息发送记录 (初始化时用)
	 * 
	 * @param msr
	 * @return
	 */
	MsgSendRecord saveMsgSendRecord(String ifIdentify, Long targetId, String ext);

	/**
	 * 通过id获取MsgSendRecord列表
	 * 
	 * @param ids
	 * @return
	 */
	MsgSendRecord findMsgSendRecordById(Long id);

	/**
	 * 获取所有MsgSendRecord列表
	 * 
	 * @return
	 */
	List<MsgSendRecord> findAllMsgSendRecordList();
	
	/**
	 * 获取所有未反馈(feedbacktime==nulll)MsgSendRecord列表
	 * @return
	 */
	List<MsgSendRecord> findAllNoFeedbackMsgSendRecordList();

	/**
	 * 通过ids获取MsgSendRecord列表
	 * 
	 * @param ids
	 * @return
	 */
	List<MsgSendRecord> findMsgSendRecordListByIds(List<Long> ids);

	/**
	 * 通过参数map获取MsgSendRecord列表
	 * 
	 * @param paraMap
	 * @return
	 */
	List<MsgSendRecord> findMsgSendRecordListByQueryMap(Map<String, Object> paraMap);

	/**
	 * 分页获取MsgSendRecord列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	Pagination<MsgSendRecord> findMsgSendRecordListByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap);

	/**
	 * 处理过的MsgReceiveContent, 将is_proccessed修改成true
	 * 
	 * @param ids
	 */
	void updateMsgRecContIsProByIds(List<Long> ids);
	
	/**
	 * 分页获取MsgSendRecord MsgSendRecord列表
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	Pagination<MsgSendRecordCommand> findMsgSendRecordAndContentListByQueryMapWithPage(Page page, Sort[] sorts,Map<String, Object> paraMap);

	/**	 * 分页获取MsgReceiveContent列表
	 * like 条件查询
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	Pagination<MsgReceiveContent> findMsgReceiveContentListByPage(Page page, Sort[] sorts, Map<String, Object> paraMap);

	/**	 * 分页获取MsgReceiveContent列表
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
//	Pagination<MsgReceiveContent> findMsgReceiveContentListByPage(Page page, Sort[] sorts, Map<String, Object> paraMap);
    }

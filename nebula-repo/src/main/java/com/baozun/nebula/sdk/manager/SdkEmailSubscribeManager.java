package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.system.EmailSubscribe;

public interface SdkEmailSubscribeManager extends BaseManager{


	/**
	 * 保存订阅信息
	 * @param emailSubscribe
	 * @return
	 */
	EmailSubscribe saveEmailSubscribe(EmailSubscribe emailSubscribe);
	
	/**
	 * 获取所有EmailSubscribe列表
	 * @return
	 */
	
	List<EmailSubscribe> findAllEmailSubscribeList();
	
	/**
	 * 通过ids获取EmailSubscribe列表
	 * @param ids
	 * @return
	 */
	
	List<EmailSubscribe> findEmailSubscribeListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取EmailSubscribe列表
	 * @param paraMap
	 * @return
	 */

	List<EmailSubscribe> findEmailSubscribeListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取EmailSubscribe列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */

	Pagination<EmailSubscribe> findEmailSubscribeListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);

	/**
	 * 根据主键id查询EmailSubscribe对象
	 * @param id
	 * @return
	 */
	EmailSubscribe getEmailSubscribeById(Long id);

	/**
	 * 根据主键id集合批量删除EmailSubscribe
	 * @param idList
	 */
	void deleteAllByPrimaryKey(List<Long> ids);
	/**
	 * 根据主键id删除EmailSubscribe
	 * @param idList
	 */
	void deleteEmailSubscribeByPrimaryKey(Long id);
	
}

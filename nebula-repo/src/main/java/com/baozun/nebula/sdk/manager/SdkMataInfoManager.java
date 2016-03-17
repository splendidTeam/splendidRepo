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
package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.system.MataInfo;

/**
 * 配置信息管理类
 * @author Tianlong.Zhang
 *
 */
public interface SdkMataInfoManager extends BaseManager{
	
	/**
	 * 查询metainfo的数据
	 * 第一次会从数据库中去查询缓存到static map中
	 * @param key
	 * @return
	 */
	public String findValue(String key);
	
	/**
	 * 初始化matainfo中的数据
	 */
	public void initMetaMap();
	
	public MataInfo findMataInfoByCode(String code);
	
	/**
	 * 保存MataInfo
	 * 
	 */
	MataInfo saveMataInfo(MataInfo model);
	
	/**
	 * 通过id获取MataInfo
	 * 
	 */
	MataInfo findMataInfoById(Long id);

	/**
	 * 获取所有MataInfo列表
	 * @return
	 */
	List<MataInfo> findAllMataInfoList();
	
	/**
	 * 通过ids获取MataInfo列表
	 * @param ids
	 * @return
	 */
	List<MataInfo> findMataInfoListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取MataInfo列表
	 * @param paraMap
	 * @return
	 */
	List<MataInfo> findMataInfoListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取MataInfo列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<MataInfo> findMataInfoListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用MataInfo
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	void enableOrDisableMataInfoByIds(List<Long> ids,Integer state);
	
	/**
	 * 通过ids批量删除MataInfo
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	void removeMataInfoByIds(List<Long> ids);
	
	/**
	 * 获取有效的MataInfo列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	List<MataInfo> findAllEffectMataInfoList();
	
	/**
	 * 通过参数map获取有效的MataInfo列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	List<MataInfo> findEffectMataInfoListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的MataInfo列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	
	Pagination<MataInfo> findEffectMataInfoListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
}

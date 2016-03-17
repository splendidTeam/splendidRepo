package com.baozun.nebula.sdk.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.manager.BaseManager;

/**
 * 
 * @author 项硕
 */
public interface SdkLimitManager extends BaseManager{
	
	/**
	 * 根据条件，分页查询限购列表
	 * @param page
	 * @param sort
	 * @param queryMap
	 * @return
	 */
	public Pagination<LimitCommand> findLimitCommandConditionallyWithPage(Page page, Sort[] sorts, Map<String, Object> queryMap,String type,Long shopId);

	/**
	 * 取消启用（生效）限购
	 * @return
	 */ 
	void cancelLimit(Long pid, Long userId); 
	
	/**
	 * 保存或更新限购头
	 * @param cmd
	 * @param userId
	 * @return
	 */
	public Long saveOrUpdateLimitHead(LimitCommand cmd, Long userId);

	/**
	 * 保存或更新限购人群 
	 * @param cmd
	 * @param userId
	 * @return
	 */
	public Long saveOrUpdateLimitAudience(LimitCommand cmd, Long userId);

	/**
	 * 保存或更新限购范围
	 * @param cmd
	 * @param userId
	 * @return
	 */
	public Long saveOrUpdateLimitScope(LimitCommand cmd, Long userId);
	
	/**
	 * 保存或更新限购条件
	 * @param cmd
	 * @param userId
	 * @return
	 */
	public Long saveOrUpdateLimitCondition(LimitCommand cmd, Long userId);

	/**
	 * 根据ID查询限购VO
	 * @param id
	 * @return
	 */
	public LimitCommand findLimitCommandById(Long id);

	/**
	 * 删除限购条件
	 * @param id
	 */
	public void deleteStep(Long id);

	/**
	 * 启用前检查
	 * @param id
	 */
	public List<LimitCommand> checkBeforeActivation(Long id, Long shopId);
	
	/**
	 * 启用限购
	 * @param id
	 * @param userId
	 */
	public void activateLimit(Long id, Long userId);
	
	/**
	 * 发布限购到缓存
	 */
	public void publishLimit(Date currentDate);

}

package com.baozun.nebula.dao.rule;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.member.SimpleMemberCombo;
import com.baozun.nebula.command.rule.MemberTagRuleCommand;
import com.baozun.nebula.model.rule.MemberTagRule;

/**
 * 
 * @author 项硕
 */
public interface MemberTagRuleDao extends GenericEntityDao<MemberTagRule, Long> {

	/**
	 * 根据条件查询信息完整的组合人群
	 * 
	 * @param page
	 * @param sorts
	 * @param queryMap
	 * @return
	 */
	@NativeQuery(model = MemberTagRuleCommand.class, pagable = true, withGroupby = true)
	public Pagination<MemberTagRuleCommand> findMemberTagRuleListConditionallyWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap);

	/**
	 * 根据组合名查询
	 * 
	 * @param groupName
	 * @return
	 */
	@NativeQuery(model = MemberTagRuleCommand.class)
	public MemberTagRuleCommand findByGroupName(@QueryParam("name") String name);

	/**
	 * 根据ID查询
	 * 
	 * @param id
	 * @return
	 */
	@NativeQuery(model = MemberTagRuleCommand.class)
	public MemberTagRuleCommand findMemberTagRuleById(@QueryParam("id") Long id);

	/**
	 * 查询所有生效的自定义分组
	 * 
	 * @return
	 */
	@NativeQuery(model = MemberTagRuleCommand.class)
	public List<MemberTagRuleCommand> findAllAvailableMemberTagRuleCommandList();

	/**
	 * 获取有效的原子（会员和分组）筛选器列表
	 * 
	 * @return
	 */
	@NativeQuery(model = MemberTagRuleCommand.class)
	public List<MemberTagRuleCommand> findAvailableAtomicMemberTagRuleList();

	/**
	 * 根据类型查询
	 * 
	 * @param type
	 * @return
	 */
	@NativeQuery(model = MemberTagRuleCommand.class)
	public List<MemberTagRuleCommand> findCustomMemberGroupListByType(@QueryParam("type") Integer type);

	/**
	 * 更新生命周期
	 * 
	 * @param id
	 * @return
	 */
	@NativeUpdate
	public int updateLifecycleById(@QueryParam("id") Long id, @QueryParam("lifecycle") Integer lifecycle);

	/**
	 * 根据id列表，查询会员组合的简单信息
	 * 
	 * @param inIdList
	 * @return
	 */
	@NativeQuery(model = SimpleMemberCombo.class)
	List<SimpleMemberCombo> findSimpleMemberComboListByIdList(@QueryParam("ids") List<Long> inIdList);

	/**
	 * 根据id列表，查询会员筛选器
	 * 
	 * @param idList
	 * @return
	 */
	@NativeQuery(model = MemberTagRuleCommand.class)
	public List<MemberTagRuleCommand> findEffectMemberTagRuleListByIdList(@QueryParam("idList") List<Long> idList);
}

package com.baozun.nebula.sdk.manager;
 
import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.member.SimpleMemberCombo;
import com.baozun.nebula.command.rule.MemberTagRuleCommand;
import com.baozun.nebula.manager.BaseManager;

/**
 * 
 * @author 项硕
 */
public interface SdkMemberTagRuleManager extends BaseManager{
	
	/**
	 * 根据条件分页查询
	 * @return
	 */  
	public Pagination<MemberTagRuleCommand> findMemberTagRuleListConditionallyWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	/**
	 * 保存会员自定义分组
	 * @param customMemberGroup
	 */
	public void save(MemberTagRuleCommand customMemberGroup);

	/**
	 * 查询所有自定义分组
	 * @return
	 */
	public List<MemberTagRuleCommand> findAllAvailableMemberTagRuleCommandList();
	
	/**
	 * 启用会员标签规则 
	 * @param id
	 * @return
	 */
    public int activateMemberTagRule(Long id);

    /**
     * 禁用会员标签规则 
     * @param id
     * @return
     */
    public int inactivateMemberTagRule(Long id);

	/**
	 * 检查会员是否均在分组中
	 * @param members
	 * @param groups
	 */
	public boolean checkGroupWithMember(String members, String groups);

	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	public MemberTagRuleCommand findMemberTagRuleCommandById(Long id);

	/**
	 * @param customMemberGroup
	 */
	public void update(MemberTagRuleCommand customMemberGroup);

	/**
	 * 根据类型查询
	 * @param type
	 * @return
	 */
	public List<MemberTagRuleCommand> findCustomMemberGroupListByType(Integer type);

	/**
	 * 解析会员表达式
	 * @param exp
	 * @return
	 */
	public List<SimpleMemberCombo> findSimpleMemberComboListByExpression(
			String exp);

	/**
	 * 获取有效的原子（会员和分组）筛选器列表
	 * @return
	 */
	public List<MemberTagRuleCommand> findAvailableAtomicMemberTagRuleList();

}

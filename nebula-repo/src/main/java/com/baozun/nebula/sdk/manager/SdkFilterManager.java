package com.baozun.nebula.sdk.manager;

import java.util.Map;
import java.util.Set;

import com.baozun.nebula.manager.BaseManager;


/**
 * 
 * @author 项硕
 */
public interface SdkFilterManager extends BaseManager{
	
	/**
	 * 根据会员筛选器表达式，解析出其所包含所有会员ID列表
	 * @param exp
	 * @return
	 */
	public Set<Long> analyzeMemberExpression(String exp);
	
	/**
	 * 根据商品筛选器表达式，解析出其所包含所有商品ID列表
	 * @param exp
	 * @param shopId
	 * @return
	 */
	public Set<Long> analyzeProductExpression(String exp, Long shopId);
	
	/**
	 * 根据会员表达式，解析出是否冲突
	 * @param exp1
	 * @param exp2
	 * @return
	 */
	public boolean isMemberConfilct(String exp1, String exp2);

	/**
	 * 根据商品表达式，解析出是否冲突
	 * @param exp1
	 * @param exp2
	 * @param shopId
	 * @return
	 */
	public boolean isProductConfilct(String exp1, String exp2, Long shopId);
	
	/**
	 * 根据商品筛选器ID，解析出所对应商品分类ID与商品ID列表<br>
	 * 一、 外层Map的key:<br>
	 * 1. ‘in’ 代表包含列表<br>
	 * 2. ‘out’ 代表排除列表<br>
	 * 二、 内层Map的key:<br>
	 * 1. ‘all’ 代表全场（注：value不为null,则表示包含全场）<br>
	 * 1. ‘category’ 代表商品分类<br>
	 * 1. ‘item’ 代表商品<br>
	 * @param comboId
	 * @return
	 */
	public Map<String, Map<String, Set<Long>>> analyzeProductExpression(Long comboId);
}

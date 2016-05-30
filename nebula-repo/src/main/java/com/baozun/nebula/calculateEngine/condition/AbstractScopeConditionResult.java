package com.baozun.nebula.calculateEngine.condition;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.calculateEngine.action.AtomicAction;
import com.feilong.core.Validator;

/**
 * 条件范围结果抽象类
 * 
 * @author lihao
 * 
 */
public abstract class AbstractScopeConditionResult{

    /**
     * 类型为1,2
     */
    protected List<AtomicAction>                              atomicActionList;

    /**
     * 符号集合
     */
    protected List<String>                                    operList;

    /**
     * 类型为4
     */
    protected Map<String, List<AbstractScopeConditionResult>> scopeConditionResultMap;

    public List<AtomicAction> getAtomicActionList(){
        return atomicActionList;
    }

    public void setAtomicActionList(List<AtomicAction> atomicActionList){
        this.atomicActionList = atomicActionList;
    }

    public List<String> getOperList(){
        return operList;
    }

    public void setOperList(List<String> operList){
        this.operList = operList;
    }

    public Map<String, List<AbstractScopeConditionResult>> getScopeConditionResultMap(){
        return scopeConditionResultMap;
    }

    public void setScopeConditionResultMap(Map<String, List<AbstractScopeConditionResult>> scopeConditionResultMap){
        this.scopeConditionResultMap = scopeConditionResultMap;
    }

    /**
     * 根据范围种类id和种类所在的组查询是否符合条件结果
     * 
     * @param id
     *            例如：用户id，商品id
     * @param groupIds
     *            例如：用户所在的组集合，商品所在的分类集合
     * @return
     */
    public Boolean getResult(Long id,List<Long> groupIds){
        String itemId = id == null ? "" : String.valueOf(id);
        if (Validator.isNotNullOrEmpty(scopeConditionResultMap)){
            return getGroupResult(itemId, groupIds);
        }else{
            return getSingleResult(itemId, groupIds);
        }
    }

    /**
     * 获取单项结果
     * 
     * @param id
     *            例如：用户id，商品id
     * @param groupIds
     *            例如：用户所在的组集合，商品所在的分类集合
     * @return
     */
    public abstract Boolean getSingleResult(String id,List<Long> groupIds);

    /**
     * 获取组结果
     * 
     * @param id
     *            例如：用户id，商品id
     * @param groupIds
     *            例如：用户所在的组集合，商品所在的分类集合
     * @return
     */
    public abstract Boolean getGroupResult(String id,List<Long> groupIds);

}

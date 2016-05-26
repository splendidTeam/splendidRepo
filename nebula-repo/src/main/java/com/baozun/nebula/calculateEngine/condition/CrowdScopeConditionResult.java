package com.baozun.nebula.calculateEngine.condition;

import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.calculateEngine.action.AtomicAction;
import com.baozun.nebula.calculateEngine.param.ConditionType;
import com.baozun.nebula.calculateEngine.param.ScopeType;
import com.baozun.nebula.model.rule.MemberTagRule;

/**
 * 用户范围处理结果
 * 
 * @author lihao
 * 
 */
public class CrowdScopeConditionResult extends AbstractScopeConditionResult{

    @Override
    public Boolean getSingleResult(String memberId,List<Long> memGroupIds){
        Boolean stepFlag = null;
        Boolean hasOr = false;
        Boolean result = true;
        List<Boolean> list = new ArrayList<Boolean>();
        List<Integer> orList = new ArrayList<Integer>();
        if (null != operList && operList.size() > 0){
            if (!"".equals(operList.get(0).toString().trim())){
                operList.add(0, "");
            }
        }else{
            operList = new ArrayList<String>();
            operList.add("");
        }
        int operSize = operList.size();
        for (int i = 0; i < operSize; i++){
            if (ConditionType.OR.equals(operList.get(i).toString())){
                hasOr = true;
                orList.add(i);
            }
        }

        if (null != atomicActionList && atomicActionList.size() > 0){
            int listSize = atomicActionList.size();
            for (int i = 0; i < listSize; i++){
                Boolean isExclude = false;
                if (ConditionType.NOT_AND.equals(operList.get(i))){
                    isExclude = true;
                }

                AtomicAction atomicAction = new AtomicAction();
                atomicAction = atomicActionList.get(i);

                if (ScopeType.EXP_MID.equals(atomicAction.getType())){
                    Boolean flag = atomicAction.isOnScope(memberId);
                    if (isExclude){
                        stepFlag = !flag;
                    }else{
                        stepFlag = flag;
                    }
                }
                if (ScopeType.EXP_CUSTOM_MEM.equals(atomicAction.getType())){
                    Boolean flag = atomicAction.isOnCustomScope(memberId);
                    if (isExclude){
                        stepFlag = !flag;
                    }else{
                        stepFlag = flag;
                    }
                }
                if (ScopeType.EXP_GRPID.equals(atomicAction.getType())){
                    if (null != memGroupIds && memGroupIds.size() > 0){
                        for (Long memGroupId : memGroupIds){
                            stepFlag = atomicAction.isOnScope(String.valueOf(memGroupId));
                            if (isExclude){
                                if (!hasOr){
                                    if (stepFlag){
                                        return false;
                                    }else{
                                        stepFlag = true;
                                    }
                                }else{
                                    if (stepFlag){
                                        stepFlag = false;
                                        break;
                                    }else{
                                        stepFlag = true;
                                    }
                                }
                            }else{
                                if (stepFlag){
                                    break;
                                }
                            }
                        }
                    }
                }
                list.add(stepFlag);
            }
            if (!hasOr){
                for (Boolean re : list){
                    if (!re){
                        return false;
                    }
                }
            }else{
                Boolean tempFlag = null;
                for (Integer temp : orList){
                    if (list.get(temp - 1) || list.get(temp + 1)){
                        tempFlag = true;
                    }
                }
                if (null == tempFlag){
                    result = false;
                }
            }
        }
        return result;
    }

    @Override
    public Boolean getGroupResult(String memberId,List<Long> memGroupIds){
        Boolean flag = null;
        if (null != scopeConditionResultMap && scopeConditionResultMap.size() > 0){
            int operSize = operList.size();
            int i = 0;
            for (int k = 1; k <= scopeConditionResultMap.size(); k++){

                Boolean stepFlag = false;
                List<AbstractScopeConditionResult> crowdScopeConditionResult = scopeConditionResultMap.get(String.valueOf(k));

                for (AbstractScopeConditionResult crowdScopeConditionResultD : crowdScopeConditionResult){
                    if (null == crowdScopeConditionResultD)
                        continue;
                    if (crowdScopeConditionResultD.getSingleResult(memberId, memGroupIds)){
                        stepFlag = true;
                        break;
                    }
                }

                if (operSize > 0 && i <= operSize){
                    if (null == flag){
                        if (ConditionType.AND.equals(operList.get(i))){
                            if (!stepFlag){
                                return false;
                            }
                        }
                        if (ConditionType.OR.equals(operList.get(i))){
                            if (stepFlag){
                                return true;
                            }
                        }
                        if (ConditionType.NOT_AND.equals(operList.get(i))){
                            if (!stepFlag){
                                return false;
                            }
                        }
                    }else{
                        if (ConditionType.AND.equals(operList.get(i - 1))){
                            if (!stepFlag){
                                return false;
                            }
                        }
                        if (ConditionType.OR.equals(operList.get(i - 1))){
                            if (stepFlag){
                                return true;
                            }
                        }
                        if (ConditionType.NOT_AND.equals(operList.get(i - 1))){
                            if (stepFlag){
                                return false;
                            }
                            if (flag == true && stepFlag == false){
                                stepFlag = true;
                            }
                        }
                    }
                }
                flag = stepFlag;
                i++;
            }
        }else{
            flag = false;
        }
        return flag;
    }

}
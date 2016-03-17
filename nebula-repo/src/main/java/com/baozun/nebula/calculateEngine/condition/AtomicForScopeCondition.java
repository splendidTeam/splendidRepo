package com.baozun.nebula.calculateEngine.condition;

import java.math.BigDecimal;
import java.util.List;

import com.baozun.nebula.calculateEngine.action.AtomicAction;
import com.baozun.nebula.calculateEngine.param.ConditionType;
import com.baozun.nebula.command.Command;

public class AtomicForScopeCondition implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3154388017076551027L;

	private List<AtomicAction> atomicForItemIdActionList;

	/**
	 * 操作符
	 */
	private List<String> operStr;

	public Boolean getItemResult(String id) {
		if (null != atomicForItemIdActionList
				&& atomicForItemIdActionList.size() > 0) {
		}
		return true;
	}

	public List<AtomicAction> getAtomicForItemIdActionList() {
		return atomicForItemIdActionList;
	}

	public void setAtomicForItemIdActionList(
			List<AtomicAction> atomicForItemIdActionList) {
		this.atomicForItemIdActionList = atomicForItemIdActionList;
	}

	public List<String> getOperStr() {
		return operStr;
	}

	public void setOperStr(List<String> operStr) {
		this.operStr = operStr;
	}

}
package com.baozun.nebula.calculateEngine.condition;

import java.math.BigDecimal;

import com.baozun.nebula.command.Command;

public class AtomicCondition implements Command {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6524113250353029765L;

	private long promotionid;
	
	private long normalConditionId;
	
	private long complexConditionId;
	
	private String operateTag;
	
	private String conditionTag;
	
	private BigDecimal conditionValue;
	
	private String scopeTag;
	
	private long scopeValue;
	
	private boolean conditionResult;
	
	private Integer stepPriority;//阶梯优先级

	private String complexType;//阶梯或选购
	
	private String choiceMark;//选购的主商品，选购商品
	
	private String conditionExpression;//原子条件表达式

	private boolean multiplicationMark;//是否倍增
	
	private Integer multiplicationFactor = 1;//倍增因子
	
	public String getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(String value) {
		this.conditionExpression = value;
	}
	
	public String getChoiceMark() {
		return choiceMark;
	}

	public void setChoiceMark(String value) {
		this.choiceMark = value;
	}
	
	public String getComplexType() {
		return complexType;
	}

	public void setComplexType(String value) {
		this.complexType = value;
	}
	
	public long getNormalConditionId() {
		return normalConditionId;
	}

	public void setNormalConditionId(long value) {
		this.normalConditionId = value;
	}
	
	public long getComplexConditionId() {
		return complexConditionId;
	}

	public void setComplexConditionId(long value) {
		this.complexConditionId = value;
	}
		
	public long getPromotionId() {
		return promotionid;
	}

	public void setPromotionId(long value) {
		this.promotionid = value;
	}
	
	public Integer getStepPriority() {
		return stepPriority;
	}

	public void setStepPriority(Integer priority) {
		this.stepPriority = priority;
	}
	
	public String getOperateTag() {
		return operateTag;
	}

	public void setOperateTag(String op) {
		this.operateTag = op;
	}
	
	public String getConditionTag() {
		return conditionTag;
	}

	public void setConditionTag(String tag) {
		this.conditionTag = tag;
	}

	public BigDecimal getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(BigDecimal value) {
		this.conditionValue = value;
	}

	public String getScopeTag() {
		return scopeTag;
	}

	public void setScopeTag(String tag) {
		this.scopeTag = tag;
	}
	
	public long getScopeValue() {
		return scopeValue;
	}

	public void setScopeValue(long value) {
		this.scopeValue = value;
	}
	
	public void setConditionResult(boolean result){		
		this.conditionResult = result;
	}
	
	public boolean getConditionResult(){		
		return this.conditionResult;
	}
	
	public boolean getMultiplicationMark() {
		return multiplicationMark;
	}

	public void setMultiplicationMark(boolean multiplicationMark) {
		this.multiplicationMark = multiplicationMark;
	}

	public Integer getMultiplicationFactor() {
		return multiplicationFactor;
	}

	public void setMultiplicationFactor(Integer multiplicationFactor) {
		this.multiplicationFactor = multiplicationFactor;
	}
}
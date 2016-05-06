package com.baozun.nebula.search.command;

import java.util.List;

/**
 * 排除字段的查询对象，有些查询是需要排除掉查询的
 * @author jumbo
 *
 */
public class ExcludeSearchCommand {

	/**
	 * 字段名称，对应solr中的字段
	 */
	private String	fieldName;
	
	/**
	 * 需要排除的值
	 */
	private List<String> values;
	
	

	public String getFieldName() {
		return fieldName;
	}

	public List<String> getValues() {
		return values;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
	
	
}

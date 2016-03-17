/**
 * 
 */
package com.baozun.nebula.command.option;

import com.baozun.nebula.command.Command;

/**
 * @author xianze.zhang
 *@creattime 2013-6-19
 */
public class OptionCommand implements Command{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8651065363863721479L;
	public OptionCommand() {
		super();
	}
	public OptionCommand(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}
	/**
	 * 显示名
	 */
	private String label;
	/**
	 * 值
	 */
	private String value;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}

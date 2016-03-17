package com.baozun.nebula.solr.command;

import java.util.List;

import com.baozun.nebula.command.Command;

public class ItemCategoryCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -411632764501543152L;
	
	private Long id;
	
	private String name;
	
	private List<ItemCategoryCommand> nextItemCategoryCommand;
		
	private Long preId;
	
	private String code;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ItemCategoryCommand> getNextItemCategoryCommand() {
		return nextItemCategoryCommand;
	}

	public void setNextItemCategoryCommand(
			List<ItemCategoryCommand> nextItemCategoryCommand) {
		this.nextItemCategoryCommand = nextItemCategoryCommand;
	}

	public Long getPreId() {
		return preId;
	}

	public void setPreId(Long preId) {
		this.preId = preId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
		
}

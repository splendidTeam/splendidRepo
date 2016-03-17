package com.baozun.nebula.command.freight;

import com.baozun.nebula.command.Command;

public class DistributionModeCommand implements Command {
	private static final long serialVersionUID = -8556162068486593722L;

	private Long id;

	private String name;

	private boolean selected;

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

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}

/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.command.column;

import java.util.Map;

import com.baozun.nebula.command.Command;

/**
 * @author Tianlong.Zhang
 *
 */
public class ColumnPageCommand implements Command {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8930978353172677922L;

	private Long				id;

	private String				code;

	private String				name;
	
	private Map<String, ColumnModuleCommand> columnModuleMap;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the columnModuleMap
	 */
	public Map<String, ColumnModuleCommand> getColumnModuleMap() {
		return columnModuleMap;
	}

	/**
	 * @param columnModuleMap the columnModuleMap to set
	 */
	public void setColumnModuleMap(Map<String, ColumnModuleCommand> columnModuleMap) {
		this.columnModuleMap = columnModuleMap;
	}
	
}

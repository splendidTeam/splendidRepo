/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
package com.baozun.nebula.automake.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Justin
 *
 */
public class MakeModel {

	/**
	 * 生命周期的字段名
	 */
	private String lifecycle;
	
	/**
	 * 序列(主键自增)
	 */
	private String sequeneName;
	
	/**
	 * 主键名
	 */
	private String pkName;
	
	/**
	 * 实体类名
	 */
	private String entityName;
	
	/**
	 * 实体类包名(用于dao生成时的路径)
	 */
	private String packagName;	
	/**
	 * 表名
	 */
	private String tableName;
	
	/**
	 * 作者名(需要用户显示定义)
	 */
	private String authName;
	
	/**
	 * version字段(version参与修改,但会使用now()代替字段值)
	 */
	private String versionField;
	
	/**
	 * 是否有删除状态
	 */
	private Boolean hasDeleteLifecycle=true;

	/**
	 * 字段描述
	 */
	private List<PropertyDesc> propertyList=new ArrayList<PropertyDesc>();
	
	
	

	public Boolean getHasDeleteLifecycle() {
		return hasDeleteLifecycle;
	}

	public void setHasDeleteLifecycle(Boolean hasDeleteLifecycle) {
		this.hasDeleteLifecycle = hasDeleteLifecycle;
	}

	public String getPackagName() {
		return packagName;
	}

	public void setPackagName(String packagName) {
		this.packagName = packagName;
	}

	public List<PropertyDesc> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<PropertyDesc> propertyList) {
		this.propertyList = propertyList;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getVersionField() {
		return versionField;
	}

	public void setVersionField(String versionField) {
		this.versionField = versionField;
	}

	public String getSequeneName() {
		return sequeneName;
	}

	public void setSequeneName(String sequeneName) {
		this.sequeneName = sequeneName;
	}

	public String getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(String lifecycle) {
		this.lifecycle = lifecycle;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}
	
}

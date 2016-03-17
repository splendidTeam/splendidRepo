package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.auth.PrivilegeCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.auth.Privilege;

public interface SdkPrivilegeManager extends BaseManager{

	Pagination<PrivilegeCommand> findPrivilegeCommandPageByQueryMap(Page page,Sort[] sorts, Map<String, Object> paraMap);
	
	void removePrivilegeByIds(List<Long>  ids);
	
	void enableOrDisableById(Long id,int state);
	
	Privilege  editPrivilege(PrivilegeCommand privilege);
	
	
	Privilege  findPrivilegeById(Long id);
}


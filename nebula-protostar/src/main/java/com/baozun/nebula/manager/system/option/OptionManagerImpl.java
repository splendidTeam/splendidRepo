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
package com.baozun.nebula.manager.system.option;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.option.OptionCommand;
import com.baozun.nebula.dao.auth.OrgTypeDao;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.auth.OrgType;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.utils.spring.SpringUtil;

/**
 * 
 * @author xianze.zhang
 *@creattime 2013-6-28
 */
@Transactional
@Service("optionManager")
public class OptionManagerImpl implements OptionManager {
	
	public static final String TYPE_CHOOSE_OPTION = "chooseOption";
	public static final String TYPE_COMMON_OPTION = "commonOption";
	public static final String DEFAULT_OPTION_METHOD_NAME = "findOptionCommand";
	
	@Autowired
	private OrgTypeDao orgTypeDao;
	@Autowired
	private ChooseOptionManager chooseOptionManager;
	private PropertyUtilsBean propertyUtils = new PropertyUtilsBean();

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.system.OptionManager#getOptionCommandsByCod(java.lang.String)
	 */
	@Override
	public List<OptionCommand> getOptionCommandsByExpression(String expression){
		// TODO Auto-generated method stub
		//解析表达式，类型.code
		String[] exp = expression.split("\\.");
		String type = exp[0];
		String code = exp[1];
		
		if(OptionManagerImpl.TYPE_CHOOSE_OPTION.equals(type)){
			return this.findChooseOptionCommandsByCode(code);
		}else{
			return this.findCommonOptionCommandsByCode(code);
		}
		
		
	}

	/**
	 * 根据groupcode查询所有的option
	 * @param code
	 * @return
	 */
	@Override
	public List<OptionCommand> findChooseOptionCommandsByCode(String code){
		List<ChooseOption> chooseOptions = chooseOptionManager.findEffectChooseOptionListByGroupCode(code);
		List<OptionCommand> optionCommands = new ArrayList<OptionCommand>();
		if(chooseOptions==null){
			return optionCommands;
		}
		for(ChooseOption option:chooseOptions){
			optionCommands.add(new OptionCommand(option.getOptionLabel(),option.getOptionValue()));
		}
		return optionCommands;
	}
	/**
	 * 根据code查询通用的option
	 * 会调用code+"Dao"的findOptionCommand方法，因此在code+"Dao"类中必须定义findOptionCommand，否则返回null
	 * @param code
	 * @return
	 */
	@Override
	public List<OptionCommand> findCommonOptionCommandsByCode(String code){
		String methodName = DEFAULT_OPTION_METHOD_NAME;
		String[] exp = code.split("\\.");
		if(exp.length>1){
			methodName = exp[1];
		}
		Object bean = SpringUtil.getBean(code+"Dao");
		Method method;
		List<OptionCommand> invoke = null;
		try {
			method = bean.getClass().getMethod(methodName);
			invoke = (List<OptionCommand>)method.invoke(bean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return invoke;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.system.option.OptionManager#convertCommonOptionFromList(java.util.List, java.lang.String, java.lang.String)
	 */
	@Override
	public List<OptionCommand> convertCommonOptionFromList(List objects,String labelProperty,String valueProperty){
		// TODO Auto-generated method stub
		List<OptionCommand> optionCommands = new ArrayList<OptionCommand>();
		for(Object o : objects){
			try{
				OptionCommand oc = new OptionCommand();
				oc.setLabel(propertyUtils.getProperty(o, labelProperty).toString());
				oc.setValue(propertyUtils.getProperty(o, valueProperty).toString());
				optionCommands.add(oc);
			}catch (Exception e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return optionCommands;
	}
}

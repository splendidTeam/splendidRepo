/**
 * 
 */
package com.baozun.nebula.manager.system.option;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.baozun.nebula.command.option.OptionCommand;


/**
 * @author xianze.zhang
 *@creattime 2013-6-19
 */
public interface OptionManager {
	/**
	 * 根据表达式查询所有的option
	 * 表达式格式：type.code
	 * 例如：chooseOption.lifecycle
	 * 则返回chooseOption的groupcode为lifecycle的option列表
	 * @param code
	 * @return
	 */
	public List<OptionCommand> getOptionCommandsByExpression(String expreesion);

	/**
	 * 根据groupcode查询所有的option
	 * @param code
	 * @return
	 */
	public List<OptionCommand> findChooseOptionCommandsByCode(String code);
	/**
	 * 根据code查询通用的option
	 * 会调用code+"Dao"的findOptionCommand方法，因此在code+"Dao"类中必须定义findOptionCommand，否则返回null
	 * @param code
	 * @return
	 */
	public List<OptionCommand> findCommonOptionCommandsByCode(String code);
	/**
	 * 根据list集合，并且指定list集合中对象的label属性和value属性。该方法会根据指定的label属性和value属性转换成List<OptionCommand>
	 * @param code
	 * @return
	 */
	public List<OptionCommand> convertCommonOptionFromList(List objects,String labelProperty,String valueProperty);
	
}

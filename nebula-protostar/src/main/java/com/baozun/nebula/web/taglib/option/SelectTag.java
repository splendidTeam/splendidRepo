/**
 * 
 */
package com.baozun.nebula.web.taglib.option;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.commons.beanutils.PropertyUtilsBean;

import com.baozun.nebula.command.option.OptionCommand;
import com.baozun.nebula.manager.system.option.OptionManager;
import com.baozun.nebula.web.taglib.BaseJspTag;
import com.baozun.nebula.web.taglib.BaseTag;

/**
 * @author xianze.zhang
 *@creattime 2013-6-18
 */
public class SelectTag extends BaseJspTag{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2650102243856309410L;
	
	private String defaultValue;
	private String expression;
	private String type;
	public static final String TYPE_TEXT = "text";
	private Object data;
	private String dataLabelProperty;
	private String dataValueProperty;
	
	/**
	 * 当nullOption不为null时，会为下拉列表加一个空的option，空的option显示的值为nullOption字段的值。
	 * nullOption支持国际化字符串，即可以传国际化的code过来。
	 */
	private String nullOption;
	
	
	private static OptionManager optionManager;
	
	
	

	@Override
	public int doStartTag() throws JspException {
		List<OptionCommand> optionCommands = null;
		if(optionManager==null){
			optionManager = (OptionManager) getBean("optionManager");
		}
		//若data不为空，则直接取，忽略expression
		if(data!=null){
			if(data instanceof List){
				List dataList = (List)data;
				if(dataList.size()>0){
					//若为OptionCommand类型，则不用转换
					if(dataList.get(0) instanceof OptionCommand){
						optionCommands = dataList;
					}else{
						optionCommands = optionManager.convertCommonOptionFromList(dataList, dataLabelProperty, dataValueProperty);
					}
				}
			}
		}else{
			try {
				optionCommands = optionManager.getOptionCommandsByExpression(expression);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(SelectTag.TYPE_TEXT.equals(type)){
			printText(optionCommands);
			return SKIP_PAGE;
		}else{
			this.print("<select "+genPropertyString()+" >");
			printOptionTag(optionCommands);
		}
		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		this.print("</select>");
		return EVAL_PAGE;
	}

/**
 * 
 * 打印option
 * <option></option>
 * @param optionCommands
 */
	private void printOptionTag(List<OptionCommand> optionCommands){
		if(nullOption!=null){
			println("<option value=''>"+this.getMessage(nullOption, null)+"</option>" );
		}
		if(optionCommands==null)return;
		for(OptionCommand option:optionCommands){
			
			println("<option value='"+option.getValue()+"' " );
			if(defaultValue!=null&&defaultValue.equals(option.getValue())){
				print("selected");
			}
			print(">"+option.getLabel()+"</option>");
		}
	}
	/**
	 * 
	 * 打印文本
	 * <option></option>
	 * @param optionCommands
	 */
	private void printText(List<OptionCommand> optionCommands){
		if(defaultValue!=null){
			for(OptionCommand option:optionCommands){
				if(defaultValue.equals(option.getValue())){
					print(option.getLabel());
				}
			}
		}
	}


	
	public Object getData(){
		return data;
	}

	
	public void setData(Object data){
		this.data = data;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNullOption() {
		return nullOption;
	}

	public void setNullOption(String nullOption) {
		this.nullOption = nullOption;
	}

	
	
	public String getDataLabelProperty(){
		return dataLabelProperty;
	}

	
	public void setDataLabelProperty(String dataLabelProperty){
		this.dataLabelProperty = dataLabelProperty;
	}

	
	public String getDataValueProperty(){
		return dataValueProperty;
	}

	
	public void setDataValueProperty(String dataValueProperty){
		this.dataValueProperty = dataValueProperty;
	}

}

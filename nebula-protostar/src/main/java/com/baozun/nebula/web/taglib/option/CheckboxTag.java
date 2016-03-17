/**
 * 
 */
package com.baozun.nebula.web.taglib.option;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.DataBinder;

import com.baozun.nebula.command.option.OptionCommand;
import com.baozun.nebula.manager.system.option.OptionManager;
import com.baozun.nebula.utils.convert.ObjectConvertUtil;
import com.baozun.nebula.utils.property.PropertyUtil;
import com.baozun.nebula.web.taglib.BaseJspTag;
import com.baozun.nebula.web.taglib.BaseTag;


/**
 * 复选框标签
 * @author xianze.zhang
 *@creattime 2013-7-3
 */
public class CheckboxTag extends BaseJspTag{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2650102243856309410L;
	
	private Object defaultValues;
	private String defaultValuesProperty;
	private String expression;
	private String type;
	private Object data;
	
	private String dataLabelProperty;
	private String dataValueProperty;
	
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
		printOptionTag(optionCommands);
		return SKIP_BODY;
	}
	
/**
 * 
 * 打印checkbox。默认套一个span
 * @param optionCommands
 */
	private void printOptionTag(List<OptionCommand> optionCommands){
		//class样式默认定义在span上面
		String checkboxHead = "<span class="+cssClass;
		this.setCssClass(null);
		checkboxHead = checkboxHead+"><input type=checkbox "+genPropertyString();
		for(OptionCommand option:optionCommands){
			
			println(checkboxHead+" value='"+option.getValue()+"' " );
			if(ifChecked(option.getValue())){
				print("checked");
			}
			print("/>"+option.getLabel()+"</span>");
		}
	}
	/**
	 * 判断是否应该被选中
	 * @param value
	 * @return
	 */
	private boolean ifChecked(String value){
		if(defaultValues==null||StringUtils.isBlank(value)){
			return false;
		}
		if(defaultValues instanceof List){
			List defaultValueList = (List)defaultValues;
			for(Object defaultValue :defaultValueList){
				//若没有指定defaultValue存放value的属性名
				if(defaultValuesProperty==null){
					if(value.equals(defaultValue.toString())){
						return true;
					}
				}else{
					if(value.equals(PropertyUtil.getProperty(defaultValue,defaultValuesProperty))){
						return true;
					}
				}
			}
			return false;
		}else if(defaultValues instanceof Map){
			Map defaultValueMap = (Map)defaultValues;
			//获得key的class类型
			Class keyClass = defaultValueMap.keySet().iterator().next().getClass();
			//
			Object o = ObjectConvertUtil.simpleTypeConvert(value,keyClass);
			if(defaultValueMap.get(o)!=null){
				return true;
			}
		}
		return false;
	}


	
	public Object getData(){
		return data;
	}

	
	public void setData(Object data){
		this.data = data;
	}


	

	
	public Object getDefaultValues(){
		return defaultValues;
	}

	
	public void setDefaultValues(Object defaultValues){
		this.defaultValues = defaultValues;
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

	
	public String getDefaultValuesProperty(){
		return defaultValuesProperty;
	}

	
	public void setDefaultValuesProperty(String defaultValuesProperty){
		this.defaultValuesProperty = defaultValuesProperty;
	}

}

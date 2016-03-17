package com.baozun.nebula.web.taglib;


/**
 * 对jsp自带标签的属性进行了封装，例如：id,name,style,class
 * 需要对jsp自带的标签进行封装时，需要继承此类。
 * 例如<select>标签，jsp的<input>标签
 * @author xianze.zhang
 *@creattime 2013-6-19
 */
public abstract class BaseJspTag extends BaseTag{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1525585809296666412L;
	
	protected String id;
	protected String name;
	/**
	 * class属性
	 */
	protected String cssClass;
	/**
	 * jsp标签的其他属性。
	 * 例如：otherProperties="value=2 type=but"
	 * 在生成标签时，在属性列表内会增加value=2 type=but这两个属性
	 */
	protected String otherProperties;
	protected String style;
	/**
	 * 生成属性列表字符串
	 * @return
	 */
	protected String genPropertyString(){
		StringBuilder property = new StringBuilder();
		if(id!=null){
			property.append(" id=\""+id+"\"");
		}
		if(name!=null){
			property.append(" name=\""+name+"\"");
		}
		if(cssClass!=null){
			property.append(" class=\""+cssClass+"\"");
		}
		if(style!=null){
			property.append(" style=\""+style+"\"");
		}
		if(otherProperties!=null){
			property.append(otherProperties);
		}
		return property.toString();
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getCssClass(){
		return cssClass;
	}
	
	public void setCssClass(String cssClass){
		this.cssClass = cssClass;
	}
	
	public String getOtherProperties(){
		return otherProperties;
	}
	
	public void setOtherProperties(String otherProperties){
		this.otherProperties = otherProperties;
	}
	
	public String getStyle(){
		return style;
	}
	
	public void setStyle(String style){
		this.style = style;
	}
}
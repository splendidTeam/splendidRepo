package com.baozun.nebula.command;


public class TagCommand implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5176416254528928849L;
	
	/** PK. */
	private Long				id;
	
	/**
	 * 标签名称
	 */
	private String             name;
	
	/**
	 * 类型
	 */
	private Integer             type;
	
	/**
	 * 类型名称
	 */
	private String			    typeName;

	
	public Long getId(){
		return id;
	}

	
	public void setId(Long id){
		this.id = id;
	}

	
	public String getName(){
		return name;
	}

	
	public void setName(String name){
		this.name = name;
	}

	
	public Integer getType(){
		return type;
	}

	
	public void setType(Integer type){
		this.type = type;
	}

	
	public String getTypeName(){
		return typeName;
	}

	
	public void setTypeName(String typeName){
		this.typeName = typeName;
	}
	
	

}

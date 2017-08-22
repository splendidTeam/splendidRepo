package com.baozun.nebula.sdk.command;

import com.baozun.nebula.command.Command;

/**
 * 
 * @author 阳羽
 * @createtime 2014-2-10 下午02:22:22
 */
public class ItemSortCommand  implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5749141219490561552L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 商品编码
	 */
	private String code;
	
	/**
	 * 商品名称
	 */
	private String name;
	
	/**
	 * 商品分类
	 */
	private String categoryName;
	
	/**
	 * 排序号
	 */
	private Integer sortNo;
	
	/**
	 * 商品图片url
	 */
	private String picUrl;
	
	/**
     * 商品库存
     */
    private String repertory;
    
    
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

    
    public String getRepertory(){
        return repertory;
    }

    
    public void setRepertory(String repertory){
        this.repertory = repertory;
    }

    
    public static long getSerialversionuid(){
        return serialVersionUID;
    }
	
}

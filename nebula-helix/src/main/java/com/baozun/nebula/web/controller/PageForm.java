/**
 * 
 */
package com.baozun.nebula.web.controller;

import loxia.dao.Page;
import loxia.dao.Sort;

/**
 * 默认的分页参数输入对象
 * 
 * @author Dean.Lu
 * @data 2016年3月21日 上午11:07:25
 *
 */
public class PageForm extends BaseForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1445351485803819901L;

	/**
	 * 当前页
	 */
	private int currentPage = 1;

	/**
	 * 每页行数
	 */
	private int size = Integer.MAX_VALUE;

	/**
	 * 排序
	 */
	private String sort;

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage
	 *            the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @param sort 
	 *            the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	/**
	 * 返回Page对象
	 * @return 返回Page对象
	 */
	public Page getPage(){
		return new Page(currentPage, size);
	}
	
	/**
	 * 返回Sort[]对象
	 * @return 当sort为空或者为空字符串时，返回空数组，否则返回转义的对象。
	 */
	public Sort[] getSorts(){
		if(sort == null || sort.trim().length() == 0) return new Sort[]{};
		
		sort = sort.trim();
		String[] strSorts = sort.split(",");
		Sort[] rtnSorts = new Sort[strSorts.length];
		for(int i=0; i< rtnSorts.length; i++){
			rtnSorts[i] = convert(strSorts[i]);
		}
		return rtnSorts;
	}
	
	private Sort convert(String sort){
		String[] p = sort.split(" ");
		if(p.length > 2) throw new RuntimeException("Sort Format is not correct, please check:" + sort);
		return new Sort(sort);
	}

}

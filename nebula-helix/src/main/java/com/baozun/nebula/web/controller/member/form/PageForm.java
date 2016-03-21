/**
 * 
 */
package com.baozun.nebula.web.controller.member.form;

import com.baozun.nebula.web.controller.BaseForm;

/**
 * @author Scorpio
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
	private int currentPage;

	/**
	 * 每页行数
	 */
	private int size;

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

}

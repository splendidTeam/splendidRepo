package com.baozun.nebula.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
/**、
 * 用于显示固定多少页显示...的分页自定义标签
 * @author qiang.yang
 * @createtime 2013-12-19 AM 9:52
 *
 */
public class PaginationTag extends TagSupport {

	private static final long serialVersionUID = 2608981395905690755L;

	private int offSet;//显示前后几条
	private int totalPages;//总页数
	private int currentPage;//当前页
	private int showNum;//显示几条
	private String url;//那个url地址

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			if (totalPages == 0) {
				out.println("totalPages not zero...");
				return SKIP_BODY;
			}
			
			//当前页大于总页数，则当前页设为最后页
			if (currentPage > totalPages)	currentPage = totalPages;

			// 如果总页数小于等于要显示的页数
			if (totalPages <= showNum) {
				for (int i = 1; i <= totalPages; i++) {
					if (i == currentPage) {
						out.println("<a href='" + url + getConnector(url)+"currentPage=" + i+ "' class='selected'>" + i + "</a>");
					} else {
						out.println("<a href='" + url +  getConnector(url)+"currentPage=" + i+ "'>" + i + "</a>");
					}
				}

			} else {// 总页数大于要显示的条数
					// 如果当前也加上offSet页小于等于showNum ，就显示前showNum页-->
				if (currentPage + offSet <= showNum) {
					for (int i = 1; i <= showNum; i++) {
						if (i == currentPage) {
							out.println("<a href='" + url +  getConnector(url)+"currentPage=" + i	+ "' class='selected'>" + i + "</a>");
						} else {
							out.println("<a href='" + url +  getConnector(url)+"currentPage="+ i + "'>" + i + "</a>");
						}
					}
				} else {
					// 如果当前也加上offSet小于总页数 ，就显示前showNum页
					if (currentPage <= totalPages) {
						if (currentPage + offSet <= totalPages) {

							out.println("<a href='" + url+  getConnector(url)+"currentPage=1' >1</a>");
							//  当前页大于showNum才显示1后面的.. 
							if (currentPage >= showNum) {
								out.println("<span>...</span>");
							}
							// 当前页，当前页的前offSet页，当前页的后offSet页
							for (int i = currentPage - offSet; i <= currentPage+ offSet; i++) {
								if (i == currentPage) {
									out.println("<a href='" + url+  getConnector(url)+"currentPage=" + i	+ "' class='selected'>" + i + "</a>");
								} else {
									out.println("<a href='" + url	+  getConnector(url)+"currentPage=" + i + "'>" + i+ "</a>");
								}
							}
						} else {
							out.println("<a href='" + url+  getConnector(url)+"currentPage=1' >1</a>");
							//  当前页大于showNum才显示1后面的.. 
							if (currentPage >= showNum) {
								out.println("<span>...</span>");
							}
							// 当前页，当前页的前offSet页，当前页的后offSet页
							for (int i = currentPage - offSet; i <= totalPages; i++) {
								if (i == currentPage) {
									out.println("<a href='" + url+  getConnector(url)+"currentPage=" + i	+ "' class='selected'>" + i + "</a>");
								} else {
									out.println("<a href='" + url+  getConnector(url)+"currentPage=" + i + "'>" + i+ "</a>");
								}
							}

						}

					} 
				}
				//只要当前页面加上offSet小于总页数，才显示...
				if (currentPage + offSet < totalPages) {
					out.println("<span>...</span>");
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new JspException(e.getMessage());
		}
		  return SKIP_BODY;
	}
	public static String getConnector(String url){
		if(url.indexOf("?")>-1)
			return "&";
		return "?";
	}
	@Override
    public void release() {
        super.release();
    }

	public int getOffSet() {
		return offSet;
	}

	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getShowNum() {
		return showNum;
	}

	public void setShowNum(int showNum) {
		this.showNum = showNum;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	

}

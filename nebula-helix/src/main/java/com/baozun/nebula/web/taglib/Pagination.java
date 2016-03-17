package com.baozun.nebula.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class Pagination extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8777438090844578804L;

	private String startWith;
	
	private String endWith;
	
	private Integer curPage;
	
	private Integer maxPage;
	
	
	private String makeLink(int cur){
		
		StringBuffer result=new StringBuffer("<a ");
		
		
		
		if(cur==curPage){
			result.append("class=\"selected\" ");
			result.append("href=\"javascript:void(0)\"");
		}
		else{
			result.append("href=\"");
			result.append(startWith);
			result.append(cur);
			result.append(endWith);
			result.append("\"");
		}
		
		result.append(">");
		result.append(cur);
		result.append("</a>");

		return result.toString();
	}
	

	
	@Override
	public int doStartTag() throws JspException {
		
		if(maxPage==0){
			return SKIP_BODY; 
		}
		
		JspWriter out = this.pageContext.getOut();
		
		int startNum=1;
		int endNum=maxPage;
		
		if(curPage>maxPage)
			curPage=maxPage;
		
		int pre=1;
		int next=maxPage;
		if(curPage>1){
			pre=curPage-1;
		}
		
		if(curPage<maxPage){
			next=curPage+1;
		}
		//第1页后面的..是否有
		boolean hasBeforeTip=false;
		//最后页前面的..是否有
		boolean hasAfterTip=false;
		
		if(curPage-3>1){
			startNum=curPage-3;
			//endNum=curPage+2;
		}
		
		if(curPage+2<maxPage){
			endNum=curPage+2;
		}
		

		
		if(startNum>1+1)
			hasBeforeTip=true;
		
		if(endNum<maxPage-1)
			hasAfterTip=true;
		
		try{
			
			
			
		
			if(!curPage.equals(1)){
				out.println("<a href=\""+startWith+pre+endWith+"\" class=\"first\">上一页</a>");
			}
			else{
				out.println("<a href=\"javascript:void(0)\" class=\"first\">上一页</a>");
			}
			
			if(startNum!=1){
				
				out.println("<a href=\""+startWith+1+endWith+"\" >1</a>");
			}
			
			
			if(hasBeforeTip){
				out.println("<a href=\"javascript:void(0)\">..</a>");
			}
			
			for(int i=startNum;i<=endNum;i++){
				out.println(makeLink(i));
			}
			
			if(hasAfterTip){
				out.println("<a href=\"javascript:void(0)\">..</a>");
			}
			
			if(endNum!=maxPage){
				out.println("<a href=\""+startWith+maxPage+endWith+"\" >"+maxPage+"</a>");
				
			}
			if(!curPage.equals(maxPage)){
				out.println("<a href=\""+startWith+next+endWith+"\" class=\"last\">下一页</a>");
			}
			else{
				out.println("<a href=\"javascript:void(0)\" class=\"last\">下一页</a>");
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		
		return EVAL_PAGE;
	}

	public String getStartWith() {
		return startWith;
	}

	public void setStartWith(String startWith) {
		this.startWith = startWith;
	}

	public String getEndWith() {
		return endWith;
	}

	public void setEndWith(String endWith) {
		this.endWith = endWith;
	}

	public Integer getCurPage() {
		return curPage;
	}

	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	public Integer getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(Integer maxPage) {
		this.maxPage = maxPage;
	}
	
	
}

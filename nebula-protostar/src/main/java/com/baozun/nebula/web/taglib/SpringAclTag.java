/**
 * 
 */
package com.baozun.nebula.web.taglib;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.baozun.nebula.manager.auth.MetadataSourceManagerImpl;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.taglib.BaseTag;

/**
 * 用于spring security的标签
 * @author Justin Hu
 *
 */
public class SpringAclTag extends BaseTag{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -976494791322948347L;
	
	/**
	 * 修改为acl
	 */
	private String aclElement;
	

	
	
	
	
	
	

	@Override
	public int doStartTag() throws JspException {
		
		
	
	
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		 Object principal = authentication.getPrincipal();

		 UserDetails userDetails = (UserDetails) principal;
		 
		 
		
		 
		 //用户当前权限acl列表
		 Collection<GrantedAuthority> userRole=userDetails.getAuthorities();
		 
		 //当前uri地址需要的role列表
		 Iterator<GrantedAuthority> itUserRole =userRole.iterator();
		 
		
		 
		 while(itUserRole.hasNext()){
			   String auth=itUserRole.next().getAuthority();
			   
			
			   
			   if(auth.equals(userDetails.getCurrentOrganizationId()+"_"+aclElement)){	//如果当前用户拥有acl，则通过
					 return EVAL_BODY_INCLUDE;
			   }
			   
			   
			   
			 
		 }
		 
		
		
		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		
		return EVAL_PAGE;
	}

	public String getAclElement() {
		return aclElement;
	}

	public void setAclElement(String aclElement) {
		this.aclElement = aclElement;
	}






	
	

}

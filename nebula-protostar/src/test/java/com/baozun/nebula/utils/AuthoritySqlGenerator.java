/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.baozun.nebula.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yi.huang
 * @date 2013-6-25 下午04:03:48
 */
public class AuthoritySqlGenerator{

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(AuthoritySqlGenerator.class);

	/**
	 * @param args
	 */
	public static void main(String[] args){
		//首先 sql 添加 一个菜单      t_au_menu    商品关联分类
		String url = "/product/relatedCategory.htm"; //路径	
		Long parent_id = 1L;   //父菜单id  (1L ->商品管理)
		String icon = "relatedCategory";    //菜单加个 图标  
		String label = "商品关联分类";  //菜单名称
		
		
		
		System.out.println("INSERT INTO t_au_menu(ID,lifecycle,sort_no,url,VERSION,parent_id,icon,label)VALUES" +
				"(nextval('S_T_AU_MENU'),1,(SELECT COALESCE(MAX(sort_no),0) FROM t_au_menu WHERE parent_id = "+parent_id+")+1,'"+url+"',now(),1,'"+icon+"','"+label+"');");
		
		
		
		//再sql 初始化 权限 ，  org_tye_id  区分 店铺权限 以及 系统权限    t_au_privilege
		String acl = "ACL_STORE_PRODUCT_RELATED_CATEGORY";  //权限code
		String name = "商品关联分类"; //权限名称
		Long org_type_id = 2L;  //组织类型id  (2L-->店铺级别)
		String desc = "商品关联分类";   //描述
		
		System.out.println("INSERT INTO t_au_privilege(ID,acl,lifecycle,NAME,VERSION,org_type_id,description)VALUES" +
				"(nextval('S_T_AU_PRIVILEGE'),'"+acl+"',1,'"+name+"',now(),"+org_type_id+",'"+desc+"')");
		
		//以及权限url ，注意这个地方的url 和上面 菜单url 相等    t_au_privilege_url
		
		System.out.println("INSERT INTO t_au_privilege_url(ID,url,pri_id)VALUES " +
				"(nextval('S_T_AU_PRIVILEGE_URL'),'"+url+"',(SELECT ID FROM t_au_privilege WHERE acl = '"+acl+"'))");
		
	}
}

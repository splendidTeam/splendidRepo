/**
 * Copyright (c) 2008-2012 FeiLong, Inc. All Rights Reserved.
 * <p>
 * 	This software is the confidential and proprietary information of FeiLong Network Technology, Inc. ("Confidential Information").  <br>
 * 	You shall not disclose such Confidential Information and shall use it 
 *  only in accordance with the terms of the license agreement you entered into with FeiLong.
 * </p>
 * <p>
 * 	FeiLong MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, 
 * 	INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * 	PURPOSE, OR NON-INFRINGEMENT. <br> 
 * 	FeiLong SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * 	THIS SOFTWARE OR ITS DERIVATIVES.
 * </p>
 */
package solr.repository.skuItem;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;






/**
 * 读取 /删除solr 使用的test
 * 
 */
@ContextConfiguration(locations = { "classpath*:spring/spring-solr.xml"})
public class QuerySolrTest extends AbstractJUnit4SpringContextTests{

	private static final Logger	log								= LoggerFactory.getLogger(QuerySolrTest.class);

	protected SolrServer		solrServer;

	@Test
	public void queryAll() throws SolrServerException {    
		ModifiableSolrParams params = new ModifiableSolrParams();    
		// 查询关键词，*:*代表所有属性、所有值，即所有index    
		params.set("q", "id:3");    
		// 分页，start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。    
		params.set("start", 0);    
		params.set("rows", Integer.MAX_VALUE);        
		// 排序，，如果按照id 排序，，那么将score desc 改成 id desc(or asc)    
		params.set("sort", "score desc");     
		// 返回信息 * 为全部 这里是全部加上score，如果不加下面就不能使用score    
		params.set("fl", "*,score");        
		      
			
//			try {
//				solrServer = new CommonsHttpSolrServer("http://localhost:2002/solr-nebula/sku");
//				QueryResponse response = solrServer.query(params);                
//				List<ItemForSolrCommand> list = response.getBeans(ItemForSolrCommand.class);        
//				for (int i = 0; i < list.size(); i++) {            
//					ItemForSolrCommand item = list.get(i) ;  
//					System.out.print(" *** getColor *** "+item.getColor());
//					} 
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}//测试多核选择需要测试的那个
//			   
			
		}
	
	
}

/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
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
 
*
 
*/
package com.baozun.nebula.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.curator.ZkOperator;

/**
 * @author yue.ch
 * @time 2016年5月20日 上午11:08:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class CuratorTest {
	
	private static final String PATH = "/demo";

	@Autowired
	private ZkOperator zkOperator;
	
	/**
	 * 这个方法需要手动测试
	 */
	@Test
	public void testWatcher() {
		try {
			
			// 这里需要手动操作zookeeper服务端节点，查看配置的watcher是否被正确地触发
			Thread.currentThread().sleep(1000 * 60 * 60);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testZkOperator(){
		try {
			zkOperator.asyncCreate("/test001", "textvalue".getBytes(), null);
			zkOperator.delete("/test001");
			zkOperator.asyncCreate("/test001", "textvalue".getBytes(), new BackgroundCallback() {
				
				@Override
				public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
					System.out.println(event.getData());
					System.out.println(new String(zkOperator.getData(event.getPath())));
				}
			});
//			zkOperator.delete("/test001");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCache(){
		final String path = "/cache";
		NodeCache cache = new NodeCache(zkOperator.getZkClient(), path);
		try {
			cache.start();
			
			cache.getListenable().addListener(new NodeCacheListener() {
				
				@Override
				public void nodeChanged() throws Exception {
					System.out.println(path + " node data is changed");
					
				}
			});
			
			
			zkOperator.create(path);
			zkOperator.setData(path, "111".getBytes());
			zkOperator.asyncSetData(path, "222".getBytes(), null);
//			cache.getListenable().addListener(new NodeCacheListener() {
//				
//				@Override
//				public void nodeChanged() throws Exception {
//					System.out.println(path + " node data is changed");
//					
//				}
//			});
			zkOperator.asyncSetData(path, "333".getBytes(), null);
			
			Thread.currentThread().sleep(5000);
			
			System.out.println(new String(cache.getCurrentData().getData()));
			
			zkOperator.delete(path);
			
			Thread.currentThread().sleep(5000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

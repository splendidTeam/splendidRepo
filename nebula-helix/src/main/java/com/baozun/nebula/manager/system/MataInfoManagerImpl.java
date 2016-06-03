/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.JedisSentinelPool;

import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;

/**
 * 
 * 
 * @author chenguang.zhou
 * @date 2014年4月22日 上午9:46:47
 */
@Service("mataInfoManager")
@Transactional
public class MataInfoManagerImpl implements MataInfoManager{

	private Log  log = LogFactory.getLog(getClass());
	
	@Autowired
	private SdkMataInfoManager sdkMataInfoManager;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired(required=false)
	private JedisSentinelPool jedisPool;
	
	@Autowired
	private ZkOperator zkOperator;
	
	@Autowired
	protected SolrServer solrServer;

	
	@Override
	public String findValue(String key) {
		return sdkMataInfoManager.findValue(key);
	}

	@Override
	public String monitorUrl() {
		StringBuilder info = new StringBuilder();
		//检查数据库url
		try {
			jdbcTemplate.getDataSource().getConnection();
		} catch (Exception e) {
			info.append("FAIL-DB,");
			log.error("FAIL-DB", e);
			
		}
		//检查redis url
		try {
			jedisPool.getResource().getClient();
		} catch (Exception e) {
			info.append("FAIL-REDIS,");
			log.error("FAIL-REDIS", e);
			
		}
		//检查zk url
		try {
			zkOperator.checkExists(zkOperator.getLifeCycleNode());
		} catch (Exception e) {
			info.append("FAIL-ZK,");
			log.error("FAIL-ZK", e);
			
		}
		//检查solr url
		try {
			solrServer.commit();
		} catch (Exception e) {
			info.append("FAIL-SOLR,");
			log.error("FAIL-SOLR", e);
			
		}
		if(info.toString().equals("")){
			info.append("SUCCESS,");
		}
		String msg = info.substring(0, info.length()-1);
		return msg;
	}
	
}

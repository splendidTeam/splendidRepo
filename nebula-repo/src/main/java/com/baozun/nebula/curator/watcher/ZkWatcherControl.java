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
package com.baozun.nebula.curator.watcher;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yue.ch
 * @time 2016年6月4日 上午9:12:38
 */
public class ZkWatcherControl {
	
	private static final Logger LOG = LoggerFactory.getLogger(ZkWatcherControl.class);
	
	private List<IWatcher> watchers;
	
	public void initWatchers(){
		if(watchers != null) {
			for(IWatcher watcher : watchers) {
				try {
					watcher.initListen();
				} catch (Exception e) {
					LOG.error("[zookeeper] init watcher error!", e);
				}
			}
		}
	}

	public List<IWatcher> getWatchers() {
		return watchers;
	}

	public void setWatchers(List<IWatcher> watchers) {
		this.watchers = watchers;
	}

}

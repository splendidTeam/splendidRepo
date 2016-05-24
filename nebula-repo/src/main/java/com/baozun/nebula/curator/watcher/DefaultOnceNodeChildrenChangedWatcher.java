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

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;

/**
 * 默认的一次性NodeChildrenChanged监听实现类
 * 
 * <p>
 * NodeChildrenChanged事件不支持多级监听，即仅当指定节点的直接子节点有变化时才会触发Watcher的回调；<br/>
 * 该类的实例需要配置{@code listenerPath}用于指定监听的路径，以及一个IWatcherInvoke实例用于业务逻辑的处理；<br/>
 * 该实现仅会触发一次Watcher回调。
 * </p>
 * 
 * <h3>默认的参数如下：</h3>
 * <ol>
 * <li>EventType = EventType.NodeChildrenChanged</li>
 * <li>CreateMode = CreateMode.PERSISTENT</li>
 * <li>ACL = Ids.OPEN_ACL_UNSAFE</li>
 * </ol>
 * 
 * @author yue.ch
 * @time 2016年5月23日 上午10:16:06
 */
public class DefaultOnceNodeChildrenChangedWatcher extends AbstractWatcher {

	/* (non-Javadoc)
	 * @see com.baozun.nebula.curator.watcher.IWatcher#getEventType()
	 */
	@Override
	public EventType getEventType() {
		return EventType.NodeChildrenChanged;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.curator.watcher.IWatcher#getCreateMode()
	 */
	@Override
	public CreateMode getCreateMode() {
		return CreateMode.PERSISTENT;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.curator.watcher.IWatcher#getAcl()
	 */
	@Override
	public List<ACL> getAcl() {
		return Ids.OPEN_ACL_UNSAFE;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.curator.watcher.IWatcher#needContinueWatch()
	 */
	@Override
	public boolean needContinueWatch() {
		return !isWatched.get();
	}
}

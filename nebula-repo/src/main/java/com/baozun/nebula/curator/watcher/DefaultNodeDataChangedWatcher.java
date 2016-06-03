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
 * 默认的NodeDataChanged监听实现类
 * 
 * <p>
 * 该类的实例需要配置{@code listenerPath}用于指定监听的路径，以及一个IWatcherInvoke实例用于业务逻辑的处理。<br/>
 * 该默认实现总是会不断地循环监听指定路径的NodeDataChanged事件，即{@link IWatcher#needContinueWatch()}方法永远返回true。
 * </p>
 * 
 * <h3>默认的参数如下：</h3>
 * <ol>
 * <li>EventType = EventType.NodeDataChanged</li>
 * <li>CreateMode = CreateMode.PERSISTENT</li>
 * <li>ACL = Ids.OPEN_ACL_UNSAFE</li>
 * </ol>
 * 
 * @author yue.ch
 * @time 2016年5月23日 上午10:16:06
 */
public class DefaultNodeDataChangedWatcher extends AbstractWatcher {

	/* (non-Javadoc)
	 * @see com.baozun.nebula.curator.watcher.IWatcher#getEventType()
	 */
	@Override
	public EventType getEventType() {
		return EventType.NodeDataChanged;
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
		return true;
	}
}

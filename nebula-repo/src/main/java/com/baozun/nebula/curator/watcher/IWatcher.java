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

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.ACL;

import com.baozun.nebula.curator.ZkOperator;

/**
 * 自定义Watcher接口
 * 
 * <p>
 * <ul>
 * <li>Apache Curator封装的监听器有两类：Listener和Watcher；</li>
 * <li>Listener仅在当前Clinet（CuratorFramework）实例中有效，不能跨进程使用，
 * 一般用于监听当前进程中的异步（后台）执行的事件，并进行事件的回调；</li>
 * <li>Watcher可以跨进程使用，一般用于某个应用对节点进行操作之后通知所有对该节点配置了监听的应用进行后续的业务处理的场景。
 * 比如PTS修改了某个节点的数据，然后期望Helix能及时地知道该节点的数据已经变更，并对当前变更做进一步的处理。</li>
 * </ul>
 * </p>
 * 
 * @author yue.ch
 * @time 2016年5月21日 下午12:46:13
 */
public interface IWatcher extends CuratorWatcher {
	
	/**
	 * 初使化节点监听
	 * @throws Exception
	 */
	void initListen() throws Exception;

	/**
	 * 获取监听的节点路径
	 * @return
	 */
	String getListenerPath();

	/**
	 * 获取监听事件
	 * @return
	 */
	EventType getEventType();

	/**
	 * 获取节点创建类型
	 * @return
	 */
	CreateMode getCreateMode();

	/**
	 * 获取节点访问控制策略
	 * @return
	 */
	List<ACL> getAcl();

	/**
	 * 是否需要继续监听
	 * 
	 * <p>
	 * 所有已注册的Watcher仅会被触发一次，在完成事件通知后，服务端随即会删除对应的Watcher，
	 * 后续节点的相同事件不会再次触发Watcher；如果需要持续监听节点的事件，需要保证该方法返回true。
	 * </p>
	 * @return
	 */
	boolean needContinueWatch();
	
	/**
	 * 是否匹配相关的事件
	 * 
	 * <p>
	 * 针对指定节点配置了Watcher，不管该节点发生的事件是什么类型（如节点的创建、删除、数据变更等），
	 * 均会触发该节点上面配置的Watcher，为了确保事情如我们所愿地进行，这里需要判断触发Watcher的事件
	 * 是否是我们所期望的事件，以保证业务逻辑的正确性。
	 * </p>
	 * 
	 * @param type
	 * @return
	 */
	boolean isMatch(EventType type);
	
	/**
	 * 是否已触发Watcher
	 * 
	 * @return
	 */
	boolean isWatched();

	/**
	 * 重新注册Watcher
	 * 
	 * <p>
	 * 所有已注册的Watcher仅会被触发一次，在完成事件通知后，服务端随即会删除对应的Watcher，
	 * 后续节点的相同事件不会再次触发Watcher；如果需要对指定的节点事件持续不断地进行监听，
	 * 要确保在Watcher被触发后重新将该Watcher注册到指定的节点。
	 * </p>
	 * 
	 * @throws Exception
	 */
	void watchAgain() throws Exception;
	
	/**
	 * 获取Zookeeper操作对象
	 * @return
	 */
	ZkOperator getZkOperator();
}

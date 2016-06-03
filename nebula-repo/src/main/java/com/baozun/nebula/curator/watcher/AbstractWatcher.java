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

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.baozun.nebula.curator.ZkOperator;

/**
 * @author yue.ch
 * @time 2016年5月21日 下午12:49:45
 */
public abstract class AbstractWatcher implements IWatcher {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractWatcher.class);

	protected AtomicBoolean isWatched = new AtomicBoolean(false);

	private String listenerPath;

	private IWatcherInvoke watcherInvoke;

	@Autowired
	protected ZkOperator zkOperator;

	/**
	 * 默认的Watcher初使化注册方法
	 * 
	 * <p>
	 * 如果注册Watcher的节点当前不存在，则默认按指定的CreateMode、ACL创建一个空节点；
	 * </p>
	 */
	@PostConstruct
	public void initListen() throws Exception {
		Assert.notNull(getListenerPath(), "listenerPath can not be null!");
		Assert.notNull("watcherInvoke", "watcherInvoke can not be null!");

		String path = getListenerPath();

		// 如果当前节点不存在，创建之
		Stat stat = getZkOperator().checkExists(path);
		if (stat == null) {
			getZkOperator().create(path, getCreateMode(), getAcl());
		}

		// 注册watcher
		usingWatcher(path);

		// 注册session失效的监听器，以便session重置后重新注册watcher
		registerWatcherAfterReconnected();

		// invoke(getListenerPath());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.curator.framework.api.CuratorWatcher#process(org.apache.
	 * zookeeper.WatchedEvent)
	 */
	@Override
	public boolean isMatch(EventType type) {
		return getEventType().equals(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.curator.framework.api.CuratorWatcher#process(org.apache.
	 * zookeeper.WatchedEvent)
	 */
	@Override
	public void watchAgain() throws Exception {
		if (needContinueWatch()) {
			usingWatcher(getListenerPath());
		}
	}

	/**
	 * 默认的Watcher回调方法
	 * 
	 * <p>
	 * 这里有一点需要注意，由于回调是异步执行的，所以会存在节点数据的瞬时状态问题。
	 * 即从服务端节点发生了相关的事件到通知客户端触发Watcher回调执行这期间，服务端节点的数据可能会被更改，
	 * 这个时候回调方法中获取的数据可能已经不是触发本次回调事件瞬间的真实节点数据了。
	 * </p>
	 */
	@Override
	public void process(WatchedEvent event) throws Exception {
		if (isMatch(event.getType())) {
			watcherInvoke.invoke(getListenerPath(), getZkOperator().getData(getListenerPath()));
			isWatched.set(true);
		}

		if (event.getState() == KeeperState.SyncConnected) {
			watchAgain();
		}
	}

	@SuppressWarnings("static-access")
	private void usingWatcher(String path) throws Exception {
		CuratorFrameworkState currentState = getZkOperator().getZkClient().getState();
		while (!CuratorFrameworkState.STARTED.equals(currentState)) {
			Thread.currentThread().sleep(1000);
		}

		LOG.info("[zookeeper] Using watcher: path = '{}', eventType = {}, class = {}", path, getEventType(),
				this.getClass().getName());

		if (EventType.NodeChildrenChanged.equals(getEventType())) {
			getZkOperator().getChildren(path, this);
		} else {
			getZkOperator().checkExists(path, this);
		}

		isWatched.set(false);
	}

	/**
	 * session失效后的Watcher重新注册
	 * 
	 * @param path
	 */
	private void registerWatcherAfterReconnected() {
		getZkOperator().addConnectionStateListener(new ConnectionStateListener() {
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				// 当session恢复时，重新注册watcher
				if (newState == ConnectionState.RECONNECTED) {
					try {
						// // 如果是临时节点，session超时会被server端删除，所以session重置后需要重新创建
						// if (CreateMode.EPHEMERAL.equals(getCreateMode())
						// ||
						// CreateMode.EPHEMERAL_SEQUENTIAL.equals(getCreateMode()))
						// {
						// Stat stat = zkClient.checkExists().forPath(path);
						// if (stat != null) {
						// zkClient.delete().forPath(path);
						// }
						// zkClient.create().creatingParentsIfNeeded().withMode(getCreateMode()).withACL(getAcl())
						// .forPath(path);
						// }

						if (needContinueWatch()) {
							String path = getListenerPath();
							LOG.info(
									"[zookeeper] Prepare to reusing watcher after client reconnected: path={}, eventType={}",
									path, getEventType());

							usingWatcher(path);

							LOG.info("[zookeeper] Reusing watcher successed: path={}, eventType={}", path,
									getEventType());
						}

					} catch (Exception e) {
						LOG.error("[zookeeper] Using watcher error after client reconnected!", e);
					}
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.curator.framework.api.CuratorWatcher#process(org.apache.
	 * zookeeper.WatchedEvent)
	 */
	@Override
	public boolean isWatched() {
		return isWatched.get();
	}

	public IWatcherInvoke getWatcherInvoke() {
		return watcherInvoke;
	}

	public void setWatcherInvoke(IWatcherInvoke watcherInvoke) {
		this.watcherInvoke = watcherInvoke;
	}

	public void setListenerPath(String listenerPath) {
		this.listenerPath = listenerPath;
	}

	public String getListenerPath() {
		String rootpath = zkOperator.getLifeCycleNode();
		LOG.info("listenerpath:root="+rootpath + ",listenerpath="+listenerPath);
		return  rootpath + listenerPath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.curator.watcher.IWatcher#getZkOperator()
	 */
	@Override
	public ZkOperator getZkOperator() {
		return zkOperator;
	}
}

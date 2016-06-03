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
package com.baozun.nebula.curator;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于Apache Curator封装的zookeeper操作类
 * 
 * <p>
 * 该类封装了一些zookeeper操作的常用方法，更多方法请参考{@link org.apache.curator.framework.CuratorFramework}
 * </p>
 * 
 * <p>
 * <h3>关于事务操作的说明</h3>
 * 该类并未实现对事务操作的封装，但可以通过Curator原生的方法进行事务的操作，如以下示例代码将create、setData、delete合并到一个事务中执行：
 * <pre>getZkClient().inTransaction()
 *	.create().forPath("/path1")
 *	.and().setData().forPath("/path2", "pathvalue".getBytes())
 *	.and().delete().forPath("/path")
 *	.and().commit();</pre>
 * </p>
 * 
 * <p>
 * <h3>关于异步操作的说明</h3>
 * <ul>
 * <li>每个异步操作可以指定一个回调函数，针对不同的操作，回调函数可以获取的事件返回数据可能会不同，具体请参考《附录1》。</li>
 * <li>可以通过{@code addCuratorListener(CuratorListener listener)}方法配置一个全局的异步操作回调函数。</li>
 * </ul>
 * </p>
 * 
 * <p>
 * <h3>附录1：</h3>
 * <table border="1" cellspacing="0" cellpadding="4">
 * <tr align="left"><th>事件类型 </th><th>事件返回数据</th></tr>
 * <tr><td>CREATE</td><td>getResultCode() and getPath()</td></tr>
 * <tr><td>DELETE</td><td>getResultCode() and getPath()</td></tr>
 * <tr><td>EXISTS</td><td>getResultCode(), getPath() and getStat()</td></tr>
 * <tr><td>GET_DATA</td><td>getResultCode(), getPath(), getStat() and getData()</td></tr>
 * <tr><td>SET_DATA</td><td>getResultCode(), getPath() and getStat()</td></tr>
 * <tr><td>CHILDREN</td><td>getResultCode(), getPath(), getStat(), getChildren()</td></tr>
 * <tr><td>SYNC</td><td>getResultCode(), getStat()</td></tr>
 * <tr><td>GET_ACL</td><td>getResultCode(), getACLList()</td></tr>
 * <tr><td>SET_ACL</td><td>getResultCode()</td></tr>
 * <tr><td>TRANSACTION</td><td>getResultCode(), getOpResults()</td></tr>
 * <tr><td>WATCHED</td><td>getWatchedEvent()</td></tr>
 * <tr><td>GET_CONFIG</td><td>getResultCode(), getData()</td></tr>
 * <tr><td>RECONFIG</td><td>getResultCode(), getData()</td></tr>
 * </table>
 * </p>
 * 
 * @author yue.ch
 * @time 2016年5月16日 下午2:41:55
 */
public class ZkOperator {

	private static final Logger LOG = LoggerFactory.getLogger(ZkOperator.class);
	
	private Executor executor;

	private volatile CuratorFramework zkClient;
	
	/* 配置参数start */
	/** 连接字符串（eg:192.168.10.1:2181,192.168.10.2:2181） */
	private String connectionString;

	/** 命名空间，用于区分不同应用的存储路径 */
	private String namespace = "nebula_temp";

	/** 会话超时时间 （ms） */
	private int sessionTimeoutMs = 10000;

	/** 连接超时时间 （ms） */
	private int connectionTimeoutMs = 10000;

	/**
	 * 失败重试策略，默认采用{@code RetryForever}
	 * <p>
	 * 可选值：
	 * <ol>
	 * <li>{@code RetryUntilElapsed} 指定最大重连超时时间和重连时间间隔,间歇性重连直到超时或者链接成功</li>
	 * <li>{@code ExponentialBackoffRetry} 基于"backoff"方式重连,和 {@code RetryUntilElapsed}的区别是重连的时间间隔是动态的</li>
	 * <li>{@code RetryOneTime} 在指定的时间后仅重试一次</li>
	 * <li>{@code RetryNTimes} 按指定的重试间隔时间重试指定的次数</li>
	 * <li>{@code RetryForever} 按指定的重试间隔时间永久重试</li>
	 * </ol>
	 * </p>
	 */
	private int retryType = 5;

	/** 最大重试次数 */
	private int maxRetries = 10;

	/** 初使重试间隔时间 */
	private int baseSleepTimeMs = 1000;

	/** 最大重试间隔时间 */
	private int maxSleepTimeMs = 10000;

	/** 最大重试时间（指整个重试过程，而非单次重试间隔） */
	private int maxElapsedTimeMs = 1000 * 60 * 10;

	/** 重试间隔时间 */
	private int sleepMsBetweenRetries = 3000;

	private List<AuthInfo> authInfos;
	
	/** 后台执行线程池大小 */
	private int bgThreadPoolSize = 10;
	/* 配置参数end */

	/**
	 * 获取CuratorFramework实例
	 * <p>
	 * CuratorFramework是线程安全的，应该保证系统中仅存在一个CuratorFramework的实例。<br/>
	 * CuratorFramework实例注册了默认的ConnectionStateListener和默认的CuratorListener。
	 * 这两个默认的Listener并没有实际的操作，仅是在连接状态变更或者事件触发时记录日志所用。<br/>
	 * 如果业务需要注册自定义的Watcher，请使用{@link com.baozun.nebula.curator.watcher.ZkWatcher }并配置{@link com.baozun.nebula.curator.wathcer.ZkWatcherInvoke}的实现类
	 * </p>
	 * @return
	 */
	public CuratorFramework getZkClient() {
		if(zkClient == null) {
			synchronized (this) {
				// 初使化后台操作线程池
				executor = Executors.newFixedThreadPool(bgThreadPoolSize);
				
				Builder builder = CuratorFrameworkFactory.builder().namespace(namespace).connectString(connectionString)
						.retryPolicy(getRetryPolicy()).connectionTimeoutMs(connectionTimeoutMs)
						.sessionTimeoutMs(sessionTimeoutMs)
						.defaultData(String.valueOf(System.currentTimeMillis()).getBytes())
						// 这是个激进的策略，仅当连接状态是LOST时，client才会认为session真正失效了，
						// 默认不配置的情况下，连接状态是SUSPENDED和LOST时，client均会认为session已失效，尝试重新连接server
						// curator 3.0.x以上版本支持，需要依赖zookeeper 3.5.x版本
						//.connectionStateErrorPolicy(new SessionConnectionStateErrorPolicy())
						.threadFactory(Executors.defaultThreadFactory());
				
				// 添加连接授权
				if(authInfos != null) {
					builder.authorization(authInfos);
				}
				
				zkClient = builder.build();
				
				// 添加默认的连接状态监听器，这里只是简单地记录下日志。
				zkClient.getConnectionStateListenable().addListener(new ConnectionStateListener(){
					@Override
			        public void stateChanged(CuratorFramework client, ConnectionState newState)
			        {
						LOG.info("[Zookeeper] Client connection status is changed, current status: " + newState.name());
			        }
				});
				
				// 注册默认的后台操作监听器，这里只是简单地记录下日志。
				zkClient.getCuratorListenable().addListener(new CuratorListener() {
					@Override
					public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
						LOG.info("[Zookeeper] Background or watcher event: path={}, data={}, event={} ", event.getPath(), event.getData() == null ? "" : new String(event.getData()), event.getType().name());
					}
				}, executor);
				
				// 注册默认的后台操作异常监听器，这里只是简单地记录下日志。
				zkClient.getUnhandledErrorListenable().addListener(new UnhandledErrorListener() {
					@Override
					public void unhandledError(String message, Throwable e) {
						LOG.info("[Zookeeper] Background operation error！", e);
					}
				}, executor);
				
				zkClient.start();
			}
		}
		
		return zkClient;
	}
	
	/**
	 * 创建节点（支持级联创建）,节点默认值为当前时间戳
	 * 
	 * <p>
	 * <ul>
	 * <li>默认节点类型：{@code CreateMode.PERSISTENT}</li>
	 * <li>默认ACL：{@code Ids.OPEN_ACL_UNSAFE}</li>
	 * </ul>
	 * </p>
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String create(String path) throws Exception {
		return getZkClient().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).withACL(Ids.OPEN_ACL_UNSAFE).forPath(path);
	}
	
	/**
	 * 创建节点并初使化数据（支持级联创建）
	 * 
	 * <p>
	 * <ul>
	 * <li>默认节点类型：{@code CreateMode.PERSISTENT}</li>
	 * <li>默认ACL：{@code Ids.OPEN_ACL_UNSAFE}</li>
	 * </ul>
	 * </p>
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String create(String path, byte[] data) throws Exception {
		return getZkClient().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).withACL(Ids.OPEN_ACL_UNSAFE).forPath(path, data);
	}

	/**
	 * 创建节点（支持级联创建）,节点默认值为当前时间戳
	 * 
	 * @param path
	 * @param createMode
	 * @param acl
	 * @return
	 * @throws Exception
	 */
	public String create(String path, CreateMode createMode, List<ACL> acl) throws Exception {
		return getZkClient().create().creatingParentsIfNeeded().withMode(createMode).withACL(acl).forPath(path);
	}
	
	/**
	 * 创建节点并初使化数据（支持级联创建）
	 * 
	 * @param path
	 * @param data
	 * @param createMode
	 * @param acl
	 * @return
	 * @throws Exception
	 */
	public String create(String path, byte[] data, CreateMode createMode, List<ACL> acl) throws Exception {
		return getZkClient().create().creatingParentsIfNeeded().withMode(createMode).withACL(acl).forPath(path, data);
	}
	
	/**
	 * 创建一个容器节点（支持级联创建）
	 * 
	 * <p>
	 * 如果zookeeper版本过于老旧不支持container node，则取而代之创建{@code PERSISTENT}类型的znode。
	 * </p>
	 * @param path eg: /a/b/c
	 * @throws Exception
	 */
	public void createContainers(String path) throws Exception {
		getZkClient().createContainers(path);
	}
	
	/**
	 * 获取节点数据
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public byte[] getData(String path) throws Exception {
		return getZkClient().getData().forPath(path);
	}
	
	/**
	 * 获取节点数据的同时注册一个Watcher
	 * 
	 * @param path
	 * @param watcher
	 * @return
	 * @throws Exception
	 */
	public byte[] getData(String path, CuratorWatcher watcher) throws Exception {
		return getZkClient().getData().usingWatcher(watcher).forPath(path);
	}
	
	/**
	 * 检查节点是否存在
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public Stat checkExists(String path) throws Exception {
		return getZkClient().checkExists().forPath(path);
	}
	
	/**
	 * 检查节点是否存在的同时注册一个Watcher
	 * 
	 * @param path
	 * @param watcher
	 * @return
	 * @throws Exception
	 */
	public Stat checkExists(String path, CuratorWatcher watcher) throws Exception {
		return getZkClient().checkExists().usingWatcher(watcher).forPath(path);
	}
	
	/**
	 * 为节点设置数据
	 * 
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Stat setData(String path, byte[] data) throws Exception {
		return getZkClient().setData().forPath(path, data);
	}
	
	/**
	 * 删除节点
	 * 
	 * @param path
	 * @throws Exception
	 */
	public void delete(String path) throws Exception {
		getZkClient().delete().forPath(path);
	}
	
	/**
	 * 获取子节点
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public List<String> getChildren(String path) throws Exception {
		return getZkClient().getChildren().forPath(path);
	}
	
	/**
	 * 获取子节点的同时注册一个Watcher
	 * 
	 * @param path
	 * @param watcher
	 * @return
	 * @throws Exception
	 */
	public List<String> getChildren(String path, CuratorWatcher watcher) throws Exception {
		return getZkClient().getChildren().usingWatcher(watcher).forPath(path);
	}
	
	/**
	 * 添加连接状态监听器
	 * 
	 * @param listener
	 */
	public void addConnectionStateListener(ConnectionStateListener listener) {
		getZkClient().getConnectionStateListenable().addListener(listener);
	}
	
	/**
	 * 添加后台操作监听器
	 * 
	 * @param listener
	 */
	public void addCuratorListener(CuratorListener listener) {
		getZkClient().getCuratorListenable().addListener(listener);
	}
	
	/**
	 * 异步创建节点
	 * 
	 * @param path 节点路径
	 * @param data 节点数据
	 * @param callback 一个可选的回调函数，对于create操作来说，回调函数可获取的事件返回数据包括了getResultCode() and getPath()
	 * @throws Exception
	 */
	public void asyncCreate(String path, byte[] data, BackgroundCallback callback) throws Exception {
		if(callback != null) {
			getZkClient().create().creatingParentsIfNeeded().inBackground(callback, executor).forPath(path, data);
		} else {
			getZkClient().create().creatingParentsIfNeeded().inBackground().forPath(path, data);
		}
	}
	
	/**
	 * 异步设置数据
	 * @param path 节点路径
	 * @param data 节点数据
	 * @param callback 一个可选的回调函数，对于setData操作来说，回调函数可获取的事件返回数据包括了getResultCode(), getPath() and getStat()
	 * @throws Exception
	 */
	public void asyncSetData(String path, byte[] data, BackgroundCallback callback) throws Exception {
		if(callback != null) {
			getZkClient().setData().inBackground(callback, executor).forPath(path, data);
		} else {
			getZkClient().setData().inBackground().forPath(path, data);
		}
	}
	
	/**
	 * 异步获取数据
	 * @param path 节点路径
	 * @param callback 一个可选的回调函数，对于getData操作来说，回调函数可获取的事件返回数据包括了getResultCode(), getPath(), getStat() and getData()
	 * @throws Exception
	 */
	public void asyncGetData(String path, BackgroundCallback callback) throws Exception {
		if(callback != null) {
			getZkClient().getData().inBackground(callback, executor).forPath(path);
		} else {
			getZkClient().getData().inBackground().forPath(path);
		}
	}
	
	/**
	 * 异步删除节点
	 * @param path 节点路径
	 * @param callback 一个可选的回调函数，对于delete操作来说，回调函数可获取的事件返回数据包括了getResultCode() and getPath()
	 * @throws Exception
	 */
	public void asyncDelete(String path, BackgroundCallback callback) throws Exception {
		if(callback != null) {
			getZkClient().delete().inBackground(callback, executor).forPath(path);
		} else {
			getZkClient().delete().inBackground().forPath(path);
		}
	}
	
	/**
	 * 异步检查节点
	 * @param path 节点路径
	 * @param callback 一个可选的回调函数，对于checkExists操作来说，回调函数可获取的事件返回数据包括了getResultCode(), getPath() and getStat()
	 * @throws Exception
	 */
	public void asyncCheckExists(String path, BackgroundCallback callback) throws Exception {
		if(callback != null) {
			getZkClient().checkExists().inBackground(callback, executor).forPath(path);
		} else {
			getZkClient().checkExists().inBackground().forPath(path);
		}
	}
	
	/**
	 * 异步获取子节点
	 * @param path 节点路径
	 * @param callback 一个可选的回调函数，对于getChildren操作来说，回调函数可获取的事件返回数据包括了getResultCode(), getPath(), getStat(), getChildren()
	 * @throws Exception
	 */
	public void asyncGetChildren(String path, BackgroundCallback callback) throws Exception {
		if(callback != null) {
			getZkClient().getChildren().inBackground(callback, executor).forPath(path);
		} else {
			getZkClient().getChildren().inBackground().forPath(path);
		}
	}
	
	/**
	 * 获取失败重试策略
	 * 
	 * @return
	 */
	private RetryPolicy getRetryPolicy() {
		RetryPolicy retryPolicy = null;
		switch (retryType) {
		case 1:
			retryPolicy = new RetryUntilElapsed(maxElapsedTimeMs, sleepMsBetweenRetries);
			break;
		case 2:
			retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries, maxSleepTimeMs);
			break;
		case 3:
			retryPolicy = new RetryOneTime(sleepMsBetweenRetries);
			break;
		case 4:
			retryPolicy = new RetryNTimes(maxRetries, sleepMsBetweenRetries);
			break;
		case 5:
			retryPolicy = new RetryForever(sleepMsBetweenRetries);
			break;
		default:
			retryPolicy = new RetryForever(sleepMsBetweenRetries);
		}

		return retryPolicy;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public int getSessionTimeoutMs() {
		return sessionTimeoutMs;
	}

	public void setSessionTimeoutMs(int sessionTimeoutMs) {
		this.sessionTimeoutMs = sessionTimeoutMs;
	}

	public int getConnectionTimeoutMs() {
		return connectionTimeoutMs;
	}

	public void setConnectionTimeoutMs(int connectionTimeoutMs) {
		this.connectionTimeoutMs = connectionTimeoutMs;
	}

	public int getRetryType() {
		return retryType;
	}

	public void setRetryType(int retryType) {
		this.retryType = retryType;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public int getBaseSleepTimeMs() {
		return baseSleepTimeMs;
	}

	public void setBaseSleepTimeMs(int baseSleepTimeMs) {
		this.baseSleepTimeMs = baseSleepTimeMs;
	}

	public int getMaxSleepTimeMs() {
		return maxSleepTimeMs;
	}

	public void setMaxSleepTimeMs(int maxSleepTimeMs) {
		this.maxSleepTimeMs = maxSleepTimeMs;
	}

	public int getMaxElapsedTimeMs() {
		return maxElapsedTimeMs;
	}

	public void setMaxElapsedTimeMs(int maxElapsedTimeMs) {
		this.maxElapsedTimeMs = maxElapsedTimeMs;
	}

	public int getSleepMsBetweenRetries() {
		return sleepMsBetweenRetries;
	}

	public void setSleepMsBetweenRetries(int sleepMsBetweenRetries) {
		this.sleepMsBetweenRetries = sleepMsBetweenRetries;
	}

	public List<AuthInfo> getAuthInfos() {
		return authInfos;
	}

	public void setAuthInfos(List<AuthInfo> authInfos) {
		this.authInfos = authInfos;
	}

	public int getBgThreadPoolSize() {
		return bgThreadPoolSize;
	}

	public void setBgThreadPoolSize(int bgThreadPoolSize) {
		this.bgThreadPoolSize = bgThreadPoolSize;
	}
}

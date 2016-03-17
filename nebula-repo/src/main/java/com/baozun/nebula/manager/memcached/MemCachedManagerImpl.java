//package com.baozun.nebula.manager.memcached;
//
//import java.util.Collection;
//import java.util.Map;
//import java.util.concurrent.Future;
//
//import loxia.support.cache.NullObject;
//import net.spy.memcached.MemcachedClient;
//import net.spy.memcached.OperationTimeoutException;
//import net.spy.memcached.internal.OperationFuture;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//
///**
// * 可以用spring 管理的 memcached,如果有刘总annotation处理不了的或者不方便的 memcached 可以使用这个.
// * 
// * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
// * @version 1.0 Nov 21, 2012 6:11:29 PM
// */
//@Service("memCachedManager")
//public class MemCachedManagerImpl implements MemCachedManager{
//
//	/** The Constant log. */
//	private static final Logger	log	= LoggerFactory.getLogger(MemCachedManagerImpl.class);
//
//	/** The memcached client. */
//	@Autowired(required = false)
//	// 设置为 required=false 这样不需要的商城 启动不会报错
//	private MemcachedClient		memcachedClient;
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.jumbo.brandstore.manager.memcached.MemCachedManager#incr(java.lang.String, int)
//	 */
//	public long incr(String key,int by) throws OperationTimeoutException,IllegalStateException{
//		return memcachedClient.incr(key, by);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.jumbo.brandstore.manager.memcached.MemCachedManager#decr(java.lang.String, int)
//	 */
//	public long decr(String key,int by) throws OperationTimeoutException,IllegalStateException{
//		return memcachedClient.decr(key, by);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.jumbo.brandstore.manager.memcached.MemCachedManager#set(java.lang.String, java.lang.Object)
//	 */
//	@Override
//	public Future<Boolean> set(String key,Object value) throws IllegalStateException{
//		int expiredTime = TimeInterval.SECONDS_PER_DAY * 29;
//		return set(key, expiredTime, value);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.jumbo.brandstore.manager.memcached.MemCachedManager#set(java.lang.String, int, java.lang.Object)
//	 */
//	@Override
//	public Future<Boolean> set(String key,int expiredTime,Object value) throws IllegalStateException{
//		// 借鉴 CacheAspect
//		if (null == value){
//			NullObject NULL = new NullObject();
//			value = NULL;
//		}
//		return memcachedClient.set(key, expiredTime, value);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.jumbo.brandstore.manager.memcached.MemCachedManager#get(java.lang.String)
//	 */
//	@Override
//	@SuppressWarnings("unchecked")
//	public <T> T get(String key) throws OperationTimeoutException,RuntimeException{
//		Object object = memcachedClient.get(key);
//
//		if (null == object){
//			log.debug("key :{} doesn't exists", key);
//			return null;
//		}
//
//		if (object instanceof NullObject){
//			log.debug("key :{} exists,value is NullObject", key);
//			return null;
//		}
//		return (T) object;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.jumbo.brandstore.manager.memcached.MemCachedManager#delete(java.lang.String)
//	 */
//	@Override
//	public Future<Boolean> delete(String key) throws IllegalStateException{
//		return memcachedClient.delete(key);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.jumbo.brandstore.manager.memcached.MemCachedManager#shutDown()
//	 */
//	@Override
//	public void shutDown(){
//		memcachedClient.shutdown();
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.jumbo.brandstore.manager.memcached.MemCachedManager#getBulk(java.util.Collection)
//	 */
//	public Map<String, Object> getBulk(Collection<String> keys) throws OperationTimeoutException,RuntimeException{
//		return memcachedClient.getBulk(keys);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.jumbo.brandstore.manager.memcached.MemCachedManager#flushAll()
//	 */
//	@Override
//	@Deprecated
//	public void flushAll(){
//		// TODO Auto-generated method stub
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.jumbo.brandstore.manager.memcached.MemCachedManager#flush()
//	 */
//	public OperationFuture<Boolean> flush() throws IllegalStateException{
//		OperationFuture<Boolean> flush = memcachedClient.flush();
//		return flush;
//	}
//}

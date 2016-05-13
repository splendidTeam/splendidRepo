package com.baozun.nebula.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import com.baozun.nebula.command.cache.CacheExpiredCommand;
import com.baozun.nebula.command.cache.CacheItemCommand;
import com.baozun.nebula.dao.system.CacheItemDao;
import com.baozun.nebula.exception.CacheException;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.SerializableUtil;

@Service("dataCacheManager")
public class CacheManagerImpl implements CacheManager {
	
	protected Log				log					= LogFactory.getLog(getClass());

	/**
	 * 默认情况下返回的filedName
	 */
	private static final String DEFAULT_FIELD = "default-field";
	
	private static final String EXCEPTION_ERROR_MSG ="Call Redis Error";

	// @Autowired
	// private ShardedJedisPool jedisPool;

	// 当使用redis的分布式环境时使用以下连接池(sentine做failover)

	@Autowired
	private SdkMataInfoManager sdkMataInfoManager;

	//
	@Autowired(required = false)
	private JedisSentinelPool jedisPool;

	@Autowired
	private CacheItemDao cacheItemDao;

	/**
	 * 配置不同环境不同项目的key前辍
	 */
	private static final String REDIS_KEY_START = "redis.keystart";

	private Jedis getJedis() {
		return jedisPool.getResource();
	}

	/**
	 * 无论是key为null或是配置的key前辍为null,都返回key的值
	 * 
	 * @param key
	 * @return
	 */
	private String processKey(String key) {

		Properties pro = ProfileConfigUtil.findPro("config/redis.properties");

		String confKeyStart = pro.getProperty(REDIS_KEY_START, null);

		if (StringUtils.isBlank(key) || StringUtils.isBlank(confKeyStart))
			return key;

		return confKeyStart + "_" + key;

	}

	/**
	 * 返回true表示会使用缓存
	 * 
	 * @return
	 */
	private boolean useCache() {

		String value = sdkMataInfoManager.findValue(MataInfo.KEY_HAS_CACHE);

		if (value != null && "true".equals(value)) {
			return true;
		}

		return false;
	}

	private void returnResource(Jedis redis) {
		if (redis != null) {
			jedisPool.returnResource(redis);
		}
	}

	public void setValue(String key, String value, Integer expireSeconds) {
		if (!useCache())
			return;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.setex(key, expireSeconds, value);

		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
	}

	public Long addSetValue(String key, String value, Integer expireSeconds) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.expire(key, expireSeconds);
			return jredis.sadd(key, value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
	}

	public Long getSetLength(String key) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.scard(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	public void setValue(String key, String value) {
		if (!useCache())
			return;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.set(key, value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
	}

	public Long remove(String key) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.del(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	public String getValue(String key) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.get(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}

	}

	public Map<String, String> getAllMap(String key) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.hgetAll(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	public void pushToListHead(String key, String[] values) {
		if (!useCache())
			return;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.lpush(key, values);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
	}

	public void pushToListHead(String key, String value) {
		if (!useCache())
			return;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.lpush(key, value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
	}

	public void pushToListFooter(String key, String[] values) {
		if (!useCache())
			return;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.rpush(key, values);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
	}

	public void pushToListFooter(String key, String value) {
		if (!useCache())
			return;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.rpush(key, value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
	}

	public String popListHead(String key) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.lpop(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	public String popListFooter(String key) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.rpop(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	public String findListItem(String key, long index) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.lindex(key, index);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	public List<String> findLists(String key, long start, long end) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.lrange(key, start, end);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}

	}

	public long listLen(String key) {
		if (!useCache())
			return 0;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.llen(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return 0;
		} finally {
			returnResource(jredis);
		}
	}

	public void addSet(String key, String[] values) {
		if (!useCache())
			return;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.sadd(key, values);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
	}

	public String popSet(String key) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.spop(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	public boolean existsInSet(String key, String member) {
		if (!useCache())
			return false;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.sismember(key, member);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return false;
		} finally {
			returnResource(jredis);
		}
	}

	public Long removeFromSet(String key, String[] values) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.srem(key, values);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	public Set<String> findSetAll(String key) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.smembers(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	public long findSetCount(String key) {
		if (!useCache())
			return 0;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.scard(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return 0;
		} finally {
			returnResource(jredis);
		}

	}

	@Override
	public <T> void setObject(String key, T t) {
		if (!useCache())
			return;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			String value = SerializableUtil.convert2String((Serializable) t);
			setValue(key, value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public <T> void setObject(String key, T t, Integer expireSeconds) {
		if (!useCache())
			return;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			String value = SerializableUtil.convert2String((Serializable) t);

			setValue(key, value, expireSeconds);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getObject(String key) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			String value = getValue(key);
			if (StringUtils.isBlank(value)) {
				return null;
			}
			return (T) SerializableUtil.convert2Object(value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	public void addSortSet(String key, String value, long sortNo) {
		if (!useCache())
			return;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.zadd(key, sortNo, value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}

	}

	public Set<String> findSortSets(String key, long start, long end) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.zrange(key, start, end);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	public long findSortSetCount(String key) {
		if (!useCache())
			return 0;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.zcard(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return 0;
		} finally {
			returnResource(jredis);
		}
	}

	public long findSortSetCount(String key, long min, long max) {
		if (!useCache())
			return 0;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.zcount(key, min, max);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return 0;
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public void removeMapValue(String key, String field) {
		if (!useCache())
			return;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.hdel(key, field);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
	}

	public void setMapValue(String key, String field, String value, int seconds) {
		if (!useCache())
			return;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();

			CacheExpiredCommand<String> cec = new CacheExpiredCommand<String>();

			cec.setObject(value);
			cec.setExpiredTime(System.currentTimeMillis() + seconds * 1000l);

			jredis.hset(key, field, SerializableUtil.convert2String((Serializable) cec));
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
	}

	@SuppressWarnings("unchecked")
	public String getMapValue(String key, String field) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			String value = jredis.hget(key, field);

			if (StringUtils.isBlank(value)) {
				return null;
			}
			CacheExpiredCommand<String> cec = (CacheExpiredCommand<String>) SerializableUtil.convert2Object(value);

			if (System.currentTimeMillis() < cec.getExpiredTime())
				return cec.getObject();
			else
				return null;
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public <T> void setMapObject(String key, String field, T t, int seconds) {
		if (!useCache())
			return;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();

			CacheExpiredCommand<T> cec = new CacheExpiredCommand<T>();

			cec.setObject(t);
			cec.setExpiredTime(System.currentTimeMillis() + seconds * 1000l);
			String value = SerializableUtil.convert2String((Serializable) cec);
			jredis.hset(key, field, value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getMapObject(String key, String field) {
		if (!useCache())
			return null;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			String value = jredis.hget(key, field);
			if (StringUtils.isBlank(value)) {
				return null;
			}
			CacheExpiredCommand<T> cec = (CacheExpiredCommand<T>) SerializableUtil.convert2Object(value);
			if (System.currentTimeMillis() < cec.getExpiredTime())
				return (T) cec.getObject();
			else
				return null;
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public String generateMapFieldByDefault(Object... objArray) {
		StringBuffer sb = new StringBuffer();

		for (Object obj : objArray) {
			sb.append(obj.toString() + "-");
		}
		if (sb.length() > 0) {
			sb = sb.delete(sb.length() - 1, sb.length());
		} else {
			sb.append(DEFAULT_FIELD);

		}
		return sb.toString();
	}

	/**
	 * 计数器减少一个数量
	 */
	@Override
	public Long Decr(String key, long value) {
		if (!useCache())
			return -1L;
		Long valueCurr = 0L;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			valueCurr = jredis.decrBy(key, value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
		return valueCurr;
	}

	/**
	 * 计数器增加一个数量
	 */
	@Override
	public Long Incr(String key, long value) {
		if (!useCache())
			return -1L;
		Long valueCurr = 0L;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			valueCurr = jredis.incrBy(key, value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
		return valueCurr;
	}

	@Override
	public Long incr(String key, int expireSeconds) {
		if (!useCache()) {
			return -1L;
		}
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			// reply new value
			Long valueCurr = jredis.incr(key);
			jredis.expire(key, expireSeconds);
			return valueCurr;
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			log.error(EXCEPTION_ERROR_MSG);
			return null;
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public List<CacheItemCommand> findAllCacheItem(RowMapper<CacheItemCommand> rowMapper, Map<String, Object> paraMap) {
		List<CacheItemCommand> list = new ArrayList<CacheItemCommand>();
		// 获取全部的缓存项
		list = cacheItemDao.findAllCacheItem(new BeanPropertyRowMapper<CacheItemCommand>(CacheItemCommand.class),
				paraMap);
		return list;
	}

	public boolean applyRollingTimeWindow(String key, long limit, long window) {
		if (!useCache())
			return true;
		key = processKey(key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			long now = System.currentTimeMillis();
			jredis.zremrangeByScore(key, 0, now - window * 1000);
			jredis.zadd(key, now, String.valueOf(now));
			if (jredis.zcard(key) <= limit) {
				return true;
			}
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jredis);
			//throw new CacheException(e);
		} finally {
			returnResource(jredis);
		}
		return false;
	}
}

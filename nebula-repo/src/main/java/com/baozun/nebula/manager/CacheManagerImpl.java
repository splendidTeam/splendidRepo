package com.baozun.nebula.manager;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.cache.CacheExpiredCommand;
import com.baozun.nebula.exception.CacheException;
import com.baozun.nebula.utilities.common.SerializableUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

@Service("dataCacheManager")
public class CacheManagerImpl extends AbstractCacheManager{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManagerImpl.class);

    @Autowired(required = false)
    private JedisSentinelPool jedisSentinelPool;

    //----------------------------------------------------------------------------------------

    @Override
    public void setValue(String key,final String value,final Integer expireSeconds){
        handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                return jedis.setex(finalKey, expireSeconds, value);
            }
        });
    }

    @Override
    public void setValue(String key,final String value){
        handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                return jedis.set(finalKey, value);
            }
        });
    }

    @Override
    public Long remove(String key){
        return (Long) handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                return jedis.del(finalKey);
            }
        });
    }

    @Override
    public String getValue(String key){
        return (String) handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                return jedis.get(finalKey);
            }
        });
    }

    @Override
    public void pushToListFooter(String key,final String value){
        handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                return jedis.rpush(finalKey, value);
            }
        });
    }

    @Override
    public String popListHead(String key){
        return (String) handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                return jedis.lpop(finalKey);
            }
        });
    }

    @Override
    public Long listLen(String key){
        return (Long) handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                return jedis.llen(finalKey);
            }
        });
    }

    @Override
    public void addSet(String key,final String[] values){
        handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                return jedis.sadd(finalKey, values);
            }
        });
    }

    @Override
    public Long removeFromSet(String key,final String[] values){
        return (Long) handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                return jedis.srem(finalKey, values);
            }
        });
    }

    @Override
    public void removeMapValue(String key,final String field){
        handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                return jedis.hdel(finalKey, field);
            }
        });
    }

    @Override
    public <T> void setMapObject(String key,final String field,final T t,final int seconds){
        handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                CacheExpiredCommand<T> cec = new CacheExpiredCommand<T>();

                cec.setObject(t);
                cec.setExpiredTime(System.currentTimeMillis() + seconds * 1000l);
                String value = SerializableUtil.convert2String((Serializable) cec);
                return jedis.hset(finalKey, field, value);

            }
        });
    }

    @Override
    public <T> T getMapObject(String key,final String field){
        return (T) handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                String value = jedis.hget(finalKey, field);
                if (StringUtils.isBlank(value)){
                    return null;
                }
                CacheExpiredCommand<T> cec = (CacheExpiredCommand<T>) SerializableUtil.convert2Object(value);
                if (System.currentTimeMillis() < cec.getExpiredTime()){
                    return (T) cec.getObject();
                }else{
                    return null;
                }
            }
        });
    }

    /**
     * 计数器减少一个数量
     */
    @Override
    public Long Decr(String key,final long value){
        return (Long) handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                return jedis.decrBy(finalKey, value);
            }
        });
    }

    @Override
    public Long incr(String key,final int expireSeconds){
        return (Long) handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                // reply new value
                Long valueCurr = jedis.incr(finalKey);
                jedis.expire(finalKey, expireSeconds);
                return valueCurr;
            }
        });
    }

    @Override
    public Boolean applyRollingTimeWindow(String key,final long limit,final long window){
        return (Boolean) handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                long now = System.currentTimeMillis();

                jedis.zremrangeByScore(finalKey, 0, now - window * 1000);
                jedis.zadd(finalKey, now, String.valueOf(now));

                return (jedis.zcard(finalKey) <= limit);
            }
        });
    }

    @Override
    public int removeByPrefix(String key){
        final String cleanKey = buildCleanKeyPattern(key);

        return (int) handler(key, new RedisHandler(){

            @Override
            public Object handler(String finalKey,Jedis jedis){
                int delCount = 0;
                Set<String> delKeys = jedis.keys(cleanKey);
                if (isNotNullOrEmpty(delKeys)){
                    for (String delkey : delKeys){
                        jedis.del(delkey);
                        delCount++;
                    }
                }
                return delCount;
            }
        });
    }

    //---------------------------------------------------------------------------------

    public interface RedisHandler{

        Object handler(String finalKey,Jedis jedis);
    }

    //---------------------------------------------------------------------

    private Object handler(String key,RedisHandler redisHandler){
        if (!useCache()){
            return null;
        }

        String userKey = processKey(key);
        return doHandler(userKey, redisHandler);
    }

    private Object doHandler(String userKey,RedisHandler redisHandler){
        Jedis jedis = null;

        try{
            jedis = jedisSentinelPool.getResource();

            return redisHandler.handler(userKey, jedis);

        }catch (Exception e){
            jedisSentinelPool.returnBrokenResource(jedis);
            LOGGER.error("", e);
            throw new CacheException(e);
        }finally{
            if (jedis != null){
                jedisSentinelPool.returnResource(jedis);
            }
        }
    }
}

package com.bsoft.iot.bed.redis;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bsoft.iot.bed.util.LogUtil;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;

@Component
public class RedisClient {
	@Resource
	private JedisPool jedisPool;
	
	public Boolean hset(String key, String field, String value) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.hset(key, field, value == null ? "" : value);
			return true;
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("redis save {} {} {} error:" + e.getMessage(), key, field, value);
			return false;
		} finally {
			jedis.close();
		}
	}

	public Boolean hset(String key, Map<String, String> map) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.hmset(key, map);
			return true;
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("redis save {} {} error:" + e.getMessage(), key, map);
			return false;
		} finally {
			jedis.close();
		}
	}

	public Boolean hdel(String key, String field) {
		Jedis jedis = jedisPool.getResource();

		try {
			jedis.hdel(key, field);
			return true;
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis hdel {} {} error:" + e.getMessage(), key, field);
			return false;
		} finally {
			jedis.close();
		}
	}

	public String hget(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		try {
			String value = jedis.hget(key, field);
			return value;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.log(this.getClass()).error("Redis Query {} {} error:" + e.getMessage(), key, key);
			return null;
		} finally {
			jedis.close();
		}
	}

	public Map<String, String> hgetAll(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			Map<String, String> map = jedis.hgetAll(key);
			return map;
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis Query {} error:" + e.getMessage(), key);
			return null;
		} finally {
			jedis.close();
		}
	}

	public boolean exists(String hashkey) {
		Jedis jedis = jedisPool.getResource();
		Boolean exists = jedis.exists(hashkey);
		jedis.close();
		return exists;
	}

	public boolean del(String hashkey) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.del(hashkey);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.log(this.getClass()).error("Redis del {} error:" + e.getMessage(), hashkey);
			return false;
		} finally {
			jedis.close();
		}
	}

	public boolean zadd(String key, String member, Double score) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.zadd(key, score, member);
			return true;
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis zadd {} {} error:{}", key, member, e.getMessage());
			return false;
		} finally {
			jedis.close();
		}
	}

	public Long zadd(String key, Map<String, Double> scoreMembers) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.zadd(key, scoreMembers);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis zadd {} {} error:{}", key, scoreMembers, e.getMessage());
			return null;
		} finally {
			jedis.close();
		}
	}

	public Set<Tuple> zget(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.zrangeWithScores(key, 0, 100);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis zget {} error:{}", key, e.getMessage());
			return null;
		} finally {
			jedis.close();
		}
	}

	public long zdel(String key, String member) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.zrem(key, member);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis zdel {} {} error:{}", key, member, e.getMessage());
			return 0;
		} finally {
			jedis.close();
		}
	}
	
	public Double zincrby(String key, String member, double score) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.zincrby(key, score, member);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis zaddincr {} error:{}", key, e.getMessage());
			return 0.0;
		} finally {
			jedis.close();
		}
	}
	
	public Set<Tuple> zrevrangeWithScores(String key, long end) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.zrevrangeWithScores(key, 0, end);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis zrevrangeWithScores {} error:{}", key, e.getMessage());
			return null;
		} finally {
			jedis.close();
		}
	}

	public Boolean set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.set(key, value);
			return true;
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis set {} error:{}", key, e.getMessage());
			return false;
		} finally {
			jedis.close();
		}
	}

	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.get(key);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis get {} error:{}", key, e.getMessage());
			return null;
		} finally {
			jedis.close();
		}
	}

	public Long expire(String key, Integer seconds) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.expire(key, seconds);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis get {} error:{}", key, e.getMessage());
			return null;
		} finally {
			jedis.close();
		}
	}
	public Long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.ttl(key);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis get {} error:{}", key, e.getMessage());
			return 0L;
		} finally {
			jedis.close();
		}
	}

	public Long lpush(String key, String... value) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.lpush(key, value);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis lpush {} error:{}", key, e.getMessage());
			return null;
		} finally {
			jedis.close();
		}
	}

	public String rpop(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.rpop(key);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis rpop {} error:{}", key, e.getMessage());
			return null;
		} finally {
			jedis.close();
		}
	}

	public Long llen(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.llen(key);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis llen {} error:{}", key, e.getMessage());
			return 0L;
		} finally {
			jedis.close();
		}
	}

	public List<String> lrang(String key, Long start, Long end) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.lrange(key, start, end);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis lrange {} error:{}", key, e.getMessage());
			return null;
		} finally {
			jedis.close();
		}
	}

	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.incr(key);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis incr {} error:{}", key, e.getMessage());
			return null;
		} finally {
			jedis.close();
		}
	}

	public Long setnx(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.setnx(key, value);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis setnx {} error:{}", key, e.getMessage());
			return 0L;
		} finally {
			jedis.close();
		}
	}

	public String setex(String key,  int seconds, String value) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.setex(key, seconds, value);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis setex {} error:{}", key, e.getMessage());
			return null;
		} finally {
			jedis.close();
		}
	}
	
	public Long sadd(String key,  String... members) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.sadd(key, members);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis sadd {} error:{}", key, e.getMessage());
			return 0L;
		} finally {
			jedis.close();
		}
	}
	
	public Set<String> smembers(String key) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.smembers(key);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis smembers  {} error:{}", key, e.getMessage());
			return null;
		} finally {
			jedis.close();
		}
	}

	public Set<String> keys(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.keys(key);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis keys  {} error:{}", key, e.getMessage());
			return new HashSet<String>();
		} finally {
			jedis.close();
		}
	}

	public Boolean sismember(String key, String member) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.sismember(key, member);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis sismember  {} error:{}", key, e.getMessage());
			return false;
		} finally {
			jedis.close();
		}
	}
	
	public Boolean hexists(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.hexists(key, field);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis hexists {},{} error:{}", key, field, e.getMessage());
			return false;
		} finally {
			jedis.close();
		}
	}

	public Long hincrby(String key, String field, int number) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.hincrBy(key, field, number);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis hincrby {}  error:{}", key, e.getMessage());
			return 0l;
		} finally {
			jedis.close();
		}
	}

	public int hgetallSize(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			int size = jedis.hgetAll(key).size();
			return size;
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis hgetallSize {}  error:{}", key, e.getMessage());
			return 0;
		} finally {
			jedis.close();
		}

	}

	public void hset(String key, String field, Integer number) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.hset(key, field, number.toString());
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis hset  key:{} ,field_Key:{}  error:{}", key, field,
					e.getMessage());
		} finally {
			jedis.close();
		}

	}
	
	public Long srem(String key, String... members) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.srem(key, members);
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis srem  {} error:{}", key, e.getMessage());
			return 0L;
		} finally {
			jedis.close();
		}
	}

	public void hset(String key, String field, Long number) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.hset(key, field, number.toString());
		} catch (Exception e) {
			LogUtil.log(this.getClass()).error("Redis hset  key:{} ,field_Key:{}  error:{}", key, field,
					e.getMessage());
		} finally {
			jedis.close();
		}
	}

}

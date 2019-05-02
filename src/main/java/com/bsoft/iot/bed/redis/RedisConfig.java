package com.bsoft.iot.bed.redis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@Configuration
@PropertySource("classpath:redis.properties")
public class RedisConfig {

	@Autowired
	Environment env;

	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(Integer.valueOf(env.getProperty("maxTotal")));
		poolConfig.setMinIdle(Integer.valueOf(env.getProperty("minIdle")));
		return poolConfig;
	}

	/**
	 * Redis 
	 * 
	 * @param jedisPoolConfig
	 * @return
	 */
	/*
	 * public JedisPool(final GenericObjectPoolConfig poolConfig, final String host,
	 * int port, int timeout, final String password, final int database) {
	 * this(poolConfig, host, port, timeout, password, database, null); }
	 */
	@Bean
	@Profile("pro")
	public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig) {
		return new JedisPool(jedisPoolConfig, env.getProperty("host"), Integer.valueOf(env.getProperty("port")),
				Protocol.DEFAULT_TIMEOUT, env.getProperty("password"), Integer.valueOf(env.getProperty("database")),
				null);
	}

	@Bean
	@Profile("dev")
	public JedisPool jedisPool2(JedisPoolConfig jedisPoolConfig) {
		return new JedisPool(jedisPoolConfig, env.getProperty("host2"), Integer.valueOf(env.getProperty("port2")),
				Protocol.DEFAULT_TIMEOUT, env.getProperty("password2"), Integer.valueOf(env.getProperty("database2")),
				null);
	}

	/**
	 * redis��Ⱥ
	 * 
	 * @param jedisPoolConfig
	 * @return
	 */
	/*
	 * @Bean
	 * 
	 * @Profile("dev")
	 */
	public JedisCluster jedisCluster(JedisPoolConfig jedisPoolConfig) {
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.addAll(Arrays.asList(
				new HostAndPort(env.getProperty("addr1"), Integer.valueOf(env.getProperty("h1"))),
				new HostAndPort(env.getProperty("addr2"), Integer.valueOf(env.getProperty("h2"))),
				new HostAndPort(env.getProperty("addr3"), Integer.valueOf(env.getProperty("h3"))),
				new HostAndPort(env.getProperty("addr4"), Integer.valueOf(env.getProperty("h4"))),
				new HostAndPort(env.getProperty("addr5"), Integer.valueOf(env.getProperty("h5"))),
				new HostAndPort(env.getProperty("addr6"), Integer.valueOf(env.getProperty("h6"))))
				);
		return new JedisCluster(nodes, jedisPoolConfig);
	}

}

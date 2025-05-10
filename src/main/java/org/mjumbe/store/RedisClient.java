package org.mjumbe.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisClient implements AutoCloseable{
	private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);
	private final JedisPool jedisPool;

	public RedisClient(String host, int port, String password) {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		if (password != null && !password.isEmpty()) {
			this.jedisPool = new JedisPool(jedisPoolConfig, host, port, 3000, password);
		} else {
			this.jedisPool = new JedisPool(jedisPoolConfig, host, port);
		}

		try (Jedis jedis = jedisPool.getResource()) {
			jedis.ping();
			logger.info("Successfully connected to Redis at {}:{}", host, port);
		} catch (JedisConnectionException e) {
			logger.error("Failed to connect to RedisClient at {}:{}", host, port, e);
			throw new RuntimeException("Failed to connect to RedisClient", e);
		}
	}

	public String queryCache(String key) {
		logger.info("Retrieving values for Key: {}", key);
		try (Jedis jedis = jedisPool.getResource()) {
			String data = jedis.get(key);
			logger.info("Data saved to RedisClient: {} -> {}", key, data);
			return data;
		} catch (Exception e) {
			logger.error("Failed to query data from Redis: {}", e.getMessage());
			throw new RuntimeException("Failed to save data to RedisClient", e);
		}
	}

	public void cacheData(String key, String value) {
		logger.info("Saving data to Redis with Key: {}", key);
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.set(key, value);
			logger.info("Data saved to Redis: {} ", key);
		} catch (Exception e) {
			logger.error("Failed to save data to Redis", e);
			throw new RuntimeException("Failed to save data to Redis", e);
		}
	}

	@Override
	public void close() {
		if (jedisPool != null) {
			jedisPool.close();
		}
		logger.info("RedisClient connection pool closed");
	}
}

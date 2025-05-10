package org.mjumbe.config;

public class RedisConfig {
	public String redisHost;
	public int redisPort;
	public String redisPassword;
	public String redisKey;

	public RedisConfig() {
		this.redisHost = System.getenv("REDIS_HOST");
		String redisPortStr = System.getenv("REDIS_PORT");
		this.redisPort = (redisPortStr != null && !redisPortStr.isEmpty())
				? Integer.parseInt(redisPortStr) : 6379;
		this.redisPassword = System.getenv("REDIS_PASSWORD");
		this.redisKey = System.getenv("REDIS_KEY");
	}

	@Override
	public String toString() {
		return "RedisClient{" +
				"host='" + redisHost + '\'' +
				", redisPort=" + redisPort +
				", password='" + (redisPassword != null ? "******" : null) + '\'' +
				", key='" + redisKey + '\'' +
				'}';
	}
}

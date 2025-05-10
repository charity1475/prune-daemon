package org.mjumbe;

import org.mjumbe.config.RedisConfig;
import org.mjumbe.store.RedisClient;
import org.mjumbe.transform.ResizeTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		try {
			RedisConfig config = new RedisConfig();
			logger.info("RedisConfig loaded : {}", config);
			ResizeTransformer transformer = new ResizeTransformer();

			try (RedisClient dataCache = new RedisClient(config.redisHost, config.redisPort, config.redisPassword)) {
				String data = dataCache.queryCache(config.redisKey);
				logger.info("Data successfully fetched from : {}", config.redisKey);
				if (data == null || data.isEmpty()) {
					logger.warn("No data found in Redis for key: {} : {}", config.redisKey, data);
					return;
				}

				String prunedData = transformer.prune(data);
				logger.info("Data successfully pruned cache");

				dataCache.cacheData(config.redisKey, prunedData);
				logger.info("Data successfully updated in Redis with key: {}", config.redisKey);
			}
		} catch (Exception e) {
			logger.error("An error occurred during the scheduled job execution: {}", e.getMessage());
			System.exit(1);
		}
		logger.info("Cache prune job completed successfully at {} ...", LocalDateTime.now());
	}
}
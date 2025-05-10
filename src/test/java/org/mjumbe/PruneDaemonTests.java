package org.mjumbe;

import org.junit.jupiter.api.Test;
import org.mjumbe.transform.ResizeTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PruneDaemonTests {
	private static final Logger logger = LoggerFactory.getLogger(PruneDaemonTests.class);

	@Test
	public void testPruneSingleNode() {
		ResizeTransformer transformer = new ResizeTransformer();
		String oneEnded = "[{\"title\": \"Program 1\", \"endTime\": \"2026-10-01T10:00:00\"}, {\"title\": \"Program 2\", \"endTime\": \"2023-10-02T12:00:00\"}]";
		logger.info("oneEnded Input JSON: {}", oneEnded);
		String result = transformer.prune(oneEnded);
		logger.info("oneEnded Transformed Output: {}", result);
		assertTrue(!("[]".equals(result)));
	}

	@Test
	public void testPruneAllEnded() {
		ResizeTransformer transformer = new ResizeTransformer();
		String allEnded = "[{\"title\": \"Program 1\", \"endTime\": \"2024-10-01T10:00:00\"}, {\"title\": \"Program 2\", \"endTime\": \"2023-10-02T12:00:00\"}]";
		logger.info("Input JSON: {}", allEnded);
		String result = transformer.prune(allEnded);
		logger.info("Transformed Output: {}", result);
		assertTrue("[]".equals(result));
	}

}

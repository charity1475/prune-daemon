package org.mjumbe.transform;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResizeTransformer {
	private static final Logger logger = LoggerFactory.getLogger(ResizeTransformer.class);
	private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	public String prune(String jsonInput) {
		JSONArray programs = new JSONArray(jsonInput);
		for (int i = 0; i < programs.length(); i++) {
			JSONObject programObj = programs.getJSONObject(i);
			String endTime = programObj.getString("endTime");
			if (endTime.isEmpty()) {
				logger.error("Program with no end time will be removed: {}", programObj);
				programs.remove(i);
				i--;
				continue;
			}

			LocalDateTime programEndTime = LocalDateTime.parse(endTime, formatter);
			LocalDateTime currentTime = LocalDateTime.now();
			if (programEndTime.isBefore(currentTime)) {
				logger.info("Removing program: {} that has ended: {}", programObj.get("title"), programEndTime);
				programs.remove(i);
				i--;
			} else {
				logger.info("Ongoing program: {} till: {}", programObj.get("title"), programEndTime);
			}
		}

		logger.info("Finished pruning, remaining programs: {}", programs);
		return programs.toString();
	}


}

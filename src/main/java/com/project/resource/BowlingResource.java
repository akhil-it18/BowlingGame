package com.project.resource;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.pojo.Player;
import com.project.service.BowlingService;

@RestController
public class BowlingResource {

	@Autowired
	private BowlingService bowlingService;
	
	@PostMapping(value = "/bowling/result", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String, Object> bowlingResult(@RequestBody Map<String, List<Player>> playersListMap) {
		List<Player> playersList = playersListMap.get("players");
		Map<String, Object> frameScoreMap = bowlingService.countScore(playersList);
		return frameScoreMap;
	}
}

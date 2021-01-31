package com.project.service;

import java.util.List;
import java.util.Map;

import com.project.pojo.Player;

public interface BowlingService {

	Map<String, Object> countScore(List<Player> playerList);

}

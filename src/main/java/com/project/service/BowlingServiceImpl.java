package com.project.service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.pojo.Frame;
import com.project.pojo.Player;
import com.project.pojo.Score;

@Service("bowlingService")
public class BowlingServiceImpl implements BowlingService {
	
	public static final int NUM_FRAMES = 4;
	public static final int MAX_POSSIBLE_SCORE = 10;

	@Override
	public Map<String, Object> countScore(List<Player> playerList) {
		Map<String, Object> frameScoreMap = new HashMap<>();
		int maxScore = -1;
		String winner = null;
		Gson gson = new Gson();
		
		// Count score for each player after each frame
		for(int i=0; i < NUM_FRAMES; i++) {
			Map<String, Score> playerScoreMap = new HashMap<String, Score>();
			for(Player player : playerList) {
				// mark it false if we use only one ball in the frame
				boolean twoBallsUsed = true;
				Frame f = player.getFrameList().get(i);
				int score = f.getBall1();
				int score2 = f.getBall2();
				
				// player's score at the end of the current frame
				int pfScore = 0;
				Score pScore = new Score();
				if(score == MAX_POSSIBLE_SCORE) {
					//strike
					pfScore = MAX_POSSIBLE_SCORE;
					pScore.setBonus("strike");
					twoBallsUsed = false;
				}
				else if(score2 == MAX_POSSIBLE_SCORE - score) {
					//spare
					pfScore = 10;
					pScore.setBonus("spare");
				}		
				else {
					pfScore = score + score2;
					pScore.setBonus("none");
				}
				/* if we had a strike/spare in the previous frame, we would count the score of this frame
				in the previous */
				
				/* pfScore represents score of the player by the end of this frame */
				if(i >= 1) {
					Type type = new TypeToken<Map<String, Score>>() {}.getType();    
					String scorePrevFrameJson = gson.toJson(frameScoreMap.get("Frame " + (i-1)), type);
					Map<String, Score> psMap = gson.fromJson(scorePrevFrameJson, type);
					Score scorePrevFrame = psMap.get(player.getName());
					String prevBonus = scorePrevFrame.getBonus();
					
					/* If previous frame saw a strike, score on the two balls of this frame
					 *  would also be added to the previous frame.
					 * pfScore is the sum of the score on both the balls.
					 */
					if(prevBonus.equals("strike")) {
						pfScore = (2 * pfScore) + scorePrevFrame.getScore(); 
					}
					
					/* If previous frame saw a spare, score on the first ball 
					 * (hence excluding score2 here) of this frame would also be 
					 * added to the previous frame. */
					
					else if(prevBonus.equals("spare")) {
						pfScore = pfScore + score + scorePrevFrame.getScore(); 
					}
					else
						pfScore = pfScore + scorePrevFrame.getScore();
				}
				
				// the frame before previous
				if(!twoBallsUsed && i >= 2) {
					Type type = new TypeToken<Map<String, Score>>() {}.getType();    
					String scorePenPrevFrameJson = gson.toJson(frameScoreMap.get("Frame " + (i-2)), type);
					Map<String, Score> psMap = gson.fromJson(scorePenPrevFrameJson, type);
					Score scorePenPrevFrame = psMap.get(player.getName());
					String penPrevBonus = scorePenPrevFrame.getBonus();
					if(penPrevBonus.equals("strike")) {
						pfScore += score + score2;
					}
				}
				
				pScore.setScore(pfScore);
				if(i == NUM_FRAMES-1) {
					if(pfScore > maxScore) {
						maxScore = pfScore;
						winner = player.getName();
					}
				}
				playerScoreMap.put(player.getName(), pScore);
			}
			
				
			frameScoreMap.put("Frame " + i, playerScoreMap);
			frameScoreMap.put("winner", winner);
		}
		
		return frameScoreMap;
	}
}

package com.project.pojo;

import java.util.List;

public class Player {

	private String name;
	private List<Frame> frameList;
	private Score Score;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Frame> getFrameList() {
		return frameList;
	}
	public void setFrameList(List<Frame> frameList) {
		this.frameList = frameList;
	}
	public Score getScore() {
		return Score;
	}
	public void setScore(Score score) {
		Score = score;
	}
}

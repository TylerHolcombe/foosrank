package com.foosrank.rest;

import java.util.List;

public class GameDto {
	private List<Long> teamA;
	private List<Long> teamB;

	Boolean didAWin;

	public List<Long> getTeamA() {
		return teamA;
	}

	public void setTeamA(List<Long> teamA) {
		this.teamA = teamA;
	}

	public List<Long> getTeamB() {
		return teamB;
	}

	public void setTeamB(List<Long> teamB) {
		this.teamB = teamB;
	}

	public Boolean getDidAWin() {
		return didAWin;
	}

	public void setDidAWin(Boolean didAWin) {
		this.didAWin = didAWin;
	}
}

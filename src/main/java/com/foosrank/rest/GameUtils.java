package com.foosrank.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.foosrank.data.player.Player;

public class GameUtils {
	private static GameUtils instance;

	private static final double K = 50;
	private static final int PLAYERS_PER_GAME = 4;

	private GameUtils() {
	}

	public static GameUtils getInstance() {
		if (instance == null) {
			synchronized (GameUtils.class) {
				if (instance == null) {
					instance = new GameUtils();
				}
			}
		}
		return instance;
	}

	public void updatePlayers(List<Player> winners, List<Player> losers) {
		// Update elo for each of the players, then persist them to the database
		double totalWinElo = winners.stream().mapToDouble(player -> player.getElo()).sum();
		double winEloAverage = totalWinElo / winners.size();
		double totalLoseElo = losers.stream().mapToDouble(player -> player.getElo()).sum();
		double loseEloAverage = totalLoseElo / losers.size();

		for (Player winner : winners) {
			winner.setElo(winner.getElo() + calculateEloDiff(winner.getElo(), loseEloAverage, true));
			winner.setWins(winner.getWins() + 1);
		}
		for (Player loser : losers) {
			loser.setElo(loser.getElo() + calculateEloDiff(loser.getElo(), winEloAverage, false));
			loser.setLosses(loser.getLosses() + 1);
		}
	}

	public void validateGame(GameDto game) {
		Set<Long> players = new HashSet<>();
		if (game.getDidAWin() == null) {
			throw new RuntimeException("No winning team selected");
		}
		for (Long id : game.getTeamA()) {
			if (!players.add(id)) {
				throw new RuntimeException("Duplicate players selected for a game");
			}
		}
		for (Long id : game.getTeamB()) {
			if (!players.add(id)) {
				throw new RuntimeException("Duplicate players selected for a game");
			}
		}
		if (players.size() != PLAYERS_PER_GAME) {
			throw new RuntimeException("Not enough players selected");
		}
	}

	private double calculateEloDiff(double playerElo, double opponentElo, boolean didPlayerWin) {
		double rPlayer = Math.pow(10, (playerElo / 400));
		double rOpponent = Math.pow(10, (opponentElo / 400));

		double ePlayer = rPlayer / (rPlayer + rOpponent);

		double c = 0.0;
		if (didPlayerWin)
			c = 1.0;
		double playerEloDiff = K * (c - ePlayer);

		return playerEloDiff;
	}
}

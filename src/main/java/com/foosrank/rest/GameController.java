package com.foosrank.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.foosrank.data.player.Player;
import com.foosrank.data.player.PlayerRepository;

@RestController
@RequestMapping("/api/games")
public class GameController {
	@Autowired
	PlayerRepository playerRepository;

	GameUtils gameUtils = GameUtils.getInstance();

	@RequestMapping(method = RequestMethod.PUT)
	public void resolveGame(@RequestBody GameDto gameDto) {
		gameUtils.validateGame(gameDto);

		List<Player> winners = gameDto.didAWin ? (List<Player>) playerRepository.findAll(gameDto.getTeamA())
				: (List<Player>) playerRepository.findAll(gameDto.getTeamB());
		List<Player> losers = !gameDto.didAWin ? (List<Player>) playerRepository.findAll(gameDto.getTeamA())
				: (List<Player>) playerRepository.findAll(gameDto.getTeamB());

		gameUtils.updatePlayers(winners, losers);

		winners.addAll(losers);
		playerRepository.save(winners);
	}
}

package com.foosrank.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.foosrank.data.player.Player;
import com.foosrank.data.player.PlayerRepository;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
	@Autowired
	private PlayerRepository playerRepository;

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Player> getPlayers() {
		return playerRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{username}")
	public Player getPlayerByUsername(@PathVariable String username) {
		return playerRepository.findByUsername(username);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void addPlayer(@RequestBody String username) {
		playerRepository.save(new Player(username));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/multiple")
	public void addPlayers(@RequestBody List<String> usernames) {
		List<Player> players = new ArrayList<>();
		usernames.parallelStream().forEach(username -> players.add(new Player(username)));
		playerRepository.save(players);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public void updatePlayer(@RequestBody Player player) {
		if (playerRepository.exists(player.getId())) {
			Player savedPlayer = playerRepository.findOne(player.getId());
			savedPlayer.setUsername(player.getUsername());
			playerRepository.save(savedPlayer);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deletePlayer(@PathVariable Long id) {
		playerRepository.delete(id);
	}

}

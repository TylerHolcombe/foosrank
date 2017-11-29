package com.foosrank.data.player;

import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {
	public Player findByUsername(String username);
}

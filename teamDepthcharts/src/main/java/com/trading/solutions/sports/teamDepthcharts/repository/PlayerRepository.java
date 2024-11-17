package com.trading.solutions.sports.teamDepthcharts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.solutions.sports.teamDepthcharts.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long>{
	
	public Player findByNumber(Long number);

}

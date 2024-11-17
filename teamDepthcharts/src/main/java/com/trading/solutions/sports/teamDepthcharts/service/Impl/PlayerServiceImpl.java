package com.trading.solutions.sports.teamDepthcharts.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.solutions.sports.teamDepthcharts.entity.Player;
import com.trading.solutions.sports.teamDepthcharts.repository.PlayerRepository;
import com.trading.solutions.sports.teamDepthcharts.service.PlayerService;

@Service
public class PlayerServiceImpl implements PlayerService {
	
    private PlayerRepository playerRepository;
	
	@Autowired
	public PlayerServiceImpl(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}
	
    public Player getPlayerByNumber(Long number){
    	return playerRepository.findByNumber(number);
    }

}

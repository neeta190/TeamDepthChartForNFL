package com.trading.solutions.sports.teamDepthcharts.service;

import java.util.List;
import java.util.Map;

import com.trading.solutions.sports.teamDepthcharts.entity.PlayerDepthChart;
import com.trading.solutions.sports.teamDepthcharts.pojo.PlayerDepthChartVO;
import com.trading.solutions.sports.teamDepthcharts.pojo.PlayerVO;

public interface TeamDepthChartService {
	
	public Map<String, List<PlayerVO>> getAllTeamDepthChart(Map<String, List<PlayerVO>> depthChart);
	
	public List<PlayerDepthChart> addPlayerToDepthChart(PlayerDepthChartVO playerDepthChartVO);
	
	public List<PlayerVO> getBackupsForPlayers(List<PlayerVO> playerList, PlayerDepthChartVO playerDepthChartVO);
	
	public List<PlayerVO> removePlayerFromDepthChart(PlayerDepthChartVO playerDepthChartVO);
	
}
package com.trading.solutions.sports.teamDepthcharts.service.Impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.trading.solutions.sports.teamDepthcharts.entity.Player;
import com.trading.solutions.sports.teamDepthcharts.entity.PlayerDepthChart;
import com.trading.solutions.sports.teamDepthcharts.entity.Position;
import com.trading.solutions.sports.teamDepthcharts.pojo.PlayerDepthChartVO;
import com.trading.solutions.sports.teamDepthcharts.pojo.PlayerVO;
import com.trading.solutions.sports.teamDepthcharts.repository.TeamDepthChartRepository;
import com.trading.solutions.sports.teamDepthcharts.service.PlayerService;
import com.trading.solutions.sports.teamDepthcharts.service.PositionService;
import com.trading.solutions.sports.teamDepthcharts.service.TeamDepthChartService;

@Service
public class TeamDepthChartServiceImpl implements TeamDepthChartService {

	private TeamDepthChartRepository teamDepthChartRepository;

	private PositionService positionService;

	private PlayerService playerService;

	@Autowired
	public TeamDepthChartServiceImpl(TeamDepthChartRepository teamDepthChartRepository, PositionService positionService,
			PlayerService playerService) {
		this.teamDepthChartRepository = teamDepthChartRepository;
		this.positionService = positionService;
		this.playerService = playerService;

	}

	public Map<String, List<PlayerVO>> getAllTeamDepthChart(Map<String, List<PlayerVO>> depthChart) {

		List<PlayerDepthChart> teamDepthChartList = teamDepthChartRepository
				.findAll(Sort.by(Sort.Direction.ASC, "depth"));
		// Iterate over each PlayerDepthChart and map to the depth chart by position
		teamDepthChartList.forEach(playerDepthChart -> {
			// Copy data from PlayerDepthChart to PlayerDepthChartVO
			PlayerDepthChartVO playerDepthChartVO = new PlayerDepthChartVO();
			copyEntityToVO(playerDepthChartVO, playerDepthChart);

			// Add player to the corresponding position in the player depth chart map
			depthChart.computeIfAbsent(playerDepthChartVO.getPosition(), k -> new ArrayList<>())
					.add(playerDepthChartVO.getPlayer());
		});

		// Get all positions from the repository
		List<Position> allPositions = positionService.getAllPositions();

		// Ensure that all positions have an entry in the depthChart
		allPositions.forEach(position -> depthChart.putIfAbsent(position.getPosition(), Collections.emptyList()));

		return depthChart;
	}

	public List<PlayerDepthChart> addPlayerToDepthChart(PlayerDepthChartVO playerDepthChartVO) {

		PlayerDepthChart playerDepthChart = new PlayerDepthChart();
		String position = playerDepthChartVO.getPosition();
		Long number = playerDepthChartVO.getPlayer().getNumber();
		String name = playerDepthChartVO.getPlayer().getName();

		// Fetch position and player data
		Position playerPosition = positionService.findByPosition(position);
		Player retrievedPlayer = playerService.getPlayerByNumber(number);

		// Set position and player with a requested player if retrieved player is null
		playerDepthChart.setPosition(playerPosition);
		playerDepthChart.setPlayer(Optional.ofNullable(retrievedPlayer).orElse(new Player(number, name)));

		// Set the depth directly from the VO
		playerDepthChart.setDepth(playerDepthChartVO.getDepth());

		// Fetch existing players ordered by depth for a particular position
		List<PlayerDepthChart> playerListAsPerPosition = teamDepthChartRepository
				.findByPositionOrderByDepth(playerDepthChart.getPosition());

		// Handle the depth assignment and insertion
		Long depth = playerDepthChart.getDepth();
		if (depth == null) {
			// Set depth to the size of the list and add the requested player at the end
			playerDepthChart.setDepth((long) playerListAsPerPosition.size());
			playerListAsPerPosition.add(playerDepthChart);
		} else {
			// Add the player at the specified depth and update subsequent players' depths
			playerListAsPerPosition.add(depth.intValue(), playerDepthChart);

			// Update depths for players after insertion of requested player
			for (int i = depth.intValue() + 1; i < playerListAsPerPosition.size(); i++) {
				playerListAsPerPosition.get(i).setDepth((long) i);
			}
		}

		// Save the updated list
		return teamDepthChartRepository.saveAllAndFlush(playerListAsPerPosition);
	}

	public List<PlayerVO> getBackupsForPlayers(List<PlayerVO> playerList, PlayerDepthChartVO playerDepthChartVO) {

		// Get the player details
		String positionValue = playerDepthChartVO.getPosition();
		Long number = playerDepthChartVO.getPlayer().getNumber();
		Position position = positionService.findByPosition(positionValue);

		List<PlayerDepthChart> playerListAsPerPosition = teamDepthChartRepository.findByPositionOrderByDepth(position);

		// Find the player's index
		Optional<PlayerDepthChart> playerDepthChartOpt = playerListAsPerPosition.stream()
				.filter(playerDepthChart -> number.equals(playerDepthChart.getPlayer().getNumber())
						&& playerDepthChartVO.getPlayer().getName().equals(playerDepthChart.getPlayer().getName()))
				.findFirst();

		if (playerDepthChartOpt.isPresent()) {
			int startIndex = playerListAsPerPosition.indexOf(playerDepthChartOpt.get()) + 1;
			for (int i = startIndex; i < playerListAsPerPosition.size(); i++) {
				PlayerVO playerVO = new PlayerVO();
				Player player = playerListAsPerPosition.get(i).getPlayer();
				playerVO.setName(player.getName());
				playerVO.setNumber(player.getNumber());
				// Add other properties from Player to PlayerVO as needed
				playerList.add(playerVO);
			}
		}
		return playerList;
	}

	public List<PlayerVO> removePlayerFromDepthChart(PlayerDepthChartVO playerDepthChartVO) {
		List<PlayerVO> playerDeleted = new ArrayList<>();
		// Get the player details
		String positionValue = playerDepthChartVO.getPosition();
		Long number = playerDepthChartVO.getPlayer().getNumber();
		String name = playerDepthChartVO.getPlayer().getName();

		// Retrieve the position and player list
		Position position = positionService.findByPosition(positionValue);
		List<PlayerDepthChart> playerListAsPerPosition = teamDepthChartRepository.findByPositionOrderByDepth(position);

		// Find the player matching the number and name
		Optional<PlayerDepthChart> optionalPlayerDepthChart = playerListAsPerPosition.stream()
				.filter(playerDepthChartData -> playerDepthChartData.getPlayer().getNumber() == number
						&& playerDepthChartData.getPlayer().getName().equals(name))
				.findFirst();

		// Process the player if found
		optionalPlayerDepthChart.ifPresent(playerDepthChart -> {
			int depth = playerDepthChart.getDepth().intValue();

			// Remove the player at the specified depth and update the list of player depth
			// chart
			teamDepthChartRepository.delete(playerDepthChart);
			playerListAsPerPosition.remove(depth);

			// Update the depth for the remaining players in the list
			for (int i = depth; i < playerListAsPerPosition.size(); i++) {
				// Set the depth in the player depth chart using the index
				playerListAsPerPosition.get(i).setDepth((long) i);
			}

			// Save the updated list to the repository
			teamDepthChartRepository.saveAllAndFlush(playerListAsPerPosition);

			// Add the removed player to the deleted list
			PlayerVO playerVO = new PlayerVO();
			BeanUtils.copyProperties(playerDepthChart.getPlayer(), playerVO);
			playerDeleted.add(playerVO);
		});
		return playerDeleted;
	}

	private void copyEntityToVO(PlayerDepthChartVO playerDepthChartVO, PlayerDepthChart playerDepthChart) {
		BeanUtils.copyProperties(playerDepthChart, playerDepthChartVO);
		PlayerVO playerVO = new PlayerVO();
		BeanUtils.copyProperties(playerDepthChart.getPlayer(), playerVO);
		playerDepthChartVO.setPlayer(playerVO);
		playerDepthChartVO.setPosition(playerDepthChart.getPosition().getPosition());
	}
}

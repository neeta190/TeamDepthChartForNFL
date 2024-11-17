package com.trading.solutions.sports.teamDepthcharts.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.solutions.sports.teamDepthcharts.pojo.PlayerDepthChartVO;
import com.trading.solutions.sports.teamDepthcharts.pojo.PlayerVO;
import com.trading.solutions.sports.teamDepthcharts.service.TeamDepthChartService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;


@RestController
@RequestMapping("depth/chart")
public class TeamDepthChartController {

	private TeamDepthChartService teamDepthChartService;

	@Autowired
	public TeamDepthChartController(TeamDepthChartService teamDepthChartService) {
		this.teamDepthChartService = teamDepthChartService;
	}

	@GetMapping(value = "/getDepthChart")
	public ResponseEntity<Map<String, List<PlayerVO>>> getFullDepthChart() {

		Map<String, List<PlayerVO>> depthChart = new HashMap<String, List<PlayerVO>>();
		// Call service to return the full team depth chart
		depthChart = teamDepthChartService.getAllTeamDepthChart(depthChart);
        // Return with 200 OK 
		return ResponseEntity.ok(depthChart);
	}

	@PostMapping(value = "/addPlayerToDepthChart")
	public ResponseEntity<Void> addPlayerToDepthChart(@RequestBody PlayerDepthChartVO playerDepthChartVO) {
        // Call the service to add a player to the depth chart
		teamDepthChartService.addPlayerToDepthChart(playerDepthChartVO);
		//Create a URI  of full depth chart 
		URI location = createUriForFullDepthList();
		// Return with 201 Created and the location to get the full team depth chart
		return ResponseEntity.created(location).build();
	}

	@GetMapping(value = "/getBackups")
	public ResponseEntity<String> getBackups(@RequestParam  @NotBlank(message="Position is required") String playerPosition, 
			@RequestParam @NotBlank(message="player is required") String player) {
		
	    // Initialize with request parameters received in input
		List<PlayerVO> playerList = new ArrayList<>();
		PlayerDepthChartVO playerDepthChartVO = new PlayerDepthChartVO();
		playerDepthChartVO.setPosition(playerPosition);
		PlayerVO playerVO = parsePlayerInfoFromJsonString(player);
		playerDepthChartVO.setPlayer(playerVO);
		// Call the service to get the backup of the requested player from the depth chart
		playerList = teamDepthChartService.getBackupsForPlayers(playerList, playerDepthChartVO);
		//Create a URI  of full depth chart 
		String location = createUriForFullDepthList().toString();
		// Return with 200 OK and the location to get the full team depth chart
		return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.LOCATION, location)
				.body("" + playerList);
	}

	@DeleteMapping(value = "/removePlayerFromDepthChart")
	public ResponseEntity<String> removePlayerFromDepthChart(@RequestParam  String playerPosition, 
			@RequestParam String player) {
		// Initialize with request parameters received in input
		PlayerDepthChartVO playerDepthChartVO = new PlayerDepthChartVO();
		playerDepthChartVO.setPosition(playerPosition);
		PlayerVO playerVO = parsePlayerInfoFromJsonString(player);
		playerDepthChartVO.setPlayer(playerVO);
		
		// Call the service to delete the backup of requested player from the depth chart
		List<PlayerVO> playerDeleted = teamDepthChartService.removePlayerFromDepthChart(playerDepthChartVO);

		//Create a URI  of full depth chart 
		String location = createUriForFullDepthList().toString();
		// Return with 200 OK and the location to get the full team depth chart
		return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.LOCATION, location)
				.body(playerDeleted.toString());
	}

	private URI createUriForFullDepthList() {
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/depth/chart/getDepthChart")
				.buildAndExpand().toUri();
		return location;
	}

	private PlayerVO parsePlayerInfoFromJsonString(String playerData) {
		// Parse the JSON string into a PlayerVO object
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(playerData, PlayerVO.class);
		} catch (Exception e) {
			throw new HttpMessageNotReadableException("Invalid JSON input");
		}
	}

}

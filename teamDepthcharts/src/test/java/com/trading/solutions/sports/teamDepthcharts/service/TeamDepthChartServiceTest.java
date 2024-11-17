package com.trading.solutions.sports.teamDepthcharts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.trading.solutions.sports.teamDepthcharts.entity.Player;
import com.trading.solutions.sports.teamDepthcharts.entity.PlayerDepthChart;
import com.trading.solutions.sports.teamDepthcharts.entity.Position;
import com.trading.solutions.sports.teamDepthcharts.pojo.PlayerDepthChartVO;
import com.trading.solutions.sports.teamDepthcharts.pojo.PlayerVO;
import com.trading.solutions.sports.teamDepthcharts.repository.PlayerRepository;
import com.trading.solutions.sports.teamDepthcharts.repository.PositionRepository;
import com.trading.solutions.sports.teamDepthcharts.repository.TeamDepthChartRepository;
import com.trading.solutions.sports.teamDepthcharts.service.Impl.PlayerServiceImpl;
import com.trading.solutions.sports.teamDepthcharts.service.Impl.PositionServiceImpl;
import com.trading.solutions.sports.teamDepthcharts.service.Impl.TeamDepthChartServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TeamDepthChartServiceTest {

	// Mock the repository
	@Mock
	private TeamDepthChartRepository teamDepthChartRepository;

	@Mock
	private PositionRepository positionRepository;

	@Mock
	private PlayerRepository playerRepository;

	// Inject mock into the service
	@InjectMocks
	private PositionServiceImpl positionServiceImpl = new PositionServiceImpl(positionRepository);

	@InjectMocks
	private PlayerServiceImpl playerServiceImpl = new PlayerServiceImpl(playerRepository);

	@InjectMocks
	private TeamDepthChartServiceImpl teamDepthChartServiceImpl = new TeamDepthChartServiceImpl(
			teamDepthChartRepository, positionServiceImpl, playerServiceImpl);

	// Sample test data
	private List<PlayerDepthChart> playerDepthChart;

	private List<Position> positionList;

	private List<PlayerVO> playerVoList;
	
	@BeforeEach
	public void setUp() {
		// Initialize test data
		playerDepthChart = Arrays.asList(
				new PlayerDepthChart(new Player(12l, "Tom Brady"), new Position("QB", "Quarterback"), 0l),
				new PlayerDepthChart(new Player(2l, "Kyle Trask"), new Position("QB", "Quarterback"), 1l),
				new PlayerDepthChart(new Player(11l, "Blaine Gabbert"), new Position("QB", "Quarterback"), 2l),
				new PlayerDepthChart(new Player(15l, "Mike Gabbert"), new Position("TE", "Tight end"), 0l));

		positionList = Arrays.asList(
				new Position("QB", "Quarterback"), 
				new Position("TE", "Tight end"),
				new Position("RB", "Running Back"));

		playerVoList = Arrays.asList(
				new PlayerVO(43l, "Josh Neil"),
				new PlayerVO(12l, "Tom Brady"),
				new PlayerVO(2l, "Kyle Trask"), 
				new PlayerVO(11l, "Blaine Gabbert"), 
				new PlayerVO(19l, "Joshua Blard"));
		}

	@Test
	public void testGetAllTeamDepthChart_Basic() {
		// Mock the behavior of the TeamDepthChartRepository and PositionService
		when(teamDepthChartRepository.findAll(Sort.by(Sort.Direction.ASC, "depth"))).thenReturn(playerDepthChart);

		when(positionServiceImpl.getAllPositions()).thenReturn(positionList);

		// Call the service method
		Map<String, List<PlayerVO>> depthChart = new HashMap<String, List<PlayerVO>>();
		depthChart = teamDepthChartServiceImpl.getAllTeamDepthChart(depthChart);

		// Verify the results
		assertEquals(3, depthChart.get("QB").size(),
				"The size of the returned TeamDepthChartList for QB position should be 3");
		assertEquals("Tom Brady", depthChart.get("QB").get(0).getName(),
				"The name of first player should be Tom Brady");
		assertEquals("Kyle Trask", depthChart.get("QB").get(1).getName(),
				"The name of  second player should be Kyle Trask");
		assertEquals(1, depthChart.get("TE").size(),
				"The size of the returned TeamDepthChartList for TE position should be 1");
	}

	@Test
	public void testGetAllTeamDepthChart_EmptyList() {
		// Mocking the behavior of the TeamDepthChartRepository and PositionService
		when(teamDepthChartRepository.findAll(Sort.by(Sort.Direction.ASC, "depth"))).thenReturn(Arrays.asList());

		when(positionServiceImpl.getAllPositions()).thenReturn(positionList);

		// Calling the service method
		Map<String, List<PlayerVO>> depthChart = new HashMap<String, List<PlayerVO>>();
		depthChart = teamDepthChartServiceImpl.getAllTeamDepthChart(depthChart);

		// Verifying the results
		assertEquals(0, depthChart.get("QB").size(),
				"The size of the returned TeamDepthChartList for QB position should be 0");
		assertEquals(0, depthChart.get("TE").size(),
				"The size of the returned TeamDepthChartList for TE position should be 0");
	}

	@Test
	public void testAddPlayerToDepthChart_WithDepthInInput() {

		PlayerDepthChartVO playerDepthChartVO = new PlayerDepthChartVO(positionList.get(1).getPosition(),
				playerVoList.get(0), 0l);

		// Mocking the behavior of the TeamDepthChartRepository, PlayerService and
		// PositionService
		List<PlayerDepthChart> playerDepthChartForPositionList = new ArrayList<PlayerDepthChart>();
		playerDepthChartForPositionList.add(playerDepthChart.get(3));
		
		assertEquals(1, playerDepthChartForPositionList.size(),
				"The size of the TeamDepthChartList for TE position before updation of Player should be 1");

		when(positionServiceImpl.findByPosition("TE")).thenReturn(positionList.get(1));
		when(playerServiceImpl.getPlayerByNumber(43l)).thenReturn(new Player(43l, "Josh Neil"));
		when(teamDepthChartRepository.findByPositionOrderByDepth(positionList.get(1)))
				.thenReturn(playerDepthChartForPositionList);
		when(teamDepthChartRepository.saveAllAndFlush(playerDepthChartForPositionList))
				.thenReturn(playerDepthChartForPositionList);
		// Calling the service method
		playerDepthChartForPositionList = teamDepthChartServiceImpl.addPlayerToDepthChart(playerDepthChartVO);

		
		assertEquals(2, playerDepthChartForPositionList.size(),
				"The size of the TeamDepthChartList for TE position after updation should be 2");
		// New Player gets added at the depth 0
		assertEquals("Josh Neil", playerDepthChartForPositionList.get(0).getPlayer().getName(),
				"The name of first player should be Josh Neil");
		assertEquals("Mike Gabbert", playerDepthChartForPositionList.get(1).getPlayer().getName(),
				"The name of second player should be Mike Gabbert");

	}

	@Test
	public void testAddPlayerToDepthChart_WithDepthNotInInput() {

		PlayerDepthChartVO playerDepthChartVO = new PlayerDepthChartVO(positionList.get(1).getPosition(),
				playerVoList.get(0), null);

		// Mocking the behavior of the TeamDepthChartRepository, PlayerService and
		// PositionService
		List<PlayerDepthChart> playerDepthChartForPositionList = new ArrayList<PlayerDepthChart>();
		playerDepthChartForPositionList.add(playerDepthChart.get(3));
		
		assertEquals(1, playerDepthChartForPositionList.size(),
				"The size of the TeamDepthChartList for TE position before updation of Player should be 1");
		
		when(positionServiceImpl.findByPosition("TE")).thenReturn(positionList.get(1));
		when(playerServiceImpl.getPlayerByNumber(43l)).thenReturn(new Player(43l, "Josh Neil"));
		when(teamDepthChartRepository.findByPositionOrderByDepth(positionList.get(1)))
				.thenReturn(playerDepthChartForPositionList);
		when(teamDepthChartRepository.saveAllAndFlush(playerDepthChartForPositionList))
				.thenReturn(playerDepthChartForPositionList);
		// Calling the service method
		playerDepthChartForPositionList = teamDepthChartServiceImpl.addPlayerToDepthChart(playerDepthChartVO);

		
		assertEquals(2, playerDepthChartForPositionList.size(),
				"The size of the TeamDepthChartList for TE position after updation should be 2");
		// New Player gets added at the depth 0
		assertEquals("Mike Gabbert", playerDepthChartForPositionList.get(0).getPlayer().getName(),
				"The name of first player should be Mike Gabbert");
		assertEquals("Josh Neil", playerDepthChartForPositionList.get(1).getPlayer().getName(),
				"The name of second player should be Josh Neil");

	}

	@Test
	public void testGetBackupsForPlayers_ForPlayerPresentAtFirstDepth() {

		PlayerDepthChartVO playerDepthChartVO = new PlayerDepthChartVO(positionList.get(0).getPosition(),
				playerVoList.get(1), null);
		List<PlayerVO> playerList = new ArrayList<>();
		// Mocking the behavior of the TeamDepthChartRepository, PlayerService and
		// PositionService
		List<PlayerDepthChart> playerDepthChartList = new ArrayList<PlayerDepthChart>();
		playerDepthChartList.add(playerDepthChart.get(0));
		playerDepthChartList.add(playerDepthChart.get(1));
		playerDepthChartList.add(playerDepthChart.get(2));
		when(positionServiceImpl.findByPosition("QB")).thenReturn(positionList.get(1));
		when(teamDepthChartRepository.findByPositionOrderByDepth(positionList.get(1))).thenReturn(playerDepthChartList);

		// Calling the service method
		playerList = teamDepthChartServiceImpl.getBackupsForPlayers(playerList, playerDepthChartVO);

		assertEquals(2, playerList.size(),
				"The size of the backup player listfor QE position for player 'Tom Brady' should be 2");
		assertEquals("Kyle Trask", playerList.get(0).getName(), "The name of first backup player should be Kyle Trask");
		assertEquals("Blaine Gabbert", playerList.get(1).getName(),
				"The name of second backup player should be Blaine Gabbert");
	}

	@Test
	public void testGetBackupsForPlayers_ForPlayerPresentAtRandomMiddleDepth() {

		PlayerDepthChartVO playerDepthChartVO = new PlayerDepthChartVO(positionList.get(0).getPosition(),
				playerVoList.get(2), null);
		List<PlayerVO> playerList = new ArrayList<>();
		// Mocking the behavior of the TeamDepthChartRepository, PlayerService and
		// PositionService
		List<PlayerDepthChart> playerDepthChartList = new ArrayList<PlayerDepthChart>();
		playerDepthChartList.add(playerDepthChart.get(0));
		playerDepthChartList.add(playerDepthChart.get(1));
		playerDepthChartList.add(playerDepthChart.get(2));
		when(positionServiceImpl.findByPosition("QB")).thenReturn(positionList.get(1));
		when(teamDepthChartRepository.findByPositionOrderByDepth(positionList.get(1))).thenReturn(playerDepthChartList);

		// Calling the service method
		playerList = teamDepthChartServiceImpl.getBackupsForPlayers(playerList, playerDepthChartVO);

		assertEquals(1, playerList.size(),
				"The size of the backup player listfor QE position for player 'Kyle Trask' should be 1");
		assertEquals("Blaine Gabbert", playerList.get(0).getName(),
				"The name of backup player should be Blaine Gabbert");
	}

	@Test
	public void testGetBackupsForPlayers_ForPlayerPresentAtLastDepth() {

		PlayerDepthChartVO playerDepthChartVO = new PlayerDepthChartVO(positionList.get(0).getPosition(),
				playerVoList.get(3), null);
		List<PlayerVO> playerList = new ArrayList<>();
		// Mocking the behavior of the TeamDepthChartRepository, PlayerService and
		// PositionService
		List<PlayerDepthChart> playerDepthChartList = new ArrayList<PlayerDepthChart>();
		playerDepthChartList.add(playerDepthChart.get(0));
		playerDepthChartList.add(playerDepthChart.get(1));
		playerDepthChartList.add(playerDepthChart.get(2));
		when(positionServiceImpl.findByPosition("QB")).thenReturn(positionList.get(1));
		when(teamDepthChartRepository.findByPositionOrderByDepth(positionList.get(1))).thenReturn(playerDepthChartList);

		// Calling the service method
		playerList = teamDepthChartServiceImpl.getBackupsForPlayers(playerList, playerDepthChartVO);

		assertEquals(0, playerList.size(),
				"The size of the backup player listfor QE position for player 'Blaine Gabbert' should be 0");
	}

	@Test
	public void testGetBackupsForPlayers_ForPlayerNotPresentInDepthChart() {

		PlayerDepthChartVO playerDepthChartVO = new PlayerDepthChartVO(positionList.get(0).getPosition(),
				playerVoList.get(4), null);
		List<PlayerVO> playerList = new ArrayList<>();
		// Mocking the behavior of the TeamDepthChartRepository, PlayerService and
		// PositionService
		List<PlayerDepthChart> playerDepthChartList = new ArrayList<PlayerDepthChart>();
		playerDepthChartList.add(playerDepthChart.get(0));
		playerDepthChartList.add(playerDepthChart.get(1));
		playerDepthChartList.add(playerDepthChart.get(2));
		when(positionServiceImpl.findByPosition("QB")).thenReturn(positionList.get(1));
		when(teamDepthChartRepository.findByPositionOrderByDepth(positionList.get(1))).thenReturn(playerDepthChartList);

		// Calling the service method
		playerList = teamDepthChartServiceImpl.getBackupsForPlayers(playerList, playerDepthChartVO);

		assertEquals(0, playerList.size(),
				"The size of the backup player listfor QE position for player 'Joshua Blard' should be 0");
	}

	@Test
    public void testRemovePlayerFromDepthChart_ForPlayerPresentAtFirstDepth() {
    
    	PlayerDepthChartVO playerDepthChartVO = new PlayerDepthChartVO(positionList.get(0).getPosition(), playerVoList.get(1), null);
    	List<PlayerVO> playerList = new ArrayList<>();
    	
    	List<PlayerDepthChart> playerDepthChartListForDeletion = new ArrayList<PlayerDepthChart>();
    	playerDepthChartListForDeletion.add(playerDepthChart.get(0));
    	playerDepthChartListForDeletion.add(playerDepthChart.get(1));
    	playerDepthChartListForDeletion.add(playerDepthChart.get(2));
    	
    	assertEquals(3, playerDepthChartListForDeletion.size(), "The size of the list before player gets deleted for QE position should be 3"); 
    	
    	// Mocking the behavior of the TeamDepthChartRepository and  PositionService
       	when(positionServiceImpl.findByPosition("QB")).thenReturn(positionList.get(1));
    	when(teamDepthChartRepository.findByPositionOrderByDepth(positionList.get(1))).thenReturn(playerDepthChartListForDeletion);
    	when(teamDepthChartRepository.saveAllAndFlush(playerDepthChartListForDeletion)).thenReturn(playerDepthChartListForDeletion);
    
    	
    	// Calling the service method
    	 playerList = teamDepthChartServiceImpl.removePlayerFromDepthChart(playerDepthChartVO);
    	 
    	assertEquals(2, playerDepthChartListForDeletion.size(), "The size of the list before player gets deleted for QE position should be 2");  
    	assertEquals(1, playerList.size(), "The size of the list after player deleted for QE position should be 1"); 
    	assertEquals("Tom Brady", playerList.get(0).getName(), "The name of backup player should be Tom Brady");
    }

	@Test
	public void testRemovePlayerFromDepthChart_ForPlayerPresentAtRandomMiddleDepth() {

		PlayerDepthChartVO playerDepthChartVO = new PlayerDepthChartVO(positionList.get(0).getPosition(), playerVoList.get(2), null);
    	List<PlayerVO> playerList = new ArrayList<>();
    	
    	List<PlayerDepthChart> playerDepthChartListForDeletion = new ArrayList<PlayerDepthChart>();
    	playerDepthChartListForDeletion.add(playerDepthChart.get(0));
    	playerDepthChartListForDeletion.add(playerDepthChart.get(1));
    	playerDepthChartListForDeletion.add(playerDepthChart.get(2));
    	
    	assertEquals(3, playerDepthChartListForDeletion.size(), "The size of the list before player gets deleted for QE position should be 3"); 
    	
    	// Mocking the behavior of the TeamDepthChartRepository and  PositionService
       	when(positionServiceImpl.findByPosition("QB")).thenReturn(positionList.get(1));
    	when(teamDepthChartRepository.findByPositionOrderByDepth(positionList.get(1))).thenReturn(playerDepthChartListForDeletion);
    	when(teamDepthChartRepository.saveAllAndFlush(playerDepthChartListForDeletion)).thenReturn(playerDepthChartListForDeletion);
    
    	
    	// Calling the service method
    	 playerList = teamDepthChartServiceImpl.removePlayerFromDepthChart(playerDepthChartVO);
    	 
    	assertEquals(2, playerDepthChartListForDeletion.size(), "The size of the list before player gets deleted for QE position should be 2"); 
    	assertEquals(1, playerList.size(), "The size of the list after player deleted for QE position should be 1"); 
    	assertEquals("Kyle Trask", playerList.get(0).getName(), "The name of backup player should be Kyle Trask");
	}

	@Test
	public void testRemovePlayerFromDepthChart_ForPlayerPresentAtLastDepth() {

		PlayerDepthChartVO playerDepthChartVO = new PlayerDepthChartVO(positionList.get(0).getPosition(), playerVoList.get(3), null);
    	List<PlayerVO> playerList = new ArrayList<>();
    	
    	List<PlayerDepthChart> playerDepthChartListForDeletion = new ArrayList<PlayerDepthChart>();
    	playerDepthChartListForDeletion.add(playerDepthChart.get(0));
    	playerDepthChartListForDeletion.add(playerDepthChart.get(1));
    	playerDepthChartListForDeletion.add(playerDepthChart.get(2));
    	
    	assertEquals(3, playerDepthChartListForDeletion.size(), "The size of the list before player gets deleted for QE position should be 3"); 
    	
    	// Mocking the behavior of the TeamDepthChartRepository and  PositionService
       	when(positionServiceImpl.findByPosition("QB")).thenReturn(positionList.get(1));
    	when(teamDepthChartRepository.findByPositionOrderByDepth(positionList.get(1))).thenReturn(playerDepthChartListForDeletion);
    	when(teamDepthChartRepository.saveAllAndFlush(playerDepthChartListForDeletion)).thenReturn(playerDepthChartListForDeletion);
    
    	
    	// Calling the service method
    	 playerList = teamDepthChartServiceImpl.removePlayerFromDepthChart(playerDepthChartVO);
    	 
    	assertEquals(2, playerDepthChartListForDeletion.size(), "The size of the list before player gets deleted for QE position should be 2");  
    	assertEquals(1, playerList.size(), "The size of the list after player deleted for QE position should be 1"); 
    	assertEquals("Blaine Gabbert", playerList.get(0).getName(), "The name of backup player should be Blaine Gabbert");
	}

	@Test
	public void testRemovePlayerFromDepthChart_ForPlayerNotPresentInDepthChart() {

		PlayerDepthChartVO playerDepthChartVO = new PlayerDepthChartVO(positionList.get(0).getPosition(), playerVoList.get(4), null);
    	List<PlayerVO> playerList = new ArrayList<>();
    	
    	List<PlayerDepthChart> playerDepthChartListForDeletion = new ArrayList<PlayerDepthChart>();
    	playerDepthChartListForDeletion.add(playerDepthChart.get(0));
    	playerDepthChartListForDeletion.add(playerDepthChart.get(1));
    	playerDepthChartListForDeletion.add(playerDepthChart.get(2));
    	
    	assertEquals(3, playerDepthChartListForDeletion.size(), "The size of the list before player gets deleted for QE position should be 3"); 
    	
    	// Mocking the behavior of the TeamDepthChartRepository and  PositionService
       	when(positionServiceImpl.findByPosition("QB")).thenReturn(positionList.get(1));
    	when(teamDepthChartRepository.findByPositionOrderByDepth(positionList.get(1))).thenReturn(playerDepthChartListForDeletion);
    	
    	// Calling the service method
    	 playerList = teamDepthChartServiceImpl.removePlayerFromDepthChart(playerDepthChartVO);
    	 
    	assertEquals(3, playerDepthChartListForDeletion.size(), "The size of the list before player gets deleted for QE position should be 3");  
    	assertEquals(0, playerList.size(), "The size of the list after player deleted for QE position should be 0"); 
	}

}

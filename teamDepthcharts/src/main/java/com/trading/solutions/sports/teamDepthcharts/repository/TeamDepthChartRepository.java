package com.trading.solutions.sports.teamDepthcharts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.solutions.sports.teamDepthcharts.entity.PlayerDepthChart;
import com.trading.solutions.sports.teamDepthcharts.entity.Position;

public interface TeamDepthChartRepository extends JpaRepository<PlayerDepthChart, Long>{
	
	public List<PlayerDepthChart> findByPositionOrderByDepth(Position position);

}

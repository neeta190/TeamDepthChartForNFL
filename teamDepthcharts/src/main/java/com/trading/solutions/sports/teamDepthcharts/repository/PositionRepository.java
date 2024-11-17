package com.trading.solutions.sports.teamDepthcharts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.solutions.sports.teamDepthcharts.entity.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {
	
	public Position findByPosition(String position);

}

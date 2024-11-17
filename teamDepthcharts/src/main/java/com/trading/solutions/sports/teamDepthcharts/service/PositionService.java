package com.trading.solutions.sports.teamDepthcharts.service;

import java.util.List;

import com.trading.solutions.sports.teamDepthcharts.entity.Position;

public interface PositionService {

	public List<Position> getAllPositions();

	public Position savePosition(Position position);

	public Position findByPosition(String position);

}

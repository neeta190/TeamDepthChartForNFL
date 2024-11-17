package com.trading.solutions.sports.teamDepthcharts.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.solutions.sports.teamDepthcharts.entity.Position;
import com.trading.solutions.sports.teamDepthcharts.repository.PositionRepository;
import com.trading.solutions.sports.teamDepthcharts.service.PositionService;

@Service
public class PositionServiceImpl implements PositionService {

	private PositionRepository positionRepository;
	
	@Autowired
	public PositionServiceImpl(PositionRepository positionRepository) {
		this.positionRepository = positionRepository;
	}

	public List<Position> getAllPositions() {
		return positionRepository.findAll();
	}

	public Position savePosition(Position position) {
		return positionRepository.save(position);
	}

	public Position findByPosition(String position) {
		return positionRepository.findByPosition(position);
	}

}

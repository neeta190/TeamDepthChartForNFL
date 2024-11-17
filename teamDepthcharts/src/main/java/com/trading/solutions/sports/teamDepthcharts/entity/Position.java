package com.trading.solutions.sports.teamDepthcharts.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Position {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String position;
	private String position_name;
	
	
	@OneToMany(mappedBy = "position" , fetch = FetchType.LAZY)
    List<PlayerDepthChart> playerDepthChart;
	
	public Position() {}

	public Position(String position, String position_name) {
		super();
		this.position = position;
		this.position_name = position_name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition_name() {
		return position_name;
	}

	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}

	@JsonManagedReference
	public List<PlayerDepthChart> getPlayerDepthChart() {
		return playerDepthChart;
	}

	public void setPlayerDepthChart(List<PlayerDepthChart> playerDepthChart) {
		this.playerDepthChart = playerDepthChart;
	}

		
}

package com.trading.solutions.sports.teamDepthcharts.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Player {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long number;
	
	private String name;
	/*
	 * @ManyToOne(fetch = FetchType.LAZY, optional = false)
	 * 
	 * @JoinColumn(name = "team_id") private Team team;
	 */
	@OneToMany(mappedBy = "player", fetch = FetchType.LAZY)
    List<PlayerDepthChart> playerDepthChart;
	
	public Player() {
		
	}
	
	public Player(Long number, String name/* , Team team */) {
		super();
		this.number = number;
		this.name = name;
		//this.team = team;
	}

	public Long getId() {
		return id;
	}

	public Long getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * public Team getTeam() { return team; }
	 * 
	 * public void setTeam(Team team) { this.team = team; }
	 */
	@JsonManagedReference
	public List<PlayerDepthChart> getPlayerDepthChart() {
		return playerDepthChart;
	}

	public void setPlayerDepthChart(List<PlayerDepthChart> playerDepthChart) {
		this.playerDepthChart = playerDepthChart;
	}
}

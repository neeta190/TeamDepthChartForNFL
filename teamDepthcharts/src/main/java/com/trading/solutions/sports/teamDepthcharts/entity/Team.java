package com.trading.solutions.sports.teamDepthcharts.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sport_id")
	private Sport sport;
	
	/*
	 * @JsonManagedReference
	 * 
	 * @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade =
	 * CascadeType.ALL) private List<Player> players;
	 */

	public Team() {}

	public Team(String name, Sport sport) {
		super();
		this.name = name;
		this.sport = sport;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Sport getSport() {
		return sport;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	/*
	 * public List<Player> getPlayers() { return players; }
	 * 
	 * public void setPlayers(List<Player> players) { this.players = players; }
	 */
	
}

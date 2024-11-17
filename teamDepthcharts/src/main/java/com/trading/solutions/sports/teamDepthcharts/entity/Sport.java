package com.trading.solutions.sports.teamDepthcharts.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Sport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	public Sport(String name) {
		super();
		this.name = name;
	}

	@JsonManagedReference
	@OneToMany(mappedBy = "sport", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Team> teams;
	
	public Sport() {
		
	}
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Team> getTeams() {
		return teams;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	
	
}

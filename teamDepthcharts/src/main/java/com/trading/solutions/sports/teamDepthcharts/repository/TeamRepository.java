package com.trading.solutions.sports.teamDepthcharts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.solutions.sports.teamDepthcharts.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long>{

}

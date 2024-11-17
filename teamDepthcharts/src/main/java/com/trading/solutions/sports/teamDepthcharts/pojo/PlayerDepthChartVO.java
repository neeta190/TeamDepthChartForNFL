package com.trading.solutions.sports.teamDepthcharts.pojo;

import jakarta.validation.constraints.NotBlank;

public class PlayerDepthChartVO {

	@NotBlank(message = "Position cannot be empty")
	private String position;
	
	private PlayerVO player;
	
	private Long depth;
	
	public PlayerDepthChartVO(){
		
	}

	public PlayerDepthChartVO(String position, PlayerVO player, Long depth) {
		super();
		this.position = position;
		this.player = player;
		this.depth = depth;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public PlayerVO getPlayer() {
		return player;
	}

	public void setPlayer(PlayerVO player) {
		this.player = player;
	}

	public Long getDepth() {
		return depth;
	}

	public void setDepth(Long depth) {
		this.depth = depth;
	}
}


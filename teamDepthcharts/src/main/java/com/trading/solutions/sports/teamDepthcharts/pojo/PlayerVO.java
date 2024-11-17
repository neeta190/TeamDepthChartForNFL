package com.trading.solutions.sports.teamDepthcharts.pojo;

import jakarta.validation.constraints.NotBlank;

public class PlayerVO {
	
	@NotBlank(message = "Number cannot be empty")
	private Long number;
	
	@NotBlank(message = "Name cannot be empty")
	private String name;
	
	public PlayerVO() {
		
	}

	public PlayerVO(Long number, String name) {
		super();
		this.number = number;
		this.name = name;
	}
	
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "(#" + number + ", name=" + name + ")";
	}   


}

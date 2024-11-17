package com.trading.solutions.sports.teamDepthcharts.pojo;

public class CustomErrorResponse {
	
    private String errorMsg;
    private String errorDetails;
    
    
	public CustomErrorResponse(String errorMsg, String errorDetails) {
		super();
		this.errorMsg = errorMsg;
		this.errorDetails = errorDetails;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	    
}

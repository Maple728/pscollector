package org.maple.profitsystem.models;

public class EarningDateModel {

	private Long id;
	
	private String symbol;
	
	private Integer reportDate;
	
	private String time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Integer getReportDate() {
		return reportDate;
	}

	public void setReportDate(Integer reportDate) {
		this.reportDate = reportDate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}

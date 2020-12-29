package org.maple.profitsystem.models;

public class OIModel {
	
	@Override
	public boolean equals(Object o) {
		if(null == o || !(o instanceof OIModel)) {
			return false;
		} else {
			return this.hashCode() == ((OIModel)o).hashCode();
		}
	}
	
	@Override
	public int hashCode() {
		try {
			return toString().hashCode();
		} catch(Exception e) {
			return 0;
		}

	}
	
	@Override
	public String toString() {
		return this.companyId + "," + this.oiDate + "," + this.callPut + "," + this.strike + "," + this.expiration;
	}
	
	private Long id;
	
	private Long companyId;
	
	private Integer oiDate;	// yyyyMMdd
	
	private Character callPut;
	
	private Double strike;
	
	private Integer expiration; 
	
	private Double open;
	
	private Double high;
	
	private Double low;
	
	private Double close;
	
	private Integer volume;
	
	private Integer oi;

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public Integer getOiDate() {
		return oiDate;
	}

	public void setOiDate(Integer oiDate) {
		this.oiDate = oiDate;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	public Integer getOi() {
		return oi;
	}

	public void setOi(Integer oi) {
		this.oi = oi;
	}

	public Character getCallPut() {
		return callPut;
	}

	public void setCallPut(Character callPut) {
		this.callPut = callPut;
	}

	public Double getStrike() {
		return strike;
	}

	public void setStrike(Double strike) {
		this.strike = strike;
	}

	public Integer getExpiration() {
		return expiration;
	}

	public void setExpiration(Integer expiration) {
		this.expiration = expiration;
	}
}

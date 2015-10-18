package pl.edu.agh.io.pojo;

import java.math.BigDecimal;

public class Point {
	
	private BigDecimal latitude;
	private BigDecimal longitude;
	
	
	public Point(BigDecimal latitude, BigDecimal longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}


	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
}

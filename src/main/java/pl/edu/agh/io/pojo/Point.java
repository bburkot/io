package pl.edu.agh.io.pojo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"latitude", "longitude"}, 
		name="UK_latitude_longitude" )})
public class Point {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;	
	
	@Column(columnDefinition="decimal(8,6)")
	private BigDecimal latitude;
	
	@Column(columnDefinition="decimal(8,6)")
	private BigDecimal longitude;
	
	
	// 0.001'    = 0.1095 km
	// 0.0001'   = 109,5 m
	// 0.000001'  = 1,095 m
	public Point(){};
	public Point(BigDecimal latitude, BigDecimal longitude) {
		this.latitude = latitude.setScale(6, RoundingMode.HALF_UP);   // dokladnosc ~1m
		this.longitude = longitude.setScale(6, RoundingMode.HALF_UP); // dokladnosc ~1m
	}

	
	// getters and setters
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((getLatitude() == null) ? 0 : getLatitude().hashCode());
		result = prime * result	+ ((getLongitude() == null) ? 0 : getLongitude().hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Point))
			return false;
		Point other = (Point) obj;
		if (getLatitude() == null) {
			if (other.getLatitude() != null)
				return false;
		} else if (!getLatitude().equals(other.getLatitude()))
			return false;
		if (getLongitude() == null) {
			if (other.getLongitude() != null)
				return false;
		} else if (!getLongitude().equals(other.getLongitude()))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Point [id=" + id + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}
}

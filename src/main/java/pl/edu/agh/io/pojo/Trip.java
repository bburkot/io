package pl.edu.agh.io.pojo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="TRIP")
public class Trip {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name="trip_id", length=30)
	private String tripId;
	
	@Column(name="taxi_id")
	private Integer taxiId;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name="call_type", columnDefinition="smallint")
	private CallType callType;
	
	@Column(name="ORIGIN_CALL")
	private Integer originCall;

	@Column(name="ORIGIN_STAND")
	private Integer originStand;
		
	private Date timestamp;

	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="route_id")
	private Route route;
	
	
	@Column(columnDefinition="decimal(22,19)", name="start_lat")
	private BigDecimal startLat;	
	@Column(columnDefinition="decimal(22,19)", name="start_long")
	private BigDecimal startLong;
	
	
	@Column(columnDefinition="decimal(22,19)", name="end_lat")
	private BigDecimal endLat;	
	@Column(columnDefinition="decimal(22,19)", name="end_long")
	private BigDecimal endLong;
 	
	
	
	public Trip(){}	
	public Trip(Integer taxiId, CallType callType, Integer originCall,
			Integer originStand, Date timestamp, String route, 
			BigDecimal startLat, BigDecimal startLong, 
			BigDecimal endLat, BigDecimal endLong) {

		this.taxiId = taxiId;
		this.callType = callType;
		this.originCall = originCall;
		this.originStand = originStand;
		this.timestamp = timestamp;
		this.route = new Route(route);
		this.startLat = startLat;
		this.startLong = startLong;
		this.endLat = endLat;
		this.endLong = endLong;
	}
	
	
	
	// getters and setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Integer getTaxiId() {
		return taxiId;
	}
	public void setTaxiId(Integer taxiId) {
		this.taxiId = taxiId;
	}
	public CallType getCallType() {
		return callType;
	}
	public void setCallType(CallType callType) {
		this.callType = callType;
	}
	public Integer getOriginCall() {
		return originCall;
	}
	public void setOriginCall(Integer originCall) {
		this.originCall = originCall;
	}
	public Integer getOriginStand() {
		return originStand;
	}
	public void setOriginStand(Integer originStand) {
		this.originStand = originStand;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getTripId() {
		return tripId;
	}
	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
}

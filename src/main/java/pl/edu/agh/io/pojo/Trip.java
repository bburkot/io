package pl.edu.agh.io.pojo;

import java.util.Date;
import java.util.List;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="TRIP")
public class Trip {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;	
	
	@Column(name="trip_ID")
	private long tripId;
			
	@Enumerated(EnumType.ORDINAL)
	@Column(name="call_type", columnDefinition="smallint")
	private CallType callType;
	
	@Column(name="ORIGIN_CALL")
	private Integer originCall;

	@Column(name="ORIGIN_STAND")
	private Integer originStand;
		
	@Column(name="taxi_id")
	private Integer taxiId;

	private Date timestamp;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name="day_type", columnDefinition="smallint")
	private DayType dayType;
	
	@Column(name="missing_data")
	private boolean missingData;
			
	@ManyToOne(cascade=CascadeType.DETACH)
	@JoinColumn(name="start_point_id")
	private Point start;
	
	@ManyToOne(cascade=CascadeType.DETACH)
	@JoinColumn(name="end_point_id")
	private Point end;

	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.DETACH)
	@JoinTable(name = "trip_point", joinColumns = { 
			@JoinColumn(name = "trip_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "point_ID")})
	private List<Point> route;
 	
	@Column(name="duration_in_seconds")
	private long duration;
	
	
	public Trip(){}
	public Trip(long tripid, CallType callType, Integer originCall,
			Integer originStand, Integer taxiId, Date timestamp,
			DayType dayType, boolean missingData, List<Point> route,
			Point start, Point end) {
		
		this.tripId = tripid;
		this.callType = callType;
		this.originCall = originCall;
		this.originStand = originStand;
		
		this.taxiId = taxiId;
		this.timestamp = timestamp;
		this.dayType = dayType;		
		this.missingData = missingData;
		
		this.route = route;
		this.start = start;
		this.end = end;
	}
	
	
	
	// getters and setters
	/**
	 * @return start time in seconds
	 */
	public long getStartTime() {
		return timestamp.getTime()/1000;
	}
	/**
	 * @return end time in seconds
	 */
	public long getEndTime() {
		return getStartTime() + getDuration();
	}

	
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
	public DayType getDayType() {
		return dayType;
	}
	public void setDayType(DayType dayType) {
		this.dayType = dayType;
	}
	public boolean isMissingData() {
		return missingData;
	}
	public void setMissingData(boolean missingData) {
		this.missingData = missingData;
	}
	public Point getStart() {
		return start;
	}
	public void setStart(Point start) {
		this.start = start;
	}
	public Point getEnd() {
		return end;
	}
	public void setEnd(Point end) {
		this.end = end;
	}
	@Override
	public String toString() {
		return "Trip [id=" + id + ", tripId=" + tripId + ", taxiId=" + taxiId
				+ ", timestamp=" + timestamp + ", start=" + start + ", end="
				+ end + "]";
	}
	public String toStringShort() {
		return "[" +getId() + " " + getTimestamp().getTime() + "]";
	}
	public Long getDuration() {
		return duration;
	}
	/**
	 * @return duration time in milliseconds
	 */
	public Long getDurationMills() {
		return duration * 1000;
	}
	/**
	 * @return duration time in seconds
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}
}

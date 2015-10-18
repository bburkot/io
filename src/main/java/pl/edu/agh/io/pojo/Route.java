package pl.edu.agh.io.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="ROUTE")
public class Route {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(columnDefinition="TEXT")
	private String value;

	
	public Route(){}
	public Route(String value) {
		this.value = value;
	}
	
	
	// getters and setter
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}

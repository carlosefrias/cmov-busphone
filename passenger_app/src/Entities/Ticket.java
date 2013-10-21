package Entities;

import java.io.Serializable;
import java.util.Date;

public class Ticket implements Serializable{
	private static final long serialVersionUID = 1L;
	private String idticket, type;
	private boolean isvalidated, isused;
	private Date timeofvalidation;
	private long busid; 
	
	
	public String getIdticket() {
		return idticket;
	}
	public void setIdticket(String idticket) {
		this.idticket = idticket;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isIsvalidated() {
		return isvalidated;
	}
	public void setIsvalidated(boolean isvalidated) {
		this.isvalidated = isvalidated;
	}
	public boolean isIsused() {
		return isused;
	}
	public void setIsused(boolean isused) {
		this.isused = isused;
	}
	public Date getTimeofvalidation() {
		return timeofvalidation;
	}
	public void setTimeofvalidation(Date timeofvalidation) {
		this.timeofvalidation = timeofvalidation;
	}
	public long getBusid() {
		return busid;
	}
	public void setBusid(long busid) {
		this.busid = busid;
	}
	@Override
	public String toString(){
		return "" + this.idticket + ", " + this.type + ", " + this.isvalidated + ", " + this.isused + ", " + this.timeofvalidation + ", " + this.busid;
	}
	public String toStringShortVersion(){
		return "Ticket id:" + this.idticket;
	}
}
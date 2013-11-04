package Entities;

import java.io.Serializable;

public class Ticket implements Serializable{
	private static final long serialVersionUID = 1L;
	private String idticket, type;
	private boolean isvalidated, ischecked;
	private String timeofvalidation;
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
	public boolean isChecked() {
		return ischecked;
	}
	public void setisChecked(boolean ischecked) {
		this.ischecked = ischecked;
	}
	public String getTimeofvalidation() {
		return timeofvalidation;
	}
	public void setTimeofvalidation(String timeofvalidation) {
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
		String bus = "" + busid;
		String time = timeofvalidation;
		if(busid == -1) bus = "none";
		if(time.equals("1990/01/01 00:00:00")) time = "none";
		return "Ticket id:" + this.idticket + "\nType:" + this.type + "\nValidated:" + this.isvalidated + "\nChecked:" + this.ischecked + "\nTime of validation:" + time + "\nBus id:" + bus;
	}
	public String toStringShortVersion(){
		return "Ticket of type " + this.type;
	}
}
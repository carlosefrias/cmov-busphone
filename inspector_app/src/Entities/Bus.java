package Entities;

import java.io.Serializable;

public class Bus implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	public Bus(int id){
		this.setId(id);
	}
	@Override
	public String toString(){
		return "Bus number " + getId();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}

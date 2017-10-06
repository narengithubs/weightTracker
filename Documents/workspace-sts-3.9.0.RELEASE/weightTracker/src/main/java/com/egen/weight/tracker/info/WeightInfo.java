package com.egen.weight.tracker.info;

import java.util.Date;

public class WeightInfo {

	public Date date;
	public String value;
	public String name;
	public int baseWeight;
	
	public WeightInfo() {
		
	}
	
    public WeightInfo(Date date,String value,String name,int baseWeight) {
		
	}
	
	public Date getTimeStamp() {
		return date;
	}
	public void setTimeStamp(Date timeStamp) {
		this.date = timeStamp;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBaseWeight() {
		return baseWeight;
	}
	public void setBaseWeight(int baseWeight) {
		this.baseWeight = baseWeight;
	}
	
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
		}  
}

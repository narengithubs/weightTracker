package com.egen.weight.tracker.morphia.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;

@Entity(value = "metrics")
public class WeightEntity extends BaseEntity implements Cloneable {

	public Date date;
	public String value;
	public String name;
	public int baseWeight;
	
	public WeightEntity() {
		
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

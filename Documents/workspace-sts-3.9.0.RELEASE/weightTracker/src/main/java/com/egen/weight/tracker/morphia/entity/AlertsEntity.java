package com.egen.weight.tracker.morphia.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;

@Entity(value = "alerts")
public class AlertsEntity extends BaseEntity {

	public Date date;
	public String value;
	public String name;
	public String type;
	public int baseWeight;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getBaseWeight() {
		return baseWeight;
	}
	public void setBaseWeight(int baseWeight) {
		this.baseWeight = baseWeight;
	}
}

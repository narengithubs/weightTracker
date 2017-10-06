package com.egen.weight.tracker.easyRules;

import java.util.Date;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;

import com.egen.weight.tracker.morphia.entity.AlertsEntity;
import com.egen.weight.tracker.morphia.entity.WeightEntity;
import com.egen.weight.tracker.service.AlertService;

@Rule(name="MetricRuleOverWeight")
public class MetricRuleOverWeight {
	
	private static final int PERCENTAGE = 10;
	
	private WeightEntity weightEntity;
	
	AlertService alertService;
	
	public MetricRuleOverWeight() {
		
    } 
	
	public MetricRuleOverWeight(WeightEntity weightEntity,AlertService alertService) { 
        this.weightEntity = weightEntity;
        this.alertService = alertService;
    } 
 
    @Condition 
    public boolean isOverWeight() {
    	float percentage = 0;
    	int percent = 0;
    	int baseWeight = weightEntity.getBaseWeight();
    	int currentWeight = Integer.parseInt(weightEntity.getValue());
    	if(currentWeight > baseWeight) {
    		int result = (currentWeight-baseWeight);
    		percentage = result % (baseWeight * 100);
    		percent = (int) percentage;
    	}
        return percent > 0 && percent > PERCENTAGE;
    } 
 
    @Action
    public void triggerAlert(){ 
    	AlertsEntity alertsEntity = new AlertsEntity();
		try {
			alertsEntity.setName(weightEntity.getName());
			alertsEntity.setTimeStamp(new Date());
			alertsEntity.setValue(weightEntity.getValue());
			alertsEntity.setType("OverWeight");
			alertsEntity.setBaseWeight(weightEntity.getBaseWeight());
			alertService.createAlert(alertsEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
    } 
}

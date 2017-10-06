package com.egen.weight.tracker.easyRules;

import java.util.Date;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;

import com.egen.weight.tracker.morphia.entity.AlertsEntity;
import com.egen.weight.tracker.morphia.entity.WeightEntity;
import com.egen.weight.tracker.service.AlertService;

@Rule(name="MetricRuleUnderWeight")
public class MetricRuleUnderWeight {
	
	private static final float PERCENTAGE = 10;
	
	private WeightEntity weightEntity;
	
	AlertService alertService;
	
	public MetricRuleUnderWeight() {
		
    } 
	
	public MetricRuleUnderWeight(WeightEntity weightEntity,AlertService alertService) {
        this.weightEntity = weightEntity;
        this.alertService = alertService;
    } 
 
	@Condition 
    public boolean isUnderWeight() {
    	float percentage = 0;
    	int percent = 0;
    	int baseWeight = weightEntity.getBaseWeight();
    	int currentWeight = Integer.parseInt(weightEntity.getValue());
    	if(baseWeight > currentWeight) {
    		int result = (baseWeight-currentWeight);
    		percentage = result % (baseWeight * 100);
    		percent = (int) percentage;
    	}
        return percent != 0 && percent > PERCENTAGE;
    } 
 
    @Action
    public void triggerAlert(){ 
    	AlertsEntity alertsEntity = new AlertsEntity();
		try {
			alertsEntity.setName(weightEntity.getName());
			alertsEntity.setTimeStamp(new Date());
			alertsEntity.setValue(weightEntity.getValue());
			alertsEntity.setType("UnderWeight");
			alertsEntity.setBaseWeight(weightEntity.getBaseWeight());
			alertService.createAlert(alertsEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
    } 
}

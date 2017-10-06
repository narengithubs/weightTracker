package com.egen.weight.tracker.service;

import java.util.List;

import com.egen.weight.tracker.morphia.entity.AlertsEntity;
import com.mongodb.DBObject;

public interface AlertService {
	public boolean createAlert(AlertsEntity alertsEntity);
	public List<DBObject> read();
	public List<DBObject> readByTimeRange(String timeStamp);
}

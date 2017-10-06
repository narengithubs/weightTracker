package com.egen.weight.tracker.service;

import java.util.List;

import com.mongodb.DBObject;

public interface MetricService {
	public boolean create(String data,int baseWeight);
	public List<DBObject> read();
	public List<DBObject> readByTimeRange(String timeRange);
}

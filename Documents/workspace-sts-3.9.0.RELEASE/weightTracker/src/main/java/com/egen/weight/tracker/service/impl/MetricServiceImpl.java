package com.egen.weight.tracker.service.impl;

import static org.easyrules.core.RulesEngineBuilder.aNewRulesEngine;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.easyrules.api.RulesEngine;
import org.json.JSONException;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egen.weight.tracker.easyRules.MetricRuleOverWeight;
import com.egen.weight.tracker.easyRules.MetricRuleUnderWeight;
import com.egen.weight.tracker.morphia.DataBaseMorphia;
import com.egen.weight.tracker.morphia.entity.WeightEntity;
import com.egen.weight.tracker.service.AlertService;
import com.egen.weight.tracker.service.MetricService;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

@Service
public class MetricServiceImpl implements MetricService {
	
	private final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	AlertService alertService;
	
	/* (non-Javadoc)
	 * Method to create data in metrics collection and invokes easy rule engine
	 * @see com.egen.weight.tracker.service.MetricService#create(java.lang.String, int)
	 */
	@Override
	public boolean create(String data,int baseWeight) {
		Datastore datastore = DataBaseMorphia.getDataStore();
		try {
			JSONObject jsonObject = new JSONObject(data);
			
			/** Entity class mapped with metrics collection **/
			WeightEntity weightEntity = new WeightEntity();
			
			/** Persisting time stamp as date format with time zone **/
			Long timeStamp  = Long.parseLong(jsonObject.get("timeStamp").toString());
			Date date = convertMillsToDate(timeStamp);
			weightEntity.setTimeStamp(date);
			
			weightEntity.setValue(jsonObject.get("value").toString());
			String name = jsonObject.has("name") ? jsonObject.get("name").toString():"Narayanan";
			weightEntity.setName(name);
			weightEntity.setBaseWeight(baseWeight);
			
			/** Persisting data into metrics collection **/
			datastore.save(weightEntity);
			
			/** Easy rule engine for over and under weight**/
			RulesEngine rulesEngine = aNewRulesEngine().build();
			rulesEngine.registerRule(new MetricRuleOverWeight(weightEntity,alertService));
			rulesEngine.registerRule(new MetricRuleUnderWeight(weightEntity,alertService));
			rulesEngine.fireRules();
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * Method to read all data's from metrics collection
	 * @see com.egen.weight.tracker.service.MetricService#read()
	 */
	@Override
	public List<DBObject> read() {
		Datastore datastore = DataBaseMorphia.getDataStore();
		DBCollection collection = datastore.getCollection(WeightEntity.class);
		DBCursor dbCursor = collection.find();
		List<DBObject> weightEntities = dbCursor.toArray();
		return weightEntities;
	}

	/* (non-Javadoc
	 * Method to read  data's from metrics collection for given time interval
	 * @see com.egen.weight.tracker.service.MetricService#readByTimeRange(java.lang.String)
	 */
	@Override
	public List<DBObject> readByTimeRange(String timeRange) {
		Datastore datastore = DataBaseMorphia.getDataStore();
		List<DBObject> weightEntities = null;
		try {
			JSONObject jsonObject = new JSONObject(timeRange);
			Long fromTimeStamp  = Long.parseLong(jsonObject.has("fromTimeStamp") ?
													jsonObject.get("fromTimeStamp").toString():"1507215646993");
			Long toTimeStamp  = Long.parseLong(jsonObject.get("toTimeStamp").toString());
			
			Date fromDate = convertMillsToDate(fromTimeStamp);
			Date toDate = convertMillsToDate(toTimeStamp);
			
			DBCollection collection = datastore.getCollection(WeightEntity.class);
			
			DBObject query = new QueryBuilder()
					         .start()
					         .and(new QueryBuilder().start().put("date").greaterThanEquals(fromDate).get(),
					              new QueryBuilder().start().put("date").lessThanEquals(toDate).get())
					         .get();
			
			DBCursor dbCursor = collection.find(query);
			weightEntities = dbCursor.toArray();
		} catch (JSONException e) {
			e.printStackTrace();
			return weightEntities;
		}
		return weightEntities;
	}
	
	/**
	 * Date conversion utility method
	 * @param timeMillis
	 * @return Date
	 */
	private Date convertMillsToDate(Long timeMillis) {
		Date date = null;
		try {
			date = new Date(timeMillis);
		} catch(Exception e) {
			log.error("Exception in convertMillsToDate"+e.getMessage());
		}
		return date;
	}

}

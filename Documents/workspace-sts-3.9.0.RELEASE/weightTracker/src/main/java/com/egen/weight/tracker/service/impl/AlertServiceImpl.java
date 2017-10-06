package com.egen.weight.tracker.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;
import org.springframework.stereotype.Service;

import com.egen.weight.tracker.morphia.DataBaseMorphia;
import com.egen.weight.tracker.morphia.entity.AlertsEntity;
import com.egen.weight.tracker.service.AlertService;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

@Service
public class AlertServiceImpl implements AlertService {
	
	private final Logger log = Logger.getLogger(this.getClass());
	

	/* (non-Javadoc)
	 * Method to create data in alerts collection
	 * @see com.egen.weight.tracker.service.AlertService#createAlert(com.egen.weight.tracker.morphia.entity.AlertsEntity)
	 */
	@Override
	public boolean createAlert(AlertsEntity alertsEntity) {
		Datastore datastore = DataBaseMorphia.getDataStore();
		try {
			datastore.save(alertsEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * Method to read all data's from alerts collection
	 * @see com.egen.weight.tracker.service.AlertService#read()
	 */
	@Override
	public List<DBObject> read() {
		Datastore datastore = DataBaseMorphia.getDataStore();
		DBCollection collection = datastore.getCollection(AlertsEntity.class);
		DBCursor dbCursor = collection.find();
		List<DBObject> alertEntities = dbCursor.toArray();
		return alertEntities;
	}

	/* (non-Javadoc)
	 * Method to read  data's from alerts collection for given time interval
	 * @see com.egen.weight.tracker.service.AlertService#readByTimeRange(java.lang.String)
	 */
	@Override
	public List<DBObject> readByTimeRange(String timeRange) {
		Datastore datastore = DataBaseMorphia.getDataStore();
		List<DBObject> alertEntities = null;
		try {
			JSONObject jsonObject = new JSONObject(timeRange);
			Long fromTimeStamp  = Long.parseLong(jsonObject.has("fromTimeStamp") ?
					jsonObject.get("fromTimeStamp").toString():"1507215646993");
			Long toTimeStamp  = Long.parseLong(jsonObject.get("toTimeStamp").toString());
			
			Date fromDate = convertMillsToDate(fromTimeStamp);
			Date toDate = convertMillsToDate(toTimeStamp);
			
			DBCollection collection = datastore.getCollection(AlertsEntity.class);
			
			DBObject query = new QueryBuilder()
					         .start()
					         .and(new QueryBuilder().start().put("date").greaterThanEquals(fromDate).get(),
					              new QueryBuilder().start().put("date").lessThanEquals(toDate).get())
					         .get();
			
			DBCursor dbCursor = collection.find(query);
			alertEntities = dbCursor.toArray();
		} catch (JSONException e) {
			e.printStackTrace();
			return alertEntities;
		}
		return alertEntities;
	}
	
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

package com.egen.weight.tracker.controller;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.egen.weight.tracker.service.MetricService;
import com.mongodb.DBObject;

/**
 * @author Narayanan
 * Controller class to create, read and read data from the specific time stamp from metric collection.
 */
@RestController
public class WeightTrackerController {
	
	private final Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * variable to hold the base weight. It helps in identify the percentage difference in weight.
	 */
	public static int baseWeight = 0;
	
	@Autowired
	MetricService metricService;
	
	/**
	 * Method to create data in metrics collection
	 * MethodType: POST
	 * @return HttpStatus
	 */
	@RequestMapping(value="/create", method=RequestMethod.POST)
	private ResponseEntity<HttpStatus> create(@RequestBody String inputData) {
		String METHOD_NAME = this.getClass()+ ".createMetrics:";
		Optional<String> dataHelper = Optional.ofNullable(inputData);
		ResponseEntity<HttpStatus> responseEntity = null;
		if(dataHelper.isPresent()) {
			try {
				if(baseWeight == 0) {
					JSONObject jsonObject = new JSONObject(inputData);
					baseWeight = Integer.parseInt(jsonObject.get("value").toString());
				}
				metricService.create(inputData,baseWeight);
				responseEntity = new ResponseEntity<HttpStatus>(HttpStatus.OK);
			} catch (Exception e) {
				log.error("Exception in "+METHOD_NAME+e.getMessage());
				responseEntity = new ResponseEntity<HttpStatus>(HttpStatus.EXPECTATION_FAILED);
			}
		}
		return responseEntity;
	}
	
	/**
	 * Method to return all the data's from metrics collection
	 * MethodType: POST
	 * @return List<Metrics>
	 */
	@RequestMapping(value="/readMetrics", method=RequestMethod.POST)
	private ResponseEntity<List<DBObject>> read() {
		String METHOD_NAME = this.getClass()+ ".read:";
		List<DBObject> metricList = null;
		ResponseEntity<List<DBObject>> responseEntity = null;
		try {
			metricList = metricService.read();
			responseEntity = new ResponseEntity<List<DBObject>>(metricList, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Exception in "+METHOD_NAME+e.getMessage());
			responseEntity = new ResponseEntity<List<DBObject>>(metricList, HttpStatus.EXPECTATION_FAILED);
		}
		return responseEntity;
	}
	
	/**
	 * Method to return data from metrics collection or the given time stamp
	 * MethodType: POST
	 * @param timeRange -> Required JSON input with keys {fromTimeStamp and toTimeStamp}
	 * @return List<Metrics> for  given timeStamp
	 */
	@RequestMapping(value="/readMetricsByTimeRange", method=RequestMethod.POST)
	private ResponseEntity<List<DBObject>> readByTimeRange(@RequestBody String timeRange) {
		String METHOD_NAME = this.getClass()+ ".readByTimeRange:";
		ResponseEntity<List<DBObject>> responseEntity = null;
		List<DBObject> metricList = null;
		try {
			metricList = metricService.readByTimeRange(timeRange);
			responseEntity = new ResponseEntity<List<DBObject>>(metricList, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Exception in "+METHOD_NAME+e.getMessage());
			responseEntity = new ResponseEntity<List<DBObject>>(metricList, HttpStatus.EXPECTATION_FAILED);
		}
		return responseEntity;
	}
}

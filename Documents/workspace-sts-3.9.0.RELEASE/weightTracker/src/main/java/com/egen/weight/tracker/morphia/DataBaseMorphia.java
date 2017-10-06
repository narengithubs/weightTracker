package com.egen.weight.tracker.morphia;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Class to connect with mongodb. Its helps to create data store and collections.
 * Also, its return instance of data store and collections.
 * @author Narayanan
 *
 */
public class DataBaseMorphia {
	
	private final Logger log = Logger.getLogger(this.getClass());
	
	private final static String DB_NAME = "tracker";
	private final static String COLLECTION_METRICS = "metrics";
	private final static String COLLECTION_ALERTS = "alerts";

    private static MongoClient mongoClient; 
    private static Map<String, MongoDatabase> databases = new HashMap<String, MongoDatabase>();
    private static Datastore ds = null;
    private static Map<String, MongoCollection<Document>> collectionMap = new HashMap<String, MongoCollection<Document>>(); 
    private static final Morphia morphia = new Morphia(); 
    private static DataBaseMorphia instance;
    
    public static Map<String, MongoCollection<Document>> getCollectionMap() {
		return collectionMap;
	}

	public static void setCollectionMap(Map<String, MongoCollection<Document>> collectionMap) {
		DataBaseMorphia.collectionMap = collectionMap;
	}

	public DataBaseMorphia() { 
        try { 
            if (mongoClient == null) 
                mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017")); 
        } catch (Exception e) { 
            throw new RuntimeException(e); 
        } 
    } 

 
    public synchronized static void setup() { 
        if (instance == null) 
            instance = new DataBaseMorphia(); 
    } 
 
    public static void tearDown() { 
        mongoClient.close(); 
    } 
 
    /**
     * Its the main mongodb method to create datastore, collection if its not exist already.
     * If datastore/collection exist, then its return the existing instance of its.
     * @return Datastore
     */
    public static Datastore getDataStore() { 
        if (!databases.containsKey(DB_NAME)) {
        	setup();
            MongoDatabase db = mongoClient.getDatabase(DB_NAME);
            databases.put(DB_NAME, db);
            ds = morphia.createDatastore(mongoClient, db.getName()); 
            morphia.mapPackage("com.egen.weight.tracker.morphia");
            MongoCollection<Document> metricCollection = db.getCollection(COLLECTION_METRICS);
            Optional<MongoCollection<Document>> metricOption = Optional.ofNullable(db.getCollection(COLLECTION_METRICS));
            if(!metricOption.isPresent()) {
            	db.createCollection(COLLECTION_METRICS);
            	collectionMap.put(COLLECTION_METRICS, metricCollection);
            }
            MongoCollection<Document> alertCollection = db.getCollection(COLLECTION_ALERTS);
            Optional<MongoCollection<Document>> alertOption = Optional.ofNullable(db.getCollection(COLLECTION_ALERTS));
            if(!alertOption.isPresent()) {
            	db.createCollection(COLLECTION_ALERTS);
            	collectionMap.put(COLLECTION_ALERTS, alertCollection);
            }
            ds.ensureCaps(); 
            ds.ensureIndexes(); 
        } 
        return ds; 
    }
}

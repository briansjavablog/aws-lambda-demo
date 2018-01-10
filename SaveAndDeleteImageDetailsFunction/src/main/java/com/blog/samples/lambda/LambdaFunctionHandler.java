package com.blog.samples.lambda;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.blog.samples.lambda.model.EnumEventName;
import com.blog.samples.lambda.model.ImageData;

/**
 * Function handles 2 types of event from S3. 
 * 1. Item Added Event - a new file is added to the S3 bucket
 * 2. Item Deleted Event - a file is deleted from the S3 bucket 
 * 
 * The function looks at the S3Event meta data passed in
 * and decides what kind of event it is dealing with. The 
 * appropriate event handler method is then called to process
 * the event
 * 
 * @author brianh
 */
public class LambdaFunctionHandler implements RequestHandler<S3Event, Void> {
	
    private String DYNAMODB_TABLE_NAME = "ImageDetails";
    private Regions REGION = Regions.EU_WEST_1;
    
    private AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
											   		 .withRegion(REGION)
											   		 .build();
    
    private AmazonDynamoDB dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
											   	   					   .withRegion(REGION)
											   	   					   .build();
    
    @Override
    public Void handleRequest(S3Event event, Context context) {
        
    	context.getLogger().log("Received S3Event: " + event.toJson());
    	
    	/* use name of event to construct EnumEventName */
    	EnumEventName eventName = EnumEventName.valueOf(event.getRecords().get(0).getS3().getConfigurationId());    	
    	
        /* Get S3 bucket and key from the supplied event */
        String bucket = event.getRecords().get(0).getS3().getBucket().getName();
        String key = event.getRecords().get(0).getS3().getObject().getKey();
        
        try {
        	
        	if(eventName.equals(EnumEventName.ItemAddedEvent)){
        		
        		context.getLogger().log(String.format("Processing ItemAdded Event for bucket[%s] and key[%s]", bucket, key));
        		handleCreateItemEvent(context, bucket, key);
        	}        	
        	else if(eventName.equals(EnumEventName.ItemDeletedEvent)){
        		
        		context.getLogger().log(String.format("Processing ItemDeleted Event for bucket[%s] and key[%s]", bucket, key));
        		handleDeleteItemEvent(context, bucket, key);
        	}      
        	else{
        		throw new RuntimeException("Unable to process unexpected event type");
        	}
        } catch (Exception ex) {
            
        	context.getLogger().log("Error ocurred processing request");
            throw ex;
        }
        
		return null;
    }
    
    
    private void handleCreateItemEvent(Context context, String bucket, String key){
    	
    	/* call S3 to retrieve object that triggered event */
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucket, key));
        
        /* get required file data from S3Object */        
        String name = s3Object.getKey();
        String contentType = s3Object.getObjectMetadata().getContentType();            
        String s3Uri = s3Object.getObjectContent().getHttpRequest().getURI().toString();
        Long sizeBytes = (Long)s3Object.getObjectMetadata().getRawMetadataValue("Content-Length");
        String lastModified = formatDate((Date)s3Object.getObjectMetadata().getRawMetadataValue("Last-Modified"));
        
        /* build up ImageData object to encapsulate data we want to save to dynamo */
        ImageData imageData = new ImageData(bucket, name, contentType, s3Uri, sizeBytes, lastModified);
        
        context.getLogger().log(imageData.toString());
        
        /* create request object for save to Dynamo */
        PutItemRequest putItemRequest = new PutItemRequest();
        putItemRequest.setTableName(DYNAMODB_TABLE_NAME);            
        putItemRequest.addItemEntry("bucket",new AttributeValue(bucket));
        putItemRequest.addItemEntry("name", new AttributeValue(imageData.getName()));
        putItemRequest.addItemEntry("s3Url", new AttributeValue(imageData.getS3Uri()));
        putItemRequest.addItemEntry("contentType", new AttributeValue(imageData.getContentType()));
        putItemRequest.addItemEntry("size", new AttributeValue(String.valueOf(imageData.getSizeBytes())));
        putItemRequest.addItemEntry("lastModified", new AttributeValue(String.valueOf(imageData.getLastModified())));
        
        /* save data to DynamoDB */
        PutItemResult putItemResult = dynamoDbClient.putItem(putItemRequest);
        
        context.getLogger().log(putItemResult.toString());
    }
    
    
    private void handleDeleteItemEvent(Context context, String bucket, String key){
    
    	String s3Url = buildS3Url(bucket, key);
    	
		DeleteItemRequest deleteItemRequest = new DeleteItemRequest();
		deleteItemRequest.setTableName(DYNAMODB_TABLE_NAME);
		deleteItemRequest.addKeyEntry("s3Url", new AttributeValue(s3Url));
		
    	/* delete from DynamoDB */
    	DeleteItemResult deleteItemResult = dynamoDbClient.deleteItem(deleteItemRequest);
    	
    	context.getLogger().log(deleteItemResult.toString());
    }
    
    
    private String buildS3Url(String bucket, String key){
    	
    	return new StringBuffer()
    					.append("https://")
    					.append(bucket)
    					.append(".s3-")
    					.append(REGION.getName())
    					.append(".amazonaws.com/")
    					.append(key)    					
    					.toString();  	
    }
    
    private String formatDate(Date date){
    	
    	LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();    	
    	return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));  	
    }
    
}
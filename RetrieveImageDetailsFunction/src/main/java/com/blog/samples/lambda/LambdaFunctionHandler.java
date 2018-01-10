package com.blog.samples.lambda;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.blog.samples.lambda.model.ImageData;

public class LambdaFunctionHandler implements RequestHandler<Object, List<ImageData>> {

	private String DYNAMODB_TABLE_NAME = "ImageDetails";
    private Regions REGION = Regions.EU_WEST_1;    
    private AmazonDynamoDB dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
											   	   					   .withRegion(REGION)
											   	   					   .build();
		
    @Override
    public List<ImageData> handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);
        
        /* create scan request object for retrieving results from Dynamo */        
        ScanRequest scanRequest = new ScanRequest()
					        		.withTableName(DYNAMODB_TABLE_NAME)
					        		.withLimit(100);
        
        ScanResult scanResult = dynamoDbClient.scan(scanRequest);
       
        context.getLogger().log("ScanResult: " + scanResult.toString());
        
        /* convert scan results to a List of images */
        List<ImageData> images = scanResult.getItems()
        								   .stream()
        								   .map(map ->{        	             						 
        		 return new ImageData()
        			 .setBucket(getValueByKey(map, "bucket"))
        			 .setName(getValueByKey(map, "name"))
        			 .setS3Uri(getValueByKey(map, "s3Url"))
        			 .setContentType(getValueByKey(map, "contentType"))
        			 .setSizeBytes(Long.valueOf(getValueByKey(map, "size"))) 
        			 .setLastModified(getValueByKey(map, "lastModified"));        	        
        		 
        }).collect(Collectors.toList());
        
        context.getLogger().log("Returning Images: " + images);
        return images;
    }

    
    private String getValueByKey(Map<String, AttributeValue> map, String key){
    	
    	AttributeValue attrValue = map.get(key);
    	return attrValue.getS();
    }
}

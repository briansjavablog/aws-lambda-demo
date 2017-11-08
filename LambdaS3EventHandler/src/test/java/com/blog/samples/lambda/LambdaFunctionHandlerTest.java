package com.blog.samples.lambda;

import java.io.IOException;

import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.blog.samples.lambda.LambdaFunctionHandler;

/**
 * A simple test harness for locally invoking the Lambda function.
 * These tests use a mocked S3Event source (read from JSON test resource)
 * but use a real DynamoDB client to interact with an actual DynamoDB table
 */
public class LambdaFunctionHandlerTest {


    private Context createContext() {
        TestContext ctx = new TestContext();
        return ctx;
    }

    @Test
    public void testS3AddItemEventHandler() throws IOException {
        
    	S3Event s3DeleteEvent = TestUtils.parse("/s3-event.put.json", S3Event.class);
    	LambdaFunctionHandler handler = new LambdaFunctionHandler();        
        handler.handleRequest(s3DeleteEvent, createContext());        
    }
    
    @Test
    public void testS3DeleteItemEventHandler() throws IOException {
        
    	S3Event s3DeleteEvent = TestUtils.parse("/s3-event.delete.json", S3Event.class);
    	LambdaFunctionHandler handler = new LambdaFunctionHandler();        
        handler.handleRequest(s3DeleteEvent, createContext());        
    }
}

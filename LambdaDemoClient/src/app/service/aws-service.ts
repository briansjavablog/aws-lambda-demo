import { Component, OnInit, Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';
import * as AWS from 'aws-sdk';

@Injectable()
export class AwsService {

	private s3Client: AWS.S3;
  private apiGatewayUrl: string;
  private s3BucketName: string;

	constructor (private http: Http){	}

  public getS3Client(): AWS.S3 {
    return this.s3Client;
  }
  
  public retrieveImageDetails(): Observable<Response> {

    return this.http.get(this.apiGatewayUrl)
      .map(
        (response: Response)=>{   
             console.log(response.json());  
             return response.json();
        }
      )          
  }

  public getS3BucketName(): string{
    return this.s3BucketName;
  }

  public configureS3Client(accessKeyId: string, secretAccessKey: string,
                           apiGatewayUrl: string, s3BucketName: string,
                           awsRegion: string){

    AWS.config.accessKeyId = accessKeyId;
    AWS.config.secretAccessKey = secretAccessKey;
    this.apiGatewayUrl = apiGatewayUrl;
    this.s3BucketName = s3BucketName;
    AWS.config.region = awsRegion;

    this.s3Client = new AWS.S3({ params: { Bucket: this.s3BucketName }});
  }

}

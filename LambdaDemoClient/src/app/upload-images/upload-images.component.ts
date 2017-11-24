import { Component, OnInit } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Subject } from 'rxjs/Subject';
import { AwsService } from '../service/aws-service';
import 'rxjs/Rx';
import * as AWS from 'aws-sdk';

@Component({
  selector: 'app-upload-images',
  templateUrl: './upload-images.component.html',
  styleUrls: ['./upload-images.component.css']
})
export class UploadImagesComponent {

  private file:any;
  private uploading: boolean = false;
  private deleting: boolean = false;
  private imageDetails: any;

  constructor(private awsService: AwsService) { }

  uploadfile():any {
    
    this.uploading = true;         
    var params = { Key: this.file.name, 
                   Body: this.file, 
                   Bucket: this.awsService.getS3BucketName(),
                   ACL: 'public-read' };
    
    this.awsService.getS3Client()
            .upload(params, 
                     (err, data)=>{            
                      if(data){
                        console.log(data);
                      }
                      else if (err) {
                        console.log(err);
                      }
                      this.uploading = false; 
                  });
  }

  fileEvent(fileInput: any){ 
  	// var files = fileInput.srcElement.files; 
  	// var file = fileInput.srcElement.files[0]; 
  	this.file = fileInput.srcElement.files[0];// file; 
  } 

  private uploadingImage(): boolean {
    return this.uploading;
  }

}

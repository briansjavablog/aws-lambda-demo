import { Component, OnInit } from '@angular/core';
import { AwsService } from '../service/aws-service';
import {Observable} from 'rxjs/Rx';

@Component({
  selector: 'app-display-image-details',
  templateUrl: './display-image-details.component.html',
  styleUrls: ['./display-image-details.component.css']
})
export class DisplayImageDetailsComponent implements OnInit {

  private imageDetails: any;
  private deletingImageName:string;

  constructor(private awsService: AwsService) { }

  ngOnInit() {
    /* call API Gateway endpoint every 3 seconds to 
       get latest image details */
  	Observable.interval(3000)
                .switchMap(() => {
                	return this.awsService.retrieveImageDetails();
             	  }).subscribe((responseData: any) => {
             				this.imageDetails = responseData;           			
          			},
          			(err) => {
          				console.log(err);
          			});	
  }

  private deleteImage(imageDetail){

  	this.deletingImageName = imageDetail.name;
  	
  	var params = {Key: imageDetail.name, Bucket: this.awsService.getS3BucketName() };
      
      this.awsService
      		.getS3Client()
      			.deleteObject(params, (err, data)=>{
			          if(data){
			            console.log(data);              
			          }
			          else if (err) {
			            console.log(err);
			          }
			           this.deletingImageName = null;
			      });
  }
}

import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AwsService } from '../service/aws-service'; 

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

	awsDetailsForm: FormGroup;
	accessKeyId: string;
	secretAccessKeyId: string;
	apiGatewayUrl: string;
	awsRegion: string;
	s3BucketName: string;

	constructor(private router: Router, private awsService: AwsService) { }

	ngOnInit() {
		this.setupForm();
	}

	private setupForm(){
		this.awsDetailsForm = new FormGroup({
			'accessKeyId': new FormControl(this.accessKeyId, Validators.required),
			'secretAccessKeyId': new FormControl(this.secretAccessKeyId, Validators.required),
			'apiGatewayUrl': new FormControl(this.apiGatewayUrl, Validators.required),
			's3BucketName': new FormControl(this.s3BucketName, Validators.required),
			'awsRegion': new FormControl(this.awsRegion, Validators.required)
		});	
	}
	
	onSubmit(){

		this.awsService.configureS3Client(this.awsDetailsForm.controls.accessKeyId.value,
										  this.awsDetailsForm.controls.secretAccessKeyId.value,
		                                  this.awsDetailsForm.controls.apiGatewayUrl.value,
		                                  this.awsDetailsForm.controls.s3BucketName.value,
		                                  this.awsDetailsForm.controls.awsRegion.value);
		this.router.navigate(['upload-images']);		
	}

}

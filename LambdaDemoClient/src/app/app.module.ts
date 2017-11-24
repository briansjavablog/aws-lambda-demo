import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AwsService } from './service/aws-service';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './home/home.component';
import { UploadImagesComponent } from './upload-images/upload-images.component';
import { DisplayImageDetailsComponent } from './display-image-details/display-image-details.component';
import { ConvertToKbPipe } from './pipe/convert-to-kb.pipe';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    UploadImagesComponent,
    DisplayImageDetailsComponent,
    ConvertToKbPipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    AppRoutingModule
  ],
  providers: [ AwsService ],
  bootstrap: [AppComponent]
})
export class AppModule { }

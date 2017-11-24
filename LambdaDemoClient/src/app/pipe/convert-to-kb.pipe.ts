import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'convertToKb'
})
export class ConvertToKbPipe implements PipeTransform {

  transform(value: any): any {
    const kbVal = value * 0.001; 
    return Math.round(kbVal * 100) / 100 + 'KB';    
  }

}

import {Component, OnInit} from '@angular/core';
import {Observable, ReplaySubject} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{


  constructor(private http: HttpClient) {

  }


  //image
  base64Output: string = '';

  ngOnInit(): void {
  }

  onFileSelected(e: any) {
    console.log(e);
    this.convertFile(e.event.target.files[0]).subscribe(base64 => {
      console.log('converted file to base64: ');
      this.base64Output = base64;
      let promo = {
        id: 1,
        text: 'sdljfhsdjkhfajksdhfasjdf',
        fileName: 'ldkfjkldsjfs',
        fileContent: base64
      };

      this.http.post('http://localhost:8080/api/documents/pushFile', promo).subscribe((res) => {
        console.log('response from server: ', res);

      });
    });
  }

  convertFile(file : File) : Observable<string> {
    const result = new ReplaySubject<string>(1);
    const reader = new FileReader();
    reader.readAsBinaryString(file);
    reader.onload = (event) => result.next(btoa(event!.target!.result!.toString()));
    return result;
  }


}

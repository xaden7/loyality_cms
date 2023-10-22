import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PromotionsService {

  constructor(private http: HttpClient) { }

  getPromotions(): Observable<any> {
    return this.http.get('http://localhost:8080/api/promotions/get-all');
  }

  getPromotionById(id: number): Observable<any> {
    return this.http.get(`http://localhost:8080/api/promotions/get-details-by-promotion-id?promotionId=${id}`);
  }
}

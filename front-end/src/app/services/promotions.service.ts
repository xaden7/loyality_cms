import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {Promotion} from "../models/promotion";

@Injectable({
  providedIn: 'root'
})
export class PromotionsService {

  currentPromotion: BehaviorSubject<Promotion> = new BehaviorSubject<Promotion>(<Promotion>{});

  constructor(private http: HttpClient) { }


  getPromotions(): Observable<any> {
    return this.http.get('http://localhost:8080/api/promotions/get-all');
  }

  getPromotionById(id: number): Observable<any> {
    return this.http.get(`http://localhost:8080/api/promotions/get-by-id?id=${id}`);
  }

  setCurrentPromotion(promotion: Promotion) {
    this.currentPromotion.next(promotion);
  }

  getCurrentPromotion(): Observable<Promotion> {
    return this.currentPromotion.asObservable();
  }

  savePromotion(savedPromotion: Promotion) {
    return this.http.post('http://localhost:8080/api/promotions/new-promotion', savedPromotion);
  }
}

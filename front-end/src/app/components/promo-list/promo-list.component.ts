import {Component, OnInit} from '@angular/core';
import {PromotionsService} from "../../services/promotions.service";
import {Promotion} from "../../models/promotion";

@Component({
  selector: 'app-promo-list',
  templateUrl: './promo-list.component.html',
  styleUrls: ['./promo-list.component.css']
})
export class PromoListComponent implements OnInit {
  promotions: Promotion[] = [];

  constructor(private promoService: PromotionsService) {

  }

  ngOnInit(): void {
    this.promoService.getPromotions().subscribe((res) => {
      this.promotions = res;
    });
  }

}

import {Component, OnInit} from '@angular/core';
import {PromotionsService} from "../../services/promotions.service";
import {Promotion} from "../../models/promotion";
import {Router} from "@angular/router";

@Component({
  selector: 'app-promo-list',
  templateUrl: './promo-list.component.html',
  styleUrls: ['./promo-list.component.css']
})
export class PromoListComponent implements OnInit {
  promotions: Promotion[] = [];

  constructor(private promoService: PromotionsService,
              private router: Router) {

  }

  ngOnInit(): void {
    this.promoService.getPromotions().subscribe((res) => {
      this.promotions = res;
    });
  }

  openDetails(promo: Promotion) {
    this.promoService.setCurrentPromotion(promo);
    this.router.navigateByUrl('/promotions/' + promo.id);
  }
}

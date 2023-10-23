import {Component, OnInit} from '@angular/core';
import {PromotionsService} from "../../services/promotions.service";
import {ActivatedRoute} from "@angular/router";
import {Promotion} from "../../models/promotion";
import {FormArray, FormBuilder, FormControl, FormGroup} from "@angular/forms";



@Component({
    selector: 'app-promo-details',
    templateUrl: './promo-details.component.html',
    styleUrls: ['./promo-details.component.css']
})
export class PromoDetailsComponent implements OnInit {

    currentPromotion: Promotion = <Promotion>{};
    currentPromotionId = null;
    mode = 'create';
    mainForm = this.fb.group({
        name: [''],
        description: [''],
        range: new FormGroup({
            start: new FormControl<Date | null>(null),
            end: new FormControl<Date | null>(null),
        }),
        upToDiscount: [''],
        upToBonus: [''],
        image: [''],
        productsArray: this.fb.array([
            this.fb.group({
                name: [''],
                description: [''],
                price: [''],
                image: [''],
            })
            ])
    });

    get productsControls() {
        return (this.mainForm.get('productsArray') as FormArray).controls;
    }

    constructor(private route: ActivatedRoute, private promoService: PromotionsService, private fb: FormBuilder) {

    }

    ngOnInit(): void {
        if (this.route.snapshot.params['id']) {
            // this.route.params.subscribe(params => {
            //     this.currentPromotionId = params['id'];
            //     this.promoService.getPromotionById(this.currentPromotionId!).subscribe((res) => {
            //         this.currentPromotion = res;
            this.promoService.getCurrentPromotion().subscribe((res) => {
                    this.currentPromotion = res;
                    console.log(this.currentPromotion);
                    this.mode = 'edit';
                    this.fillForm();
                });
        } else {
            this.mode = 'create';
        }
    }

    fillForm() {
        this.mainForm.reset(
            {
                name: this.currentPromotion.name,
                description: this.currentPromotion.description,
                range: {
                    start: this.currentPromotion.startDate,
                    end: this.currentPromotion.endDate,
                },
            }
        )
    }

}

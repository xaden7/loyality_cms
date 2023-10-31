import {Component, OnInit} from '@angular/core';
import {PromotionsService} from "../../services/promotions.service";
import {ActivatedRoute} from "@angular/router";
import {Promotion} from "../../models/promotion";
import {AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {DomSanitizer} from "@angular/platform-browser";
import {Observable, ReplaySubject} from "rxjs";


@Component({
    selector: 'app-promo-details',
    templateUrl: './promo-details.component.html',
    styleUrls: ['./promo-details.component.css']
})
export class PromoDetailsComponent implements OnInit {

    imagePath: any;

    currentPromotion: Promotion = <Promotion>{};
    currentPromotionId = null;
    mode = 'create';
    mainForm = this.fb.group({
        name: new FormControl<string>('', Validators.required),
        description: new FormControl<string>('', Validators.required),
        range: new FormGroup({
            start: new FormControl<Date>(//start of next month
                new Date(new Date().getFullYear(), new Date().getMonth() + 1, 1),
                Validators.required),
            //end of next month
            end: new FormControl<Date>(
                new Date(new Date().getFullYear(), new Date().getMonth() + 2, 0),
                Validators.required),
        }),
        upToDiscount: new FormControl<number>(0),
        upToBonus: new FormControl<number>(0),
        image: [''],
        productsArray: this.fb.array([]),
    });



    get productsControls() {
        return (this.mainForm.get('productsArray') as FormArray);
    }

    constructor(private route: ActivatedRoute,
                private promoService: PromotionsService,
                private fb: FormBuilder,
                private _sanitizer: DomSanitizer) {

    }

    ngOnInit(): void {
        if (this.route.snapshot.params['id']) {
            this.route.params.subscribe(params => {
                this.currentPromotionId = params['id'];
                this.promoService.getPromotionById(this.currentPromotionId!).subscribe((res) => {
                    this.currentPromotion = res;
                    console.log(this.currentPromotion);
                    this.mode = 'edit';
                    this.fillForm();
                })
            });
        } else {
            this.mode = 'create';
        }

        console.log('mode', this.mode);
    }

    fillForm() {
        this.mainForm.reset(
            {
                image: undefined,
                productsArray: undefined,
                name: this.currentPromotion.name,
                description: this.currentPromotion.description,
                range: {
                    start: this.currentPromotion.startDate,
                    end: this.currentPromotion.endDate,
                },
                upToDiscount: this.currentPromotion.upToDiscount,
                upToBonus: this.currentPromotion.upToBonus,
            }
        );

        if(this.currentPromotion.promotionDetails && this.currentPromotion.promotionDetails.length >= 1) {
            this.currentPromotion.promotionDetails.forEach((product) => {
                const newProduct = this.fb.group({
                    name: product.productName,
                    price: product.productPrice,
                    image: this._sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64' + product.image),
                });
                this.productsControls.push(newProduct);
            });
        }
        console.log('products', this.productsControls);
        console.log('promotiondetails', this.currentPromotion.promotionDetails);

        this.imagePath = this._sanitizer.bypassSecurityTrustResourceUrl(this.currentPromotion.image);

    }


    addProduct() {
        const newProduct = this.fb.group({
            name: [''],
            price: [''],
            image: new FormControl<string | null>(null),
        });
        this.productsControls.push(newProduct);

        console.log('products', this.productsControls);
        console.log('mainForm', this.mainForm.value);
    }

    onMainFileSelected(event: any) {
        const file:File = event.target.files[0];
        if (file) {
            this.currentPromotion.image = '';
            this.mainForm.get('image')?.setValue('');
            this.convertFile(file).subscribe(base64 => {

               this.imagePath = this._sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + base64);

            });
        }
    }

    onProductFileSelected(event: any, control: AbstractControl<any>) {
        const file:File = event.target.files[0];

        if (file) {
            this.convertFile(file).subscribe(base64 => {
                control.patchValue({
                    image: this._sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + base64),
                })
                // this.productsControls.controls[index].get('image')?.setValue(this._sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + base64));
            });
        }
    }

    deleteMainFile() {
        this.currentPromotion.image = '';
        this.mainForm.get('image')?.setValue('');
        this.imagePath = '';
    }

    deleteProductFile(index: number) {
        this.productsControls.controls[index].get('image')?.setValue('');
        this.mainForm.get('productsArray')?.get(index.toString())?.get('image')?.setValue('');
        console.log('products', this.mainForm.get('productsArray')?.value[0]);
        console.log('mainForm', this.mainForm.value);
        console.log('products', this.productsControls);
    }

    convertFile(file : File) : Observable<string> {
        const result = new ReplaySubject<string>(1);
        const reader = new FileReader();
        reader.readAsBinaryString(file);
        reader.onload = (event) => result.next(btoa(event!.target!.result!.toString()));
        return result;
    }

    deleteProduct(index: number) {
        this.productsControls.removeAt(index);
    }

    save() {

        const savedPromotion: Promotion = {
            id: this.currentPromotionId??0,
            name: this.mainForm.get('name')!.value!,
            description: this.mainForm.get('description')!.value!,
            startDate: this.mainForm.get('range')?.get('start')!.value!,
            endDate: this.mainForm.get('range')?.get('end')!.value!,
            upToDiscount: this.mainForm.get('upToDiscount')?.value??0,
            upToBonus: this.mainForm.get('upToBonus')?.value??0,
            image: this.imagePath.changingThisBreaksApplicationSecurity,
            promotionDetails: [],
        };

        this.productsControls.controls.forEach((product) => {
            savedPromotion.promotionDetails.push({
                productName: product.get('name')?.value,
                productPrice: product.get('price')?.value,
                productDiscount: 0,
                productBonus: 0,
                imageName: '',
                imageType: '',
                image: product.get('image')?.value? product.get('image')?.value.changingThisBreaksApplicationSecurity : '',
            });
        });

         console.log('savedPromotion', savedPromotion);

        this.promoService.savePromotion(savedPromotion).subscribe((res) => {
            console.log('response from server: ', res);
        });
    }

    protected readonly String = String;

    getImage(i: number) {
        return this.productsControls.controls[i].get('image')?.value;
    }
}

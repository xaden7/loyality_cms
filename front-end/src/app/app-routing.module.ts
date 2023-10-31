import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PromoListComponent} from "./components/promo-list/promo-list.component";
import {PromoDetailsComponent} from "./components/promo-details/promo-details.component";

const routes: Routes = [

  {path: 'promotions/:id', component: PromoDetailsComponent },
  {path: 'promotions', component: PromoListComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

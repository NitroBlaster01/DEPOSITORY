import { Component, OnInit } from '@angular/core';
import { Product } from '../product';
import { User } from '../user';
import { EstoreService } from '../estore.service';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { MessageService } from '../message.service';
import { HttpClient } from '@angular/common/http';
import { elementAt } from 'rxjs';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  currentUser: User | undefined;
  products: Product[] | undefined;

  constructor( 
    private estoreService: EstoreService,
    private router: Router,
    private location: Location,
    private messageService: MessageService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const path = this.location.path();
    this.estoreService
      .getUser(Number(path.substring(path.indexOf("/") + 1, path.indexOf("/", path.indexOf("/") + 1))))
      .subscribe(async (user) => {
        var prods :Product[] = []
        this.currentUser = user
        this.currentUser?.cart.forEach((item) => {
          this.estoreService.getProduct(item.id).subscribe((i: Product) =>  {
            const id :number = i.id
            const quantity :number = item.quantity

            if (quantity != 0 && this.currentUser != null) {
              prods.push(i);
              this.estoreService.updateItemInCart(this.currentUser.id, id, Math.min(i.id, quantity)).subscribe();
            }
          })
        })
        this.products = prods
        this.currentUser = user
      });
  }

  getProductQty(id:number):number{
    var number:number = 0;
    if(this.currentUser != null){
      this.currentUser.cart.forEach((element:Product) => {
        if(element.id == id){
          if(this.products != null){
            this.products.forEach((prod :Product)=>{
              if(prod.id==element.id){
                number = Math.min(element.quantity,prod.quantity);
              }
            });
          }
        }
      });
    }
    return number;
  }

  update(id:number, qty:string):void {
    if(this.products != null) {
      this.products.forEach((prod:Product) => {
        if(this.currentUser != null && prod.id == id){ 
          this.estoreService.updateItemInCart(this.currentUser.id, id, Math.min(prod.quantity, parseInt(qty))).subscribe();
        }
      });
    }
  }

  reloadComponent() {
    let currentUrl = this.router.url;
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate([currentUrl]);
  }

  product_Total(id:number, qty:number) {
    var number = 0;

    if (this.currentUser != null) {
      this.currentUser.cart.forEach((element:Product) => {
        if (element.id == id) {
          if (this.products != null) {
            this.products.forEach((prod :Product) => {
              if (prod.id == element.id) {
                number = qty * prod.price;
              }
            });
          }
        }
      });
    }
    return number;
  }

  order_Total() {
    var total = 0;

    if(this.currentUser != null) {
      this.currentUser.cart.forEach((element:Product) => {
        total = total + (element.price * element.quantity);
      });
    }
    return total;
  }

  logOut(): void {
    this.router.navigate(['/home']);
  }

  goBack(): void {
    this.router.navigate([`${this.currentUser?.id}/checkout`]);
  }

}

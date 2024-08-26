import { Component, OnInit } from '@angular/core';
import { Product } from '../product';
import { User } from '../user';
import { EstoreService } from '../estore.service';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { MessageService } from '../message.service';
import { HttpClient } from '@angular/common/http';
import { SearchService } from '../search.service';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css'],
})
export class ShoppingCartComponent implements OnInit {
  currentUser: User | undefined;
  products: Product[] | undefined;

  constructor(
    private estoreService: EstoreService,
    private router: Router,
    private location: Location,
    private messageService: MessageService,
    private http: HttpClient,
    private searchService: SearchService
  ) {}
  ngOnInit(): void {
    const path = this.location.path();
    this.estoreService
      .getUser(
        Number(
          path.substring(
            path.indexOf('/') + 1,
            path.indexOf('/', path.indexOf('/') + 1)
          )
        )
      )
      .subscribe(async (user) => {
        var prods: Product[] = [];
        this.currentUser = user;
        this.currentUser.cart.forEach((item) => {
          this.estoreService.getProduct(item.id).subscribe((i: Product) => {
            const id: number = i.id;
            const quantity: number = item.quantity;
            if (quantity != 0 && this.currentUser != null) {
              prods.push(i);
              this.estoreService
                .updateItemInCart(
                  this.currentUser.id,
                  id,
                  Math.min(i.quantity, quantity)
                )
                .subscribe();
            }
          });
        });
        //this.estoreService.updateUserByValues(user.id,user.username,user.password,cart).subscribe();
        this.products = prods;
        this.currentUser = user;
      });
  }
  update(id: number, name: string, qty: string): void {
    if (this.products != null) {
      this.products.forEach((prod: Product) => {
        if (this.currentUser != null && prod.id == id) {
          if (parseInt(qty) == 0) {
            this.deleteItemInCart(this.currentUser.id, id);
          }
          this.estoreService
            .updateItemInCart(
              this.currentUser.id,
              id,
              Math.min(prod.quantity, parseInt(qty))
            )
            .subscribe();
        }
      });
    }
    this.reloadComponent();
  }

  deleteItemInCart(userId: number, itemId: number): void {
    this.estoreService.deleteItemInCart(userId, itemId).subscribe();
    this.reloadComponent();
  }

  getProductQty(id: number): number {
    var number: number = 0;
    if (this.currentUser != null) {
      this.currentUser.cart.forEach((element: Product) => {
        if (element.id == id) {
          if (this.products != null) {
            this.products.forEach((prod: Product) => {
              if (prod.id == element.id) {
                number = Math.min(element.quantity, prod.quantity);
              }
            });
          }
        }
      });
    }
    return number;
  }
  reloadComponent() {
    let currentUrl = this.router.url;
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate([currentUrl]);
  }
  logOut(): void {
    this.router.navigate(['/home']);
  }
  goBack(): void {
    this.router.navigate([`${this.currentUser?.id}/storefront`]);
  }
  calculateProductPrice(quantity: string, price: number): number {
    const intQuantity = parseInt(quantity);
    return intQuantity * price;
  }

  // save(id: number, name: string, p: number, q: number, imageUrl: string): void {
  //   this.estoreService
  //     .updateProduct({ id, name, p, q, imageUrl } as unknown as Product)
  //     .subscribe();
  //   this.searchService
  //     .getProducts()
  //     .subscribe((products) => (this.products = products));
  //   // this.reloadComponent();
  // }

  checkOut() {
    if (this.products && this.currentUser) {
      for (let prod of this.products) {
        this.estoreService
          .updateProduct({
            id: prod.id,
            name: prod.name,
            price: prod.price,
            quantity: prod.quantity - this.getProductQty(prod.id),
            imageUrl: prod.imageUrl,
          } as Product)
          .subscribe(() => {
            if (this.currentUser) {
              // this.searchService.getProducts().subscribe();
              this.deleteItemInCart(this.currentUser.id, prod.id);
            }
            this.reloadComponent();
          });
      }
    }
    // this.reloadComponent();
  }

  getTotalPrice(): number {
    var totalPrice = 0;
    if (this.products) {
      for (let prod of this.products) {
        const qty = this.getProductQty(prod.id);
        const price = prod.price;
        totalPrice += qty * price;
      }
    }
    return totalPrice;
  }
}

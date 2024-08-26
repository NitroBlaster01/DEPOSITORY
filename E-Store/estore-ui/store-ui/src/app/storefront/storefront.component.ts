import { Component, OnInit } from '@angular/core';
import { Product } from '../product';
import { EstoreService } from '../estore.service';
import { Router } from '@angular/router';
import { MessageService } from '../message.service';
import { Location } from '@angular/common';
import { SearchService } from '../search.service';
import { User } from '../user';
@Component({
  selector: 'app-storefront',
  templateUrl: './storefront.component.html',
  styleUrls: ['./storefront.component.css'],
})
export class StorefrontComponent implements OnInit {
  products: Product[] | undefined;
  currentUser: User | undefined;
  constructor(
    private route: Router,
    private estoreService: EstoreService,
    private messageService: MessageService,
    private location: Location,
    private searchService: SearchService
  ) {}

  ngOnInit(): void {
    const path = this.location.path();
    this.searchService
      .getProducts()
      .subscribe((products) => (this.products = products));
    this.estoreService
      .getUser(
        Number(
          path.substring(
            path.indexOf('/') + 1,
            path.indexOf('/', path.indexOf('/') + 1)
          )
        )
      )
      .subscribe((user) => {
        this.currentUser = user;
      });
  }
  redirect(prodSelected: Product): void {
    const thisId = prodSelected.id;
    const url = this.location.path().substring(0, 4) + `highlight/${thisId}`;
    this.messageService.add(`StorefrontComponent: path is ${url}`);
    if (thisId != null && thisId != undefined) {
      this.route.navigate([`${url}`]);
      this.messageService.add(
        `StorefrontComponent: Successfully navigated to ${url}`
      );
    }
  }
  addToCart(prodSelected: Product, qty: string): void {
    if (this.currentUser != null && qty != '') {
      const id: number = prodSelected.id;
      const name: string = prodSelected.name;
      const price: number = prodSelected.price;
      const quantity: number = parseInt(qty);
      const product = { id, name, price, quantity } as Product;
      var cart: any[] = this.currentUser?.cart;
      var count: number = product.quantity;
      cart.forEach((prod) => {
        if (prod.productId == product.id) {
          count += prod.quantity;
        }
      });
      cart = cart.filter((prod) => prod.productId != product.id);
      count = Math.max(0, Math.min(count, prodSelected.quantity));
      cart.push({ productId: prodSelected.id, quantity: count });
      this.estoreService
        .updateUserByValues(
          this.currentUser.id,
          this.currentUser.username,
          this.currentUser.password,
          cart
        )
        .subscribe((user) => (this.currentUser = user));
      this.messageService.add('Current cart:');
      cart.forEach((prod) => {
        this.messageService.add(
          `id:${prod.productId}, quantity:${prod.quantity}`
        );
      });
    }
  }
  logOut(): void {
    this.route.navigate(['/home']);
  }

  accessCart(): void {
    this.route.navigate([`${this.currentUser?.id}/checkout`]);
  }
}

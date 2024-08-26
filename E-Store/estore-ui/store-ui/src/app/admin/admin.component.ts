import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from '../product';
import { EstoreService } from '../estore.service';
import { MessageService } from '../message.service';
import { Location } from '@angular/common';
import { convertUpdateArguments } from '@angular/compiler/src/compiler_util/expression_converter';
import { SearchService } from '../search.service';
@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css'],
})
export class AdminComponent implements OnInit {
  products: Product[] | undefined;
  constructor(
    private route: Router,
    private estoreService: EstoreService,
    private messageService: MessageService,
    private location: Location,
    private searchService: SearchService
  ) {}
  ngOnInit(): void {
    this.searchService
      .getProducts()
      .subscribe((products) => (this.products = products));
  }

  save(id: number, name: string, p: string, q: string, imageUrl: string): void {
    const price = parseFloat(p);
    const quantity = parseInt(q);
    this.estoreService
      .updateProduct({ id, name, price, quantity, imageUrl } as Product)
      .subscribe();
    this.searchService
      .getProducts()
      .subscribe((products) => (this.products = products));
    this.reloadComponent();
  }
  delete(id: number): void {
    this.estoreService.deleteProduct(id).subscribe();
    this.searchService
      .getProducts()
      .subscribe((products) => (this.products = products));
    window.location.reload();
  }
  add(name: string, p: string, q: string, imageUrl: string): void {
    name = name.trim();
    imageUrl = imageUrl.trim();
    const price = Number([parseInt(p).toFixed(2)]);
    const quantity = Number([parseInt(q).toFixed(0)]);
    var copy: boolean = false;
    this.products?.forEach((prod) => {
      if (name === prod.name) {
        copy = true;
      }
    });
    if (!p || !q || !name || copy) {
      return;
    }
    if (!imageUrl) {
      imageUrl =
        'https://i1.wp.com/lanecdr.org/wp-content/uploads/2019/08/placeholder.png?w=1200&ssl=1';
    }
    this.estoreService
      .addProduct({ name, price, quantity, imageUrl } as Product)
      .subscribe((product) => this.products?.push(product));
  }
  reloadComponent() {
    let currentUrl = this.route.url;
    this.route.routeReuseStrategy.shouldReuseRoute = () => false;
    this.route.onSameUrlNavigation = 'reload';
    this.route.navigate([currentUrl]);
  }

  logOut(): void {
    this.route.navigate(['/home']);
  }
}

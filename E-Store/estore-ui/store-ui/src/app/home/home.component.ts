import { Component, OnInit } from '@angular/core';
import { StorefrontComponent } from '../storefront/storefront.component';
import { Product } from '../product';
import { EstoreService } from '../estore.service';
import { Router } from '@angular/router';
import { MessageService } from '../message.service';
import { Location } from '@angular/common';
import { SearchService } from '../search.service';
import { User } from '../user';

var myModal = document.getElementById('myModal');
var myInput = document.getElementById('myInput');

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  products: Product[] | undefined;
  StorefrontComponent: any;

  constructor(
    private route: Router,
    private estoreService: EstoreService,
    private messageService: MessageService,
    private location: Location,
    private searchService: SearchService
  ) {
    if (myModal) {
      myModal.addEventListener('shown.bs.modal', function () {
        if (myInput) {
          myInput.focus();
        }
      });
    }
  }

  ngOnInit(): void {
    this.searchService
      .getProducts()
      .subscribe((products) => (this.products = products));
  }
}

import { Component, OnInit } from '@angular/core';
import { Product } from '../product';
import { EstoreService } from '../estore.service';
import { Router } from '@angular/router';
import { MessageService } from '../message.service';  
import { Location } from '@angular/common';
@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnInit {

  constructor(private route : Router,private location: Location,private messageService: MessageService) { }

  ngOnInit(): void {
  }
  redirect(query : string) {
    let currentUrl = this.route.url.split("?")[0];
    if(query != ""){
        this.route.routeReuseStrategy.shouldReuseRoute = () => false;
        this.route.onSameUrlNavigation = 'reload';
        this.route.navigate([currentUrl],{queryParams:{search:query}});
    }
    else{
        this.route.routeReuseStrategy.shouldReuseRoute = () => false;
        this.route.onSameUrlNavigation = 'reload';
        this.route.navigate([currentUrl]);
    }
  }
}

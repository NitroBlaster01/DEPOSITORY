import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { EstoreService } from './estore.service';
import { Observable, of } from 'rxjs';
import { Product } from './product';
@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(private estoreService: EstoreService,private location:Location) { }
  
  getProducts():Observable<Product[]>{
    const path = this.location.path();
    if(path.lastIndexOf("?search")!=-1){
      return this.estoreService.searchProducts(path.substring(path.lastIndexOf("?search=")+8));
    }
    return this.estoreService.getProducts();
  }

}

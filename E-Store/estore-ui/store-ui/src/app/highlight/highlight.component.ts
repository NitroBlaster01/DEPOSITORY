import { Component, OnInit } from '@angular/core';
import { Product} from '../product';
import { User} from '../user';
import { EstoreService } from '../estore.service';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-highlight',
  templateUrl: './highlight.component.html',
  styleUrls: ['./highlight.component.css']
})
export class HighlightComponent implements OnInit {
  currentProduct : Product | undefined;
  currentUser : User | undefined;
  
  constructor(private estoreService : EstoreService, private router : Router,private location : Location, private messageService: MessageService) { }

  ngOnInit(): void {
    const path = this.location.path();
    this.estoreService.getProduct(Number(path.substring(path.lastIndexOf("/")+1))).subscribe(currProduct => this.currentProduct = currProduct);
    this.estoreService
    .getUser(Number(path.substring(path.indexOf("/")+1,path.indexOf("/",path.indexOf("/")+1))))
    .subscribe((user) => {
      this.currentUser = user;
    });
  }

}

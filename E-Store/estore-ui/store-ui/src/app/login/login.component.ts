import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EstoreService } from '../estore.service';
import { User } from '../user';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  user: User | undefined;
  alert: string | undefined;

  constructor(
    private route: Router,
    private estoreService: EstoreService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {}

  logIn(username: string, password: string): void {
    username = username.trim();
    password = password.trim();

    // Login as admin, no need to check password
    if (username === 'admin' && password==='admin') {
      this.route.navigate(['/admin']);
      this.messageService.add('successfully logged in as admin');
    }
    // Login as normal user
    else {
      this.estoreService
        .getCurrentUser({ username, password } as User)
        .subscribe((user) => {
          this.user = user;
          const thisId = this.user?.id;
          if (thisId !== null && thisId !== undefined) {
            this.route.navigate([`/${thisId}/storefront`]);
          }
          else{
            this.alert = "Wrong username or password"
          }
        });
    }
  }
}

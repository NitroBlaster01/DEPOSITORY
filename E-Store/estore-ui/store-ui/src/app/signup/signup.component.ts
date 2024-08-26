import { Component, OnInit } from '@angular/core';
// import { Location } from '@angular/common';
import { EstoreService } from '../estore.service';
import { Router } from '@angular/router';

import { User } from '../user';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent implements OnInit {
  users: User[] = [];
  alert: string | undefined;
  success: boolean = false;

  constructor(
    // private location: Location,
    private estoreService: EstoreService,
    private route: Router
  ) {}

  goBack(): void {
    // this.location.back();
    this.route.navigate(['/login']);
  }

  ngOnInit(): void {}

  /**
   * register a new user with [] cart
   * @param username username, have to match with api field name
   * @param password userpassword, have to match with api field name
   * @returns 409 and do nothing if username already registered, 200 if successful
   */
  signUp(username: string, password: string): void {
    username = username.trim();
    password = password.trim();
    if (!username || !password) {
      this.alert = 'Username and Password must be filled.';
      this.success = false;
      return;
    }
    if(username==='admin'){
      this.alert = 'Cannot use admin username.';
      this.success = false;
      return;
    }
    this.estoreService
      .addUser({ username, password } as User)
      .subscribe((user) => {
        this.users.push(user);
        this.alert = undefined;
        this.success = true;
        //Check http status
        if (user === undefined) {
          this.success = false;
          this.alert = 'Username already exists';
        }
      });
  }
}

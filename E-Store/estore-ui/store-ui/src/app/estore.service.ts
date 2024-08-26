import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { User } from './user';
import { Product } from './product';
import { MessageService } from './message.service';

@Injectable({
  providedIn: 'root',
})
export class EstoreService {
  private estoreUrl = 'http://localhost:8080';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService
  ) {}

  // /**
  //  * Create a new user with [] cart
  //  * @param user user with username and password
  //  * @returns a new user with [] cart
  //  */
  // addUser(user: User): Observable<User> {
  //   const url = `${this.estoreUrl}/users`;
  //   var cart: any[] = [];
  //   var u: User = {
  //     id: user.id,
  //     username: user.username,
  //     password: user.password,
  //     cart: cart,
  //   } as User;
  //   return this.http.post<User>(url, u, this.httpOptions).pipe(
  //     tap((newUser: User) => this.log(`added User w/ id=${newUser.id}`)),
  //     catchError(this.handleError<User>('addUser'))
  //   );
  // }
  /**
   * Create a new user with [] cart
   * @param user user with username and password
   * @returns a new user with [] cart
   */
  addUser(user: User): Observable<User> {
    const url = `${this.estoreUrl}/users`;
    return this.http.post<User>(url, user, this.httpOptions).pipe(
      tap((newUser: User) => this.log(`added User w/ id=${newUser.id}`)),
      catchError(this.handleError<User>('addUser'))
    );
  }

  getCurrentUser(user: User): Observable<User> {
    const url = `${this.estoreUrl}/users/authorization/${user.username}/${user.password}`;
    return this.http.get<User>(url).pipe(
      tap((_) => this.log(`fetched User username=${user.username}`)),
      catchError(this.handleError<User>(`getUser username=${user.username}`))
    );
  }

  getUser(id: number): Observable<User> {
    const url = `${this.estoreUrl}/users/${id}`;
    return this.http.get<User>(url).pipe(
      tap((_) => this.log(`fetched User with id = ${id}`)),
      catchError(this.handleError<User>(`getUser id= ${id}`))
    );
  }

  getCurrentUserNo404(user: User): Observable<User> {
    const url = `${this.estoreUrl}/users/authorization/?user.username=${user.username}/?user.userpassword=${user.password}`;
    return this.http.get<User[]>(url).pipe(
      map((Users) => Users[0]), // returns a {0|1} element array
      tap((h) => {
        const outcome = h ? 'fetched' : 'did not find';
        this.log(`${outcome} User username=${user.username}`);
      }),
      catchError(this.handleError<User>(`getUser username=${user.username}`))
    );
  }

  /** GET Products from the server */
  getProducts(): Observable<Product[]> {
    const url = `${this.estoreUrl}/products`;
    return this.http.get<Product[]>(url).pipe(
      tap((_) => this.log('fetched Products')),
      catchError(this.handleError<Product[]>('getProducts', []))
    );
  }

  /** GET Product by id. Return `undefined` when id not found */
  getProductNo404<Data>(id: number): Observable<Product> {
    const url = `${this.estoreUrl}/products/?id=${id}`;
    return this.http.get<Product[]>(url).pipe(
      map((Products) => Products[0]), // returns a {0|1} element array
      tap((h) => {
        const outcome = h ? 'fetched' : 'did not find';
        this.log(`${outcome} Product id=${id}`);
      }),
      catchError(this.handleError<Product>(`getProduct id=${id}`))
    );
  }

  /** GET Product by id. Will 404 if id not found */
  getProduct(id: number): Observable<Product> {
    const url = `${this.estoreUrl}/products/${id}`;
    return this.http.get<Product>(url).pipe(
      tap((_) => this.log(`fetched Product id=${id}`)),
      catchError(this.handleError<Product>(`getProduct id=${id}`))
    );
  }

  /* GET Products whose name contains search term */
  searchProducts(term: string): Observable<Product[]> {
    if (!term.trim()) {
      // if not search term, return empty Product array.
      return of([]);
    }
    return this.http
      .get<Product[]>(`${this.estoreUrl}/products/?name=${term}`)
      .pipe(
        tap((x) =>
          x.length
            ? this.log(`found Products matching "${term}"`)
            : this.log(`no Products matching "${term}"`)
        ),
        catchError(this.handleError<Product[]>('searchProducts', []))
      );
  }

  //////// Save methods //////////

  /** POST: add a new Product to the server */
  addProduct(product: Product): Observable<Product> {
    const url = `${this.estoreUrl}/products`;
    return this.http.post<Product>(url, product, this.httpOptions).pipe(
      tap((newProduct: Product) =>
        this.log(`added Product w/ id=${newProduct.id}`)
      ),
      catchError(this.handleError<Product>('addProduct'))
    );
  }

  /** DELETE: delete the Product from the server */
  deleteProduct(id: number): Observable<Product> {
    const url = `${this.estoreUrl}/products/${id}`;
    this.messageService.add(url);
    return this.http.delete<Product>(url, this.httpOptions).pipe(
      tap((_) => this.log(`deleted Product id=${id}`)),
      catchError(this.handleError<Product>('deleteProduct'))
    );
  }

  /** PUT: update the Product on the server */
  updateProduct(product: Product): Observable<any> {
    const url = `${this.estoreUrl}/products`;
    return this.http.put(url, product, this.httpOptions).pipe(
      tap((_) => this.log(`updated Product id=${product.id}`)),
      catchError(this.handleError<any>('updateProduct'))
    );
  }

  /**
   * @author Elijah Thibodeau
   * @param id : id of user to be updated
   * @param username of user to be updated
   * @param password of user to be updated
   * @param cart of user to be updated
   * @returns the user
   * PUT: update the User on the server
   */
  updateUserByValues(
    id: number,
    username: string,
    password: string,
    cart: Array<any>
  ): Observable<any> {
    const url = `${this.estoreUrl}/users`;
    const user: User = {
      id: id,
      username: username,
      password: password,
      cart: cart,
    } as User;
    return this.http.put(url, user, this.httpOptions).pipe(
      tap((_) => this.log(`EstoreService: updated User id=${user.id}`)),
      catchError(this.handleError<any>('updateUserByValues'))
    );
  }
  /**
   * @author Elijah Thibodeau
   * @param user User to be updated
   * @returns the user
   * PUT: update the User on the server
   */
  updateUser(user: User): Observable<any> {
    const url = `${this.estoreUrl}/users`;
    return this.http.put(url, user, this.httpOptions).pipe(
      tap((_) => this.log(`updated User id=${user.id}`)),
      catchError(this.handleError<any>('updateUser'))
    );
  }

  deleteItemInCart(userId: number, itemId: number){
    const url = `${this.estoreUrl}/users/${userId}/cart/${itemId}`;
    return this.http.delete<Product>(url, this.httpOptions).pipe(
      tap((_) => this.log(`deleted itemInCart id=${itemId}`)),
      catchError(this.handleError<Product>('deleteItemInCart'))
    );
  }

  addItemInCart(userId:number, itemId:number,quantity:number){
    const url = `${this.estoreUrl}/users/${userId}/cart`
    const item = {"productId":itemId,"quantity":quantity};
    return this.http.post<User>(url,item,this.httpOptions).pipe(
      tap((_) => this.log(`added itemInCart id=${itemId}`)),
      catchError(this.handleError<User>('addItemInCart'))
    );
  }
  updateItemInCart(userId:number, itemId:number,quantity:number){
    const url = `${this.estoreUrl}/users/${userId}/cart`
    const item = {"productId":itemId,"quantity":quantity};
    return this.http.put<User>(url,item,this.httpOptions).pipe(
      tap((_) => this.log(`updated itemInCart id=${itemId}`)),
      catchError(this.handleError<User>('updateItemInCart'))
    );
  }
  // updateItemInCart(userId: number, item: ) {
  //   const url = `${this.estoreUrl}/users/${userId}/cart/${itemId}`;
  //   return this.http.put<Product>(url, this.httpOptions).pipe(
  //     tap((_) => this.log(`deleted itemInCart id=${itemId}`)),
  //     catchError(this.handleError<Product>('deleteItemInCart'))
  //   );
  // }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a EstoreService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`EstoreService: ${message}`);
  }
}

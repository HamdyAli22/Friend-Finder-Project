import { Injectable } from '@angular/core';
import {EventEmitter} from 'protractor';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseUrl = 'http://localhost:8081/auth/';
 // userLoggedIn = new EventEmitter<string>();

  constructor(private http: HttpClient) { }

  createAccount(username: string, email: string, password: string): Observable<any>{
    return this.http.post<any>(this.baseUrl + 'signup', {username, email, password}).pipe(
      map(
        response => response
      )
    );
  }

  login(email: string, password: string): Observable<any>{
    return this.http.post<any>(this.baseUrl +  'login', {email, password}).pipe(
      map(response => {
          if (response?.token) {
            localStorage.setItem('token', response.token);
            localStorage.setItem('email', response.email);
            localStorage.setItem('roles', response.roles);
            localStorage.setItem('userId', response.userId);
            localStorage.setItem('username', response.username);
      //      this.userLoggedIn.emit(email);
          }
          return response;
        }
      )
    );
  }

  logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('roles');
  }

  isUserLogin(): boolean{
    return localStorage.getItem('token') !== null &&
      localStorage.getItem('token') !== undefined;
  }
}

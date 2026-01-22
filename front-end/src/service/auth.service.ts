import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Account} from '../model/account';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseUrl = 'http://localhost:8081/auth/';
  userLoggedIn = new EventEmitter<string>();

  private profileImageSubject = new BehaviorSubject<string | null>(null);
  profileImage$ = this.profileImageSubject.asObservable();

  private usernameSubject = new BehaviorSubject<string | null>(null);
  username$ = this.usernameSubject.asObservable();


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
            this.getProfile().subscribe();
            this.userLoggedIn.emit(email);
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

  getProfile(userId?: number): Observable<Account> {
    let params = {};
    if (userId) {
      params = { id: userId };
    }

    return this.http
      .get<Account>(this.baseUrl + 'user-profile', { params })
      .pipe(
        tap(profile => {
          this.profileImageSubject.next(profile.profileImageUrl);
          this.usernameSubject.next(profile.username);
        })
      );
  }

  setProfileImage(path: string | null) {
    this.profileImageSubject.next(path);
  }

  setUsername(username: string | null) {
    this.usernameSubject.next(username);
  }
}

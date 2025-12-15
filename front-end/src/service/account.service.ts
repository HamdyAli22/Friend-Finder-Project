import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ChangePassReq} from '../model/change-pass-req';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private baseUrl = 'http://localhost:8081/account/';

  constructor(private http: HttpClient) { }

  changePassword(request: ChangePassReq): Observable<any>{
    return this.http.post<any>(this.baseUrl + 'change-password', request);
  }

  resetPassword(request: ChangePassReq): Observable<any>{
    return this.http.post<any>(this.baseUrl + 'reset-password', request);
  }

  getAllAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(this.baseUrl + 'all-accounts');
  }

  toggleAccountStatus(id: number): Observable<any> {
    return this.http.put<any>(this.baseUrl + 'toggle-account?id=' + id, {});
  }

  updateAccountDetails(email: string, details: any): Observable<Account> {
    return this.http.put<Account>(
      this.baseUrl + 'update-details?email=' + email,
      details
    );
  }
}

import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ContactInfo} from '../model/contact-info';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ContactInfoService {

  private baseUrl = 'http://localhost:8081/contact';

  constructor(private http: HttpClient) { }

  saveContact(contact: ContactInfo): Observable<ContactInfo> {
    return this.http.post<ContactInfo>(this.baseUrl + '/save-contact', contact);
  }

  getAllMessages(pageNo, pageSize): Observable<any> {
    return this.http.get(this.baseUrl + '/all-contacts?page= ' + pageNo + '&size=' + pageSize);
  }

  getMessagesByUser(email: string, pageNo, pageSize): Observable<any> {
    return this.http.get(this.baseUrl + '/user-contacts?email=' + email + '&page=' + pageNo + '&size=' + pageSize);
  }

  updateMessage(contact: ContactInfo): Observable<ContactInfo> {
    return this.http.put<ContactInfo>(this.baseUrl + '/update-message', contact);
  }

  searchMessages(keyword: string, pageNo: number, pageSize: number): Observable<any> {
    return this.http.get(this.baseUrl + '/search?keyword=' + keyword + '&page=' + pageNo + '&size=' + pageSize);
  }
}

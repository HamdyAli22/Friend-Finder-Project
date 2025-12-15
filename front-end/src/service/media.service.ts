import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserMedia} from '../model/user-media';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class MediaService {

  private baseUrl = 'http://localhost:8081/media/';

  constructor(private http: HttpClient) { }

  getMyMedia(type?: 'IMAGE' | 'VIDEO'): Observable<UserMedia[]> {

    let url = this.baseUrl + 'my-media';

    if (type) {
      url += '?type=' + type;
    }

    return this.http
      .get<UserMedia[]>(url)
      .pipe(map(response => response));
  }

}

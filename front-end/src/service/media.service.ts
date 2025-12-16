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

  getUserMedia(
    type?: 'IMAGE' | 'VIDEO',
    userId?: number
  ): Observable<UserMedia[]> {

    const params: any = {};

    if (type) {
      params.type = type;
    }

    if (userId) {
      params.userId = userId;
    }

    return this.http.get<UserMedia[]>(this.baseUrl + 'user-media', { params });
  }

}

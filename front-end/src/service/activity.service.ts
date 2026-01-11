import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Activity} from '../model/activity';

@Injectable({
  providedIn: 'root'
})
export class ActivityService {

  private baseUrl = 'http://localhost:8081/activity';

  constructor(private http: HttpClient) { }

  getActivities(userId?: number): Observable<Activity[]> {

    let url = this.baseUrl + '/all-activities';

    if (userId) {
      url += '?userId=' + userId;
    }

    return this.http.get<Activity[]>(url);
  }

}

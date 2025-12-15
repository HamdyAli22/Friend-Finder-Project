import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Friendship} from '../model/friendship';

@Injectable({
  providedIn: 'root'
})
export class FriendshipService {

  private baseUrl = 'http://localhost:8081/friendships';

  constructor(private http: HttpClient) { }

  getSuggestions(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl + '/suggestions');
  }

  sendFriendRequest(receiverId: number): Observable<Friendship> {
    return this.http.post<Friendship>(
      this.baseUrl + '/send-request?receiverId=' + receiverId,
      null
    );
  }

  acceptFriendRequest(friendshipId: number): Observable<void> {
    return this.http.post<void>(this.baseUrl + '/accept?friendshipId=' + friendshipId, null);
  }

  rejectFriendRequest(friendshipId: number): Observable<void> {
    return this.http.post<void>(this.baseUrl + '/reject?friendshipId=' + friendshipId, null);
  }

  cancelFriendRequest(friendshipId: number): Observable<void> {
    return this.http.post<void>(this.baseUrl + '/cancel?friendshipId=' + friendshipId, null);
  }

  getMyFriends(): Observable<Friendship[]> {
    return this.http.get<Friendship[]>(this.baseUrl + '/my-friends');
  }

  getPendingReceivedRequests(): Observable<Friendship[]> {
    return this.http.get<Friendship[]>(this.baseUrl + '/pending/received');
  }

  getPendingSentRequests(): Observable<Friendship[]> {
    return this.http.get<Friendship[]>(this.baseUrl + '/pending/sent');
  }
}

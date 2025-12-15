import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {forkJoin, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReactionService {

  private baseUrl = 'http://localhost:8081/reactions/';

  constructor(private http: HttpClient) { }

  react(postId: number, type: 'LIKE' | 'DISLIKE'): Observable<any> {
    return this.http.post(
      this.baseUrl + 'react?postId=' + postId + '&type=' + type,
      {}
    );
  }


  getLikesCount(postId: number): Observable<number> {
    return this.http.get<number>(this.baseUrl + 'likes?postId=' + postId);
  }

  getDislikesCount(postId: number): Observable<number> {
    return this.http.get<number>(this.baseUrl + 'dislikes?postId=' + postId);
  }

  reactAndUpdateCounts(postId: number, type: 'LIKE' | 'DISLIKE'): Observable<{likes: number, dislikes: number}> {
    return new Observable(observer => {
      this.react(postId, type).subscribe({
        next: () => {
          forkJoin({
            likes: this.getLikesCount(postId),
            dislikes: this.getDislikesCount(postId)
          }).subscribe({
            next: counts => {
              observer.next(counts);
              observer.complete();
            },
            error: err => observer.error(err)
          });
        },
        error: err => observer.error(err)
      });
    });
  }

}

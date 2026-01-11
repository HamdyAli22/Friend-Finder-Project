import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Post} from '../model/post';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private baseUrl = 'http://localhost:8081/posts/';

  constructor(private http: HttpClient) { }

  getFeed(pageNo: number, pageSize: number): Observable<any> {
    return this.http.get<Post[]>(this.baseUrl + 'feed?page=' + pageNo + '&size=' + pageSize)
      .pipe(map(response => response));
  }

  publishPost(payload: any): Observable<any> {
    return this.http.post<Post[]>(this.baseUrl + 'create-post', payload)
      .pipe(map(response => response));
  }

  updatePost(payload: any): Observable<any> {
    return this.http.put<Post[]>(this.baseUrl + 'update-post', payload)
      .pipe(map(response => response));
  }

  deletePost(postId: number): Observable<void> {
    return this.http.delete<void>(this.baseUrl + 'delete-post?id=' + postId);
  }

  getMyPosts(pageNo: number, pageSize: number): Observable<any> {
    return this.http.get<Post[]>(this.baseUrl + 'my-posts?page=' + pageNo + '&size=' + pageSize)
      .pipe(map(response => response));
  }

  getPostById(postId: number): Observable<any> {
    return this.http.get<Post[]>(this.baseUrl + 'by-id?id=' + postId)
      .pipe(map(response => response));
  }

  getUserPosts(pageNo: number, pageSize: number , userId: number): Observable<any> {
    return this.http.get<Post[]>(this.baseUrl + 'user-posts?page=' + pageNo + '&size=' + pageSize + '&userId=' + userId)
      .pipe(map(response => response));
  }

  searchPosts(key: string, pageNo: number, pageSize: number): Observable<any> {
    return this.http.get<any>(
      this.baseUrl +
      'search?keyword=' + key +
      '&page=' + pageNo +
      '&size=' + pageSize
    );
  }


}

import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Comment} from '../model/comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private baseUrl = 'http://localhost:8081/comments/';

  constructor(private http: HttpClient) { }

  getComments(postId: number, page: number, size: number): Observable<any> {
    return this.http.get<Comment[]>(
      this.baseUrl + 'all?postId=' + postId + '&page=' + page + '&size=' + size
    );
  }

  addComment(comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(this.baseUrl + 'create-comment', comment);
  }

  updateComment(comment: Comment): Observable<Comment> {
    return this.http.put<Comment>(this.baseUrl + 'update-comment', comment);
  }

  deleteComment(id: number): Observable<void> {
    return this.http.delete<void>(this.baseUrl + 'delete-comment?id=' + id);
  }

}

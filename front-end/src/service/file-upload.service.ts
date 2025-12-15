import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  private url = 'http://localhost:8081/files/upload-file';

  constructor(private http: HttpClient) { }


  uploadFile(file: File, folder: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('folder', folder);

    return this.http.post(this.url, formData);
  }
}

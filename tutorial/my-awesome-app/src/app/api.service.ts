import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private httpClient: HttpClient) { }

  getSomeData(): Observable<any> {
    return this.httpClient.get(`${this.apiUrl}/hello`, {responseType: 'text'});
  }

}

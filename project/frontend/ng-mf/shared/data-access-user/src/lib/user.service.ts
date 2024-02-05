import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';



@Injectable({
  providedIn: 'root'
})
export class UserService {


  constructor(private httpClient: HttpClient) {}

  private isUserLoggedIn = new BehaviorSubject(false);
  private token: string | undefined;
  isUserLoggedIn$ = this.isUserLoggedIn.asObservable();
  checkCredentials(username: string, password: string) {
    this.httpClient.post<any>('http://localhost:80/api/auth/signin', {username: username, password: password})
    .subscribe(response => {
      this.isUserLoggedIn.next(true);
      this.token = response.token;
    }, (e: HttpErrorResponse) => this.isUserLoggedIn.next(false));
  }

  public sendEvent(event: string) {
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json');
    headers = headers.set('Authorization', `Bearer ${this.token}`);
    return this.httpClient.post('http://localhost:80/api/events', {eventSender: event}, {headers});
  }

  public getAllEvents() {
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json');
    headers = headers.set('Authorization', `Bearer ${this.token}`);
    return this.httpClient.get<{ id: string, dateTime: Date, eventSender: string }[]>('http://localhost:80/api/events', {headers});
  }

  logout() {
    this.isUserLoggedIn.next(false);
  }
}

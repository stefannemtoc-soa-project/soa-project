import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { NxWelcomeComponent } from './nx-welcome.component';
import { distinctUntilChanged } from 'rxjs/operators';
import { UserService } from '@ng-mf/shared/data-access-user';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { ToastModule } from 'primeng/toast'
import { MessageService } from 'primeng/api';
import { WebsocketService } from './websocket.service';

@Component({
  standalone: true,
  imports: [NxWelcomeComponent, RouterModule, CommonModule, HttpClientModule, ToastModule],
  selector: 'ng-mf-root',
  template: `
  <p-toast></p-toast>
  <div class="dashboard-nav">Admin Dashboard</div>
  <div *ngIf="isLoggedIn$ | async; else signIn">
    <textarea #myTextArea></textarea>
    <button mat-raised-button color="primary" (click)="buttonClicked(myTextArea.value)">Send Event</button>
    <div>
      <ol>
        <li *ngFor="let event of eventsList"> {{ event.id }} | {{ event.eventSender }} | {{ event.dateTime }} </li>
      </ol>
    </div>
  </div>
  <ng-template #signIn><router-outlet></router-outlet></ng-template>
`,
  providers: [MessageService]
})
export class AppComponent implements OnInit {
  isLoggedIn$ = this.userService.isUserLoggedIn$;
  constructor(private userService: UserService, private router: Router, private messageService: MessageService, private websocketService: WebsocketService) { }

  eventsList: { id: string, dateTime: Date, eventSender: string }[] = []

  public buttonClicked(text: string) {
    this.userService.sendEvent(text)
      .subscribe(response => {
        this.messageService.add({ severity: 'success', summary: 'Successfully sent' });
      }, (e: HttpErrorResponse) => {
        this.userService.logout();
      })
  }
  ngOnInit() {
    this.websocketService.connect();
    this.websocketService.messageReceived.subscribe((message: string) => {
      this.userService.getAllEvents().subscribe((response) => {
        this.eventsList = response
      })
    })

    this.isLoggedIn$
      .pipe(distinctUntilChanged())
      .subscribe(async (loggedIn) => {
        // Queue the navigation after initialNavigation blocking is completed
        setTimeout(() => {
          if (!loggedIn) {
            this.router.navigateByUrl('login');
          } else {
            this.router.navigateByUrl('');
          }
        });
      });
  }
}

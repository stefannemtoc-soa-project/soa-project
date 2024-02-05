import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'ng-mf-shared-data-access-user',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './shared-data-access-user.component.html',
  styleUrl: './shared-data-access-user.component.css',
})
export class SharedDataAccessUserComponent {}

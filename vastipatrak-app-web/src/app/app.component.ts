import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AppService } from './services/app/app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title: string = 'Vastipatrak';
  selectedTab: string = 'dashboard';

  constructor(public appService: AppService,
    public dialog: MatDialog) {
      this.selectedTab = 'dashboard';
  }

  
}

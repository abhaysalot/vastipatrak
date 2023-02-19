import { Component, OnInit } from '@angular/core';
import { DashboardData } from '../types/dashboard-data';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent  implements OnInit  {


  // dashboardData: DashboardData;

  constructor() {
  }

  ngOnInit(): void {
    this.reloadDashboard();
  }

  reloadDashboard(): void {
    //this.dashboardService.getDashboard(this, this.appService.currentRole.proprietor);
  }

  setDashboard(dashboardData: DashboardData): void {
    
  }

}

import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {CookieService} from 'ngx-cookie-service';
import { LoginData } from 'src/app/types/login-data';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { JwtTokenData } from 'src/app/types/jwt-token-data';
import { LoginModalComponent } from 'src/app/modals/login-modal/login-modal.component';
import { RegisterModalComponent } from 'src/app/modals/register-modal/register-modal.component';
import Utility from 'src/app/utils/utility';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  authenticateLoginWithTokenUrl: string = '/api/authenticate/authenticateLoginWithToken';

  loginDataSetFlag: boolean = false;
  loginData: null | LoginData;


  constructor(public http: HttpClient, private cookieService: CookieService, public dialog: MatDialog, public snackBar: MatSnackBar) {
    this.loginDataSetFlag = false;
    this.loginData = null;
    this.getCredentialsFromCookie();
  }

  getCredentialsFromCookie(): void {
    let loginId = null;
    if (this.cookieService.check('loginId') && this.cookieService.check('token')) {
      loginId = this.cookieService.get('loginId');
      this.authenticateLoginWithToken(loginId);
    } else {
      this.loginFail();
    }
  }

  authenticateLoginWithToken(loginId: string): void {
    this.http.post<JwtTokenData>(this.authenticateLoginWithTokenUrl, null)
      .subscribe(
        ((resp: JwtTokenData) => {
          if (Utility.isNotNullOrUndefined(resp)) {
            this.cookieService.set('token', resp.token, 1);
            this.cookieService.set('loginId', resp.loginId, 1);
            this.getLogin(loginId);
          } else {
            this.cookieService.delete('token');
            this.cookieService.delete('loginId');
            this.loginFail();
            this.showAlert('Error', 'Auto-login failed! Please try again.');
          }
        }
        ),
        (error => {
          this.cookieService.delete('token');
          this.cookieService.delete('loginId');
          this.loginFail();
          this.showAlert('Error', 'Auto-login failed! Please try again.');
          console.error(error);
        })
      );
  }
  
  showAlert(type: 'Error' | 'Success', message: string): void {
    const snackBarClass: string[] = ['snackbar-text'];
    if (type === 'Error') {
      snackBarClass.push('bg-danger');
    } else if (type === 'Success') {
      snackBarClass.push('bg-success');
    }
    this.snackBar.open(message, 'OK', {
      panelClass: snackBarClass
    });
  }

  
  getLogin(loginId: string): void {
    
    // this.http.get<UiResponse<LoginData>>(this.getLoginUrl + loginId)
    //   .subscribe(
    //   ((resp: UiResponse<LoginData>) => {
    //     if (resp.errorMessage !== null) {
    //       this.showAlert('Error', resp.errorMessage);
    //       this.loginDataSetFlag = false;
    //     } else {
    //       this.loginData = resp.responseData;
    //       this.currentRole = this.loginData.roles.filter((value: RoleData) => value.primaryRole)[0];
    //       if (!isNotNullOrUndefined(this.currentRole)) {
    //         this.currentRole = this.loginData.roles[0];
    //       }
    //       this.loginDataSetFlag = true;
    //       this.showAlert('Success', 'Welcome ' + this.loginData.userFirstName + ' ' + this.loginData.userLastName);
    //     }
    //   }),
    //   ((error: HttpErrorResponse) => {
    //       this.loginDataSetFlag = false;
    //       this.showApplicationError(error);
    //       console.error(error);
    //   }));
  }


  loginFail(): void {
    this.dialog.open(LoginModalComponent, {
      width: '750px',
      hasBackdrop: true,
      data: {appService: this}
    }).afterClosed().subscribe(
      ((loginDone: boolean) => {
        if (!loginDone) {
          this.dialog.open(RegisterModalComponent, {
            width: '750px',
            hasBackdrop: true,
            data: {appService: this},
            maxWidth: '100vw'
          }).afterClosed().subscribe(
            () => {
              this.loginFail();
            }
          );
        }
      }),
      (error => {
        console.error(error);
      })
    );
  }

}



import { Component, OnInit , Inject} from '@angular/core';
import {MatDialog, MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import Utility from 'src/app/utils/utility';
import { ForgotPasswordModalComponent } from '../forgot-password-modal/forgot-password-modal.component';

@Component({
  selector: 'app-login-modal',
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.scss']
})
export class LoginModalComponent {

  modalTitleText: string = 'Login';
  modalContentForgotPasswordTopText: string = 'Forgot password? Click here.';
  modalContentRegisterTopText: string = 'Not a member? Register here.';
  modalContentLoginTopText: string = 'Please enter your login credentials';
  loginButtonText: string = 'Login';
  submitInProgress: boolean = false;

  appService: any;

  loginId: string;
  passw: string;

  constructor(
    public dialogRef: MatDialogRef<LoginModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialog: MatDialog
  ) {

  this.loginId = '';
  this.passw = '';

  }

  isDataValid(): boolean {
    return Utility.isNotNullOrUndefined(this.loginId) && this.loginId.length > 0 &&
      Utility.isNotNullOrUndefined(this.passw) && this.passw.length > 0;
  }


  loginSuccess(): void {
    this.dialogRef.close(true);
  }

  loginFail(): void {
    this.appService.showAlert('Error', 'Login ID or password is incorrect. Please try again.');
  }

  openForgotPassword(): void {
    this.dialog.open(ForgotPasswordModalComponent, {
      width: '400px',
      hasBackdrop: true,
      data: {appService: this.appService}
    });
  }

  openRegister(): void {
    this.dialogRef.close(false);
  }

  checkLogin(): void {
    this.submitInProgress = true;
    if (this.isDataValid()) {
      this.appService.authenticateLogin(this.loginId, this.passw, this);
    } else {
      this.appService.showAlert('Error', 'Please fill all required fields.');
      this.submitInProgress = false;
    }
  }
    

}

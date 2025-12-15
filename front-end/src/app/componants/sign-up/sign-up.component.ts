import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../../service/auth.service';
import {Router} from '@angular/router';
import {MessageHandlerService} from '../../../service/message-handler.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {


  showPassword = false;
  showConfirmPassword = false;

  constructor(private authService: AuthService,
              private router: Router,
              private messageService: MessageHandlerService) { }

  ngOnInit(): void {
  }

  get messageEn(): string {
    return this.messageService.messageEn;
  }

  get messageAr(): string {
    return this.messageService.messageAr;
  }

  clearMessage = () => {
    this.messageService.clearMessage();
  }


  createAccount = (name: string, email: string, password: string, confirmPassword: string): void => {

    if (!this.validateAccount(name, email, password, confirmPassword)) {
      this.messageService.autoClearMessage();
      return;
    }

    localStorage.clear();

    this.authService.createAccount(name, email, password).subscribe(
      response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('userName', response.username);
        localStorage.setItem('roles', response.roles);
        localStorage.setItem('userId', response.userId);
        this.router.navigateByUrl('/mainpage').then(() => {
          window.location.reload();
        });
      },
      errorResponse => {
      this.messageService.handleError(errorResponse);
      }
    );

  }

  validateAccount(name: string , email: string, password: string, confirmPassword: string): boolean {

    if (!name) {
      this.messageService.messageEn = 'name is required.';
      this.messageService.messageAr = 'الاسم مطلوب.';
      return false;
    }

    if (!email) {
      this.messageService.messageEn = 'Email is required.';
      this.messageService.messageAr = 'البريد الالكترونى مطلوب.';
      return false;
    }

    if (!password) {
      this.messageService.messageEn = 'Password is required.';
      this.messageService.messageAr = 'كلمة المرور مطلوبة.';
      return false;
    }

    if (!confirmPassword) {
      this.messageService.messageEn = 'Confirm password is required.';
      this.messageService.messageAr = 'تأكيد كلمة المرور مطلوب.';
      return false;
    }

    if (password !== confirmPassword) {
      this.messageService.messageEn = 'Password and confirm password do not match.';
      this.messageService.messageAr = 'كلمة المرور وتأكيد كلمة المرور غير متطابقين.';
      return false;
    }
    return true;
  }

  togglePasswordVisibility(field: 'password' | 'confirm'): void {
    if (field === 'password') {
      this.showPassword = !this.showPassword;
    } else {
      this.showConfirmPassword = !this.showConfirmPassword;
    }
  }

}

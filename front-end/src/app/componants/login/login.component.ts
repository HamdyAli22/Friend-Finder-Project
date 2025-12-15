import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../../service/auth.service';
import {Router} from '@angular/router';
import {MessageHandlerService} from '../../../service/message-handler.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  showPassword = false;

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

  login(username: string, password: string): void {

    if (!this.validateAccount(username, password)) {
      this.messageService.autoClearMessage();
      return;
    }

    this.authService.login(username, password).subscribe(
      response => {
       // localStorage.setItem('token', response.token);
       // localStorage.setItem('email', response.email);
       // localStorage.setItem('roles', response.roles);
        this.router.navigateByUrl('/mainpage');
      },
      error => {
       this.messageService.handleError(error);
      }
    );

  }

  validateAccount(email: string, password: string): boolean {

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
    return true;
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }
}

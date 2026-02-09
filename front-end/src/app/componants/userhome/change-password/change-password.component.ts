import { Component, OnInit } from '@angular/core';
import {ChangePassReq} from '../../../../model/change-pass-req';
import {AccountService} from '../../../../service/account.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MessageHandlerService} from '../../../../service/message-handler.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  form: ChangePassReq = new ChangePassReq();
  mode: 'main' | 'forgot' = 'main';
  isSuccess = false;
  isLoading = false;

  showOldPassword = false;
  showNewPassword = false;
  showConfirmPassword = false;


  constructor( private accountService: AccountService,
               private route: ActivatedRoute,
               private router: Router,
               private messageService: MessageHandlerService) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params.mode === 'forgot') {
        this.mode = 'forgot';
      }
    });
  }

  get messageEn(): string {
    return this.messageService.messageEn;
  }

  get messageAr(): string {
    return this.messageService.messageAr;
  }

  clearMessage(): void {
    this.messageService.clearMessage();
  }

  onSubmit(): void {
    this.isLoading = true;
    this.isSuccess = false;

    this.messageService.clearMessage();

    const request =
      this.mode === 'forgot'
        ? this.accountService.resetPassword(this.form)
        : this.accountService.changePassword(this.form);

    request.subscribe({
      next: res => {
        this.isLoading = false;
        this.isSuccess = true;
        this.messageService.messageAr = res.ar;
        this.messageService.messageEn = res.en;

        this.messageService.autoClearMessage();

        this.form = new ChangePassReq();

        localStorage.clear();
        sessionStorage.clear();

        if (this.mode !== 'forgot') {
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        }

      },
      error: error => {
        this.isLoading = false;
        this.isSuccess = false;

        this.messageService.handleError(error);
      }
    });
  }

  togglePasswordVisibility(field: 'old' | 'new' | 'confirm'): void {
    if (field === 'old') {
      this.showOldPassword = !this.showOldPassword;
    } else if (field === 'new') {
      this.showNewPassword = !this.showNewPassword;
    } else {
      this.showConfirmPassword = !this.showConfirmPassword;
    }
  }
}

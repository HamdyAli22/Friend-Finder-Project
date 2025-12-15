import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MessageHandlerService {

  messageEn = '';
  messageAr = '';

  constructor() { }

  handleError = (errorResponse: any) => {
    if (Array.isArray(errorResponse.error) && errorResponse.error.length > 0) {
      this.messageEn = errorResponse.error[0].en;
      this.messageAr = errorResponse.error[0].ar;
    } else {
      this.messageEn = errorResponse.error.en;
      this.messageAr = errorResponse.error.ar;
    }
    this.autoClearMessage();
  }

  autoClearMessage(): void {
    setTimeout(() => this.clearMessage(), 3000);
  }

  clearMessage(): void {
    this.messageEn = '';
    this.messageAr = '';
  }
}

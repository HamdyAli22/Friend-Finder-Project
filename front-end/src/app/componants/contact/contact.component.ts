import { Component, OnInit } from '@angular/core';
import {ContactInfoService} from '../../../service/contact-info.service';
import {MessageHandlerService} from '../../../service/message-handler.service';
import {ContactInfo} from '../../../model/contact-info';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {

  contact = {
    name: null,
    email:null,
    phone: null,
    message: null
  };

  constructor(private contactService: ContactInfoService,
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

  saveContact(): void {
    this.contactService.saveContact(this.contact).subscribe({
      next: (response: any) => {
        this.messageService.messageAr = 'تم إرسال الرسالة بنجاح.';
        this.messageService.messageEn = 'Message sent successfully.';
        this.contact = new ContactInfo();
        this.messageService.autoClearMessage();
      },
      error: error => {
        this.messageService.handleError(error);
      }
    });

  }
}

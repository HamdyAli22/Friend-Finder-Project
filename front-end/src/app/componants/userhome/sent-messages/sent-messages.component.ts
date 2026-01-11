import { Component, OnInit } from '@angular/core';
import {ContactInfo} from '../../../../model/contact-info';
import {ContactInfoService} from '../../../../service/contact-info.service';
import {MessageHandlerService} from '../../../../service/message-handler.service';

@Component({
  selector: 'app-sent-messages',
  templateUrl: './sent-messages.component.html',
  styleUrls: ['./sent-messages.component.css'],
})
export class SentMessagesComponent implements OnInit {

  messages: ContactInfo[] = [];
  searchKeyword: string = '';
  pageNo: number = 1;
  pageSize: number = 4;
  totalMessages: number = 0;
  email: string = localStorage.getItem('email') || '';

  constructor(private contactService: ContactInfoService,
              private messageService: MessageHandlerService) { }

  get messageEn(): string {
    return this.messageService.messageEn;
  }

  get messageAr(): string {
    return this.messageService.messageAr;
  }

  clearMessage = () => {
    this.messageService.clearMessage();
  }

  ngOnInit(): void {
    this.loadMessages();
  }

  loadMessages(keyword?: string): void {
    const search = keyword?.trim() || '';
    if (search) {
      this.contactService.searchMessages(search, this.pageNo, this.pageSize).subscribe({
        next: (res: any) => {
          this.messages = res.messages.filter((m: ContactInfo) => m.email === this.email);
          this.totalMessages = res.totalMessages;
        },
        error: error => {
          this.messages = [];
          this.totalMessages = 0;
          this.messageService.handleError(error);
        }
      });
    } else {
      this.contactService.getMessagesByUser(this.email, this.pageNo, this.pageSize).subscribe({
        next: (res: any) => {
          this.messages = res.messages;
          this.totalMessages = res.totalMessages;
        },
        error: error => {
          this.messages = [];
          this.totalMessages = 0;
          this.messageService.handleError(error);
        }
      });
    }
  }

  applyFilter(): void {
    this.pageNo = 1;
    this.loadMessages(this.searchKeyword);
  }

  pageChange(page: number): void {
    this.pageNo = page;
    this.loadMessages(this.searchKeyword);
  }

  changePageSize(event: Event): void {
    this.pageSize = +(event.target as HTMLInputElement).value;
    this.pageNo = 1;
    this.loadMessages(this.searchKeyword);
  }

}

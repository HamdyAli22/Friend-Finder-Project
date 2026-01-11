import {Account} from './account';

export class Notification {
  id: number;
  message: string;
  read: boolean;
  account?: Account;
  createdDate: string| Date;
  type: string;
  postId?: number;
  showMenu?: boolean;
  dropdownStyle?: { top: string; left: string };
}

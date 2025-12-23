import {Role} from './role';
import {AccountDetails} from './account-details';

export class Account {
  id: number;
  username: string;
  email: string;
  enabled: boolean;
  roles: Role[];
  accountDetails?: AccountDetails;
  friendsCount: number;
}

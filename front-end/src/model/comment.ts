import { Account } from './account';

export class Comment {
  id?: number;
  content: string;
  postId: number;
  owner?: Account;
  createdDate?: string;

  showDropdown?: boolean;
  editing?: boolean;
  editedContent?: string;
}

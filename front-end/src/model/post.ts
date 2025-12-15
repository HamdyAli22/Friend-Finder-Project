import {Account} from './account';
import {Comment} from './comment';

export class Post {
  id: number;
  content: string;
  mediaUrl: string;
  mediaType: 'IMAGE' | 'VIDEO' | 'NONE';
  owner: Account;
  likesCount: number;
  commentsCount: number;
  dislikesCount: number;
  createdDate: string;
  comments: Comment[] = [];
  commentPage = 1;
  showMore = true;
  showDropdown = false;
  newComment?: string;
  showDeleteConfirm?: boolean;
}

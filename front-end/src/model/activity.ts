export class Activity {
  type: 'POST' | 'COMMENT' | 'REACTION';
  message: string;
  date: string;
  postId: number;
  mediaType?: 'IMAGE' | 'VIDEO' | 'TEXT';
}

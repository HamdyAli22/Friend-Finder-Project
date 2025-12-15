export class Friendship {
  id: number;
  requesterId: number;
  receiverId: number;
  friendId: number;
  isFriend: boolean;
  requestSent?: boolean;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED' | 'CANCELLED';
}


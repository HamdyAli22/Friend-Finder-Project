export class ChangePassReq {
  email?: string;
  oldPassword?: string;
  newPassword: string;
  confirmPassword: string;
}

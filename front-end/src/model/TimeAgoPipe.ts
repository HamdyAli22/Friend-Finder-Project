import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timeAgo'
})
export class TimeAgoPipe implements PipeTransform {
  transform(value: Date | string | number): string {
    if (!value) { return ''; }

    const date = new Date(value);
    const now = new Date();
    const diff = (now.getTime() - date.getTime()) / 1000; // الفرق بالثواني

    if (diff < 60) {
      return `${Math.floor(diff)}s`; // ثواني
    } else if (diff < 3600) {
      return `${Math.floor(diff / 60)}m`; // دقائق
    } else if (diff < 86400) {
      return `${Math.floor(diff / 3600)}h`; // ساعات
    } else if (diff < 172800) {
      return `Yesterday at ${date.getHours()}:${('0' + date.getMinutes()).slice(-2)}`; // امبارح
    } else {
      return date.toLocaleDateString(); // اكتر من يومين
    }
  }
}

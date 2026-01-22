import {Component, Input, OnInit} from '@angular/core';
import {Activity} from '../../../../model/activity';
import {ActivityService} from '../../../../service/activity.service';
import {AuthService} from '../../../../service/auth.service';

@Component({
  selector: 'app-time-line-detailes',
  templateUrl: './time-line-detailes.component.html',
  styleUrls: ['./time-line-detailes.component.css']
})
export class TimeLineDetailesComponent implements OnInit {

  @Input() username!: string;
  activities: Activity[] = [];

  constructor(private activityService: ActivityService,
              private authService: AuthService) { }

  ngOnInit(): void {

    this.authService.username$.subscribe(name => {
      this.username = name ?? undefined;
    });

    this.loadActivities();
  }

  loadActivities(userId?: number): void {
    this.activityService.getActivities(userId).subscribe(res => {
      this.activities = res;
    });
  }

}

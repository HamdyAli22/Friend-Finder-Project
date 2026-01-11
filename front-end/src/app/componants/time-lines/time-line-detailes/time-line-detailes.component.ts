import {Component, Input, OnInit} from '@angular/core';
import {Activity} from '../../../../model/activity';
import {ActivityService} from '../../../../service/activity.service';

@Component({
  selector: 'app-time-line-detailes',
  templateUrl: './time-line-detailes.component.html',
  styleUrls: ['./time-line-detailes.component.css']
})
export class TimeLineDetailesComponent implements OnInit {

  @Input() username!: string;
  activities: Activity[] = [];

  constructor(private activityService: ActivityService) { }

  ngOnInit(): void {
    this.loadActivities();
  }

  loadActivities(userId?: number): void {
    this.activityService.getActivities(userId).subscribe(res => {
      this.activities = res;
    });
  }

}

import {Component, Input, OnInit} from '@angular/core';
import {Activity} from '../../../../model/activity';
import {ActivityService} from '../../../../service/activity.service';
import {AuthService} from '../../../../service/auth.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-time-line-detailes',
  templateUrl: './time-line-detailes.component.html',
  styleUrls: ['./time-line-detailes.component.css']
})
export class TimeLineDetailesComponent implements OnInit {

  @Input() username!: string;
  activities: Activity[] = [];

  currentUserId = Number(localStorage.getItem('userId') || 0);
  userId?: number;

  constructor(private activityService: ActivityService,
              private authService: AuthService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {

    this.authService.username$.subscribe(name => {
      this.username = name ?? undefined;
    });

    this.route.queryParams.subscribe(params => {
      this.userId = params['userId'] ? +params['userId'] : this.currentUserId;
      console.log(this.userId);
      this.loadActivities();
    });

  }

  loadActivities(userId?: number): void {
    this.activityService.getActivities(this.userId).subscribe(res => {
      this.activities = res;
    });
  }

}

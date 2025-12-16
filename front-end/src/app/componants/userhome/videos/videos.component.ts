import { Component, OnInit } from '@angular/core';
import {UserMedia} from '../../../../model/user-media';
import {MediaService} from '../../../../service/media.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-videos',
  templateUrl: './videos.component.html',
  styleUrls: ['./videos.component.css']
})
export class VideosComponent implements OnInit {

  videos: UserMedia[] = [];
  loading = false;
  userId?: number;

  constructor(private mediaService: MediaService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.userId = params.userId;
      this.loadVideos();
    });
  }

  loadVideos(): void {
    this.loading = true;

    this.mediaService.getUserMedia('VIDEO', this.userId).subscribe({
      next: (res) => {
        this.videos = res;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }


}

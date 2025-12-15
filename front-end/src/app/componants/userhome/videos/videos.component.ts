import { Component, OnInit } from '@angular/core';
import {UserMedia} from '../../../../model/user-media';
import {MediaService} from '../../../../service/media.service';

@Component({
  selector: 'app-videos',
  templateUrl: './videos.component.html',
  styleUrls: ['./videos.component.css']
})
export class VideosComponent implements OnInit {

  videos: UserMedia[] = [];
  loading = false;

  constructor(private mediaService: MediaService) { }

  ngOnInit(): void {
    this.loadVideos();
  }

  loadVideos(): void {
    this.loading = true;

    this.mediaService.getMyMedia('VIDEO').subscribe({
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

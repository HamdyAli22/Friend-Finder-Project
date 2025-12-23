import { Component, OnInit } from '@angular/core';
import {UserMedia} from '../../../../model/user-media';
import {MediaService} from '../../../../service/media.service';
import {ActivatedRoute, Router} from '@angular/router';

declare var $: any;

@Component({
  selector: 'app-videos',
  templateUrl: './videos.component.html',
  styleUrls: ['./videos.component.css']
})
export class VideosComponent implements OnInit {

  videos: UserMedia[] = [];
  loading = false;
  userId?: number;

  showBar = true;

  selectedVideo?: UserMedia;


  constructor(private mediaService: MediaService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {

    if (this.router.url.includes('/timeline')) {
      this.showBar = false;
    }

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

  openPreview(video: UserMedia): void {
    this.selectedVideo = video;
    $('#videoPreviewModal').modal('show'); // Bootstrap modal
  }


}

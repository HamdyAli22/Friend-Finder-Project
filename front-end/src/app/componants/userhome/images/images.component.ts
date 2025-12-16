import { Component, OnInit } from '@angular/core';
import {UserMedia} from '../../../../model/user-media';
import {MediaService} from '../../../../service/media.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-images',
  templateUrl: './images.component.html',
  styleUrls: ['./images.component.css']
})
export class ImagesComponent implements OnInit {

  images: UserMedia[] = [];
  loading = false;
  userId?: number;

  constructor(private mediaService: MediaService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.userId = params.userId;
      this.loadImages();
    });
  }

  loadImages(): void {
    this.loading = true;

    this.mediaService.getUserMedia('IMAGE', this.userId).subscribe({
      next: (res) => {
        this.images = res;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

}

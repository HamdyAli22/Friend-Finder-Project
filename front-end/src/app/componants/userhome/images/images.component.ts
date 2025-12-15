import { Component, OnInit } from '@angular/core';
import {UserMedia} from '../../../../model/user-media';
import {MediaService} from '../../../../service/media.service';

@Component({
  selector: 'app-images',
  templateUrl: './images.component.html',
  styleUrls: ['./images.component.css']
})
export class ImagesComponent implements OnInit {

  images: UserMedia[] = [];
  loading = false;

  constructor(private mediaService: MediaService) { }

  ngOnInit(): void {
    this.loadImages();
  }

  loadImages(): void {
    this.loading = true;

    this.mediaService.getMyMedia('IMAGE').subscribe({
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

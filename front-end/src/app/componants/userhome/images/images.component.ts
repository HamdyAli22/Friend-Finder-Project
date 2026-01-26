import { Component, OnInit } from '@angular/core';
import {UserMedia} from '../../../../model/user-media';
import {MediaService} from '../../../../service/media.service';
import {ActivatedRoute, Router} from '@angular/router';

declare var $: any;

@Component({
  selector: 'app-images',
  templateUrl: './images.component.html',
  styleUrls: ['./images.component.css']
})
export class ImagesComponent implements OnInit {

  images: UserMedia[] = [];
  loading = false;

  currentUserId = Number(localStorage.getItem('userId') || 0);
  userId?: number;

  showBar = true;

  selectedImage?: UserMedia;

  constructor(private mediaService: MediaService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {

    if (this.router.url.includes('/timeline')) {
      this.showBar = false;
    }

    this.route.queryParams.subscribe(params => {
      this.userId = params['userId'] ? +params['userId'] : this.currentUserId;
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

  openPreview(img: UserMedia): void {
    this.selectedImage = img;
    $('#imagePreviewModal').modal('show');
  }

}

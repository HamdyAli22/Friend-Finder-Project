import {PostService} from '../../../../service/post.service';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import {FileUploadService} from '../../../../service/file-upload.service';

@Component({
  selector: 'app-publish',
  templateUrl: './publish.component.html',
  styleUrls: ['./publish.component.css']
})
export class PublishComponent implements OnInit {

  @Output() postCreated = new EventEmitter<any>();

  content = '';
  selectedFile: File | null = null;
  mediaUrl = '';
  mediaType: 'IMAGE' | 'VIDEO' | 'NONE' = 'NONE';
  uploading = false;
  previewUrl: string | null = null;


  constructor( private postService: PostService,
               private fileUploadService: FileUploadService) { }

  ngOnInit(): void {
  }

  onFileSelected = (event: any, type: 'IMAGE' | 'VIDEO') => {
    const file = event.target.files?.[0];
    if (!file) { return; }

    this.selectedFile = file;
    this.mediaType = type;

    // Preview Only
    const reader = new FileReader();
    reader.onload = () => this.previewUrl = reader.result as string;
    reader.readAsDataURL(file);
  }


  publish = () => {
    if ((this.content?.trim()?.length === 0) && !this.selectedFile) { return; }

    this.uploading = true;

    if (this.selectedFile) {
      const userId = localStorage.getItem('userId');
      const folder = `users/${userId}/posts`;

      // Upload file only on publish
      this.fileUploadService.uploadFile(this.selectedFile, folder)
        .subscribe({
          next: (res) => {
            this.mediaUrl = res.path;
            this.submitPost();
          },
          error: () => {
            this.uploading = false;
            alert('File upload failed!');
          }
        });

    } else {
      this.submitPost();
    }
  }

  private submitPost = () => {
    const payload = {
      content: this.content,
      mediaType: this.mediaType,
      mediaUrl: this.mediaUrl
    };

    this.postService.publishPost(payload).subscribe({
      next: (post) => {
        this.postCreated.emit(post);
        this.reset();
      },
      error: () => {
        alert('Failed to publish post');
        this.uploading = false;
      }
    });
  }

  reset = () => {
    this.content = '';
    this.selectedFile = null;
    this.previewUrl = null;
    this.mediaUrl = '';
    this.mediaType = 'NONE';
    this.uploading = false;
  }

  removeMedia = (type: 'IMAGE' | 'VIDEO') => {
    this.selectedFile = null;
    this.previewUrl = null;
    this.mediaUrl = '';
    this.mediaType = 'NONE';

    // Reset the correct file input
    const fileInput = document.querySelector(
      type === 'IMAGE' ? 'input[accept="image/*"]' : 'input[accept="video/*"]'
    ) as HTMLInputElement;

    if (fileInput) { fileInput.value = ''; }
  };


}

import {Component, Input, OnInit, Output} from '@angular/core';
import {Post} from '../../../model/post';
import {EventEmitter} from '@angular/core';
import {PostService} from '../../../service/post.service';
import {FileUploadService} from '../../../service/file-upload.service';

@Component({
  selector: 'app-post-edit',
  templateUrl: './post-edit.component.html',
  styleUrls: ['./post-edit.component.css']
})
export class PostEditComponent implements OnInit {

  @Input() post!: Post;
  @Output() closed = new EventEmitter<void>();
  @Output() postUpdated = new EventEmitter<Post>();

  content = '';
  selectedFile: File | null = null;
  mediaUrl = '';
  mediaType: 'IMAGE' | 'VIDEO' | 'NONE' = 'NONE';
  uploading = false;
  previewUrl: string | null = null;

  constructor(private postService: PostService,
              private fileUploadService: FileUploadService) { }

  ngOnInit(): void {
    if (this.post) { this.loadPostForEdit(this.post); }
  }

  onFileSelected = (event: any, type: 'IMAGE' | 'VIDEO') => {
    const file = event.target.files?.[0];
    if (!file) { return; }

    this.selectedFile = file;
    this.mediaType = type;

    const reader = new FileReader();
    reader.onload = () => this.previewUrl = reader.result as string;
    reader.readAsDataURL(file);
  }

  updatePost = () => {
    if (!this.content?.trim() && !this.selectedFile) { return; }

    this.uploading = true;

    if (this.selectedFile) {
      const userId = localStorage.getItem('userId');
      const folder = `users/${userId}/posts`;

      this.fileUploadService.uploadFile(this.selectedFile, folder).subscribe({
        next: res => {
          this.mediaUrl = res.path;
          this.submitUpdate();
        },
        error: () => {
          this.uploading = false;
          alert('File upload failed!');
        }
      });
    } else {
      this.submitUpdate();
    }
  }

  private submitUpdate = () => {
    const payload: any = {
      id: this.post.id,
      content: this.content,
      mediaType: this.mediaType,
      mediaUrl: this.mediaUrl
    };

    this.postService.updatePost(payload).subscribe({
      next: updatedPost => {
        this.postUpdated.emit(updatedPost);
        this.close();
      },
      error: () => {
        alert('Failed to update post');
        this.uploading = false;
      }
    });
  }

  loadPostForEdit = (post: Post) => {
    this.content = post.content;
    this.mediaUrl = post.mediaUrl || '';
    this.mediaType = post.mediaType || 'NONE';
    this.previewUrl = this.mediaUrl || null;
  }

  removeMedia = (type: 'IMAGE' | 'VIDEO') => {
    this.selectedFile = null;
    this.previewUrl = null;
    this.mediaUrl = '';
    this.mediaType = 'NONE';

    const fileInput = document.querySelector(
      type === 'IMAGE' ? 'input[accept="image/*"]' : 'input[accept="video/*"]'
    ) as HTMLInputElement;

    if (fileInput) { fileInput.value = ''; }
  }

  close = () => {
    this.closed.emit();
  }


}

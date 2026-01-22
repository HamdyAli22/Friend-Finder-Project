import {Component, HostListener, OnInit} from '@angular/core';
import {Post} from '../../../../model/post';
import {PostService} from '../../../../service/post.service';
import {MessageHandlerService} from '../../../../service/message-handler.service';
import {CommentService} from '../../../../service/comment.service';
import {ReactionService} from '../../../../service/reaction.service';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../../../service/auth.service';

@Component({
  selector: 'app-time-line',
  templateUrl: './time-line.component.html',
  styleUrls: ['./time-line.component.css']
})
export class TimeLineComponent implements OnInit {

  posts: Post[] = [];
  filteredPosts: Post[] = [];
  pageNo = 1;
  pageSize = 3;
  total = 0;
  loading = false;
  private serverBase = 'http://localhost:8081';

  currentSearchKey = '';
  currentUserId = Number(localStorage.getItem('userId') || 0);
  userId: number;
  currentUsername?: string;
  currentProfileImage?: string;

  showEditModal = false;
  postBeingEdited?: Post;

  postIdFromNotification?: number;

  constructor(private postService: PostService,
              private messageService: MessageHandlerService,
              private commentService: CommentService,
              private reactionService: ReactionService,
              private authService: AuthService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {

    this.authService.username$.subscribe(name => {
      this.currentUsername = name ?? undefined;
    });

    this.authService.profileImage$.subscribe(img => {
      this.currentProfileImage = img
        ? this.serverBase + img
        : 'assets/images/users/default-avatar-icon.jpg';
    });

    this.route.queryParams.subscribe(params => {
      const id = params.userId;
      console.log('params', id);
      this.userId = id
        ? Number(id)
        : this.currentUserId;

      const postIdParam = params.postId;
      this.postIdFromNotification = postIdParam ? Number(postIdParam) : undefined;

      const key = params['q'] || '';
      this.currentSearchKey = key;

      this.posts = [];
      this.filteredPosts = [];
      this.pageNo = 1;
      this.total = 0;

      if (this.postIdFromNotification) {
        this.loadSinglePost(this.postIdFromNotification);
      } else {
        this.loadPosts();
      }
    });
  }

  get messageEn(): string {
    return this.messageService.messageEn;
  }

  get messageAr(): string {
    return this.messageService.messageAr;
  }

  clearMessage = () => {
    this.messageService.clearMessage();
  }

  @HostListener('window:scroll', [])
  onWindowScroll(): void {
    if (this.loading || (this.posts.length >= this.total && this.total !== 0)) { return; }

    const windowHeight = window.innerHeight;
    const docHeight = document.documentElement.scrollHeight;
    const scrollTop = window.scrollY;

    if (windowHeight + scrollTop >= docHeight - 200) {
      this.loadPosts();
    }
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;

    this.posts.forEach(post => {
      if (post.showDropdown) {
        // نجيب عناصر الزر والقائمة
        const dropdown = document.querySelector(`#dropdown-${post.id}`);
        const dotsBtn = document.querySelector(`#dots-btn-${post.id}`);

        if (
          dropdown && !dropdown.contains(target) &&
          dotsBtn && !dotsBtn.contains(target)
        ) {
          post.showDropdown = false;
        }
      }
    });

    // إغلاق dropdown التعليقات
    this.posts.forEach(post => {
      post.comments.forEach(comment => {
        if (comment.showDropdown) {

          const dropdown = document.querySelector(`#comment-dropdown-${comment.id}`);
          const dotsBtn = document.querySelector(`#comment-dots-btn-${comment.id}`);

          if (
            dropdown && !dropdown.contains(target) &&
            dotsBtn && !dotsBtn.contains(target)
          ) {
            comment.showDropdown = false;
          }
        }
      });
    });
  }



  loadPosts(): void {
    if (this.loading || (this.posts.length >= this.total && this.total !== 0)) { return; }

    this.loading = true;

    this.postService.getUserPosts(this.pageNo, this.pageSize , this.userId).subscribe(
      (response: any) => {
        this.posts = [
          ...this.posts,
          ...response.posts.map((p: any) => {
            if (p.mediaUrl) { p.mediaUrl = this.serverBase + p.mediaUrl; }
            return {
              ...p,
              comments: [],
              newComment: '',
              commentPage: 1,
              showMore: p.commentsCount > 3,
              showDropdown: false,
              showDeleteConfirm: false, // ← كل بوست هيبقى له modal
              likesTooltip: '',
              dislikesTooltip: ''
            };
          })
        ];

        this.total = response.total;
        this.pageNo++;
        this.loading = false;

        this.posts.forEach(post => {
          if (post.commentsCount > 0) { this.loadInitialComment(post); }
          this.loadReactionUsers(post);
        });

        this.filterPosts(this.currentSearchKey);

      },
      (error) => {
        this.loading = false;
        this.messageService.handleError(error);
      }
    );
  }

  loadSinglePost(postId: number): void {
    this.loading = true;
    this.postService.getPostById(postId).subscribe({
      next: (post: any) => {
        if (post.mediaUrl) { post.mediaUrl = this.serverBase + post.mediaUrl; }

        this.posts = [{
          ...post,
          comments: [],
          commentPage: 1,
          showMore: post.commentsCount > 3,
          showDropdown: false,
          showDeleteConfirm: false,
          newComment: ''
        }];

        this.total = 1;
        this.pageNo = 2;

        if (post.commentsCount > 0) {
          this.loadInitialComment(this.posts[0]);
        }
        this.loadReactionUsers(post);
        this.loading = false;
        this.filterPosts(this.currentSearchKey);
      },
      error: (err) => {
        this.loading = false;
        this.messageService.handleError(err);
      }
    });
  }


  loadInitialComment(post: Post): void {
    this.commentService.getComments(post.id, 1, 3)
      .subscribe(res => {

        post.comments = res.comments.map(c => ({
          ...c,
          showDropdown: false,
          editing: false,
          editedContent: ''
        }));

        post.commentPage = 2;

        if (post.commentsCount <= 3) {
          post.showMore = false;
          return;
        }

        post.showMore = true;
      });
  }

  loadMoreComments(post: Post): void {
    this.commentService.getComments(post.id, post.commentPage, 3)
      .subscribe(res => {
        post.comments.push(
          ...res.comments.map(c => ({
            ...c,
            showDropdown: false,
            editing: false,
            editedContent: ''
          }))
        );

        post.commentPage++;

        if (post.comments.length >= post.commentsCount) {
          post.showMore = false;
        }
      });
  }


  showLessComments(post: Post): void {
    post.comments.forEach(c => {
      c.showDropdown = false; // إغلاق أي dropdown مفتوح
      c.editing = false;
    });

    post.comments = post.comments.slice(-3);
    post.commentPage = 2;
    post.showMore = true;

  }


  addComment(post: Post): void {
    if (!post.newComment || !post.newComment.trim()) { return; }

    const comment = {
      content: post.newComment,
      postId: post.id
    };

    this.commentService.addComment(comment).subscribe(
      (created: any) => {
        if (!post.comments) {
          post.comments = [];
        }

        post.comments.unshift(created);
        post.commentsCount++;

        post.showMore = post.comments.length < post.commentsCount;

        post.newComment = ''; // ← امسح حقل البوست نفسه
      },
      error => this.messageService.handleError(error)
    );
  }

  addPostToTop(post: any): void {
    if (post.mediaUrl) { post.mediaUrl = this.serverBase + post.mediaUrl; }
    this.posts.unshift({
      ...post,
      comments: [],
      commentPage: 1,
      showMore: true,
      newComment: '',
      showDropdown: false,
      showDeleteConfirm: false
    });
  }

  toggleDropdown(post: Post): void {
    this.posts.forEach(p => {
      if (p !== post) { p.showDropdown = false; }
    });
    post.showDropdown = !post.showDropdown;
  }

  deletePost(post: Post): void {
    post.showDeleteConfirm = true; // فتح الـ modal
  }

  confirmDelete(post: Post): void {
    post.showDeleteConfirm = false; // إغلاق الـ modal
    post.showDropdown = false;      // أقفل الـ dropdown بتاع البوست نفسه

    this.postService.deletePost(post.id).subscribe({
      next: () => this.posts = this.posts.filter(p => p.id !== post.id),
      error: (err) => this.messageService.handleError(err)
    });
  }

// عند الضغط على No
  cancelDelete(post: Post): void {
    post.showDeleteConfirm = false;
    post.showDropdown = false;
  }

  trackByPostId(index: number, post: Post): number {
    return post.id;
  }

  editPost(post: Post): void {
    this.postBeingEdited = { ...post }; // نسخ البوست للتعديل
    this.showEditModal = true;           // فتح الـ Modal
  }

  handlePostUpdated(updatedPost: Post): void {
    const index = this.posts.findIndex(p => p.id === updatedPost.id);
    if (index !== -1) {
      if (updatedPost.mediaUrl) {
        updatedPost.mediaUrl = this.serverBase + updatedPost.mediaUrl;
      }
      this.posts[index] = { ...this.posts[index], ...updatedPost };
    }
    this.showEditModal = false;
    this.postBeingEdited = undefined;
  }

  // دالة لغلق الـ Modal بدون تعديل
  handleModalClosed(): void {
    this.showEditModal = false;
    this.postBeingEdited = undefined;
  }

  startEditComment = (c: any, post: Post) => {
    this.posts.forEach(p =>
      p.comments.forEach(cm => cm.showDropdown = false)
    );

    c.editing = true;
    c.editedContent = c.content;
  }

  cancelEditComment = (c: any) => {
    c.editing = false;
    c.editedContent = '';
  }

  saveCommentEdit = (c: any, post: Post) => {
    if (!c.editedContent || !c.editedContent.trim()) { return; }

    const updated = {
      id: c.id,
      content: c.editedContent,
      postId: post.id
    };

    this.commentService.updateComment(updated).subscribe(
      res => {
        c.content = res.content;      // تحديث النص
        c.editing = false;            // إيقاف التعديل
        c.editedContent = '';         // مسح buffer
      },
      err => this.messageService.handleError(err)
    );
  }

  toggleCommentDropdown = (c: any, post: Post) => {
    post.comments.forEach(cm => {
      if (cm !== c) { cm.showDropdown = false; }
    });
    c.showDropdown = !c.showDropdown;
    console.log('showDropdown: ' || c.showDropdown);
  }

  deleteComment(c: any, post: Post): void {
    post.comments.forEach(cm => cm.showDropdown = false);

    this.commentService.deleteComment(c.id).subscribe({
      next: () => {
        post.comments = post.comments.filter(cm => cm.id !== c.id);

        post.commentsCount--;

        post.showMore = post.comments.length < post.commentsCount;
      },
      error: err => this.messageService.handleError(err)
    });
  }

  trackByCommentId(index: number, comment: any): number {
    return comment.id;
  }

  likePost(post: Post): void {
    this.reactionService.reactAndUpdateCounts(post.id, 'LIKE').subscribe({
      next: counts => {
        post.likesCount = counts.likes;
        post.dislikesCount = counts.dislikes;
      },
      error: err => this.messageService.handleError(err)
    });
  }

  dislikePost(post: Post): void {
    this.reactionService.reactAndUpdateCounts(post.id, 'DISLIKE').subscribe({
      next: counts => {
        post.likesCount = counts.likes;
        post.dislikesCount = counts.dislikes;
      },
      error: err => this.messageService.handleError(err)
    });
  }

  loadReactionUsers(post: Post): void {
    this.reactionService.getReactionUsers(post.id).subscribe({
      next: (res: any) => {
        post.likesTooltip = res.likes.join('\n') || 'No likes yet';
        post.dislikesTooltip = res.dislikes.join('\n') || 'No dislikes yet';
        post.likesCount = res.likes.length;
        post.dislikesCount = res.dislikes.length;
      },
      error: err => this.messageService.handleError(err)
    });
  }

  onSearchKey(key: string): void {
    this.currentSearchKey = key;
    this.filterPosts(key);
  }

  filterPosts(key: string): void {
    if (!key || !key.trim()) {
      this.filteredPosts = [...this.posts];
      return;
    }

    const lowerKey = key.toLowerCase();

    this.filteredPosts = this.posts.filter(post => {
      const matchesPostContent = (post.content ?? '').toLowerCase().includes(lowerKey);
      const matchesPostUser = (post.owner?.username ?? '').toLowerCase().includes(lowerKey);

      const matchesComment = post.comments?.some(c =>
        (c.content ?? '').toLowerCase().includes(lowerKey) ||
        (c.owner?.username ?? '').toLowerCase().includes(lowerKey)
      );

      return matchesPostContent || matchesPostUser || matchesComment;
    });
  }


}

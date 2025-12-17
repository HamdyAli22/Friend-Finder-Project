import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { HeaderComponent } from './componants/header/header.component';
import { FooterComponent } from './componants/footer/footer.component';
import { SignUpComponent } from './componants/sign-up/sign-up.component';
import { ContactComponent } from './componants/contact/contact.component';
import { UserhomeComponent } from './componants/userhome/userhome.component';
import { LeftBarComponent } from './componants/userhome/left-bar/left-bar.component';
import { RightBarComponent } from './componants/userhome/right-bar/right-bar.component';
import { PublishComponent } from './componants/userhome/publish/publish.component';
import { FriendsComponent } from './componants/userhome/friends/friends.component';
import { CoolImagesComponent } from './componants/userhome/cool-images/cool-images.component';
import { MainPageComponent } from './componants/userhome/main-page/main-page.component';
import { TimeLinesComponent } from './componants/time-lines/time-lines.component';
import { TimeLineComponent } from './componants/time-lines/time-line/time-line.component';
import { TimeAboutComponent } from './componants/time-lines/time-about/time-about.component';
import { TimeAlbumComponent } from './componants/time-lines/time-album/time-album.component';
import { TimeFriendsComponent } from './componants/time-lines/time-friends/time-friends.component';
import { TimeLineProfileComponent } from './componants/time-lines/time-line-profile/time-line-profile.component';
import { TimeLineDetailesComponent } from './componants/time-lines/time-line-detailes/time-line-detailes.component';
import {RouterModule, Routes} from '@angular/router';
import { LoginComponent } from './componants/login/login.component';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {LoginSignUpGuard} from '../guard/login-sign-up.guard';
import {AuthGuard} from '../guard/auth.guard';
import {APP_BASE_HREF} from '@angular/common';
import {AuthInterceptor} from '../interceptors/auth.interceptor';
import { PostEditComponent } from './componants/post-edit/post-edit.component';
import { FriendRequestsComponent } from './componants/userhome/friends/friend-requests/friend-requests.component';
import { ImagesComponent } from './componants/userhome/images/images.component';
import { VideosComponent } from './componants/userhome/videos/videos.component';
import {SharedModule} from '../model/shared-module';



// http://localhost:4200

const routes: Routes = [
  {path: 'sign-up', component: SignUpComponent, canActivate: [LoginSignUpGuard]},
  {path: 'login', component: LoginComponent, canActivate: [LoginSignUpGuard]},
  {path: 'mainpage', component: MainPageComponent, canActivate: [AuthGuard]},
  {path: 'contact', component: ContactComponent, canActivate: [AuthGuard]},
  {path: 'timeline', component: TimeLineComponent, canActivate: [AuthGuard]},
  {path: 'timeline-about', component: TimeAboutComponent, canActivate: [AuthGuard]},
  {path: 'timeline-friends', component: TimeFriendsComponent, canActivate: [AuthGuard]},
  {path: 'timeline-album', component: TimeAlbumComponent, canActivate: [AuthGuard]},
  { path: 'friends', component: FriendsComponent , canActivate: [AuthGuard]},
  { path: 'friend-requests', component: FriendRequestsComponent , canActivate: [AuthGuard]},
  {path: 'images', component: ImagesComponent , canActivate: [AuthGuard]},
  {path: 'videos', component: VideosComponent , canActivate: [AuthGuard]},
  { path: '', redirectTo: '/mainpage', pathMatch: 'full' },
  { path: '**', redirectTo: '/mainpage', pathMatch: 'full' },
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    SignUpComponent,
    ContactComponent,
    UserhomeComponent,
    LeftBarComponent,
    RightBarComponent,
    PublishComponent,
    FriendsComponent,
    CoolImagesComponent,
    MainPageComponent,
    TimeLinesComponent,
    TimeLineComponent,
    TimeAboutComponent,
    TimeAlbumComponent,
    TimeFriendsComponent,
    TimeLineProfileComponent,
    TimeLineDetailesComponent,
    LoginComponent,
    PostEditComponent,
    FriendRequestsComponent,
    ImagesComponent,
    VideosComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes),
    FormsModule,
    HttpClientModule,
    SharedModule
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    { provide: APP_BASE_HREF, useValue: '/' }],
  bootstrap: [AppComponent]
})
export class AppModule { }

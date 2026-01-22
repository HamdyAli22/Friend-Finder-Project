import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AboutProfile} from '../model/profile/about-profile';
import {WorkExperience} from '../model/profile/work-experience';
import {Interest} from '../model/profile/interest';
import {Language} from '../model/profile/language';

@Injectable({
  providedIn: 'root'
})
export class AboutProfileService {

  private baseUrl = 'http://localhost:8081/about-profile';

  constructor(private http: HttpClient) {

  }

  /* =======================
       GET Profile(ROOT)
       ======================= */
  getProfile(accountId: number): Observable<AboutProfile> {
    return this.http.get<AboutProfile>(
      this.baseUrl + '/get-profile?accountId=' + accountId
    );
  }

  createProfile(payload: AboutProfile): Observable<AboutProfile> {
    return this.http.post<AboutProfile>(
      this.baseUrl + '/create-profile',
      payload
    );
  }

  updateProfile(payload: Partial<AboutProfile>): Observable<AboutProfile> {
    return this.http.put<AboutProfile>(
      this.baseUrl + '/update-profile',
      payload
    );
  }

  deleteAboutProfile(aboutProfileId: number): Observable<void> {
    return this.http.delete<void>(
      this.baseUrl + '/delete-profile?aboutProfileId=' + aboutProfileId
    );
  }

  deletePersonalInfo(aboutProfileId: number): Observable<void> {
    return this.http.delete<void>(
      this.baseUrl + '/delete-personal-info?aboutProfileId=' + aboutProfileId
    );
  }

  deleteAddress(aboutProfileId: number): Observable<void> {
    return this.http.delete<void>(
      this.baseUrl + '/delete-address?aboutProfileId=' + aboutProfileId
    );
  }


  /* =======================
      WORK EXPERIENCE
      ======================= */
  createWorkExperience(payload: WorkExperience): Observable<WorkExperience> {
    return this.http.post<WorkExperience>(
      this.baseUrl + '/work-experience/create-work',
      payload
    );
  }

  updateWorkExperience(payload: WorkExperience): Observable<WorkExperience> {
    return this.http.put<WorkExperience>(
      this.baseUrl + '/work-experience/update-work',
      payload
    );
  }

  getWorkExperiences(accountId: number): Observable<WorkExperience[]> {
    return this.http.get<WorkExperience[]>(
      this.baseUrl + '/get-work?accountId=' + accountId
    );
  }

  deleteWorkExperience(workExperienceId?: number): Observable<void> {
    return this.http.delete<void>(
      workExperienceId == null
        ? this.baseUrl + '/work-experience/delete-work'
        : this.baseUrl + '/work-experience/delete-work?workExperienceId=' + workExperienceId
    );
  }


  /* =======================
      INTEREST
      ======================= */
  createInterest(payload: Interest): Observable<Interest> {
    return this.http.post<Interest>(
      this.baseUrl + '/interest/create-interest',
      payload
    );
  }

  updateInterest(payload: Interest): Observable<Interest> {
    return this.http.put<Interest>(
      this.baseUrl + '/interest/update-interest',
      payload
    );
  }

  getInterests(accountId: number): Observable<Interest[]> {
    return this.http.get<Interest[]>(
      this.baseUrl + '/get-interest?accountId=' + accountId
    );
  }


  deleteInterest(interestId?: number): Observable<void> {
    return this.http.delete<void>(
      interestId == null
        ? this.baseUrl + '/interest/delete-interest'
        : this.baseUrl + '/interest/delete-interest?interestId=' + interestId
    );
  }


  /* =======================
     LANGUAGE
     ======================= */
  createLanguage(payload: Language): Observable<Language> {
    return this.http.post<Language>(
      this.baseUrl + '/language/create-language',
      payload
    );
  }

  updateLanguage(payload: Language): Observable<Language> {
    return this.http.put<Language>(
      this.baseUrl + '/language/update-language',
      payload
    );
  }

  getLanguages(accountId: number): Observable<Language[]> {
    return this.http.get<Language[]>(
      this.baseUrl + '/get-language?accountId=' + accountId
    );
  }

  deleteLanguage(languageId?: number): Observable<void> {
    return this.http.delete<void>(
      languageId == null
        ? this.baseUrl + '/language/delete-language'
        : this.baseUrl + '/language/delete-language?languageId=' + languageId
    );
  }

}

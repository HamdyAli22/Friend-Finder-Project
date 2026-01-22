  import { Component, OnInit } from '@angular/core';
  import { ChangeDetectorRef } from '@angular/core';
  import {AboutProfile} from '../../../../model/profile/about-profile';
  import {AboutProfileService} from '../../../../service/about-profile.service';
  import {ActivatedRoute} from '@angular/router';

  @Component({
    selector: 'app-time-about',
    templateUrl: './time-about.component.html',
    styleUrls: ['./time-about.component.css']
  })
  export class TimeAboutComponent implements OnInit {

    currentUserId = Number(localStorage.getItem('userId') || 0);
    userId: number;

    aboutProfile!: AboutProfile;
    loading = true;

    personalInfoModalOpen = false;
    personalInfoTemp = '';

    locationModalOpen = false;
    locationTemp = '';


    workModalOpen = false;
    editingWork: any = null;
    workTemp: any = {
      companyName: '',
      jobTitle: '',
      startDate: '',
      endDate: '',
      currentJob: false
    };

    interestModalOpen = false;
    editingInterest: any = null;
    interestTemp: any = {
      name: ''
    };


    languageModalOpen = false;
    editingLanguage: any = null;
    languageTemp: any = {
      name: ''
    };

    deleteDialogOpen = false;
    deleteTargetType: 'personalInfo' | 'location' | 'work' | 'interest' | 'language' = 'personalInfo';
    deleteTargetData: any = null;

    constructor(private aboutProfileService: AboutProfileService,
                private route: ActivatedRoute,
                private cdr: ChangeDetectorRef) { }

    ngOnInit(): void {

      this.route.queryParams.subscribe(params => {
        const id = params.userId;
        console.log('params', id);
        this.userId = id
          ? Number(id)
          : this.currentUserId;
      });

      this.loadProfile();
    }

    loadProfile(): void {
      this.loading = true;
      this.aboutProfileService.getProfile(this.userId).subscribe({
        next: (res) => {
          this.aboutProfile = res || {
            personalInfo: '',
            address: '',
            workExperiences: [],
            interests: [],
            languages: [],
            id: undefined
          };
          this.loading = false;
        },
        error: () => {
          this.aboutProfile = {
            personalInfo: '',
            address: '',
            workExperiences: [],
            interests: [],
            languages: [],
            id: undefined
          };
          this.loading = false;
        }
      });
    }

    //Person Info
    openPersonalInfoModal(): void {
      // نسخ البيانات الحالية للمودال
      this.personalInfoTemp = this.aboutProfile.personalInfo || '';
      this.personalInfoModalOpen = true;
    }

    closePersonalInfoModal(): void {
      this.personalInfoModalOpen = false;
    }

    savePersonalInfo(): void {
      const trimmedValue = this.personalInfoTemp.trim();
      if (trimmedValue === '') return;

      if (this.aboutProfile.id) {
        const payload = {
          id: this.aboutProfile.id,       // <--- مهم في حالة التحديث
          personalInfo: trimmedValue
        };

        // تعديل
        this.aboutProfileService.updateProfile(payload).subscribe({
          next: (res) => {
            this.aboutProfile.personalInfo = res.personalInfo;
            this.personalInfoModalOpen = false;
          },
          error: (err) => console.error('Error updating Personal Info:', err)
        });
      } else {
        // إنشاء بيانات جديدة
        const payload: AboutProfile = {
          id:null,
          personalInfo: trimmedValue,
          address: this.aboutProfile.address,
          workExperiences: this.aboutProfile.workExperiences,
          interests: this.aboutProfile.interests,
          languages: this.aboutProfile.languages
        };
        this.aboutProfileService.createProfile(payload).subscribe({
          next: (res) => {
            this.aboutProfile.personalInfo = res.personalInfo;
            this.personalInfoModalOpen = false;
          },
          error: (err) => console.error('Error creating Personal Info:', err)
        });
      }
    }

    //Location
    openLocationModal(): void {
      this.locationTemp = this.aboutProfile.address || '';
      this.locationModalOpen = true;
    }

    closeLocationModal(): void {
      this.locationModalOpen = false;
    }

    saveLocation(): void {
      const trimmedValue = this.locationTemp.trim();
      if (trimmedValue === '') return;

      if (this.aboutProfile.id) {
        // Update
        const payload = {
          id: this.aboutProfile.id,
          address: trimmedValue
        };

        this.aboutProfileService.updateProfile(payload).subscribe({
          next: (res) => {
            this.aboutProfile.address = res.address;
            this.locationModalOpen = false;
          },
          error: (err) => console.error('Error updating Location:', err)
        });

      } else {
        // Create
        const payload: AboutProfile = {
          id:null,
          personalInfo: this.aboutProfile.personalInfo,
          address: trimmedValue,
          workExperiences: this.aboutProfile.workExperiences,
          interests: this.aboutProfile.interests,
          languages: this.aboutProfile.languages
        };

        this.aboutProfileService.createProfile(payload).subscribe({
          next: (res) => {
            this.aboutProfile.address = res.address;
            this.locationModalOpen = false;
          },
          error: (err) => console.error('Error creating Location:', err)
        });
      }
    }


   //Work Experiences
    openWorkModal(work?: any): void {
      if (work) {
        // Edit
        this.editingWork = work;
        this.workTemp = { ...work };
      } else {
        // Add
        this.editingWork = null;
        this.workTemp = {
          companyName: '',
          jobTitle: '',
          startDate: '',
          endDate: '',
          currentJob: false
        };
      }

      this.workModalOpen = true;
    }

    closeWorkModal(): void {
      this.workModalOpen = false;
    }

    saveWork(): void {
      if (!this.workTemp.companyName || !this.workTemp.jobTitle) return;

      if (this.editingWork) {
        // UPDATE
        const payload = {
          ...this.workTemp,
          id: this.editingWork.id
        };

        this.aboutProfileService.updateWorkExperience(payload).subscribe({
          next: (res) => {
            const index = this.aboutProfile.workExperiences
              .findIndex(w => w.id === res.id);

            this.aboutProfile.workExperiences[index] = res;
            this.closeWorkModal();
          },
          error: err => console.error('Error updating work:', err)
        });

      } else {
        // CREATE
        const payload = {
          companyName: this.workTemp.companyName,
          jobTitle: this.workTemp.jobTitle,
          startDate: this.workTemp.startDate,
          endDate: this.workTemp.endDate,
          currentJob: this.workTemp.currentJob,
          aboutProfileId: this.aboutProfile.id
        };

        this.aboutProfileService.createWorkExperience(payload).subscribe({
          next: (res) => {
            this.aboutProfile.workExperiences.push(res);
            this.closeWorkModal();
          },
          error: err => console.error('Error creating work:', err)
        });
      }
    }

    trackByWorkId(index: number, item: any) {
      return item.id;
    }

    openInterestModal(interest?: any): void {
      if (interest) {
        this.editingInterest = interest;
        this.interestTemp = { ...interest };
      } else {
        this.editingInterest = null;
        this.interestTemp = { name: '' };
      }

      this.interestModalOpen = true;
    }

    closeInterestModal(): void {
      this.interestModalOpen = false;
    }

    saveInterest(): void {
      if (!this.interestTemp.name?.trim()) return;

      if (this.editingInterest) {
        // UPDATE
        const payload = {
          id: this.editingInterest.id,
          name: this.interestTemp.name
        };

        this.aboutProfileService.updateInterest(payload).subscribe({
          next: (res) => {
            const index = this.aboutProfile.interests
              .findIndex(i => i.id === res.id);

            this.aboutProfile.interests[index] = res;
            this.closeInterestModal();
          },
          error: err => console.error('Error updating interest:', err)
        });

      } else {
        // CREATE
        const payload = {
          name: this.interestTemp.name,
          aboutProfileId: this.aboutProfile.id
        };

        this.aboutProfileService.createInterest(payload).subscribe({
          next: (res) => {
            this.aboutProfile.interests.push(res);
            this.closeInterestModal();
          },
          error: err => console.error('Error creating interest:', err)
        });
      }
    }

    openLanguageModal(language?: any): void {
      if (language) {
        // Edit
        this.editingLanguage = language;
        this.languageTemp = { ...language };
      } else {
        // Add
        this.editingLanguage = null;
        this.languageTemp = { name: '' };
      }

      this.languageModalOpen = true;
    }

    closeLanguageModal(): void {
      this.languageModalOpen = false;
    }

    saveLanguage(): void {
      if (!this.languageTemp.name?.trim()) return;

      if (this.editingLanguage) {
        // UPDATE
        const payload = {
          id: this.editingLanguage.id,
          name: this.languageTemp.name
        };

        this.aboutProfileService.updateLanguage(payload).subscribe({
          next: (res) => {
            const index = this.aboutProfile.languages
              .findIndex(l => l.id === res.id);

            this.aboutProfile.languages[index] = res;
            this.closeLanguageModal();
          },
          error: err => console.error('Error updating language:', err)
        });

      } else {
        // CREATE
        const payload = {
          name: this.languageTemp.name,
          aboutProfileId: this.aboutProfile.id
        };

        this.aboutProfileService.createLanguage(payload).subscribe({
          next: (res) => {
            this.aboutProfile.languages.push(res);
            this.closeLanguageModal();
          },
          error: err => console.error('Error creating language:', err)
        });
      }
    }

    openDeleteDialog(type: 'personalInfo' | 'location' | 'work' | 'interest' | 'language', data?: any) {
      this.deleteTargetType = type;
      this.deleteTargetData = data || null;
      this.deleteDialogOpen = true;
    }

    confirmDelete() {
      if (!this.deleteTargetType) return;

      const finalize = () => {
        this.deleteDialogOpen = false;
        this.deleteTargetData = null;
        // Force Angular to detect changes
        this.cdr.detectChanges();
      };

      switch (this.deleteTargetType) {
        case 'personalInfo':
          if (!this.aboutProfile?.id) { finalize(); break; }
          this.aboutProfileService.deletePersonalInfo(this.aboutProfile.id).subscribe({
            next: () => {
              this.aboutProfile.personalInfo = '';
              finalize();
            },
            error: err => { console.error(err); finalize(); }
          });
          break;

        case 'location':
          if (!this.aboutProfile?.id) { finalize(); break; }
          this.aboutProfileService.deleteAddress(this.aboutProfile.id).subscribe({
            next: () => {
              this.aboutProfile.address = '';
              finalize();
            },
            error: err => { console.error(err); finalize(); }
          });
          break;

        case 'work':
         // if (!this.deleteTargetData?.id) { finalize(); break; }
          const id = this.deleteTargetData?.id;
          this.aboutProfileService.deleteWorkExperience(id).subscribe({
            next: () => {
              if (id) {
                // حذف عنصر واحد
                this.aboutProfile.workExperiences = this.aboutProfile.workExperiences.filter(
                  w => w.id !== id
                );
              } else {
                // حذف كل الخبرات
                this.aboutProfile.workExperiences = [];
              }
              finalize();
            },
            error: err => { console.error('Error deleting work:', err); finalize(); }
          });
          break;

        case 'interest':
          const interestId = this.deleteTargetData?.id;
          this.aboutProfileService.deleteInterest(interestId).subscribe({
            next: () => {
              if (interestId) {
                // حذف عنصر واحد
                this.aboutProfile.interests = this.aboutProfile.interests.filter(
                  i => i.id !== interestId
                );
              } else {
                // حذف كل العناصر
                this.aboutProfile.interests = [];
              }
              finalize();
            },
            error: err => { console.error('Error deleting interest:', err); finalize(); }
          });
          break;

        case 'language':
          const languageId = this.deleteTargetData?.id;
          this.aboutProfileService.deleteLanguage(languageId).subscribe({
            next: () => {
              if (languageId) {
                // حذف عنصر واحد
                this.aboutProfile.languages = this.aboutProfile.languages.filter(
                  l => l.id !== languageId
                );
              } else {
                // حذف كل العناصر
                this.aboutProfile.languages = [];
              }
              finalize();
            },
            error: err => { console.error('Error deleting language:', err); finalize(); }
          });
          break;

        default:
          finalize();
          break;
      }
    }




  }

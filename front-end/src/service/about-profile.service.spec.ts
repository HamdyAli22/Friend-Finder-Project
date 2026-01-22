import { TestBed } from '@angular/core/testing';

import { AboutProfileService } from './about-profile.service';

describe('AboutProfileService', () => {
  let service: AboutProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AboutProfileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

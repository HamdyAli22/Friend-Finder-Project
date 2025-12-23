import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeVideosComponent } from './time-videos.component';

describe('TimeVideosComponent', () => {
  let component: TimeVideosComponent;
  let fixture: ComponentFixture<TimeVideosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TimeVideosComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TimeVideosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

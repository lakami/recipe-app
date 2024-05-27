import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ActivateSuccessComponent} from './activate-success.component';

describe('ActivateAfterComponent', () => {
  let component: ActivateSuccessComponent;
  let fixture: ComponentFixture<ActivateSuccessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActivateSuccessComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActivateSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

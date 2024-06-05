import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RecipeCardItemComponent} from './recipe-card-item.component';

describe('CardComponent', () => {
  let component: RecipeCardItemComponent;
  let fixture: ComponentFixture<RecipeCardItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipeCardItemComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeCardItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

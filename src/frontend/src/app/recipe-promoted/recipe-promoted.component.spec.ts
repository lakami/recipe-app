import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RecipePromotedComponent} from './recipe-promoted.component';

describe('RecipePromotedComponent', () => {
  let component: RecipePromotedComponent;
  let fixture: ComponentFixture<RecipePromotedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipePromotedComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipePromotedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RecipeAuthorComponent} from './recipe-author.component';

describe('RecipeAuthorComponent', () => {
  let component: RecipeAuthorComponent;
  let fixture: ComponentFixture<RecipeAuthorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipeAuthorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeAuthorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

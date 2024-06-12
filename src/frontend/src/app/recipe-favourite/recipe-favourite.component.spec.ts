import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RecipeFavouriteComponent} from './recipe-favourite.component';

describe('RecipeFavouriteComponent', () => {
  let component: RecipeFavouriteComponent;
  let fixture: ComponentFixture<RecipeFavouriteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipeFavouriteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeFavouriteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

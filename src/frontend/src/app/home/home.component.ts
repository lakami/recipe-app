import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {RouterLink, RouterOutlet} from "@angular/router";
import {ExampleService} from "../shared/services/example.service";
import {BehaviorSubject} from "rxjs";
import {AsyncPipe, CommonModule} from "@angular/common";
import {RecipeService} from "../shared/services/recipe.service";
import {RecipeGetModel} from "../shared/dto/recipe-get.model";
import {RecipeCardItemComponent} from "../recipe-card-item/recipe-card-item.component";
import {TranslationDirective} from "../shared/translation/translation.directive";
import {SearchComponent} from "../search/search.component";

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [
    RouterOutlet,
    AsyncPipe,
    CommonModule,
    RecipeCardItemComponent,
    TranslationDirective,
    RouterLink,
    SearchComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit, OnDestroy {
  private recipeService: RecipeService = inject(RecipeService);
  private recipes = new BehaviorSubject<RecipeGetModel[]>([]);
  recipes$ = this.recipes.asObservable();

  constructor(private exampleService: ExampleService) {
  }

  ngOnInit() {
    this.recipeService.getRecipes("", "", "").subscribe({
      next: (page) => {
        this.recipes.next(page.content);
      },
      error: (error) => {
        console.error(error);
      },
      complete: () => {
        console.log('complete');
      }
    })

  }

  ngOnDestroy() {
  }
}

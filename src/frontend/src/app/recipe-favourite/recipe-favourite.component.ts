import {Component, inject, OnInit} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {RecipeService} from "../shared/services/recipe.service";
import {RecipeGetModel} from "../shared/dto/recipe-get.model";
import {AsyncPipe} from "@angular/common";
import {RecipeCardItemComponent} from "../recipe-card-item/recipe-card-item.component";
import {RouterLink} from "@angular/router";
import {TranslationDirective} from "../shared/translation/translation.directive";

@Component({
  selector: 'app-recipe-favourite',
  standalone: true,
  imports: [
    AsyncPipe,
    RecipeCardItemComponent,
    RouterLink,
    TranslationDirective
  ],
  templateUrl: './recipe-favourite.component.html',
  styleUrl: './recipe-favourite.component.scss'
})
export class RecipeFavouriteComponent implements OnInit {
  recipeService: RecipeService = inject(RecipeService);
  recipe: BehaviorSubject<RecipeGetModel[]> = new BehaviorSubject<RecipeGetModel[]>([]);
  recipes$ = this.recipe.asObservable();

  ngOnInit(): void {
    this.recipeService.getFavourites().subscribe(data => {
      this.recipe.next(data);
    });
  }

}

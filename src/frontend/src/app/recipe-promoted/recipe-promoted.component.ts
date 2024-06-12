import {Component, inject, OnInit} from '@angular/core';
import {AsyncPipe} from "@angular/common";
import {RecipeCardItemComponent} from "../recipe-card-item/recipe-card-item.component";
import {RouterLink} from "@angular/router";
import {TranslationDirective} from "../shared/translation/translation.directive";
import {RecipeService} from "../shared/services/recipe.service";
import {RecipeGetModel} from "../shared/dto/recipe-get.model";
import {BehaviorSubject} from "rxjs";

@Component({
  selector: 'app-recipe-promoted',
  standalone: true,
  imports: [
    AsyncPipe,
    RecipeCardItemComponent,
    RouterLink,
    TranslationDirective
  ],
  templateUrl: './recipe-promoted.component.html',
  styleUrl: './recipe-promoted.component.scss'
})
export class RecipePromotedComponent implements OnInit {
  recipeService: RecipeService = inject(RecipeService);
  recipe: BehaviorSubject<RecipeGetModel[]> = new BehaviorSubject<RecipeGetModel[]>([]);
  recipes$ = this.recipe.asObservable();

  ngOnInit(): void {
    this.recipeService.getAllPromoted().subscribe(data => {
      this.recipe.next(data);
    });
  }

}

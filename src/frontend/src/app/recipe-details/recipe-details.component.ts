import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink, RouterOutlet} from "@angular/router";
import {AsyncPipe, CommonModule} from "@angular/common";
import {HlmIconComponent} from "@spartan-ng/ui-icon-helm";
import {provideIcons} from "@ng-icons/core";
import {lucideClock} from "@ng-icons/lucide";
import {RecipeGetModel} from "../shared/dto/recipe-get.model";
import {RecipeService} from "../shared/services/recipe.service";
import {BehaviorSubject, map} from "rxjs";
import {data} from "autoprefixer";
import {ReactiveFormsModule} from "@angular/forms";
import {TranslationDirective} from "../shared/translation/translation.directive";

@Component({
  selector: 'app-recipe-details',
  standalone: true,
  imports: [
    RouterOutlet,
    AsyncPipe,
    CommonModule,
    [HlmIconComponent],
    ReactiveFormsModule,
    TranslationDirective,
    RouterLink
  ],
  providers: [provideIcons({ lucideClock })],
  templateUrl: './recipe-details.component.html',
  styleUrl: './recipe-details.component.scss'
})
export class RecipeDetailsComponent implements OnInit, OnDestroy {
  private recipeService: RecipeService = inject(RecipeService);
  private recipe = new BehaviorSubject<RecipeGetModel | null>(null);
  recipe$ = this.recipe.asObservable();
  private route: ActivatedRoute = inject(ActivatedRoute);
  recipeId$ = this.route.params.pipe(map(params => params['recipeId']));

  ngOnInit(): void {
    this.recipeId$.subscribe(recipeId => {
     console.log(recipeId);
      this.recipeService.getRecipeById(recipeId).subscribe(data => {
        this.recipe.next(data);
      });
    });
  }

  ngOnDestroy(): void {
  }

}

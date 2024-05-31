import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink} from "@angular/router";
import {provideIcons} from '@ng-icons/core';
import {lucideMoon, lucideUserCircle} from '@ng-icons/lucide';
import {HlmIconComponent} from '@spartan-ng/ui-icon-helm';
import {TranslationDirective} from "../shared/translation/translation.directive";
import {
  HlmMenuComponent,
  HlmMenuGroupComponent,
  HlmMenuItemDirective,
  HlmMenuItemIconDirective,
  HlmMenuItemSubIndicatorComponent,
  HlmMenuLabelComponent,
  HlmMenuSeparatorComponent,
  HlmMenuShortcutComponent,
  HlmSubMenuComponent
} from "@spartan-ng/ui-menu-helm";
import {BrnMenuTriggerDirective} from "@spartan-ng/ui-menu-brain";
import {RecipeService} from "../shared/services/recipe.service";
import {BehaviorSubject, map} from "rxjs";
import {DishGetModel} from "../shared/dto/dish-get.model";
import {AsyncPipe} from "@angular/common";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    RouterLink,
    [HlmIconComponent],
    TranslationDirective,
    BrnMenuTriggerDirective,
    HlmMenuComponent,
    HlmMenuGroupComponent,
    HlmMenuItemDirective,
    HlmMenuItemIconDirective,
    HlmMenuItemSubIndicatorComponent,
    HlmMenuLabelComponent,
    HlmMenuSeparatorComponent,
    HlmMenuShortcutComponent,
    HlmSubMenuComponent,
    HlmMenuItemDirective,
    AsyncPipe,
  ],
  providers: [provideIcons({ lucideUserCircle, lucideMoon })],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit, OnDestroy {
  private recipeService: RecipeService = inject(RecipeService);
  private dishes = new BehaviorSubject<DishGetModel[]>([]);
  dishes$ = this.dishes.asObservable();
  private  diets = new BehaviorSubject<DishGetModel[]>([]);
    diets$ = this.diets.asObservable();
  private route: ActivatedRoute = inject(ActivatedRoute);
  dishId$ = this.route.params.pipe(map(params => params['dishId']));

  ngOnInit(): void {
      this.recipeService.getDishes().subscribe({
        next: (dishes) => {
          this.dishes.next(dishes);
        }
      });

      this.recipeService.getDiets().subscribe({
        next: (diets) => {
          this.diets.next(diets);
        }
      });
  }

  ngOnDestroy(): void {
  }
}

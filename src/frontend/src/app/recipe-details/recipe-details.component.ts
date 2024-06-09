import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink, RouterOutlet} from "@angular/router";
import {AsyncPipe, CommonModule} from "@angular/common";
import {HlmIconComponent} from "@spartan-ng/ui-icon-helm";
import {provideIcons} from "@ng-icons/core";
import {lucideClock, lucideHeart} from "@ng-icons/lucide";
import {RecipeGetModel} from "../shared/dto/recipe-get.model";
import {RecipeService} from "../shared/services/recipe.service";
import {BehaviorSubject, map, Subscription} from "rxjs";
import {ReactiveFormsModule} from "@angular/forms";
import {TranslationDirective} from "../shared/translation/translation.directive";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {AccountService} from "../core/auth/account.service";
import {toObservable} from "@angular/core/rxjs-interop";
import {CommentSectionComponent} from "./comment-section/comment-section.component";

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
    RouterLink,
    HlmButtonDirective,
    HlmIconComponent,
    CommentSectionComponent
  ],
  providers: [provideIcons({ lucideClock, lucideHeart})],
  templateUrl: './recipe-details.component.html',
  styleUrl: './recipe-details.component.scss'
})
export class RecipeDetailsComponent implements OnInit, OnDestroy {
  private recipeService: RecipeService = inject(RecipeService);
  private recipe = new BehaviorSubject<RecipeGetModel | null>(null);
  recipe$ = this.recipe.asObservable();
  private route: ActivatedRoute = inject(ActivatedRoute);
  recipeId$ = this.route.params.pipe(map(params => params['recipeId']));
  accountService: AccountService = inject(AccountService);
  isFavourite: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private accountSubscription?: Subscription;
  private account$ = toObservable(this.accountService.trackCurrentAccount());

  ngOnInit(): void {
    this.recipeId$.subscribe(recipeId => {
      console.log(recipeId);
      this.recipeService.getRecipeById(recipeId).subscribe(data => {
        this.recipe.next(data);
      });
      this.accountSubscription?.unsubscribe(); //usuwanie starych listenerów
      this.accountSubscription = this.account$.subscribe(account => {
        if (account) {
          this.recipeService.isFavourite(recipeId).subscribe(isFavourite => {
            this.isFavourite.next(isFavourite);
          });
        }
      });
    });
  }

  ngOnDestroy(): void {
  }

  toggleFavourite() {
    if (!this.recipe.value) {
      return;
    }
    if (this.isFavourite.value) {
      this.recipeService.deleteFavourite(this.recipe.value.id).subscribe(() => {
        this.isFavourite.next(false);
      })
    } else {
      this.recipeService.addFavourite(this.recipe.value.id).subscribe(() => {
        this.isFavourite.next(true);
      })
    }
  }

}

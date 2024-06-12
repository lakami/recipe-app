import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {provideIcons} from '@ng-icons/core';
import {lucideHeart, lucideMoon, lucidePlus, lucideStar, lucideUserCircle} from '@ng-icons/lucide';
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
import {BehaviorSubject} from "rxjs";
import {DishGetModel} from "../shared/dto/dish-get.model";
import {AsyncPipe} from "@angular/common";
import {DietGetModel} from "../shared/dto/diet-get.model";
import {ThemeService} from "../shared/services/theme.service";
import {AccountService} from "../core/auth/account.service";
import {LoginService} from "../login/login.service";

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
    AsyncPipe,
    HlmMenuComponent,
    HlmMenuComponent,
  ],
  providers: [provideIcons({ lucideUserCircle, lucideMoon, lucidePlus, lucideHeart, lucideStar})],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit, OnDestroy {
  private themeService: ThemeService = inject(ThemeService);
  private recipeService: RecipeService = inject(RecipeService);
  private dishes = new BehaviorSubject<DishGetModel[]>([]);
  dishes$ = this.dishes.asObservable();
  private  diets = new BehaviorSubject<DietGetModel[]>([]);
    diets$ = this.diets.asObservable();
  private route: ActivatedRoute = inject(ActivatedRoute);
  private accountService = inject(AccountService);
  currentUser$ = this.accountService.trackCurrentAccount();
  private loginService = inject(LoginService);
  private router = inject(Router);

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

  toggleColor(): void {
    this.themeService.toggleDarkMode();
  }

  ngOnDestroy(): void {
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }

  isAdmin() {
    return this.accountService.hasAnyAuthority('ROLE_ADMIN');
  }
}

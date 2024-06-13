import {Component, inject, OnInit} from '@angular/core';
import {BehaviorSubject, filter, map, zip} from "rxjs";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {RecipeService} from "../shared/services/recipe.service";
import {AsyncPipe} from "@angular/common";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {TranslationDirective} from "../shared/translation/translation.directive";
import {HlmIconComponent} from "@spartan-ng/ui-icon-helm";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {provideIcons} from "@ng-icons/core";
import {lucideChevronDown, lucideChevronUp, lucidePlus, lucideSave} from "@ng-icons/lucide";
import {AccountService} from "../core/auth/account.service";
import {DishGetModel} from "../shared/dto/dish-get.model";
import {DietGetModel} from "../shared/dto/diet-get.model";
import {TagGetModel} from "../shared/dto/tag-get.model";
import {BrnSelectImports} from "@spartan-ng/ui-select-brain";
import {TranslationPipe} from "../shared/translation/translation.pipe";
import {HlmSelectImports} from "@spartan-ng/ui-select-helm";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";
import {ImageSnippet} from "../recipe-add/recipe-add.component";
import {environment} from "../../enviroments/enviroment.development";


@Component({
  selector: 'app-recipe-edit',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    HlmInputDirective,
    HlmButtonDirective,
    TranslationDirective,
    HlmSelectImports,
    BrnSelectImports,
    TranslationPipe,
    AsyncPipe,
    HlmButtonDirective,
    [HlmIconComponent],
    FormsModule,
  ],
  providers: [provideIcons({lucideChevronUp, lucideChevronDown, lucidePlus, lucideSave})],
  templateUrl: './recipe-edit.component.html',
  styleUrl: './recipe-edit.component.scss'
})
export class RecipeEditComponent implements OnInit {
  private recipeService: RecipeService = inject(RecipeService);
  private route: ActivatedRoute = inject(ActivatedRoute);
  recipeId$ = this.route.params.pipe(map(params => params['recipeId']));
  private accountService: AccountService = inject(AccountService);
  private router: Router = inject(Router);
  dishes!: DishGetModel[];
  diets!: DietGetModel[];
  tags!: TagGetModel[];
  selectedFile: ImageSnippet | null = null; //definicja wybranego zdjęcia
  originImageUrl?: string;
  readonly imageBasePath: string = environment.images;

  form: FormGroup = new FormGroup({
    name: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(1), Validators.maxLength(100)]
    }),
    description: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(1), Validators.maxLength(100)]
    }),
    preparationTime: new FormControl(0, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(1), Validators.max(10000)]
    }),
    servings: new FormControl(0, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(1), Validators.max(100)]
    }),
    dishes: new FormControl([]),
    diets: new FormControl([]),
    tags: new FormControl([]),
    steps: new FormControl('', {
      validators: [Validators.required]
    }),
    ingredients: new FormControl('', {
      validators: [Validators.required]
    }),
  });

  ngOnInit(): void {
    var dishesLoaded = new BehaviorSubject<boolean>(false);
    var dietsLoaded = new BehaviorSubject<boolean>(false);
    var tagsLoaded = new BehaviorSubject<boolean>(false);
    var formLoaded = new BehaviorSubject<boolean>(false);

    var areLoaded = zip(
      dishesLoaded.pipe(filter(loaded => loaded)),
      dietsLoaded.pipe(filter(loaded => loaded)),
      tagsLoaded.pipe(filter(loaded => loaded))
    ).subscribe(loaded => {
      console.log(loaded)
      formLoaded.next(true);
    })

    this.recipeService.getDishes().subscribe({
      next: (dishes) => {
        this.dishes = dishes;
        dishesLoaded.next(true);
      }
    });

    this.recipeService.getDiets().subscribe({
      next: (diets) => {
        this.diets = diets
        dietsLoaded.next(true);
        console.log(diets);
      }
    });

    this.recipeService.getTags().subscribe({
      next: (tags) => {
        this.tags = tags;
        tagsLoaded.next(true);
      }
    });

    this.recipeId$.subscribe(recipeId => {
      console.log(recipeId);
      this.recipeService.getRecipeById(recipeId).subscribe(recipe => {
        if (!(recipe.author.profileUrl === this.accountService.trackCurrentAccount()()?.profileUrl
          || this.accountService.hasAnyAuthority('ROLE_ADMIN'))) {
          this.router.navigate(['/recipe', recipeId]); //przekierowanie na stronę przepisu
        }
        //wczytanie danych do formularza
        this.originImageUrl = recipe.imageUrl;
        formLoaded.pipe(filter(loaded => loaded)).subscribe(() => {
          this.form.patchValue({
            name: recipe.name,
            description: recipe.description,
            preparationTime: recipe.preparationTime,
            servings: recipe.servings,
            dishes: recipe.dishes.map(dish => dish.name),
            diets: recipe.diets.map(diet => diet.name),
            tags: recipe.tags.map(tag => tag.name),
            steps: recipe.steps
              .sort((s1, s2) => s1.number-s2.number)
              .map(step => step.description)
              .join('\n'),
            ingredients: recipe.ingredients.map(ingredient => ingredient.name).join('\n')
          });
        });
      });
    });
  }

  hasNameError(): boolean {
    let nameControl = this.form.get('name');
    return nameControl!.invalid && (nameControl!.touched || nameControl!.dirty);
  }

  hasDescriptionError(): boolean {
    let descriptionControl = this.form.get('description');
    return descriptionControl!.invalid && (descriptionControl!.touched || descriptionControl!.dirty);
  }

  hasPreparationTimeError(): boolean {
    let preparationTimeControl = this.form.get('preparationTime');
    return preparationTimeControl!.invalid && (preparationTimeControl!.touched || preparationTimeControl!.dirty);
  }

  hasServingsError(): boolean {
    let servingsControl = this.form.get('servings');
    return servingsControl!.invalid && (servingsControl!.touched || servingsControl!.dirty);
  }

  submit() {
    console.log(this.form.value);
    this.recipeService.updateRecipe(
      this.route.snapshot.params['recipeId'],
      this.form.get('name')!.value,
      this.form.get('description')!.value,
      this.form.get('preparationTime')!.value,
      this.form.get('servings')!.value,
      this.form.get('dishes')!.value,
      this.form.get('diets')!.value,
      this.form.get('tags')!.value,
      this.form.get('steps')!.value.split('\n'),
      this.form.get('ingredients')!.value.split('\n')
    ).subscribe({
      next: () => {

      }
    });
  }

  processFile(imageInput: any) {
    const file: File = imageInput.files[0];
    const reader = new FileReader();
    reader.addEventListener('load', (event: any) => {
      this.selectedFile = new ImageSnippet(event.target.result, file);
      console.log(this.selectedFile);
    })
    reader.readAsDataURL(file);
  }

  updateImage() {
    if (this.selectedFile) {
      this.recipeService.updateImage(this.route.snapshot.params['recipeId'], this.selectedFile.file).subscribe({
        next: () => {
          this.recipeService.getRecipeById(this.route.snapshot.params['recipeId']).subscribe(recipe => {
            if (!(recipe.author.profileUrl === this.accountService.trackCurrentAccount()()?.profileUrl
              || this.accountService.hasAnyAuthority('ROLE_ADMIN'))) {
              this.router.navigate(['/recipe', recipe.id]); //przekierowanie na stronę przepisu
            }
            this.selectedFile = null; //czyszczenie zdjęcia
            //wczytanie danych do formularza
            this.originImageUrl = recipe.imageUrl;
            });
        }
      });
    }
  }

}

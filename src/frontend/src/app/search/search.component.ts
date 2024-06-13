import {Component, inject, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {BehaviorSubject, debounce, filter, ReplaySubject, zip} from "rxjs";
import {DishGetModel} from "../shared/dto/dish-get.model";
import {ActivatedRoute, RouterLink, RouterOutlet} from "@angular/router";
import {RecipeService} from "../shared/services/recipe.service";
import {AsyncPipe, CommonModule} from "@angular/common";
import {FormArray, FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {TranslationDirective} from "../shared/translation/translation.directive";
import {HlmIconComponent} from "@spartan-ng/ui-icon-helm";
import {RecipeGetModel} from "../shared/dto/recipe-get.model";
import {RecipeCardItemComponent} from "../recipe-card-item/recipe-card-item.component";
import {TagGetModel} from "../shared/dto/tag-get.model";
import {DietGetModel} from "../shared/dto/diet-get.model";
import {HlmCheckboxComponent} from "@spartan-ng/ui-checkbox-helm";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {provideIcons} from "@ng-icons/core";
import {lucideLoader2, lucidePlus, lucideSearch} from "@ng-icons/lucide";
import {
  HlmAccordionContentDirective,
  HlmAccordionDirective,
  HlmAccordionIconDirective,
  HlmAccordionItemDirective,
  HlmAccordionTriggerDirective
} from "@spartan-ng/ui-accordion-helm";
import {BrnAccordionContentComponent} from '@spartan-ng/ui-accordion-brain';
import {LayoutService} from "../shared/services/layout.service";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";
import {TranslationPipe} from "../shared/translation/translation.pipe";

@Component({
  selector: 'app-search',
  standalone: true,
  providers: [provideIcons({ lucideSearch, lucidePlus, lucideLoader2})],
  imports: [
    RouterOutlet,
    AsyncPipe,
    CommonModule,
    [HlmIconComponent],
    ReactiveFormsModule,
    TranslationDirective,
    RouterLink,
    RecipeCardItemComponent,
    HlmCheckboxComponent,
    HlmButtonDirective,
    BrnAccordionContentComponent,
    HlmAccordionDirective,
    HlmAccordionItemDirective,
    HlmAccordionTriggerDirective,
    HlmAccordionContentDirective,
    HlmAccordionIconDirective,
    HlmIconComponent,
    HlmInputDirective,
    TranslationPipe,
  ],
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss'
})
export class SearchComponent implements OnInit, OnDestroy, OnChanges {
  @Input() diet?: String;
  @Input() dish?: String;

  layoutService: LayoutService = inject(LayoutService);
  private recipeService: RecipeService = inject(RecipeService);
  private route: ActivatedRoute = inject(ActivatedRoute);
  queryParams$ = this.route.queryParams; //wszystkie parametry wyszukiwania przepisu
  private recipes = new BehaviorSubject<RecipeGetModel[]>([]);
  dietsChanged= new ReplaySubject<string>();
  dishesChanged= new ReplaySubject<string>();
  recipes$ = this.recipes.asObservable();
  page: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  lastSelectedDishes: string[] = [];
  lastSelectedDiets: string[] = [];
  lastSelectedTags: string[] = [];
  lastSearch: string = '';
  pagesSize: number = 4;
  isLastPage: boolean = true;
  dishes!: DishGetModel[];
  diets!: DietGetModel[];
  tags!: TagGetModel[];
  form: FormGroup = new FormGroup({
    dishes: new FormArray([]),
    diets: new FormArray([]),
    tags: new FormArray([]),
    search: new FormControl('')
  });

  ngOnInit(): void {

    var formLoaded = new BehaviorSubject<boolean>(false);
    this.dietsChanged
      .pipe(debounce(_ => formLoaded
        .pipe(filter(loaded => loaded)))) //czekam na załadowanie się formularza i biorę ostatnią wartość, która pojawiła się w trakcie oczekiwania
      .subscribe(diet => {
        console.log(diet);
        this.form.reset();
        this.form.controls["diets"].setValue(this.diets.map(d => d.name === diet));
        this.submit();
    })

    this.dishesChanged
      .pipe(debounce(_ => formLoaded
        .pipe(filter(loaded => loaded)))) //czekam na załadowanie się formularza i biorę ostatnią wartość, która pojawiła się w trakcie oczekiwania
      .subscribe(dish =>{
        console.log(dish);
        this.form.reset();
        this.form.controls["dishes"].setValue(this.dishes.map(d => d.name === dish));
        this.submit();
        }
      )

    var dishesLoaded = new BehaviorSubject<boolean>(false);
    var dietsLoaded = new BehaviorSubject<boolean>(false);
    var tagsLoaded = new BehaviorSubject<boolean>(false);

    var areLoaded = zip(
      dishesLoaded.pipe(filter(loaded => loaded)),
      dietsLoaded.pipe(filter(loaded => loaded)),
      tagsLoaded.pipe(filter(loaded => loaded))
    ).subscribe(loaded => {
        console.log(loaded)
        this.form = new FormGroup({
          dishes: new FormArray(this.dishes.map(dish => new FormControl( false))),
          diets: new FormArray(this.diets.map(diet => new FormControl( false))),
          tags: new FormArray(this.tags.map(tag => new FormControl( false))),
          search: new FormControl('')
        })
        formLoaded.next(true);
        this.submit();
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
      }
    });

    this.recipeService.getTags().subscribe({
      next: (tags) => {
        this.tags = tags;
        tagsLoaded.next(true);
      }
    });
  }

  ngOnDestroy() {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes["diet"] && changes["diet"].currentValue) {
      let currentlySelectedDiet = changes["diet"].currentValue;
      this.dietsChanged.next(currentlySelectedDiet);
      console.log(currentlySelectedDiet);
    }
    if (changes["dish"] && changes["dish"].currentValue) {
      let currentlySelectedDish = changes["dish"].currentValue;
      this.dishesChanged.next(currentlySelectedDish);
      console.log(currentlySelectedDish);
    }
  }

  get dishesFormArray() {
    return this.form.controls["dishes"] as FormArray;
  }

  get dietsFormArray() {
    return this.form.controls["diets"] as FormArray;
  }

  get tagsFormArray() {
    return this.form.controls["tags"] as FormArray;
  }

  get selectedDishes(): string[] {
    return this.form.value.dishes
      .map((checked: boolean, i: number) => checked ? this.dishes[i].name : null)
      .filter((v: string | null) => v !== null);
  }

  get selectedDiets(): string[] {
    return this.form.value.diets
      .map((checked: boolean, i: number) => checked ? this.diets[i].name : null)
      .filter((v: string | null) => v !== null);
  }

  get selectedTags(): string[] {
    return this.form.value.tags
      .map((checked: boolean, i: number) => checked ? this.tags[i].name : null)
      .filter((v: string | null) => v !== null);
  }

  submit() {
    const selectedDishesNames = this.selectedDishes;
    console.log(selectedDishesNames);

    const selectedDietsNames = this.selectedDiets;
    console.log(selectedDietsNames);

    const selectedTagsNames = this.selectedTags;
    console.log(selectedTagsNames);

    this.lastSelectedDishes = selectedDishesNames;
    this.lastSelectedDiets = selectedDietsNames;
    this.lastSelectedTags = selectedTagsNames;
    this.lastSearch = this.form.value.search;
    this.page.next(0);

    this.recipeService.getRecipes(
      selectedDishesNames.join(','),
      selectedDietsNames.join(','),
      selectedTagsNames.join(','),
      this.form.value.search,
      0,
      this.pagesSize
    ).subscribe({
      next: (p) => {
        this.recipes.next(p.content)
        console.log(p.content)
        this.isLastPage = p.last;
      }
    })
  }

  loadMore() {
    this.recipeService.getRecipes(
      this.lastSelectedDishes.join(','),
      this.lastSelectedDiets.join(','),
      this.lastSelectedTags.join(','),
      this.lastSearch,
      this.page.value + 1,
      this.pagesSize
    ).subscribe({
      next: (p) => {
        this.recipes.next([...this.recipes.value, ...p.content])
        console.log(p.content)
        this.page.next(p.number)
        this.isLastPage = p.last;
      }
    })
  }
}

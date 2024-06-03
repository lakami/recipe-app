import {Component, inject, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TranslationDirective} from "../shared/translation/translation.directive";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {DishGetModel} from "../shared/dto/dish-get.model";
import {DietGetModel} from "../shared/dto/diet-get.model";
import {TagGetModel} from "../shared/dto/tag-get.model";
import {BehaviorSubject, filter, zip} from "rxjs";
import {RecipeService} from "../shared/services/recipe.service";
import {BrnSelectImports} from "@spartan-ng/ui-select-brain";
import {HlmSelectImports} from "@spartan-ng/ui-select-helm";
import {provideIcons} from "@spartan-ng/ui-icon-helm";
import {lucideChevronDown, lucideChevronUp} from "@ng-icons/lucide";
import {TranslationPipe} from "../shared/translation/translation.pipe";
import {AsyncPipe} from "@angular/common";

class ImageSnippet {
  constructor(public src: string, public file: File) {}
}

@Component({
  selector: 'app-recipe-add',
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
    AsyncPipe
  ],
  providers: [provideIcons({lucideChevronUp, lucideChevronDown})],
  templateUrl: './recipe-add.component.html',
  styleUrl: './recipe-add.component.scss'
})
export class RecipeAddComponent implements OnInit {
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

  private recipeService: RecipeService = inject(RecipeService);
  dishes!: DishGetModel[];
  diets!: DietGetModel[];
  tags!: TagGetModel[];
  selectedFile?: ImageSnippet;

  ngOnInit(): void {
    this.form.get('dishes')!.valueChanges.subscribe((value) => {
      console.log(value);
    });
    var formLoaded = new BehaviorSubject<boolean>(false);
    var dishesLoaded = new BehaviorSubject<boolean>(false);
    var dietsLoaded = new BehaviorSubject<boolean>(false);
    var tagsLoaded = new BehaviorSubject<boolean>(false);

    var areLoaded = zip(
      dishesLoaded.pipe(filter(loaded => loaded)),
      dietsLoaded.pipe(filter(loaded => loaded)),
      tagsLoaded.pipe(filter(loaded => loaded))
    ).subscribe(loaded => {
      console.log(loaded)
      // this.form = new FormGroup({
      //   name: new FormControl('', {
      //     nonNullable: true,
      //     validators: [Validators.required, Validators.minLength(1), Validators.maxLength(100)]
      //   }),
      //   description: new FormControl('', {
      //     nonNullable: true,
      //     validators: [Validators.required, Validators.minLength(1), Validators.maxLength(100)]
      //   }),
      //   preparationTime: new FormControl(0, {
      //     nonNullable: true,
      //     validators: [Validators.required, Validators.min(1), Validators.max(10000)]
      //   }),
      //   servings: new FormControl(0, {
      //     nonNullable: true,
      //     validators: [Validators.required, Validators.min(1), Validators.max(100)]
      //   }),
      //   dishes: new FormArray(this.dishes.map(dish => new FormControl( false))),
      //   diets: new FormArray(this.diets.map(diet => new FormControl( false))),
      //   tags: new FormArray(this.tags.map(tag => new FormControl( false))),
      //   steps: new FormArray([]),
      //   ingredients: new FormArray([]),
      // })
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

  processFile(imageInput: any) {
    const file: File = imageInput.files[0];
    const reader = new FileReader();
    reader.addEventListener('load', (event: any) => {
      this.selectedFile = new ImageSnippet(event.target.result, file);
      console.log(this.selectedFile);
    })
    reader.readAsDataURL(file);
  }

  submit() {
    console.log(this.form.value);
    this.recipeService.addRecipe(
      this.form.get('name')!.value,
      this.form.get('description')!.value,
      this.form.get('preparationTime')!.value,
      this.form.get('servings')!.value,
      this.form.get('dishes')!.value,
      this.form.get('diets')!.value,
      this.form.get('tags')!.value,
      this.form.get('steps')!.value.split('\n'),
      this.form.get('ingredients')!.value.split('\n'),
      this.selectedFile!.file
    ).subscribe({
      next: (id) => {
        console.log(id);
      }
    });
  }

}

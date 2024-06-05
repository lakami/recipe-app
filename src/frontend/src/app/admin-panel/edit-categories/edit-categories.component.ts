import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {RouterLink} from "@angular/router";
import {TranslationDirective} from "../../shared/translation/translation.directive";
import {BrnSelectImports} from "@spartan-ng/ui-select-brain";
import {TranslationPipe} from "../../shared/translation/translation.pipe";
import {AsyncPipe} from "@angular/common";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {HlmSelectImports} from "@spartan-ng/ui-select-helm";
import {HlmIconComponent} from "@spartan-ng/ui-icon-helm";
import {RecipeService} from "../../shared/services/recipe.service";
import {lucidePlus} from "@ng-icons/lucide";
import {provideIcons} from "@ng-icons/core";

@Component({
  selector: 'app-edit-categories',
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
  ],
  providers: [provideIcons({lucidePlus})],
  templateUrl: './edit-categories.component.html',
  styleUrl: './edit-categories.component.scss'
})
export class EditCategoriesComponent {
  formAddDish: FormGroup = new FormGroup({
    name: new FormControl('', {
      nonNullable: true,
      validators: [Validators.minLength(1), Validators.maxLength(100)]
    })
  });

  formAddDiet: FormGroup = new FormGroup({
    name: new FormControl('', {
      nonNullable: true,
      validators: [Validators.minLength(1), Validators.maxLength(100)]
    })
  });

  formAddTag: FormGroup = new FormGroup({
    name: new FormControl('', {
      nonNullable: true,
      validators: [Validators.minLength(1), Validators.maxLength(100)]
    })
  });

  private recipeService: RecipeService = inject(RecipeService);

  submitDish() {
    console.log(this.formAddDish.value);
    this.recipeService.addDish(this.formAddDish.value).subscribe();
  }

  submitDiet() {
    console.log(this.formAddDiet.value);
    this.recipeService.addDiet(this.formAddDiet.value).subscribe();
  }

  submitTag() {
    console.log(this.formAddTag.value);
    this.recipeService.addTag(this.formAddTag.value).subscribe();
  }
}

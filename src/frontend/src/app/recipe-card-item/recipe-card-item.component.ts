import {Component, Input} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {AsyncPipe, CommonModule} from "@angular/common";
import {RecipeGetModel} from "../shared/dto/recipe-get.model";
import {HlmIconComponent} from "@spartan-ng/ui-icon-helm";
import {lucideClock} from '@ng-icons/lucide';
import {provideIcons} from "@ng-icons/core";

@Component({
  selector: 'app-recipe-card-item',
  standalone: true,
  imports: [
    RouterOutlet,
    AsyncPipe,
    CommonModule,
    [HlmIconComponent],
    HlmIconComponent,
    HlmIconComponent,
    HlmIconComponent,
    HlmIconComponent,

  ],
  providers: [provideIcons({ lucideClock })],
  templateUrl: 'recipe-card-item.component.html',
  styleUrl: './recipe-card-item.component.scss'
})
export class RecipeCardItemComponent {
  @Input({required: true}) recipe!: RecipeGetModel;
}

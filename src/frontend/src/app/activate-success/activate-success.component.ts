import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {TranslationDirective} from "../shared/translation/translation.directive";

@Component({
  selector: 'app-activate-after',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    TranslationDirective
  ],
  templateUrl: './activate-success.component.html',
  styleUrl: './activate-success.component.scss'
})
export class ActivateSuccessComponent {
}

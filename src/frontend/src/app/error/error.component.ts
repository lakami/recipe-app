import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {TranslationDirective} from "../shared/translation/translation.directive";

@Component({
  selector: 'app-error',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    TranslationDirective
  ],
  templateUrl: './error.component.html',
  styleUrl: './error.component.scss'
})
export class ErrorComponent {

}

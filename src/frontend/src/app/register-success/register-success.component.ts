import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {TranslationDirective} from "../shared/translation/translation.directive";
import {ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-register-success',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    TranslationDirective
  ],
  templateUrl: './register-success.component.html',
  styleUrl: './register-success.component.scss'
})
export class RegisterSuccessComponent {
}

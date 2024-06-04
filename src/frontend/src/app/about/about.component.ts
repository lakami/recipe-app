import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {TranslationDirective} from "../shared/translation/translation.directive";

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    TranslationDirective
  ],
  templateUrl: './about.component.html',
  styleUrl: './about.component.scss'
})
export class AboutComponent {

}

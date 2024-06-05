import {Component} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {TranslationDirective} from "../shared/translation/translation.directive";

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [
    RouterOutlet,
    TranslationDirective
  ],
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.scss'
})
export class AdminPanelComponent {

}

import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {provideIcons} from '@ng-icons/core';
import {lucideMoon, lucideUserCircle} from '@ng-icons/lucide';
import {HlmIconComponent} from '@spartan-ng/ui-icon-helm';
import {TranslationDirective} from "../shared/translation/translation.directive";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    RouterLink,
    [HlmIconComponent],
    TranslationDirective
  ],
  providers: [provideIcons({ lucideUserCircle, lucideMoon })],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
}

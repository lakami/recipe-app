import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {provideIcons} from '@ng-icons/core';
import {lucideUserCircle} from '@ng-icons/lucide';
import {HlmIconComponent} from '@spartan-ng/ui-icon-helm';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    RouterLink,
    [HlmIconComponent]
  ],
  providers: [provideIcons({ lucideUserCircle })],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
}

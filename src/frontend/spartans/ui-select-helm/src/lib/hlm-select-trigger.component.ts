import {Component, computed, ContentChild, type ElementRef, input, ViewChild} from '@angular/core';
import {provideIcons} from '@ng-icons/core';
import {lucideChevronDown} from '@ng-icons/lucide';
import {hlm} from '@spartan-ng/ui-core';
import {HlmIconComponent} from '@spartan-ng/ui-icon-helm';
import {BrnSelectTriggerDirective} from '@spartan-ng/ui-select-brain';
import type {ClassValue} from 'clsx';

@Component({
  selector: 'hlm-select-trigger',
  standalone: true,
  imports: [BrnSelectTriggerDirective, HlmIconComponent],
  providers: [provideIcons({lucideChevronDown})],
  template: `
		<button [class]="_computedClass()" #button brnSelectTrigger type="button">
			<ng-content />
			@if (icon) {
				<ng-content select="hlm-icon" />
			} @else {
				<hlm-icon class="ml-2 h-4 w-4 flex-none" name="lucideChevronDown" />
			}
		</button>
	`,
})
export class HlmSelectTriggerComponent {
  @ViewChild('button', {static: true})
  public buttonEl!: ElementRef;
  public readonly userClass = input<ClassValue>('', {alias: 'class'});
  @ContentChild(HlmIconComponent, {static: false})
  protected icon!: HlmIconComponent;
  protected readonly _computedClass = computed(() =>
    hlm(
      'flex h-10 items-center justify-between rounded-none border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus:outline-none focus:ring-1 focus:ring-ring focus:ring-offset-0 disabled:cursor-not-allowed disabled:opacity-50 w-full',
      this.userClass(),
    ),
  );
}

import {ChangeDetectionStrategy, Component, computed, inject, input, ViewEncapsulation} from '@angular/core';
import {hlm} from '@spartan-ng/ui-core';
import type {ClassValue} from 'clsx';
import {HlmCarouselComponent} from './hlm-carousel.component';

@Component({
  selector: 'hlm-carousel-content',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  host: {
    '[class]': '_computedClass()',
  },
  template: `
		<ng-content />
	`,
})
export class HlmCarouselContentComponent {
  _userClass = input<ClassValue>('', {alias: 'class'});
  private orientation = inject(HlmCarouselComponent).orientation;
  protected _computedClass = computed(() =>
    hlm('flex', this.orientation() === 'horizontal' ? '-ml-4' : '-mt-4 flex-col', this._userClass()),
  );
}

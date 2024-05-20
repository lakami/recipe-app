import {computed, Directive, inject, input} from '@angular/core';
import {HlmButtonDirective} from '@spartan-ng/ui-button-helm';
import {hlm} from '@spartan-ng/ui-core';
import type {ClassValue} from 'clsx';

@Directive({
  selector: 'button[hlmAlertDialogCancel]',
  standalone: true,
  hostDirectives: [HlmButtonDirective],
  host: {
    '[class]': '_computedClass()',
  },
})
export class HlmAlertDialogCancelButtonDirective {
  public readonly userClass = input<ClassValue>('', {alias: 'class'});
  protected readonly _computedClass = computed(() => hlm('mt-2 sm:mt-0', this.userClass()));
  private readonly _hlmBtn = inject(HlmButtonDirective, {host: true});

  constructor() {
    this._hlmBtn.variant = 'outline';
  }
}

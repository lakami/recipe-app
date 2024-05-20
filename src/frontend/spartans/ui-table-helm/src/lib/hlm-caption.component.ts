import {
  booleanAttribute,
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
  ViewEncapsulation,
} from '@angular/core';
import {hlm} from '@spartan-ng/ui-core';
import type {ClassValue} from 'clsx';
import {HlmTableComponent} from '../index';

let captionIdSequence = 0;

@Component({
  selector: 'hlm-caption',
  standalone: true,
  host: {
    '[class]': '_computedClass()',
    '[id]': 'id()',
  },
  template: `
		<ng-content />
	`,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
})
export class HlmCaptionComponent {
  public readonly hidden = input(false, {transform: booleanAttribute});
  public readonly userClass = input<ClassValue>('', {alias: 'class'});
  protected readonly id = input<string | null | undefined>(`${captionIdSequence++}`);
  protected readonly _computedClass = computed(() =>
    hlm(
      'text-center block mt-4 text-sm text-muted-foreground',
      this.hidden() ? 'sr-only' : 'order-last',
      this.userClass(),
    ),
  );
  private readonly _table = inject(HlmTableComponent, {optional: true});

  constructor() {
    effect(
      () => {
        const id = this.id();
        if (!this._table) return;
        this._table.labeledBy.set(id);
      },
      {allowSignalWrites: true},
    );
  }
}

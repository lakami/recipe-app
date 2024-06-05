import {Component, computed, Input, input, signal} from '@angular/core';
import {hlm} from '@spartan-ng/ui-core';
import {BrnMenuDirective} from '@spartan-ng/ui-menu-brain';
import {cva, type VariantProps} from 'class-variance-authority';
import type {ClassValue} from 'clsx';

export const menuVariants = cva(
  'block border-border min-w-[10rem] overflow-hidden border bg-popover p-0.5 text-popover-foreground shadow-md data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 data-[state=closed]:zoom-out-95 data-[state=open]:zoom-in-95 data-[side=bottom]:slide-in-from-top-2 data-[side=left]:slide-in-from-right-2 data-[side=right]:slide-in-from-left-2 data-[side=top]:slide-in-from-bottom-2',
  {
    variants: {
      variant: {
        default: 'my-0.5',
        menubar: 'my-2',
      },
    },
    defaultVariants: {
      variant: 'default',
    },
  },
);
type MenuVariants = VariantProps<typeof menuVariants>;

@Component({
  selector: 'hlm-menu',
  standalone: true,
  host: {
    '[class]': '_computedClass()',
  },
  hostDirectives: [BrnMenuDirective],
  template: `
		<ng-content />
	`,
})
export class HlmMenuComponent {
  public readonly userClass = input<ClassValue>('', {alias: 'class'});
  private readonly _variant = signal<MenuVariants['variant']>('default');
  protected _computedClass = computed(() => hlm(menuVariants({variant: this._variant()}), this.userClass()));

  @Input()
  set variant(value: MenuVariants['variant']) {
    this._variant.set(value);
  }
}
